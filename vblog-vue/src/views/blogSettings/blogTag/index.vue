<template>
    <div>
        <div>
            <el-form :inline="true" size="small" class="demo-form-inline" ref="form" :model="searchForm" >
                <el-form-item >
                    <el-input
                        v-model="searchForm.tag"
                        maxlength="10"
                        show-word-limit 
                        clearable
                        style="width: 240px;"
                        placeholder="输入关键字搜索"/>
                </el-form-item>
                <!-- 查询按钮 -->
                <el-form-item >
                    <el-button style="margin-left: 10px;" type="primary" icon="el-icon-search" @click="fetchData()" >搜索</el-button>
                </el-form-item >
            </el-form>
                            
            <el-table stripe :inline="true" :data="allTags" height="450" border style="width: 100%" >
                <el-table-column type="index" width="100px" label="序号"></el-table-column>
                <el-table-column prop="tag" label="标签"></el-table-column>
                <el-table-column 
                    prop="tDel" 
                    label="是否删除(当前页)" 
                    :filters="[{ text: '已删除', value: 9 }, { text: '未删除', value: 1 }]"
                    :filter-method="filterTag"
                    filter-placement="bottom-end">
                    <template slot-scope="scope">
                        <span style="color: #f56c6c">{{ scope.row.tDel == 9 ? '已删除' : '' }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" >
                    <template slot-scope="scope" >
                        <el-button
                            v-show="scope.row.tDel == 1"
                            size="mini"
                            @click="handleEdit(scope.row.tid)">编辑</el-button>
                        <el-button
                            v-show="scope.row.tDel == 1"
                            size="mini"
                            type="danger"
                            @click="handleDelete(scope.row.tid)">删除</el-button>
                        <el-button
                            v-show="scope.row.tDel == 9"
                            size="mini"
                            type="info" plain disabled>不可操作</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页数据 -->
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentPageChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 30, 40]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                style="margin-top: 20px;">
            </el-pagination>
        </div>

        <!-- 打开修改标签嵌套表单 -->
        <el-dialog title="标签编辑" :visible.sync="dialogFormVisible" width="500px" >
            <el-form ref="tagForm" :model="tagForm" label-width="100px" label-position="right" style="width: 400px;" :rules="rules">
                <el-form-item label="标签" prop="tag">
                    <el-input 
                        v-model="tagForm.tag"
                        maxlength="10" 
                        show-word-limit 
                        clearable 
                          ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateButton('tagForm')">确 定</el-button>
            </div>
        </el-dialog>

    </div>
    
</template>

<script>
import {getTagByTid, delTagsByTid, getTags, updateTag} from "@/api/tagsApi"

export default {
    data(){
        return {
            allTags: [
                {
                    tid: '',
                    tag: '',
                    tDel: ''
                },
            ],
            searchForm: {
                tag: '',
                tDel: ''
            },
            total: 0,
            currentPage: 1,
            pageSize: 10,
            dialogFormVisible: false,
            tagForm: {
                tag: ''
            },
            // 绑定字段，进行输入规则校验
            rules: {
                tag: [
                    { required: true, message: '标签不能为空', trigger: 'blur' },
                ],
            },
        }
    },
    
    created() {
        this.fetchData();
    },

    methods: {
        // 查询
        fetchData(){
            getTags(this.currentPage, this.pageSize, this.searchForm).then(response => {
                const resp = response.data;
                if(resp.flag){
                    this.total = resp.data.total;
                    this.allTags = resp.data.rows;
                    console.log(this.allTags);
                }
            })
        },
        // 打开弹出窗口并清除上次输入的数据
        openClearData() {
            this.dialogFormVisible = true;
            // 另外一种清除数据的方法，data 中未定义 pojo 初始数据, 且不能进行字段名的绑定，否则会造成有时候无法输入的问题
            // this.pojo = {}
            this.$nextTick( () => {
                // this.$nextTick() 它是一个异步事件，当渲染结束之后，它的回调函数才会被执行
                // 弹出窗口打开之后，需要加载 DOM ,需要一定的时间，我们就应该等待它加载完 DOM 之后，再进行调用 resetFields() 方法，重置表单和清除校验样式
                // 调用 element 的 resetFields() 函数,因为此时没有表单数据进行传入，所以直接获取表单的 id(ref) 名来重置表单，前提是需要在 data 中对 pojo 进行初始化数据
                this.$refs['tagForm'].resetFields();
            })
        },
        // 打开编辑窗口
        handleEdit(tid){
            // 打开弹窗，清除数据
            this.openClearData();
            getTagByTid(tid).then(response => {
                const resp = response.data;
                if(resp.flag){
                    this.tagForm = resp.data;
                }else {
                    this.$message(
                        {
                            message: '当前数据异常或不存在',
                            type: 'warning'
                        }
                    )
                }
            })
        },
        // 删除
        handleDelete(tid){
            this.$confirm('此操作将永久删除当前信息', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then( () => {
                delTagsBytid(cid).then(response => {
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
        // 页面大小改变
        handleSizeChange(val){
            this.pageSize = val;
            this.fetchData();
        },
        // 当前页改变
        handleCurrentPageChange(val){
            this.currentPage = val;
            this.fetchData();
        },
        // 修改数据按钮
        updateButton(tagForm){
            this.$refs[tagForm].validate( valid => {
                if(valid){
                    // 校验成功，可以更新数据
                    updateTag(this.tagForm).then(response => {
                        if(response.data.flag){
                            // 更新成功
                            this.tagForm.tid = null;
                            this.fetchData();
                            this.dialogFormVisible = false;
                            this.$message (
                                {
                                    message: '修改成功',
                                    type: 'success'
                                }
                            );
                        }else {
                            this.$message (
                                {
                                    message: '修改失败',
                                    type: 'warning'
                                }
                            );
                        }
                    })
                }else {
                    return false;
                }
            })
        },
        filterTag(value, row) {
            return row.tDel === value;
        },
       
    },
}
</script>

<style scoped>

</style>