## solarLunar

[![NPM version][npm-image]][npm-url]
[![build status][travis-image]][travis-url]
[![Test Coverage][coveralls-image]][coveralls-url]
[![Dependency Status][dep-image]][dep-url]
[![devDependency Status][devdep-image]][devdep-url]
[![NPM downloads][downloads-image]][npm-url]

[npm-image]: http://img.shields.io/npm/v/solarlunar.svg?style=flat-square
[npm-url]: https://www.npmjs.com/package/solarlunar
[travis-image]: https://img.shields.io/travis/yize/solarlunar.svg?style=flat-square
[travis-url]: https://travis-ci.org/yize/solarlunar
[coveralls-image]: https://img.shields.io/coveralls/yize/solarlunar.svg?style=flat-square
[coveralls-url]: https://coveralls.io/r/yize/solarlunar?branch=master
[dep-image]: http://img.shields.io/david/yize/solarlunar.svg?style=flat-square
[dep-url]: https://david-dm.org/yize/solarlunar
[devdep-image]: http://img.shields.io/david/dev/yize/solarlunar.svg?style=flat-square
[devdep-url]: https://david-dm.org/yize/solarlunar#info=devDependencies
[downloads-image]: https://img.shields.io/npm/dm/solarlunar.svg

1900 年至 2100 年公历、农历互转

* Solar : 公历 阳历
* Lunar : 农历 阴历

支持年份：`1900-2100`

## 用法：

```js
import solarLunar from 'solarLunar';

const solar2lunarData = solarLunar.solar2lunar(2015, 10, 8); // 输入的日子为公历
const lunar2solarData = solarLunar.lunar2solar(2015, 8, 26); // 输入的日子为农历
```

output:

```js
{
    lYear: 2015,
    lMonth: 8,
    lDay: 26,
    animal: '羊',
    monthCn: '八月',
    dayCn: '廿六',
    cYear: 2015,
    cMonth: 10,
    cDay: 8,
    gzYear: '乙未',
    gzMonth: '丙戌',
    gzDay: '丁巳',
    isToday: false,
    isLeap: false,
    nWeek: 4,
    ncWeek: '星期四',
    isTerm: true,
    term: '寒露'
}
```

## API

* (Object)`solarLunar.solar2lunar` : 输入的日子为公历年月日

  * 参数 : (Number)年,(Number)月,(Number)日


```js
solarLunar.solar2lunar(2015, 10, 8);
solarLunar.solar2lunar(2015, 10, 08); // 等价于上者
```

* (Object)`solarLunar.lunar2solar` : 输入的日子为农历年月日

  * 参数 : (Number)年,(Number)月,(Number)日


```js
solarLunar.lunar2solar(2015, 8, 26);
solarLunar.lunar2solar(2015, 08, 26); // 等价于上者
```

* (Array)`solarLunar.lunarInfo` : 农历 1900-2100 的润大小信息表

* (Array)`solarLunar.solarMonth` : 公历每个月份的天数普通表

* (Array)`solarLunar.gan` : 天干地支之天干速查表 - 干 `["甲","乙","丙","丁","戊","己","庚","辛","壬","癸"]`

* (Array)`solarLunar.zhi` : 天干地支之地支速查表 - 支 `["子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥"]`

* (Array)`solarLunar.animals` : 生肖表 `["鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪"]`

* (Array)`solarLunar.lunarTerm` : 24 节气速查表 `["小寒","大寒","立春","雨水","惊蛰","春分","清明","谷雨","立夏","小满","芒种","夏至","小暑","大暑","立秋","处暑","白露","秋分","寒露","霜降","立冬","小雪","大雪","冬至"]`

* (Array)`solarLunar.lTermInfo` : 1900-2100 各年的 24 节气日期速查表

* (Array)`nStr1` : 数字转中文速查表 `['日','一','二','三','四','五','六','七','八','九','十']`

* (Array)`nStr2` : 日期转农历称呼速查表 `['初','十','廿','卅']`

* (Array)`nStr3` : 月份转农历称呼速查表 `['正','一','二','三','四','五','六','七','八','九','十','冬','腊']`

* (Number)`lYearDays` : 返回农历 y 年一整年的总天数

  ```js
  const count = solarLunar.lYearDays(1987); //count=387
  ```

* (Number(0-12))`leapMonth` : 返回农历 y 年闰月是哪个月；若 y 年没有闰月 则返回 0

  ```js
  const leapMonth = solarLunar.leapMonth(1987); //leapMonth=6
  ```

* (Number(0|29|30))`leapDays` : 返回农历 y 年闰月的天数 若该年没有闰月则返回 0

  ```js
  const leapMonthDay = solarLunar.leapDays(1987); //leapMonthDay=29
  ```

* (Number(-1|29|30))`monthDays` : 返回农历 y 年 m 月（非闰月）的总天数，计算 m 为闰月时的天数请使用 leapDays 方法

  ```js
  const MonthDay = solarLunar.monthDays(1987, 9); //MonthDay=29
  ```

* (Number (-1、28、29、30、31))`solarDays` : 返回公历(!)y 年 m 月的天数

  ```js
  const solarMonthDay = solarLunar.leapDays(1987); //solarMonthDay=30
  ```

* (Number)`toGanZhi` : 传入 offset 偏移量返回干支

* (Number)`toGanZhi` : 传入公历(!)y 年获得该年第 n 个节气的公历日期

  * 第一个参数为公历年(1900-2100)；
  * 第二个参数为二十四节气中的第几个节气(1~24)；从 n=1(小寒)算起

    ```js
    const _24 = solarLunar.getTerm(1987, 3); //_24=4;意即1987年2月4日立春
    ```

* (String)`toChinaMonth` : 传入农历数字月份返回汉语通俗表示法

  ```js
  const cnMonth = solarLunar.toChinaMonth(12); //cnMonth='腊月'
  ```

* (String)`toChinaDay` : 传入农历日期数字返回汉字表示法

  ```js
  const cnDay = solarLunar.toChinaDay(21); //cnMonth='廿一'
  ```

* (String)`getAnimal` : 年份转生肖[!仅能大致转换] => 精确划分生肖分界线是“立春”

  ```js
  const animal = solarLunar.getAnimal(1987); //animal='兔'
  ```

## 返回值

* (Number)`lYear` : 农历年
* (Number)`lMonth` : 农历月
* (Number)`lDay` : 农历日
* (String)`monthCn` : 农历月中文名称，如果为闰月，则会在月份前增加 `闰` 字
* (String)`dayCn` : 农历日中文名称
* (String)`animal` : 生肖
* (String)`gzYear` : 年的农历叫法（干支）
* (String)`gzMonth` : 月的农历叫法（干支）
* (String)`gzDay` : 日的农历叫法(干支)
* (Number)`cYear` : 公历年
* (Number)`cMonth` : 公历月
* (Number)`cDay` : 公历日
* (Number)`nWeek` : 周几
* (String)`ncWeek` : 中文周几
* (Boolean)`isLeap` : 是否是闰月
* (Boolean)`isToday` : 是否是今天
* (Boolean)`isTerm` : 是否有节气
* (String)`term` : 节气，如果没有则返回空字符串

## Links

* [http://blog.jjonline.cn/userInterFace/173.html](http://blog.jjonline.cn/userInterFace/173.html)
