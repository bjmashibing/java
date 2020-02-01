//不是重点
var http = require("http");
//创建服务器对象
var app = http.createServer(function (req, res) {
    res.setHeader('Content-Type', 'text/html;charset=utf-8');
    //服务器端下发状态码
    res.statusCode = 500;
    //服务器响应数据
    res.end("老师自己开发的服务器");
});
//端口号设置
app.listen(3000);