<template>
  <div>
    <el-descriptions
      title="个人信息"
      direction="vertical"
      :column="5"
      :size="size"
      border>
      <el-descriptions-item label="用户名">{{ selfform.username }}</el-descriptions-item>
      <el-descriptions-item label="密码">{{ selfform.password }}</el-descriptions-item>
      <el-descriptions-item label="邮箱" :span="2">{{ selfform.email }}</el-descriptions-item>
      <el-descriptions-item label="余额">
        {{ selfform.balance }}
        <div style="float: right;">
          <el-button type="success" @click="openRechargeDialog">充值</el-button>
        </div>
      </el-descriptions-item>
    </el-descriptions>
    <el-button type="primary" @click="openUpdateDialog">修改个人信息</el-button>

    <!-- 充值对话框 -->
    <el-dialog title="充值" v-model="rechargeDialogVisible">
      <span>目前余额是：{{ selfform.balance }}</span>
      <br>
      <el-input v-model="rechargeAmount" placeholder="输入充值金额"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="rechargeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="recharge">确认充值</el-button>
      </span>
    </el-dialog>

    <!-- 修改个人信息对话框 -->
    <el-dialog title="修改个人信息" v-model="updateDialogVisible">
      <el-form :model="updateForm">
        <el-form-item label="用户名">
          <el-input v-model="updateForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="updateForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="updateForm.email"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="updateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateUserInfo">确认修改</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import type { ComponentSize } from 'element-plus'
const size = ref<ComponentSize>('default')
// const token ="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyaWQiOiI5IiwiZXhwIjoxNzMxODE3Njk4fQ.sCLXB70D8LgUrucCPDcVg4yBfBKppsHXykne_uaTKF0";
const token = localStorage.getItem("token");
const selfform = reactive({
  userId:1,
  username: '',
  password: '',
  email: '',
  balance: 0
})
const rechargeAmount = ref(0)
const rechargeDialogVisible = ref(false)
const updateDialogVisible = ref(false)
const updateForm = reactive({
  userId:selfform.userId,
  username: selfform.username,
  password: selfform.password,
  email: selfform.email
})

function openRechargeDialog() {
  rechargeDialogVisible.value = true
}

function openUpdateDialog() {
  updateDialogVisible.value = true
  updateForm.username = selfform.username
  updateForm.password = selfform.password
  updateForm.email = selfform.email
}

function recharge() {
  rechargeDialogVisible.value = false
  console.log(rechargeAmount.value)
  fetch(`/api/user/self/add/${rechargeAmount.value}`,{
    method: 'GET',
    headers:{
      'Content-Type': 'application/json',
      token: token
    }
  }).then((response)=>{
    if(response.ok){
      return response.json()
    }else{
      throw new Error('Error occurred while fetching data')
    }
  }).then((data)=>{
    console.log('充值成功', data)
  }).catch((error)=>{
    console.error('Error:', error)
  })
  rechargeAmount.value = 0
}

function updateUserInfo() {
  // 执行修改个人信息逻辑
  selfform.userId = updateForm.userId
  selfform.username = updateForm.username
  selfform.password = updateForm.password
  selfform.email = updateForm.email
  updateDialogVisible.value = false
  fetch(`/api/user/self/update`,{
    method: 'POST',
    headers:{
      'Content-Type': 'application/json',
      token: token
    },
    body: JSON.stringify(updateForm)
  }).then((response)=>{
    if(response.ok){
      return response.json()
    }else{
      throw new Error('Error occurred while fetching data')
    }
  }).then((data)=>{
    console.log('修改个人信息成功', data)
  }).catch((error)=>{
    console.error('Error:', error)
  })
}
function getInfom(){
  fetch('/api/user/self/list',{
    method: 'GET',
    headers:{
      'Content-Type': 'application/json',
      token: token
    }
  }).then((response)=>{
    if(response.ok){
      return response.json()
    }else{
      throw new Error('Error occurred while fetching data')
    }
  }).then((data)=>{
    console.log('获取个人信息成功', data)
    selfform.userId = data.id  // 获取userId并填充到selfform中
    selfform.username = data.username
    selfform.password = data.password
    selfform.email = data.email
    selfform.balance = data.balance  // 获取余额并填充到selfform中
  }).catch((error)=>{
    console.error('Error:', error)
  })
}
onMounted(()=>{
  getInfom()  // 获取个人信息并填充到selfform中
})
</script>

<style scoped>
.el-descriptions {
  margin-top: 20px;
}
.dialog-footer {
  text-align: right;
}
</style>
