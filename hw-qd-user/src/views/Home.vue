<template>
  <div> 
    <el-container>
      <el-sider>
        <button @click="logout" style="background-color:aqua; padding: 0;margin: 0;height: 60px;width: 80px;border-radius: 10px;">退出登录</button>
        <h5 class="mb-2" style="height: 70px;">
        <img src="../asset/login.jpg" style="height: 100px;">
      </h5>
      <el-menu
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo"
        default-active="2"
        text-color="#fff"
      >
        <el-menu-item index="1">
            <router-link to="/home/carts"  style="text-decoration: none; color: inherit;">
            <el-icon><Goods /></el-icon>
            <span>购物车</span>
          </router-link>
        </el-menu-item>
        <el-menu-item index="2">
          <router-link to="/home/books" style="text-decoration: none; color: inherit;">
          <el-icon><Notebook /></el-icon>
          <span>书 籍 购 买</span>
        </router-link>
        </el-menu-item>
        <el-menu-item index="3">
          <router-link to="/home/orders"  style="text-decoration: none; color: inherit;">
          <el-icon><Avatar /></el-icon>
          <span>个 人 订 单</span>
          </router-link>
        </el-menu-item>
        <el-menu-item index="4">
          <router-link to="/home/self"  style="text-decoration: none; color: inherit;">
          <el-icon><Avatar /></el-icon>
          <span>个 人 信 息</span>
          </router-link>
        </el-menu-item>
      </el-menu>
      </el-sider>
      <el-container>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script lang="js" setup>
import { useRouter } from 'vue-router';
const router = useRouter();
import {
  Goods,
  Avatar,
  Notebook
} from '@element-plus/icons-vue'
function logout() {
  localStorage.removeItem('jwt')
  localStorage.removeItem('id')
  localStorage.clear();
  router.push('/login');
}
</script>
<style>

</style>