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
    </el-table>
  </div>
</template>

<script lang="js" setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const tableData = reactive([]);
const token = localStorage.getItem("token");

function getInform() {
  fetch("/api/admin/cart/list", {
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


onMounted(() => {
  getInform();
});
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
