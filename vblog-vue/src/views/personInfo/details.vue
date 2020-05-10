<template>
    <!-- 用户的信息放在仓库中，即可以从仓库中获取用户信息，需要进行复用 -->
    <!-- 
        1. 用户昵称
        2. 用户姓名
        3. 电子邮箱
        4. 头像
        5. 电话号码
        6. 用户简介
        7. 创建时间
        8. 用户角色(非管理人员不可见)
     -->
    <div class="card" style="">

        <el-upload
            class="avatar-uploader"
            accept="image/jpeg,image/jpg,image/png"
            :action="'/dev-api/user/uploadAvatar/' + user.uid"
            name="uavatar"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="user.uavatar" :src="user.uavatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>

        <div class="imgTips">
          <span>点击图片即可上传头像哦</span>
        </div>

        <div class="uinfo" >
              
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
            <span style="color: #67c23a;">{{ user.uintroduce }}</span>
          </div>
          <div>
            <span class="userKey">注册时间: </span>
            <span style="color: #909399;">{{ user.ucretime }}</span>
          </div>
          <div v-if="user.uroles == 2 ">
            <span class="userKey">类型: </span>
            <span style="color: #dc3545;">{{ user.uroles == 2 ? '管理员' : '普通用户'}}</span>
          </div>
          <div v-if="user.uroles == 3">
            <span class="userKey">类型: </span>
            <span style="color: #dc3545;">{{ user.uroles == 3 ? 'SuperManager' : user.uroles == 2 ? '管理员' : '普通用户'}}</span>
          </div>
          <div>
            <span class="userKey">信息修改: </span>
            <el-switch
              v-model="editInfo"
              :change="change(editInfo)"
              active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </div>

        </div>

        <!-- 个人信息修改弹窗 -->
        <el-dialog center title="信息编辑" :before-close="handleDialogClose" :visible.sync="dialogUserFormVisible" width="520px" >
          <el-form :model="user" label-position="right" :rules="rules" label-width="80px">
            <el-form-item label="账号" prop="umail" >
              <el-input readonly v-model="user.umail"></el-input>
            </el-form-item>
            <el-form-item label="姓名" prop="uname" >
              <el-input maxlength="8" show-word-limit clearable v-model="user.uname"></el-input>
            </el-form-item>
            <el-form-item label="昵称" prop="unickname" >
              <el-input maxlength="10" show-word-limit clearable v-model="user.unickname"></el-input>
            </el-form-item>
            <el-form-item label="电话号码" prop="uphone" >
              <el-input maxlength="11" show-word-limit clearable v-model="user.uphone"></el-input>
            </el-form-item>
            <el-form-item label="简介" prop="uintroduce" >
              <el-input
                type="textarea"
                autosize
                placeholder="请输入内容"
                clearable
                maxlength="80"
                show-word-limit
                v-model="user.uintroduce">
              </el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="cancelButton()">取 消</el-button>
            <el-button type="primary" @click="confirmButton()">确 定</el-button>
          </div>
        </el-dialog>


    </div>
</template>

<script>

export default {
    data() {
      return {
        user: this.$store.state.user.userInfo,
        // ,
        // uid: store.state.user.userInfo.uid,
        editInfo: false,
        dialogUserFormVisible: false,
        rules: {
          uname: [{
            required: true, message: '姓名不能为空', trigger: 'blur'
          }],
          unickname: [{
            required: true, message: '昵称不能为空', trigger: 'blur'
          }],
          uphone: [{
            required: true, message: '电话号码不能为空', trigger: 'blur'
          }],
          uintroduce: [{
            required: true, message: '简介不能为空', trigger: 'blur'
          }]
        }
      };
    },
    
    methods: {
      handleAvatarSuccess(res, file) {
        this.user.uavatar = URL.createObjectURL(file.raw);
        // 将更新后的信息存入本地
        console.log("====================")
        console.log(res.data);
        this.$store.dispatch('SetUserInfo', res.data);
      },
      beforeAvatarUpload(file) {
        console.log("file.type == " + file.type);
        const isJPEG = file.type === 'image/jpeg';
        const isJPG = file.type === 'image/jpg';
        const isPNG = file.type === 'image/png';
        const isLt8M = file.size / 1024 / 1024 < 8;

        if (!isJPEG && !isJPG && !isPNG) {
          this.$message.error('上传头像图片支持JPEG/JPG/PNG格式!');
        }
        if (!isLt8M) {
          this.$message.error('上传头像图片大小不能超过 8MB!');
        }
        return isJPG || isJPEG || isPNG && isLt8M;
      },
      change(editInfo){
        console.log("editInfo === " + editInfo);
        if(editInfo){
          this.dialogUserFormVisible = true;
        }
      },
      handleDialogClose(){
        this.editInfo = false;
        this.dialogUserFormVisible = false;
        this.user = JSON.parse(localStorage.getItem("userInfo"));
      },
      cancelButton(){
        this.handleDialogClose();
      },
      confirmButton(){
        this.$store.dispatch('UpdateUserInfo', this.user).then(response => {
          if(response.flag){
            this.$message({
              message: response.message,
              type: "success"
            })
          }else{
            this.$message({
              message: response.message,
              type: "warning"
            })
          }
          this.editInfo = false;
          this.dialogUserFormVisible = false;
        })
      }
    }
}
</script>

<style scoped>
.card {
  margin-top: 6px;
  margin-left: 100px;
  width:248px;
  padding: 20px 30px 20px 98px;
  border: 1px solid #e3e3e3;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 2px 12px 0px;
}
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    border: 1px dashed #d9d9d9;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar-uploader-icon:hover {
    border-color: #409EFF;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }

  .imgTips{
    margin-top: -20px; 
    line-height: 18px; 
    font-size: 10px;
    color: #d8d8d8;
    margin-left: 37px;
  }
  .uinfo > div > span {
    font-size: 14px;
    font-weight: 500;
    line-height: 1.5;
    color: #a1a3a6;
    margin-right: 7px;
  }
</style>