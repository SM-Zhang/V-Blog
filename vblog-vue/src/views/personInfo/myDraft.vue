<template>
    <div>
            
        <el-form
            style="margin-top: 5px;"
            ref="searchMap"
            :inline="true"
            :model="searchMap">
            <!-- 文章查询(效率太低), 只针对标题进行查询 -->    
            <el-form-item prop="title" >
                <el-input size="medium" v-model="searchMap.title" placeholder="关键字"></el-input>
            </el-form-item> 
            <!-- 文章类型查询 -->
            <el-form-item prop="category">
                <el-input size="medium" v-model="searchMap.category" placeholder="类型"></el-input>
            </el-form-item>
            <el-form-item >
                <!-- 查询按钮 -->
                <el-button style="padding: 11px 21px;" type="primary" icon="el-icon-search" @click="fetchData()" >查询</el-button>
                <!-- 重置按钮 -->
                <el-button style="padding: 11px 24px;" @click="clearSearchMap()" >重置</el-button>
            </el-form-item>
        </el-form>
        
        <!-- 博客列表展示 -->
        <el-table  stripe :inline="true" :data="allArticle" height="450" empty-text="暂时还没客官想要看的呢/刷新重试?" border style="width: 100%" >
            <el-table-column type="index" label="序号" width="50"></el-table-column>
            <el-table-column prop="user.unickname" label="昵称"></el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="title" label="标题"></el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="summary" label="内容摘要"></el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="category.category" label="类型"></el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="tags" label="标签">
                <template slot-scope="scope">
                    <el-tag
                        size="mini"
                        v-for="item in scope.row.tags"
                        :key="item.tid"
                        :type="(item.tid % 10) > 1 ? 
                                ( (item.tid % 10) > 3 ? 
                                ( (item.tid % 10) > 5 ? 
                                ( (item.tid % 10) > 7 ? 
                                ( (item.tid % 10) > 9 ? 'danger' : 'danger' ) : 'warning' ) : 'info' ) : 'success' ) : 'primary'"
                        effect="plain">
                        {{ item.tag }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column width="70" prop="aprivate" label="私密">
                <template slot-scope="scope">
                    <el-tag
                        v-if="scope.row.aprivate == 1"
                        size="mini"
                        type="danger"
                        effect="dark">
                        是
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="publishDate" sortable label="发布时间"></el-table-column>
            
            <el-table-column label="操作" width="145" >
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        @click="handleEdit(scope.row.aid)">编辑</el-button>
                    <el-button
                        size="mini"
                        type="danger"
                        @click="handleDeleteButton(scope.row.aid)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页管理 -->
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 30, 40]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            style="margin: 20px 0 50px 0;">
        </el-pagination>
        
    </div>
    
</template>

<script>
import { getAllArticles, delUpArticleStateById } from '@/api/articleApi'

export default {
    data() {
      return {
        searchMap: {
            title: '',
            unickname: '',
            category: '',
            uroles: this.$store.state.user.userInfo.uroles,
            uid: this.$store.state.user.userInfo.uid,
            aState: 2
        },
        allArticle: [{
            id: '',
            user: '',
            title: '',
            summary: '',
            category: '',
            tags: '',
            publishDate: ''
        }],
        total: 0,
        currentPage: 1,
        pageSize: 10,
      }
    },

    created() {
        this.fetchData();
    },

    methods: {
        fetchData(){
            getAllArticles(this.currentPage, this.pageSize, this.searchMap)
            .then(response => {
                const resp = response.data;
                this.total = resp.data.total;
                this.allArticle = resp.data.rows;
                console.log(this.allArticle);
            })
        },
        handleSizeChange(val){
            console.log("handleSizeChange" + val);
            this.pageSize = val;
            this.fetchData();
        },
        handleCurrentChange(val){
            console.log("handleCurrentChange" + val);
            this.currentPage = val;
            this.fetchData();
        },
        // 清除searchMap表单
        clearSearchMap(){
            this.$nextTick( () => {
                this.$refs['searchMap'].resetFields();
            });
            this.fetchData();
        },
        // 博客编辑
        handleEdit(id){
            this.$router.push({
                path: '/sendBlog',
                query: {
                    editId: id
                }
            })
        },
        // 打开删除弹出框
        handleDeleteButton(id){
            this.$confirm('此操作将永久删除当前信息', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then( () => {
                // 删除至回收站
                delUpArticleStateById(id, 3).then(response => {
                    const resp = response.data;
                    if(resp.flag){
                         this.fetchData();
                    }
                    // 删除成功
                    this.$message (
                        {
                            message: resp.message,
                            type: resp.flag ? 'success' : 'danger'
                        }
                    )
                })
            }).catch( () => {
                // 取消删除的回调函数
                this.$message (
                    {
                        message: '已取消',
                        type: 'info'
                    }
                )
            })
        },

    }
}
    
</script>

<style scoped>
/* 设置单行显示不提示 */
 .el-tooltip__popper.is-dark{
     display:none !important;
}
.el-tag {
    margin-right: 4px!important;
}
</style>

