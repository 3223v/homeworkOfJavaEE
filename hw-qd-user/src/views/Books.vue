<template>
  <div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="id" label="ID" width="50" />
      <el-table-column prop="title" label="书名" width="200" />
      <el-table-column prop="author" label="作者" width="120" />
      <el-table-column prop="description" label="描述" width="600" />
      <el-table-column prop="price" label="单价" width="100" />
      <el-table-column fixed="right" label="操作" min-width="200">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="openAddToCartDialog(row)">
            加入购物车
          </el-button>
          <el-button link type="primary" size="small" @click="openDirectPurchaseDialog(row)">
            直接购买
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 加入购物车对话框 -->
    <el-dialog title="加入购物车" v-model="addToCartDialogVisible">
      <span>确定要将这本书加入购物车吗？</span>
      <el-input v-model="cartQuantity" placeholder="输入数量"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addToCartDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addToCart">确定</el-button>
      </span>
    </el-dialog>

    <!-- 直接购买对话框 -->
    <el-dialog title="直接购买" v-model="directPurchaseDialogVisible">
      <span>确定要直接购买这本书吗？</span>
      <el-input v-model="purchaseQuantity" placeholder="输入数量"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="directPurchaseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="directPurchase">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="js" setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

const tableData = reactive([]);
// const token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyaWQiOiI5IiwiZXhwIjoxNzMxODE3Njk4fQ.sCLXB70D8LgUrucCPDcVg4yBfBKppsHXykne_uaTKF0";
const token = localStorage.getItem("token");
const addToCartDialogVisible = ref(false);
const directPurchaseDialogVisible = ref(false);
const cartQuantity = ref(1);
const purchaseQuantity = ref(1);
const currentBook = ref(null);

function fetchData() {
  fetch("/api/user/book/list", {
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

function openAddToCartDialog(row) {
  currentBook.value = row;
  addToCartDialogVisible.value = true;
}

function openDirectPurchaseDialog(row) {
  currentBook.value = row;
  directPurchaseDialogVisible.value = true;
}

function addToCart() {
  const payload = {
    bookId: currentBook.value.id,
    quantity: cartQuantity.value
  };
  fetch("/api/user/book/addcart", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      token: token
    },
    body: JSON.stringify(payload)
  })
    .then((response) => {
      if (response.ok) {
        ElMessage.success('加入购物车成功');
        addToCartDialogVisible.value = false;
      } else {
        throw new Error('Error occurred while adding to cart');
      }
    })
    .catch((error) => {
      ElMessage.error('加入购物车失败');
      console.error('Error:', error);
    });
}

function directPurchase() {
  const payload = {
    bookId: currentBook.value.id,
    quantity: purchaseQuantity.value
  };
  fetch("/api/user/book/buy", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      token: token
    },
    body: JSON.stringify(payload)
  })
    .then((response) => {
      if (response.ok) {
        ElMessage.success('购买成功');
        directPurchaseDialogVisible.value = false;
      } else {
        throw new Error('Error occurred while purchasing');
      }
    })
    .catch((error) => {
      ElMessage.error('购买失败');
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
