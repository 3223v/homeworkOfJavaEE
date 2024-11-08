<template>
  <div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column fixed prop="cartId" label="ID号" width="60" />
      <el-table-column fixed prop="title" label="书名" width="150" />
      <el-table-column prop="author" label="作者" width="120" />
      <el-table-column prop="price" label="单价" width="120" />
      <el-table-column prop="quantity" label="本数" width="120" />
      <el-table-column prop="totalPrice" label="总价" width="120" />
      <el-table-column prop="description" label="描述" width="600" />
      <el-table-column fixed="right" label="操作" min-width="200">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openUpdateDialog(row)">
            修改
          </el-button>
          <el-button type="success" size="small" @click="openPayDialog(row)">
            支付
          </el-button>
          <el-button type="danger" size="small" @click="openDeleteDialog(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改对话框 -->
    <el-dialog title="修改购物车" v-model="updateDialogVisible">
      <el-input v-model="updateQuantity" placeholder="输入新的数量"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="updateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateCart">确定</el-button>
      </span>
    </el-dialog>

    <!-- 支付对话框 -->
    <el-dialog title="支付购物车" v-model="payDialogVisible">
      <span>确定要支付这个购物车项吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="payDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="payCart">确定</el-button>
      </span>
    </el-dialog>

    <!-- 删除对话框 -->
    <el-dialog title="删除购物车" v-model="deleteDialogVisible">
      <span>确定要删除这个购物车项吗？此操作无法撤销。</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="deleteCart">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="js" setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const tableData = reactive([]);
// const token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyaWQiOiI5IiwiZXhwIjoxNzMxODE3Njk4fQ.sCLXB70D8LgUrucCPDcVg4yBfBKppsHXykne_uaTKF0";
const token = localStorage.getItem("token");
const updateDialogVisible = ref(false);
const payDialogVisible = ref(false);
const deleteDialogVisible = ref(false);
const currentCartItem = ref(null);
const updateQuantity = ref(1);

function getInform() {
  fetch("/api/user/cart/list", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      token: token,
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

function openUpdateDialog(row) {
  currentCartItem.value = row;
  updateQuantity.value = row.quantity;
  updateDialogVisible.value = true;
}

function openPayDialog(row) {
  currentCartItem.value = row;
  payDialogVisible.value = true;
}

function openDeleteDialog(row) {
  currentCartItem.value = row;
  deleteDialogVisible.value = true;
}

function updateCart() {
  const payload = {
    cartId: currentCartItem.value.cartId,
    quantity: updateQuantity.value
  };
  fetch("/api/user/cart/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      token: token
    },
    body: JSON.stringify(payload)
  })
    .then((response) => {
      if (response.ok) {
        ElMessage.success('购物车更新成功');
        updateDialogVisible.value = false;
      } else {
        throw new Error('Error occurred while updating the cart');
      }
    })
    .catch((error) => {
      ElMessage.error('购物车更新失败');
      console.error('Error:', error);
    });
}

function payCart() {
  fetch(`/api/user/cart/pay/${currentCartItem.value.cartId}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      token: token
    }
  })
    .then((response) => {
      if (response.ok) {
        ElMessage.success('支付成功');
        payDialogVisible.value = false;
      } else {
        throw new Error('Error occurred while paying for the cart');
      }
    })
    .catch((error) => {
      ElMessage.error('支付失败');
      console.error('Error:', error);
    });
}

function deleteCart() {
  console(currentCartItem.value.cartId)
  fetch(`/api/user/cart/delete/${currentCartItem.value.cartId}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      token: token
    }
  })
    .then((response) => {
      if (response.ok) {
        ElMessage.success('购物车删除成功');
        deleteDialogVisible.value = false;
      } else {
        throw new Error('Error occurred while deleting the cart');
      }
    })
    .catch((error) => {
      ElMessage.error('购物车删除失败');
      console.error('Error:', error);
    });
}

onMounted(() => {
  getInform();
});
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
