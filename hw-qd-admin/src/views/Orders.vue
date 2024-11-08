<template>
  <div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="orderId" label="订单ID" width="0" />
      <el-table-column prop="title" label="书名" width="300" />
      <el-table-column prop="author" label="作者" width="300" />
      <el-table-column prop="price" label="单价" width="120" />
      <el-table-column prop="quantity" label="数量" width="200" />
      <el-table-column prop="totalPrice" label="总价" width="120" /> 
    </el-table>
  </div>
</template>

<script lang="js" setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
const token = localStorage.getItem("token");
const tableData = reactive([]);
const deleteDialogVisible = ref(false);
const currentOrder = ref(null);

onMounted(() => {
  getInfom();
});

function getInfom() {
  fetch(`/api/admin/order/list`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      token: token,
    },
  })
    .then((response) => response.json())
    .then((data) => {
      tableData.push(...data);
    })
    .catch((error) => {
      console.log('Error:', error);
    });
}

</script>
