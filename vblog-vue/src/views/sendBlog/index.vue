<template>
    <div>
       <div id="editor">
           <!-- hide-required-asterisk： 默认false, 不显示小红点 * -->
           <el-form :inline="true" size="small" hide-required-asterisk :model="article" :rules="rulesA" ref="articleA" label-width="40px" class="demo-ruleForm">
                <!-- 填写文章标题 -->
                <el-form-item label="标题" prop="title">
                    <el-input style="width: 750px;" v-model="article.title" placeholder="请输入标题" maxlength="50" show-word-limit clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="warning" @click="handleDialog(2, 'articleA')">保存到草稿箱</el-button>
                    <el-button type="danger" @click="handleDialog(1, 'articleA')">发布</el-button>
                </el-form-item>
            </el-form>

            <div>
                <mavon-editor
                    style="height: 735px; width: 100%;"
                    ref="md"
                    v-model="article.mdContent"
                    @imgAdd="$imgAdd"
                    @imgDel="$imgDel" 
                    @change="change"
                    :toolbars = "markdownOption">
                </mavon-editor>
            </div>
            
            <!-- 弹出框 -->
            <el-dialog title="请添加" :visible.sync="outerVisibleArticle" width="500px">
                <el-form :model="article" :rules="rulesB" ref="articleB" label-width="100px" class="demo-ruleForm" >
                    <el-form-item label="分类选择" prop="categoryName" >
                        <!-- 添加分类 -->
                        <el-input readonly @click.native="innerVisibleCategory = true" style="width: 160px;" v-model="article.categoryName" placeholder="请选择分类"></el-input>
                    </el-form-item>
                    <el-form-item class="tagsItem" label="添加标签" prop="tagList">
                        <!-- 添加标签 -->
                        <el-tag
                            v-for="(item, index) in article.tagList"
                            :key="index"
                            v-show="item != null"
                            closable
                            :disable-transitions="false"
                            @close="handleClose(item)">
                            {{ item }}
                        </el-tag>
                        <el-input
                            class="input-new-tag"
                            v-if="inputVisible"
                            v-model="inputValue"
                            ref="saveTagInput"
                            size="small"
                            maxlength="6"
                            clearable
                            @keyup.enter.native="handleInputConfirm"
                            @blur="handleInputConfirm"
                            >
                        </el-input>
                        <el-button size="small" style="color:#b7ba6b;" v-else class="button-new-tag" @click="showInput" >+ 添加标签</el-button>
                    </el-form-item>
                    <el-form-item label="设为私密" prop="aprivate" v-show="!article.aprivate">
                        <el-switch v-model="article.aprivate"></el-switch>
                    </el-form-item>
                    <el-form-item label="取消私密" prop="aprivate" v-show="article.aprivate">
                        <el-switch v-model="article.aprivate"></el-switch>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="cancleButton()">取消</el-button>
                        <el-button type="primary" @click="confirmButton('articleB')">确定</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>

            <!-- 分类选择子组件弹出框 -->
            <el-dialog title="分类选择" :visible.sync="innerVisibleCategory">
                <blog-category @select-category="selectCategory" :isDialog="true"></blog-category>
            </el-dialog>

      </div>


    </div>
</template>

<script>
import { addUpArticle, getArticleById } from '@/api/articleApi'
import { imgUpload } from '@/api/imgUpApi'
import { delTagsByAid } from '@/api/tagsApi'
// 导入子组件
import BlogCategory from '@/views/blogSettings/blogCategory'

export default {
    // 注册子组件
    components: {
        BlogCategory
    },
    data() {
      return {
        article: {
          aUid: this.$store.state.user.userInfo.uid, // 更新时不为 null
          aid: '',
          title: '',
          mdContent: '',
          htmlContent: '',
          aState: '', // 正式发布 1， 草稿箱 0
          aCategory: '', // 文章分类id,
          category: {
            category: ''
          },
          categoryName: '',
          tagList: [],
          tags: [
              {
                  tid: '',
                  tag: ''
              }
          ],
          aprivate: false, // 默认不是私密文章
        },
        inputVisible: false,
        inputValue: '',
        markdownOption: {
            bold: true, // 粗体
            italic: true, // 斜体
            header: true, // 标题
            underline: true, // 下划线
            strikethrough: true, // 中划线
            mark: true, // 标记
            superscript: true, // 上角标
            subscript: true, // 下角标
            quote: true, // 引用
            ol: true, // 有序列表
            ul: true, // 无序列表
            link: true, // 链接
            imagelink: true, // 图片链接
            code: true, // code
            table: true, // 表格
            fullscreen: true, // 全屏编辑
            readmodel: false, // 沉浸式阅读
            htmlcode: true, // 展示html源码
            help: true, // 帮助
            /* 1.3.5 */
            undo: true, // 上一步
            redo: true, // 下一步
            trash: true, // 清空
            save: false, // 保存（触发events中的save事件）
            /* 1.4.2 */
            navigation: true, // 导航目录
            /* 2.1.8 */
            alignleft: true, // 左对齐
            aligncenter: true, // 居中
            alignright: true, // 右对齐
            /* 2.2.1 */
            subfield: true, // 单双栏模式
            preview: true, // 预览
        },
        outerVisibleArticle: false,
        innerVisibleCategory: false,
        rulesA: {
            title: [{required: true, message: '标题不能为空', trigger: 'blur'}]
        },
        rulesB: {
            categoryName: [{required: true, message: '分类不能为空', trigger: 'change'}],
        }
      }
    },

    created() {
        this.fetchData();
    },

    activated() {
        if(this.$route.query.editId){
            this.fetchData();
        }
    },

    methods: {
        // 编辑博客需要查询的数据
        fetchData(){
            this.article.aid = this.$route.query.editId;
            console.log("aid === " + this.article.aid);
            if(this.article.aid != null) {
                getArticleById(this.article.aid).then(response => {
                    console.log(response)
                    if(response.data.flag){
                        this.article = response.data.data;
                        console.log("this.article.aCategory ==== " + this.article.aCategory);
                        if(response.data.data.aprivate == 1){
                            this.article.aprivate = true;
                        }else{
                            this.article.aprivate = false;
                        }
                        // 将 tags.tag数组中的标签循环给数组tagList
                        var num = [];
                        this.article.tags.forEach(item => {
                            num.push(item.tag);
                            console.log("sendBLog + fetchData === " + num);
                        })
                        this.article.tagList = num;
                        console.log("this.article.tagList === " + this.article.tagList);
                        this.article.categoryName = this.article.category.category;
                        console.log("sendBlog === " + this.article);
                    }else{
                        this.$message({
                            message: response.data.message,
                            type: 'warning'
                        });
                    }
                })
            } 
        },
        // 打开弹出窗口并清除上次输入的数据
        closeClearData() {
            this.outerVisibleArticle = false;
            // 另外一种清除数据的方法，data 中未定义 pojo 初始数据, 且不能进行字段名的绑定，否则会造成有时候无法输入的问题
            // this.pojo = {}
            if(this.article.aid != null){
                this.$nextTick( () => {
                    // this.$nextTick() 它是一个异步事件，当渲染结束之后，它的回调函数才会被执行
                    // 弹出窗口打开之后，需要加载 DOM ,需要一定的时间，我们就应该等待它加载完 DOM 之后，再进行调用 resetFields() 方法，重置表单和清除校验样式
                    // 调用 element 的 resetFields() 函数,因为此时没有表单数据进行传入，所以直接获取表单的 id(ref) 名来重置表单，前提是需要在 data 中对 article 进行初始化数据
                    this.$refs['articleB'].resetFields();
                })
            }
        },
        // 取消文章发布按钮
        cancleButton(){
            this.closeClearData();
        },
        // 打开发布到 草稿箱 或 正式发布 的弹出框
        handleDialog(state, articleA){
            this.$refs[articleA].validate((valid) => {
                if(valid){
                    this.article.aState = state;
                    this.outerVisibleArticle = true;
                }else{
                    return false;
                }
            })
        },
        // 确认文章发布按钮
        confirmButton(articleB){
            this.$refs[articleB].validate((valid) => {
                if(valid){
                    console.log("this.article.aprivate11 === "+ this.article.aprivate);
                    if(this.article.aprivate){
                        this.article.aprivate = 1;
                    }else{
                        this.article.aprivate = 2;
                    }
                    console.log("this.article.aprivate22 === "+ this.article.aprivate);
                    if(this.article.tagList != undefined  && this.article.tagList.length == 0){
                        this.$message ({
                            message: '请添加标签',
                            type: 'info'
                        });
                        return false;
                    }else{
                        addUpArticle( this.article ).then(response => {
                            if(response.data.flag){
                                this.$message({
                                    message: response.data.message,
                                    type: 'success'
                                });
                                // 关闭弹窗
                                this.outerVisibleArticle = false;
                                this.article.aprivate = false;
                                console.log("this.article.aprivate33 === "+ this.article.aprivate);
                                this.fetchData();
                                // 跳转到博客列表
                                this.$router.push({ path: '/blogList'});
                                // this.$router.replace({ path: '/blogList'});
                            }else {
                                this.$message({
                                    message: response.data.message,
                                    type: 'warning'
                                });
                            }
                        })
                    }
                }else{
                    return false;
                }
            })
            
            
        },
        change(value, render){
            // 第二种方法，将写的文字即时转为html
            // this.$refs.md.d_render,可以不适使用这种方法转换
            // 在data中定义htmlContent，将mdContent实时转换html
            this.article.htmlContent = render;
        },
        $imgAdd(pos, $file){
            // 第一步.将图片上传到服务器.
           var formdata = new FormData();
           formdata.append('image', $file);
           const is8M = $file.size / 1024 / 1024 < 8;
           console.log("formdata" + formdata == null);
           console.log("is8M" + is8M);
            if (!is8M) {
                this.$message.error('上传头像图片大小不能超过 8MB!');
                return false;
            }
           imgUpload(formdata).then(response => {
               var url = response.data.data;
               console.log("url " + url);
               this.$refs.md.$img2Url(pos, url);
           })
        },
        $imgDel(pos){
            
        },

        // 添加标签
        handleClose(tag) {
            console.log("tag === " + tag);
            this.article.tagList.splice(this.article.tagList.indexOf(tag), 1);
        },

        showInput() {
            if(this.article.tagList != undefined  && this.article.tagList.length > 4){
                this.$message ({
                    message: '最多能添加5个标签',
                    type: 'info'
                });
                return false;
            }else{
                this.inputVisible = true;
                this.$nextTick(_ => {
                    this.$refs.saveTagInput.$refs.input.focus();
                });
            }
        },

        handleInputConfirm() {
            let inputValue = this.inputValue;
            if (inputValue) {
                this.article.tagList.push(inputValue);
            }
            this.inputVisible = false;
            this.inputValue = '';
        },
        // 父子组件传递数据
        selectCategory(currentRow){
            console.log("父组件接收当前行的数据：=== " + currentRow);
            this.article.aCategory = currentRow.cid;
            console.log("this.article.aCategory ==== " + this.article.aCategory);
            this.article.categoryName = currentRow.category; 
            this.innerVisibleCategory = false; // 关闭子组件弹出窗
        }
    },
    
}
</script>

<style >
/* 标签 */
.el-tag{
    margin-left: 10px;    
}
.el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
.tagsItem{
    margin-left: -10px;
}
.tagsItem .el-form-item__label{
    padding-right: 2px!important;
}
</style>