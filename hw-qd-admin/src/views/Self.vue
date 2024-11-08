<template>
  <div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="id" label="ID" width="50" />
      <el-table-column prop="username" label="用户名" width="200" />
      <el-table-column prop="password" label="密码" width="120" />
      <el-table-column prop="email" label="邮箱" width="600" />
      <el-table-column prop="role" label="角色" width="100" />
      <el-table-column prop="balance" label="余额" width="100" />
      <el-table-column fixed="right" label="操作" min-width="200">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="deleteDialog(row)">
            删除
          </el-button>
          <el-button link type="primary" size="small" @click="updateDialog(row)">
            修改
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 删除对话框 -->
    <el-dialog title="删除" v-model="deleteDialogVisible">
      <span>确定要删除吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmdelete">确定</el-button>
      </span>
    </el-dialog>

    <!-- 修改对话框 -->
    <el-dialog title="修改" v-model="updateDialogVisible">
      <div style="margin-bottom: 20px;">
        <el-input v-model="current.username" placeholder="输入用户名"></el-input>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="current.password" placeholder="输入密码"></el-input>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="current.email" placeholder="输入邮箱"></el-input>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="current.balance" placeholder="输入余额"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="updateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmupdate">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script lang="js" setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

const tableData = reactive([]);
const token = localStorage.getItem("token");
const deleteDialogVisible = ref(false);
const updateDialogVisible = ref(false);
const current = ref(null);

function fetchData() {
  fetch("/api/admin/user/list", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      token: token
    },
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok " + response.statusText);
      }
      return response.json();
    })
    .then((data) => {
      tableData.push(...data); // 填充数据
    })
    .catch((error) => {
      console.error("There was a problem with the fetch operation:", error);
    });
}
onMounted(() => {
  fetchData();
});

function deleteDialog(row) {
  current.value = row;
  deleteDialogVisible.value = true;
}

function updateDialog(row) {
  current.value = row;
  updateDialogVisible.value = true;
}
function confirmupdate(){
  fetch(`/api/admin/user/update`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      token: token
    },
    body: JSON.stringify({
      id: current.value.id,
      password: current.value.password,
      username: current.value.username,
      email: current.value.email,
      role: current.value.role,
      balance: current.value.balance,
    }),
  })
   .then((response) => {
      if (response.ok) {
        ElMessage.success('修改成功');
        updateDialogVisible.value = false;
      } else {
        throw new Error('Error occurred while updating');
      }
    })
   .catch((error) => {
      ElMessage.error('修改失败');
      console.error('Error:', error);
    });
}
function confirmdelete() {
  fetch(`/api/admin/user/delete/${current.value.id}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      token: token
    },
  })
   .then((response) => {
      if (response.ok) {
        ElMessage.success('删除成功');
        deleteDialogVisible.value = false;
      } else {
        throw new Error('Error occurred while deleting');
      }
    })
   .catch((error) => {
      ElMessage.error('删除失败');
      console.error('Error:', error);
    });
}
</script>

<style scoped>
.el-descriptions {
  margin-top: 20px;
}
.dialog-footer {
  text-align: right;
}
</style>
