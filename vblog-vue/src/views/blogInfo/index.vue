<template>
    <div>
        <div v-show="article.aid != null" class="editContainer">
            <div class="info">
                <!-- 文章标题 -->
                <div style="margin-bottom: 20px;"><h1>{{ article.title }}</h1></div>

                <el-collapse accordion>
                    <el-collapse-item>
                        <template slot="title">
                            <!-- 文章类型 && 文章标签 -->
                            <span style="margin: 0 10px 0 0;">类型:</span>
                                <el-tag
                                    size="mini"
                                    :key="99998"
                                    :type="(article.category.cid % 10) > 1 ? 
                                    ( (article.category.cid % 10) > 3 ? 
                                    ( (article.category.cid % 10) > 5 ? 
                                    ( (article.category.cid % 10) > 7 ? 
                                    ( (article.category.cid % 10) > 9 ? 'danger' : 'danger' ) : 'warning' ) : 'info' ) : 'success' ) : 'primary'"
                                    effect="dark">
                                    {{ article.category.category }}
                                </el-tag>
                            <span style="margin: 0 10px 0 12px;">标签:</span>
                                <el-tag
                                    v-for="item in article.tags"
                                    :key="item.tid + '100000'"
                                    size="mini"
                                    :type="(item.tid % 10) > 1 ? 
                                    ( (item.tid % 10) > 3 ? 
                                    ( (item.tid % 10) > 5 ? 
                                    ( (item.tid % 10) > 7 ? 
                                    ( (item.tid % 10) > 9 ? 'danger' : 'danger' ) : 'warning' ) : 'info' ) : 'success' ) : 'primary'"
                                    effect="plain">
                                    {{ item.tag }}
                                </el-tag>
                            <el-tag
                                style="margin-left: 12px;"
                                size="mini"
                                :key="99999"
                                :type="article.aState == 1 ? 'success' : 'warning'"
                                effect="dark">
                                {{ article.aState == 1 ? '已发布' : '草稿箱' }}
                            </el-tag>
                            <span style="margin: 0 10px 0 12px;">创建时间:</span>
                            <span >{{ article.createDate }}</span>
                            <el-tag
                                v-show="article.user.uroles === 2"
                                style="margin-left: 12px;"
                                size="mini"
                                :key="article.aid"
                                type="danger"
                                effect="dark">
                                管理员
                            </el-tag>
                        </template>
                        <!-- 用户信息 -->
                        <div>
                            <!-- 用户头像 -->
                            <div class="avator"><img v-bind:src="article.user.uavatar"></div>
                            <!-- 用户信息 -->
                            <div class="nameMail">
                                <div>昵称：{{ article.user.unickname }}</div>
                                <div>账户：{{ article.user.umail }}</div>
                            </div>
                        </div>
                        <!-- 用户简介 -->
                        <div class="uintroduce">
                            简介：{{ article.user.uintroduce }}
                        </div>
                    </el-collapse-item>
                </el-collapse>
                
            </div>
            <!-- 分割线 -->
            <div class="hr"></div>
            <mavon-editor
                v-model = "article.htmlContent"
                ref = "md"
                :ishljs = "true"
                :subfield = "props.subfield"
                :defaultOpen = "props.defaultOpen"
                :toolbarsFlag = "props.toolbarsFlag"
                :editable = "props.editable"
                :scrollStyle = "props.scrollStyle"
                >
            </mavon-editor>
            <!-- 文章发布时间 -->
            <div class="publishDate">最后发布于：{{ article.publishDate }}</div>
        </div>
        <div v-show="article.aid == null" style="font-size: 16px; color: #d3c6a6;text-align: center;margin-top:100px;">
            请选择一篇博客进行查看
        </div>
    </div>
    
</template>

<script>
import { getArticleById } from '@/api/articleApi'

export default {

    data(){
        return {
             // 查看详情
            // 定义的数据
            article: {
                aid: this.$route.query.viewId,
                title: '',
                mdContent: '还没数据呢',
                htmlContent: '',
                aState: '',
                createData: '',
                publishDate: '',
                user: {
                    umail: '',
                    unickname: '',
                    uintroduce: '',
                    uavatar: ''
                },
                tags: [
                    {
                        tid: '',
                        tag: '',
                    }
                ],
                category: {
                    cid: '',
                    category: ''
                }
            },
            props: {
                subfield: false,// 单双栏模式
                defaultOpen: 'preview',//edit： 默认展示编辑区域 ， preview： 默认展示预览区域 
                editable: false,
                toolbarsFlag: false,
                scrollStyle: true
            },
        }
    },

    created() {
        this.fetchData(this.$route.query.viewId);
    },

    activated() {
        if(this.$route.query.viewId != null){
            this.fetchData(this.$route.query.viewId);
        }
    },

   methods: {
        fetchData(aid){
            this.article.aid = aid;
            console.log("aid === " + this.article.aid);
            if(this.article.aid != null) {
                getArticleById(this.article.aid).then(response => {
                    console.log(response)
                    if(response.data.flag){
                        this.article = response.data.data;
                        console.log("1 === " + this.article);
                        // this.article.mdContent = marked(this.article.mdContent);
                    }else{
                        this.$message({
                            message: response.data.message,
                            type: 'warning'
                        });
                    }
                })
            } 
        },

   },

}
</script>

<style scoped>
.shadow{
    box-shadow: none!important;
    padding-right: 150px!important;
}
.editContainer{
    /* padding: 8px 25px 15px 25px; */
    background-color: #fbfbfb;
}
.info{
    padding: 8px 30px 15px 30px;
}
.hr {
    height: 1.5px;
    margin: -15px 25px 10px 25px;
    background-image: linear-gradient(to right, #12c2e9 ,#c471ed,#f64f59);
}
.el-tag {
    margin-right: 6px!important;
}
.publishDate{
    float: right;
    margin: 20px 175px 136px 0;
    font-size: 13px;
    color: #1b315e;
}
.avator {
    float: left;
}
.avator > img {
    width:45px;
    height: 45px;
    border-radius: 50%;
}
.nameMail {
    float: left;
    margin: 1px 0 0 15px;
}
.uintroduce {
    clear: both;
    margin-left: 60px;
}
</style>