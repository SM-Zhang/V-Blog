<template>
    <div>
        <el-form
            style="margin: 20px 0 0 75px;"
            ref="searchMap"
            :inline="true"
            size="small"
            :model="searchMap">
            <!-- 账号查询 -->    
            <el-form-item prop="umail" >
                <el-input  v-model="searchMap.umail" placeholder="账号"></el-input>
            </el-form-item> 
            <!-- 用户昵称查询 -->
            <el-form-item prop="unickname">
                <el-input  v-model="searchMap.unickname" placeholder="昵称" ></el-input>
            </el-form-item>
            <el-form-item >
                <!-- 查询按钮 -->
                <el-button  type="primary" icon="el-icon-search" @click="fetchData()" >查询</el-button>
                <!-- 添加用户 -->
                <el-button  type="primary" @click="addUserButton()" >添加用户</el-button>
                <!-- 重置按钮 -->
                <el-button  @click="clearSearchMap()" >重置</el-button>
            </el-form-item>
        </el-form>

        <el-row>
            <el-col style="margin-left: 75px;margin-bottom: 20px;" :span="6" v-for="(user, index) in allUsers" :key="index" :offset="index > 0 ? 2 : 0" >
                <el-card :body-style="{ padding: '10px' }"    >
                    <img style="border-radius: 6px;" :src="user.uavatar" class="image">
                    <div style="padding: 14px;" class="uinfo">
                        <div style="margin-top: 10px;">
                            <span class="userKey">姓名: </span>
                            <span style="color: #f56c6c;">{{ user.uname }}</span>
                        </div>
                        <div>
                            <span class="userKey">账号: </span>
                            <span style="color: #e6a23c;">{{ user.umail }}</span>
                        </div>
                        <div>
                            <span class="userKey">昵称: </span>
                            <span style="color: #694d9f;">{{ user.unickname }}</span>
                        </div>
                        <div>
                            <span class="userKey">电话号码: </span>
                            <span style="color: #409eff;">{{ user.uphone }}</span>
                        </div>
                        <div>
                            <span class="userKey">简介: </span>
                            <span style="color: #67c23a;" :show-overflow-tooltip="true">{{ user.uintroduce }}</span>
                        </div>
                        <div>
                            <span class="userKey">注册时间: </span>
                            <span style="color: #909399;">{{ user.ucretime }}</span>
                        </div>
                        <div v-if="searchMap.uroles == 2 ">
                            <span class="userKey">类型: </span>
                            <span style="color: #c37e00;">{{ user.uroles == 2 ? '管理员' : '普通用户'}}</span>
                        </div>
                        <div v-if="searchMap.uroles == 3">
                            <span class="userKey">类型: </span>
                            <span style="color: #c37e00;">{{ user.uroles == 3 ? 'SuperManager' : user.uroles == 1 ? '普通用户' : '管理员'}}</span>
                        </div>
                        <div v-if="user.udel == 9">
                            <div>
                                <span class="userKey">删除时间: </span>
                                <span style="color: #909399;font-size: 14px;font-weight: 500;line-height: 1.5;">{{ user.udeltime }}</span>
                            </div>
                        </div>
                        <div v-show="user.udel == 1">
                            <span class="userKey">信息修改: </span>
                            <el-switch
                                :disabled="searchMap.uroles != 3 ? (user.uroles == 2 ? true : false) : false"
                                v-model="user.uid"
                                @change="upUserInfoSwitch($event, user.umail)"
                                active-color="#13ce66"
                                inactive-color="#d5c59f">
                            </el-switch>
                        </div>
                        <div v-if="user.udel == 9" >
                            <span class="userKey">恢复用户: </span>
                            <el-switch
                                v-model="user.uid"
                                :disabled="searchMap.uroles != 3 ? (user.uroles == 2 ? true : false) : false"
                                @change="recoverUserSwitch($event, user.umail)"
                                active-color="#13ce66"
                                inactive-color="#ff4949">
                            </el-switch>
                        </div>
                        <div >
                            <div v-show="user.udel == 1">
                                <span class="userKey">删除用户: </span>
                                <el-switch
                                    :disabled="searchMap.uroles != 3 ? (user.uroles == 2 ? true : false) : false"
                                    v-if="searchMap.uroles != 1"
                                    v-model="user.uid"
                                    @change="delUserSwitch($event, user.umail)"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949">
                                </el-switch>
                            </div>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 分页管理 -->
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[9, 18, 27, 36]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            style="margin: 20px 0 50px 71px;">
        </el-pagination>

        <!-- 个人信息修改弹窗 -->
        <el-dialog center title="信息编辑" :before-close="handleDialogUpUser" :visible.sync="dialogUserFormVisible" width="460px" >
          <el-form :model="user" label-position="right" label-width="90px">
            <el-form-item label="账号" prop="umail" >
              <el-input readonly v-model="user.umail"></el-input>
            </el-form-item>
            <el-form-item v-if="user.uroles == 1" label="设为管理员" prop="urolesSwitch">
                <el-switch :disabled="searchMap.uroles != 3" @change="changePrivilege($event, 1)" v-model="user.urolesSwitch"></el-switch>
            </el-form-item>
            <el-form-item v-if="user.uroles == 2" label="撤销管理员" prop="urolesSwitch">
                <el-switch :disabled="searchMap.uroles != 3" @change="changePrivilege($event, 2)" v-model="user.urolesSwitch"></el-switch>
            </el-form-item>
            <el-form-item label="重置密码" prop="upasswordSwitch">
                <el-switch @change="upPassword" v-model="user.upasswordSwitch"></el-switch>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="cancelButton()">取 消</el-button>
            <el-button type="primary" @click="confirmButton()">确 定</el-button>
          </div>
        </el-dialog>

        <!-- 添加用户弹出框 -->
        <el-dialog center title="添加用户" :before-close="handleDialogAddUser" :visible.sync="dialogAddUserFormVisible" width="460px" >
          <el-form :model="addUserForm" status-icon ref="addUserForm" :rules="rules" label-position="right" label-width="90px">
            <el-form-item label="邮箱账号" prop="umail" >
              <el-input v-model="addUserForm.umail"></el-input>
            </el-form-item>
            <el-form-item v-if="searchMap.uroles === 3" label="设为管理员" prop="urolesAddSwitch">
                <el-switch @change="addUserSwitch($event)" v-model="addUserForm.urolesAddSwitch"></el-switch>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="cancelAddButton()">取 消</el-button>
            <el-button type="primary" @click="confirmAddButton('addUserForm')">确 定</el-button>
          </div>
        </el-dialog>

    </div>
    
</template>

<script>
import { getAllUsers, userInfoByUmail, addUser, updateUserInfo, recoverUser, delUserByUmail } from '@/api/userApi'

export default {
    data() {
        const validateaddUmail = (rule, value, callback) => {
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
        return {
            allUsers: [
                {

                }
            ],
            searchMap: {
                uid: this.$store.state.user.userInfo.uid,
                uroles: this.$store.state.user.userInfo.uroles,
                umail: '',
                unickname: ''
            },
            total: 0,
            currentPage: 1,
            pageSize: 9,
            user: {
                uid: '',
                umail: '',
                uroles: '',
                upassword: '',
                urolesSwitch: false,
                upasswordSwitch: false

            },
            addUserForm: {
                umail: '', // 添加用户仅添加用户邮箱即可
                ustatus: 1,
                urolesAddSwitch: false
            },
            rules: {
                umail: [
                    { validator: validateaddUmail, trigger: 'blur'} // 添加校验规则
                ],
            },
            dialogUserFormVisible: false,
            dialogAddUserFormVisible: false
        };
    },

    created() {
        this.fetchData();  
    },

    methods: {
        fetchData(){
            console.log(this.$store.state.user.userInfo.uroles);
            getAllUsers(this.currentPage, this.pageSize, this.searchMap).then(response => {
                const resp = response.data;
                if(resp.flag){
                    this.total = resp.data.total;
                    this.allUsers = resp.data.rows;
                    console.log("allUsers === ");
                    console.log(this.allUsers);
                }
            })
        },
        // 页面数据条数发生变化
        handleSizeChange(val){
            console.log("handleSizeChange" + val);
            this.pageSize = val;
            this.fetchData();
        },
        // 当前页发生改变
        handleCurrentChange(val){
            console.log("handleCurrentChange" + val);
            this.currentPage = val;
            this.fetchData();
        },
        // 清空输入框触发函数
        clearSearchMap(){
            this.$nextTick( () => {
                this.$refs['searchMap'].resetFields();
            });
            this.fetchData();
        },
        // 点击添加用户弹出框的按钮
        addUserButton(){
            this.dialogAddUserFormVisible = true;
        },
        // 点击弹窗右上角X按钮, 修改用户信息
        handleDialogUpUser(){
            this.dialogUserFormVisible = false;
            this.fetchData();
        },
        // 点击弹窗右上角X按钮, 添加用户
        handleDialogAddUser(){
            this.dialogAddUserFormVisible = false;
            this.$nextTick( () => {
                if (this.$refs['addUserForm'] !== undefined) {
                    this.$refs['addUserForm'].resetFields();
                }
            });
        },
        // 弹窗取消按钮, 修改用户信息 
        cancelButton(){
            this.handleDialogUpUser();
        },
        // 弹窗取消按钮, 添加用户
        cancelAddButton(){
            this.handleDialogAddUser();
        },
        // 删除用户
        delUserSwitch(val, umail){
            this.$confirm('此操作将永久删除当前用户', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then( () => {
                // 删除至回收站
                delUserByUmail(umail).then(response => {
                    const resp = response.data;
                    // 删除成功
                    this.$message (
                        {
                            message: resp.message,
                            type: resp.flag ? 'success' : 'danger'
                        }
                    )
                    this.fetchData();
                })
            }).catch( () => {
                // 取消删除的回调函数
                this.$message (
                    {
                        message: '已取消',
                        type: 'info'
                    }
                )
                this.fetchData();
            })
        },
        // Switch 状态改变按钮, 打开修改弹出框
        upUserInfoSwitch(val, umail){
            console.log("val === " + val);
            console.log("umail === " + umail);
            if(val){
                this.dialogUserFormVisible = true;
                userInfoByUmail(umail).then(response => {
                    console.log(response);
                    if(response.data.flag){
                        this.user = response.data.data;
                        console.log(this.user);
                    }
                })
            }
        },
        // 恢复用户
        recoverUserSwitch(val, umail){
            this.$confirm('此操作将恢复当前用户', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'success'
            }).then( () => {
                if(val){
                    recoverUser(umail).then(response => {
                        if(response.data.flag){
                            this.$message({
                                message: '该用户被恢复',
                                type: 'success'
                            })
                            this.fetchData();
                        }else {
                            this.$message({
                                message: '用户恢复失败，请检查系统',
                                type: 'danger'
                            })
                            this.fetchData();
                        }
                    })
                }
            }).catch( () => {
                // 取消删除的回调函数
                this.$message (
                    {
                        message: '已取消',
                        type: 'info'
                    }
                )
                this.fetchData();
            })
        },
        // 权限修改
        changePrivilege(val, roles){
            if(val){
                if(roles == 1){
                    this.$message({
                        message: '设置为管理员',
                        type: 'info',
                        duration: 700,
                    });
                }else{
                    this.$message({
                        message: '撤销管理员',
                        type: 'info',
                        duration: 700,
                    });
                }
            }else{
                this.$message({
                    message: '已取消',
                    type: 'info',
                    duration: 700,
                });
            }
        },
        // 密码修改
        upPassword(){
            console.log("this.upasswordSwitch == " + this.user.upasswordSwitch)
            if(this.user.upasswordSwitch){
                this.$message({
                    message: '密码初始化为 666666 ',
                    type: 'info',
                    duration: 700,
                });
            }else{
                this.$message({
                    message: '已取消',
                    type: 'info',
                    duration: 700,
                });
            }
        },
        // 添加用户设置其为管理员
        addUserSwitch(val){
            if(val){
                this.$message({
                    message: '设置为管理员',
                    type: 'info',
                    duration: 700,
                });
                this.addUserForm.uroles = 2;
            }else{
                this.$message({
                    message: '已取消',
                    type: 'info',
                    duration: 700,
                });
                this.addUserForm.uroles = 1;
            }
        },
        // 确认修改用户信息按钮
        confirmButton(){
            // 普通用户
            if(this.user.uroles == 1){
                if(this.user.urolesSwitch){
                    // 权限变更
                    this.user.uroles = 2;
                }
            }else {
                if(this.user.urolesSwitch){
                    // 权限变更
                    this.user.uroles = 1;
                }
            }
            if(this.user.upasswordSwitch){
                this.user.upassword = 666666;
                
            }
            updateUserInfo(this.user).then(response => {
                const resp = response.data;
                if(resp.flag){
                    this.$message({
                        message: resp.message,
                        type: 'success'
                    });
                    this.fetchData();
                    this.dialogUserFormVisible = false;
                }else {
                    this.$message({
                        message: resp.message,
                        type: 'warning'
                    });
                }
            })
            
        },
        // 确认添加用户按钮
        confirmAddButton(addUserForm){
            this.$refs[addUserForm].validate((valid) => {
                addUser(this.addUserForm).then(response => {
                    if(response.data.flag){
                        // 添加成功
                        this.$message({
                            message: response.data.message,
                            type: 'success',
                        });
                        this.dialogAddUserFormVisible = false;
                        this.fetchData();
                    }else {
                        // 添加失败
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
    },
}
</script>

<style scoped>
.time {
    font-size: 13px;
    color: #999;
  }
  
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 178px;
    height: 178px;
    display: block;
    margin: 0px auto;
    margin-top: 20px;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }
  
  .clearfix:after {
      clear: both
  }
   .uinfo > div > span {
    font-size: 14px;
    font-weight: 500;
    line-height: 1.5;
    color: #a1a3a6;
    margin-right: 7px;
  }
  .userKey {
    font-size: 14px;
    font-weight: 500;
    line-height: 1.5;
    color: #a1a3a6;
    margin-right: 7px;
  }
  .el-switch{
      height: 18px;
  }
</style>