<template>
    <div>
        <div v-if="allCategory">
            <el-form :inline="true" size="small" class="demo-form-inline" ref="form" :model="searchForm" >
                <el-form-item >
                    <el-input
                        v-model="searchForm.category"
                        maxlength="10"
                        show-word-limit 
                        clearable
                        style="width: 240px;"
                        placeholder="输入关键字搜索"/>
                </el-form-item>
                <!-- 查询按钮 -->
                <el-form-item >
                    <el-button style="margin-left: 10px;" type="primary" icon="el-icon-search" @click="fetchData()" >查询</el-button>
                    <!-- 添加按钮 -->
                    <el-button v-if="!isDialog" style="margin-left: 10px;" type="primary" icon="el-icon-plus" @click="addCategory()" >添加</el-button>
                </el-form-item >
            </el-form>
                            
            <el-table :highlight-current-row="isDialog" @current-change="handleCurrentChange" stripe :inline="true" :data="allCategory" height="450" border style="width: 100%" >
                <el-table-column type="index" width="100px" label="序号"></el-table-column>
                <el-table-column prop="category" label="博客类型"></el-table-column>
                <el-table-column label="操作" v-if="!isDialog">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            @click="handleEdit(scope.row.cid)">编辑</el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            @click="handleDelete(scope.row.cid)">删除</el-button>
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
                :layout="!isDialog ? 'total, sizes, prev, pager, next, jumper' : 'total, prev, pager, next'"
                :total="total"
                style="margin-top: 20px;">
            </el-pagination>
        </div>
        <!-- 无数据情况下进行提示的信息 -->
        <div v-else style="text-align: center; color: #a1a3a6;margin-top: 100px;">
             暂时还没客官想要看的呢
        </div>

        <!-- 打开新增或修改嵌套表单 -->
        <el-dialog title="分类编辑" :visible.sync="dialogFormVisible" width="500px" >
            <el-form ref="categoryForm" :model="categoryForm" label-width="100px" label-position="right" style="width: 400px;" :rules="rules">
                <el-form-item label="分类名" prop="category">
                    <el-input 
                        v-model="categoryForm.category"
                        maxlength="10" 
                        show-word-limit 
                        clearable 
                          ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="categoryForm.cid === null ? addButton('categoryForm') : updateButton('categoryForm')">确 定</el-button>
            </div>
        </el-dialog>
        
    </div>
    
</template>

<script>
import categoryApi from "@/api/categoryApi"

export default {
    props: {
        isDialog: Boolean
    },
    data(){
        return {
            allCategory: [
                {
                    cid: '',
                    category: ''
                },
            ],
            categoryForm: {
                cid: null,
                category: ''
            },
            searchForm: {
                category: ''
            },
            total: 0,
            currentPage: 1,
            pageSize: 10,
            dialogFormVisible: false,
            // 绑定字段，进行输入规则校验
            rules: {
                category: [
                    { required: true, message: '分类名不能为空', trigger: 'blur' },
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
            categoryApi.getCategories(this.currentPage, this.pageSize, this.searchForm).then(response => {
                const resp = response.data;
                if(resp.flag){
                    this.total = resp.data.total;
                    this.allCategory = resp.data.rows;
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
                this.$refs['categoryForm'].resetFields();
            })
        },
        // 打开编辑窗口
        handleEdit(cid){
            // 打开弹窗，清除数据
            this.openClearData();
            categoryApi.getCategoryById(cid).then(response => {
                const resp = response.data;
                if(resp.flag){
                    this.categoryForm = resp.data;
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
        handleDelete(cid){
            this.$confirm('此操作将永久删除当前信息', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then( () => {
                categoryApi.delCategoryById(cid).then(response => {
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
        // 打开添加窗口
        addCategory(){
            // 打开弹窗，清除数据
            this.categoryForm.cid = null;
            this.openClearData();
        },
        // 添加数据按钮
        addButton(categoryForm){
            this.$refs[categoryForm].validate((valid) => {
                if(valid){
                    // 注意添加数据时没有对应的 id , 所以需要进行转换，否则会默认使用第一个参数进行封装
                    // const cf = this.categoryForm;
                    categoryApi.addCategory(this.categoryForm).then(response => {
                        const resp = response.data;
                        if(resp.flag){
                            // 关闭弹窗
                            this.dialogFormVisible = false;
                            // 添加成功，刷新数据
                            this.fetchData();
                            // 弹出提示
                            this.$message (
                                {
                                    message: '添加成功',
                                    type: 'success'
                                }
                            );
                        }else{
                            this.$message (
                                {
                                    message: '添加失败，请重新添加',
                                    type: 'danger'
                                }
                            )
                        }
                    })
                }else{
                    // 校验失败，禁止添加
                    return false;
                }
            })
        },
        // 修改数据按钮
        updateButton(categoryForm){
            this.$refs[categoryForm].validate( valid => {
                if(valid){
                    // 校验成功，可以更新数据
                    categoryApi.updateCategoryById(this.categoryForm).then(response => {
                        if(response.data.flag){
                            // 更新成功
                            this.categoryForm.cid = null;
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
        handleCurrentChange(val){
            this.$emit("select-category",val);
            console.log("子组件val === " + val);
        }
       
    },
}
</script>

<style scoped>

</style>