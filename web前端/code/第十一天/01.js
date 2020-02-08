//node平台支持JS全部核心语法
//变量
var a = 100;
var b = 200;
console.log(a + b);
//运算符[数学运算符、比较运算符、逻辑运算符]都支持
console.log(3 > 8 ? 6 : 9);
//流程控制语句
for (var i = 1; i <= 100; i++) {
  if (i % 2 == 0) console.log(i);
}
//函数
function sum(a, b) {
  return a + b;
}
console.log(sum(11, 11));
//数组
var arr = ["吃饭", "睡觉", "打豆豆"];
console.log(arr.reverse());
//构造函数
function People(name, age, sex) {
  this.name = name;
  this.age = age;
  this.sex = sex;
}
//原型对象方法
People.prototype.eat = function() {
  console.log(this.name + "可以吃八斤米饭");
};
//创建小明
var xiaoming = new People("小明", 18, "男");
xiaoming.eat();

//没有DOM、BOM
// console.log(window);
console.log(document);
