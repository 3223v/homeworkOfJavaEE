<template>
  <div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="orderId" label="订单ID" width="0" />
      <el-table-column prop="title" label="书名" width="300" />
      <el-table-column prop="author" label="作者" width="300" />
      <el-table-column prop="price" label="单价" width="120" />
      <el-table-column prop="quantity" label="数量" width="200" />
      <el-table-column prop="totalPrice" label="总价" width="120" />
      <el-table-column fixed="right" label="操作" min-width="120">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="openDeleteDialog(scope.row)">
            删除订单
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 删除订单对话框 -->
    <el-dialog title="删除订单" v-model="deleteDialogVisible">
      <span>确定要删除这个订单吗？此操作无法撤销。</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="deleteOrder">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="js" setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

// const token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyaWQiOiI5IiwiZXhwIjoxNzMxODE3Njk4fQ.sCLXB70D8LgUrucCPDcVg4yBfBKppsHXykne_uaTKF0";
const token = localStorage.getItem("token");
const tableData = reactive([]);
const deleteDialogVisible = ref(false);
const currentOrder = ref(null);

onMounted(() => {
  getInfom();
});

function getInfom() {
  fetch(`/api/user/order/list`, {
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

function openDeleteDialog(row) {
  currentOrder.value = row;
  deleteDialogVisible.value = true;
}
function deleteOrder() {
  const orderId = currentOrder.value.orderId;
  fetch(`/api/user/order/delete/${orderId}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      token: token,
    },
  })
    .then((response) => {
      if (response.ok) {
        ElMessage.success('订单删除成功');
        deleteDialogVisible.value = false;

      } else {
        throw new Error('Error occurred while deleting order');
      }
    })
    .catch((error) => {
      ElMessage.error('订单删除失败');
      console.error('Error:', error);
    });
}
</script>
