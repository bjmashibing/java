//node软件给我们提供模块（给JS赋予超能力），node软件本省就有的
//fs：可以通过JS向某一个文件写入数据
var fs = require("fs");
//queryString模块：可以将JSON数据格式转换为queryString字符串
var querystring = require("querystring");
//写入数据
fs.writeFile("./jch.txt", "老师是祖国的老花骨朵把", function () {
    console.log("数据写入......")
});
//读取数据
fs.readFile("./jch.txt", function (err, data) {
    console.log(data.toString());
});

//将JSON转换为queryString字符串
console.log(querystring.stringify({
    "a": 1,
    "b": 2
}));