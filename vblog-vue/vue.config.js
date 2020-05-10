module.exports = {
    devServer: {
        port: 8089, // 端口号
        host: "localhost", // 本机ip地址
        https: false, // 协议，没有https
        open: true, // 启动服务时自动打开浏览器访问
        proxy: {
            [process.env.VUE_APP_BASE_API]: { // 匹配该前缀进行代理
                target: process.env.VUE_APP_SERVICE_URL, // 请求目标地址
                changeOrigin: true, // 开启代理
                pathRewrite: { 
                    ['^' + process.env.VUE_APP_BASE_API]: '' // 将匹配到的代理前缀替换为空，前缀仅仅作为匹配代理作用
                }
            }
        }
    },
    lintOnSave: false, // 关闭格式检查，无效(可在vscode settings中关闭eslint)
    productionSourceMap: false // 打包时不会生成.map文件，加快打包速度
}