<template>
  <div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="id" label="ID" width="50" />
      <el-table-column prop="title" label="书名" width="200" />
      <el-table-column prop="author" label="作者" width="120" />
      <el-table-column prop="description" label="描述" width="600" />
      <el-table-column prop="price" label="单价" width="100" />
      <el-table-column prop="stock" label="库存" width="100" />
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
      <span>确定要将这本书删除吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmdelete">确定</el-button>
      </span>
    </el-dialog>

    <!-- 修改对话框 -->
    <el-dialog title="修改" v-model="updateDialogVisible">
      <div style="margin-bottom: 20px;">
        <el-input v-model="currentBook.title" placeholder="输入书名"></el-input>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="currentBook.description" placeholder="输入描述"></el-input>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="currentBook.author" placeholder="输入作者"></el-input>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="currentBook.price" placeholder="输入单价"></el-input>
      </div>
      <div style="margin-bottom: 20px;">
        <el-input v-model="currentBook.stock" placeholder="输入库存"></el-input>
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
const currentBook = ref(null);

function fetchData() {
  fetch("/api/admin/book/list", {
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
  currentBook.value = row;
  deleteDialogVisible.value = true;
}

function updateDialog(row) {
  currentBook.value = row;
  updateDialogVisible.value = true;
}
function confirmupdate(){
  fetch(`/api/admin/book/update`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      token: token
    },
    body: JSON.stringify({
      id: currentBook.value.id,
      title: currentBook.value.title,
      description: currentBook.value.description,
      author: currentBook.value.author,
      price: currentBook.value.price,
      stock: currentBook.value.stock,
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
  fetch(`/api/admin/book/delete/${currentBook.value.id}`, {
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
