<template>

  <el-row :gutter="0" style="height: 100vh;"> <!-- 设置高度为 100% -->
    <el-col :span="16">
        <div>
            <img src="../asset/login.jpg" style="width: 80%;padding-top: 100px;">
        </div>
    </el-col>
    <el-col :span="8">
    <div style="background-color:aqua; border-radius: 5px;">
        <el-container class="login-container">
    <el-main>
      <h1>登录</h1>
      <el-form :model="form" ref="formRef" label-width="80px" @submit.native.prevent="handleLogin">
        <el-form-item label="用户名" :rules="usernameRules">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" :rules="passwordRules">
          <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
    </div>
    </el-col>
  </el-row>  
</template>
  
  <script setup>
  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { ElMessage } from 'element-plus';
      const router = useRouter();
      const form = ref({
        username: '',
        password: '',
      });
      const formRef = ref(null);
      const handleLogin = async () => {
        try {
          await formRef.value.validate(); // 校验表单
          const response = await fetch('/api/user/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(form.value),
          });
          if (response.ok) {
            const data = await response.json();
            console.log(data);
            console.log(data.token);
            localStorage.setItem('token', data.token); // 存储 JWT
            console.log("登录成功")
            router.push('/home/books'); // 跳转到 home
          } else {
            ElMessage.error('登录失败'); // 显示错误消息
          }
        } catch (error) {
          ElMessage.error('请检查输入');
        }
      };
  </script>
  
  <style scoped>
  .login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    
  }
  h1 {
    text-align: center;
    margin-bottom: 20px;
  }
  .el-row {
  height: 100vh; /* 确保高度占满视口 */
  overflow: hidden; /* 隐藏溢出的内容 */
}

  </style>
  