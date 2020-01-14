//引入社区自定义模块colors
//可以改变文字颜色
var colors = require("colors");
console.log("我非常喜欢贾成豪,因为贾成豪只是深渊".green);
console.log("我很喜欢小明".rainbow);

//引入社区自定义模块solarLunar
var solarLunar = require("solarLunar");
var solar2lunarData = solarLunar.solar2lunar(1996, 3, 8);
console.log(solar2lunarData);
console.log(solar2lunarData.animal);