<template>
    <div id="header">
        <div class="header-l">
            <a href="#">栎子云</a>
        </div>
        <div class="header-r">
            <img :src="user.uavatar" />
            <el-dropdown @command="handleCommand">
                <span class="el-dropdown-link">
                     {{ user.unickname }} 
                     <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="updataPasswd" icon="el-icon-edit">&nbsp;修改密码</el-dropdown-item>
                    <el-dropdown-item command="logout" icon="el-icon-s-unfold">&nbsp;退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>

        <!-- 修改密码弹出框 -->
        <el-dialog center title="修改密码" :visible.sync="dialogPasswordVisible" width="450px">
            <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" style="width: 380px;" label-width="90px" >
                <el-form-item label="原密码" prop="oldPass">
                <el-input type="password" v-model="ruleForm.oldPass"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newpass">
                <el-input type="password" v-model="ruleForm.newpass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkPass">
                <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item style="text-align: center;" >
                <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                <el-button @click="$refs['ruleForm'].resetFields()">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog> 

    </div>
</template>

<script>

import { logout } from '@/api/userApi';
import passwdApi from '@/api/upPasswdApi'

export default {
    data() {

        // 在 return 上声明自定义校验
        // 校验原密码，需异步请求 ajax
        const validateOldPass = (rule, value, callback) => {
            // value 当前文本框输入的值
            passwdApi.checkPassword(this.user.umail, value).then(response => {
                if( response.data.flag) { 
                    callback()
                } else {
                    callback(new Error(response.data.message));
                }
            })
        };

        // 校验新密码
        const validateCheckPass = (rule, value, callback) => {
            if(this.ruleForm.checkPass !== this.ruleForm.newpass) {
                callback(new Error('两次密码输入不一致'));
            }else {
                callback();
            }
        };

        return {
            user: this.$store.state.user.userInfo,
            dialogPasswordVisible: false,
            ruleForm: {
                oldPass: '',
                newpass: '',
                checkPass: ''
            },
            rules: {
                oldPass: [ 
                    // 在这里徐配置是否必须请求，否则不会对输入文本进行校验
                    { required: true, message: '原密码不能为空', trigger: 'blur'},
                    { validator: validateOldPass, trigger: 'blur' }
                ],
                newpass: [
                    { required: true, message: '修改密码不能为空', trigger: 'blur'},
                ],
                checkPass: [
                    { required: true, message: '确认密码不能为空', trigger: 'blur'},
                    { validator: validateCheckPass, trigger: 'change' }
                ]
            },
        }

        
    },
    
    methods: {
        // 鼠标点击修改密码或退出系统
        handleCommand(command) {
            // this.$message(`当前点击了${command}`);
            switch (command) {
                case 'updataPasswd':
                    this.updataPasswd()
                    break;
                case 'logout':
                    this.logout()
                    break;
                default:
                    break;
            }
        },

        // 退出登录
        logout() {
            this.$store.dispatch('Logout').then(response => {
                if(response.flag) {
                    // 退出成功，回到登录页面
                    this.$router.push('/login');
                } else {
                    this.$message({
                        message: response.message,
                        type: 'warning',
                        duration: 1000 // 设置弹窗停留时间
                    });
                }
            });
        },

        // 修改密码--异步请求校验密码
        updataPasswd() {
            this.dialogPasswordVisible = true;
            // 异步清除表单数据
            this.$nextTick( () => {
                this.$refs['ruleForm'].resetFields();
            })
        },

        // 修改密码，密码数据提交,修改成功后确认提交新密码
        submitForm(ruleForm) {
            this.$refs[ruleForm].validate( valid => {
                if(valid) {
                    // 提交数据给后台, 将当前用户 id 和新密码传递给后台
                    passwdApi.updatePassword(this.user.umail, this.ruleForm.checkPass).then( response => {
                        if(response.data.flag) {
                            this.$message({
                                message: response.data.message,
                                type: 'success'
                            });
                            // 退出系统
                            this.logout();
                        }else {
                            this.$message({
                                message: response.data.message,
                                type: 'error'
                            });
                        }
                        // 关闭弹窗
                        this.dialogPasswordVisible = false;
                    })
                } else {
                    // 校验失败
                    return false;
                }
            })
        },
    },
}
</script>

<style scoped>
#header{
    width: 100%;
    height: 50px;
    background-color: #393D49;
    position: absolute;
}
.header-l {
    float: left;
    margin-left: 75px;
}
.header-l > a {
    color: #fff;
    font-size: 16px;
    line-height: 50px;
}
.header-r {
    float: right;
    margin-right: 75px;
}
.header-r > img {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    vertical-align: middle;
    margin-right: 18px;
}
.el-dropdown{
    line-height: 50px;
}
.el-dropdown-link {
    cursor: pointer;
    color: #ffffff;
  }
  .el-icon-arrow-down {
    font-size: 16px;
  }
</style>