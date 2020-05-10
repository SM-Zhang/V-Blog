<template>
    <div class="login-container">
        <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="51px" class="login-form">
            <h2>栎子云</h2>
            <el-form-item label="账号" prop="umail">
                <el-input v-model="loginForm.umail"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="upassword">
                <el-input v-model="loginForm.upassword" type="password"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button class="logButton" type="primary" @click="submitForm('loginForm')">登&nbsp;&nbsp;录</el-button>
            </el-form-item>
            <span @click="forgetPasswd()" style="font-size:14px;font-weight:500;color: #909399;cursor:pointer;">忘记密码 | </span>
            <span @click="regUser()" style="font-size:14px;font-weight:500;color: #409eff;cursor:pointer;">注册</span>
        </el-form>

        <!-- 注册弹出框 -->
        <el-dialog center title="注册" :visible.sync="dialogRegVisible" width="450px">
            <el-form :model="regForm" status-icon :rules="rulesRegForm" ref="regForm" style="width: 380px;" label-width="90px" >
                <el-form-item label="账号" prop="umail">
                  <el-input v-model="regForm.umail"></el-input>
                </el-form-item>
                <el-form-item label="昵称" prop="unickname">
                  <el-input v-model="regForm.unickname" maxlength="10" show-word-limit ></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="upassword">
                  <el-input v-model="regForm.upassword" maxlength="16" show-word-limit type="password" placeholder="4-16位密码"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkPass">
                  <el-input type="password" maxlength="16" show-word-limit v-model="regForm.checkPass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item style="text-align: center;" >
                  <el-button type="primary" @click="submitRegForm('regForm')">注册</el-button>
                  <el-button @click="calcelReg()">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 忘记密码弹出框 -->
        <el-dialog center title="忘记密码" :visible.sync="dialogForVisible" width="450px">
            <el-form :model="forgetForm" status-icon :rules="rulesForgetForm" ref="forgetForm" style="width: 380px;" label-width="90px" >
                <el-form-item label="账号" prop="umail">
                  <el-input v-model="forgetForm.umail"></el-input>
                </el-form-item>
                <el-form-item style="text-align: center;" >
                  <el-button type="primary" @click="submitForForm('forgetForm')">确认</el-button>
                  <el-button @click="calcelForget()">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

    </div>
</template>

<script>
import { addUser, resetPassword } from '@/api/userApi'

export default {
    // ^[\w\.-]*@([A-Za-z\d\-]*\.)+[A-Za-z]{2,}$
    data() {
      const validateUmail = (rule, value, callback) => {
        var filter = /^[\w\.-]*@([A-Za-z\d\-]*\.)+[A-Za-z]{2,}$/;
        if(value.trim() === ''){
          callback(new Error('请输入邮箱'));
        }else{
          if(filter.test(value)){
            callback();
          }else{
            callback(new Error('邮箱格式不正确'));
          }
        }
      };
      const validateUpassword = (rule, value, callback) => {
        var filter = /^[\w\.-\@\!]{4,16}$/;
        if(value === ''){
          callback(new Error('请输入密码'));
        }else{
          if(filter.test(value)){
            callback();
          }else{
            callback(new Error('密码格式不正确'));
          }
        }
      };
      // 登录校验密码
      const validateCheckPass = (rule, value, callback) => {
        if(this.ruleForm.checkPass !== this.ruleForm.newpass) {
            callback(new Error('两次密码输入不一致'));
        }else {
            callback();
        }
      };
      // 注册校验密码
      const validateCheckRegPass = (rule, value, callback) => {
        if(value === ''){
          callback(new Error('请输入密码'));
        }else {
          if(this.regForm.checkPass !== this.regForm.upassword) {
            callback(new Error('两次密码输入不一致'));
          }else {
            callback();
          }
        }
        
      };
      return {
        loginForm: {
          umail: '',
          upassword: '',
        },
        rules: {
          umail: [
            { validator: validateUmail, trigger: 'blur'}
          ],
          upassword: [
            { validator: validateUpassword, trigger: 'blur'}
          ],
        },
        // 忘记密码弹出框
        dialogForVisible: false,
        forgetForm: {
          umail: ''
        },
        rulesForgetForm: {
          umail: [
            { validator: validateUmail, trigger: 'blur'}
          ],
        },
        // 注册弹出框
        dialogRegVisible: false,
        regForm: {
          umail: '',
          unickname: '',
          upassword: '',
          ustatus: 9,
          checkPass: ''
        },
        rulesRegForm: {
          umail: [
            { validator: validateUmail, trigger: 'blur'}
          ],
          upassword: [
            { validator: validateUpassword, trigger: 'blur'}
          ],
          checkPass: [
            { validator: validateCheckRegPass, trigger: 'change' }
          ]
        }
      };
    },

    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // 调用 store action 中的 Login, 并提交载荷 
            // this.$store.dispatch('Login', this.loginForm) 返回的是一个 Promise 对象
            this.loginForm.umail = this.loginForm.umail.replace(/\s+/g,'');
            this.loginForm.upassword = this.loginForm.upassword.replace(/\s+/g,'');
            this.$store.dispatch('Login', this.loginForm).then(response => {
              if(response.flag){
                // 前往首页
                this.$router.push('/');
              } else {
                this.$message({
                  message: response.message,
                  type: "warning"
                })
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      // 打开忘记密码框
      forgetPasswd(){
        this.$nextTick( () => {
          if (this.$refs['forgetForm'] !== undefined) {
              this.$refs['forgetForm'].resetFields();
          }
        })
        this.dialogForVisible = true;
      },
      // 取消忘记密码框
      calcelForget(){
        this.dialogForVisible = false;
      },
      // 确认忘记密码
      submitForForm(forgetForm){
        this.$refs[forgetForm].validate((valid) => {
            resetPassword(this.forgetForm.umail).then(response => {
                if(response.data.flag){
                    // 邮箱校验成功, 密码重置成功
                    this.$message({
                        message: response.data.message,
                        type: 'success',
                        duration: 3 * 1000
                    });
                    this.dialogForVisible = false;
                }else {
                    // 邮箱校验失败
                    if(response.data.code === 10103){ // 用户不存在
                        this.$message({
                            message: response.data.message,
                            type: 'danger',
                        });
                    }else{
                        this.$message({ // 用户未激活
                            message: response.data.message,
                            type: 'warning',
                        });
                    }
                }
            })
        })
      },
      // 打开注册框
      regUser(){
        this.$nextTick( () => {
          if (this.$refs['regForm'] !== undefined) {
              this.$refs['regForm'].resetFields();
          }
        })
        this.dialogRegVisible = true;
      },
      // 取消注册
      calcelReg(){
        this.dialogRegVisible = false;
      },
      // 确认注册
      submitRegForm(regForm){
          this.$refs[regForm].validate((valid) => {
              addUser(this.regForm).then(response => {
                  if(response.data.flag){
                      // 注册成功
                      this.$message({
                          message: '注册成功，请前往邮箱激活',
                          type: 'success',
                          duration: 3 * 1000
                      });
                      this.dialogRegVisible = false;
                  }else {
                      // 注册失败
                      if(response.data.code === 10102){
                          this.$message({
                              message: response.data.message,
                              type: 'warning',
                          });
                      }else{
                          this.$message({
                              message: response.data.message,
                              type: 'danger',
                          });
                      }
                  }
              })
          })
      }
    }
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100%;
  position: absolute;
  /* background: url(../../assets/log_bg.jpg); */
  background-repeat: no-repeat;
}
.login-form {
  width: 350px;
  padding: 35px 35px 25px 35px;
  margin: 150px auto;
  border-radius: 10px;
  background: #e3e3e3;
  opacity: 0.85;
}
h2 {
  font-size: 20px;
  text-align: center;
  margin-bottom: 12px;
}
.logButton {
  width: 298px;
  font-size: 14px;
  padding: 9px;
}
</style>