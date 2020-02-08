import solarLunar, { solar2lunar, lunar2solar } from '../src/';
import should from 'should';

const solar2lunarData = solar2lunar(2015, 10, 2); // 转换为阴历
const solar2lunarData2 = solar2lunar(2015, 10, 8); // 转换为阴历
const lunar2solarData = lunar2solar(2015, 10, 2); // 转换为公历
const lunar2solarData2 = lunar2solar(2015, 8, 26); // 转换为公历
const solar2lunarData3 = solar2lunar(2033, 12, 23); // 转换为阴历
const solar2lunarData4 = solar2lunar(2017, 12, 14); // 转换为阴历

const solar2lunarData5 = solar2lunar(1949, 8, 14); // 转换为阴历 7.20
const solar2lunarData6 = solar2lunar(1949, 9, 14); // 转换为阴历 闰月 7.20
const solar2lunarData7 = solar2lunar(1949, 10, 14); // 转换为阴历 8.23
const solar2lunarData8 = solar2lunar(2019, 1, 31); // 转换为阴历 2019.1.27 干支
const solar2lunarData9 = solar2lunar(2019, 2, 4); // 转换为阴历 2019.1.27 干支
const solar2lunarData10 = solar2lunar(2018, 2, 2); // 转换为阴历 2019.1.27 干支

describe('should work', function () {
  describe('solar2lunar', function () {
    it('should have property solar2lunar', function () {
      solarLunar.should.have.property('solar2lunar');
    });
    it('lYear should equal 2015', function () {
      solar2lunarData.lYear.should.be.equal(2015);
    });
    it('lMonth should equal 8', function () {
      solar2lunarData.lMonth.should.be.equal(8);
    });
    it('lDay should equal 20', function () {
      solar2lunarData.lDay.should.be.equal(20);
    });
    it('animal should equal 羊', function () {
      solar2lunarData.animal.should.be.equal('羊');
    });
    it('yearCn should equal 二零一五年', function () {
      solar2lunarData.yearCn.should.be.equal('二零一五年');
    });
    it('monthCn should equal 八月', function () {
      solar2lunarData.monthCn.should.be.equal('八月');
    });
    it('dayCn should equal 二十', function () {
      solar2lunarData.dayCn.should.be.equal('二十');
    });
    it('gzYear should equal 乙未', function () {
      solar2lunarData.gzYear.should.be.equal('乙未');
    });
    it('gzMonth should equal 乙酉', function () {
      solar2lunarData.gzMonth.should.be.equal('乙酉');
    });
    it('gzDay should equal 辛亥', function () {
      solar2lunarData.gzDay.should.be.equal('辛亥');
    });
    it('isToday should equal false', function () {
      solar2lunarData.isToday.should.be.false();
    });
    it('isLeap should equal false', function () {
      solar2lunarData.isLeap.should.be.false();
    });
    it('nWeek should equal 5', function () {
      solar2lunarData.nWeek.should.be.equal(5);
    });
    it('ncWeek should equal 星期五', function () {
      solar2lunarData.ncWeek.should.be.equal('星期五');
    });
    it('isTerm should equal false', function () {
      solar2lunarData.isTerm.should.be.false();
    });
    it('term should equal empty string', function () {
      should(solar2lunarData.term).be.exactly('');
    });
  });

  describe('solar2lunar that has term', function () {
    it('isTerm should be true', function () {
      should(solar2lunarData2.isTerm).be.true();
    });
    it('term should not be null', function () {
      should(solar2lunarData2.term).not.be.null();
    });
    it('term should equal 寒露', function () {
      should(solar2lunarData2.term).be.equal('寒露');
    });
  });

  describe('lunar2solar', function () {
    it('should have property lunar2solar', function () {
      solarLunar.should.have.property('lunar2solar');
    });
    it('lYear should equal 2015', function () {
      lunar2solarData.lYear.should.be.equal(2015);
    });
    it('lMonth should equal 10', function () {
      lunar2solarData.lMonth.should.be.equal(10);
    });
    it('lDay should equal 2', function () {
      lunar2solarData.lDay.should.be.equal(2);
    });
    it('animal should equal 羊', function () {
      lunar2solarData.animal.should.be.equal('羊');
    });
    it('yearCn should equal 二零一五年', function () {
      solar2lunarData.yearCn.should.be.equal('二零一五年');
    });
    it('monthCn should equal 十月', function () {
      lunar2solarData.monthCn.should.be.equal('十月');
    });
    it('dayCn should equal 初二', function () {
      lunar2solarData.dayCn.should.be.equal('初二');
    });
    it('gzYear should equal 乙未', function () {
      lunar2solarData.gzYear.should.be.equal('乙未');
    });
    it('gzMonth should equal 丁亥', function () {
      lunar2solarData.gzMonth.should.be.equal('丁亥');
    });
    it('gzDay should equal 癸巳', function () {
      lunar2solarData.gzDay.should.be.equal('癸巳');
    });
    it('isToday should equal false', function () {
      lunar2solarData.isToday.should.be.false();
    });
    it('isLeap should equal false', function () {
      lunar2solarData.isLeap.should.be.false();
    });
    it('nWeek should equal 5', function () {
      lunar2solarData.nWeek.should.be.equal(5);
    });
    it('ncWeek should equal 星期五', function () {
      lunar2solarData.ncWeek.should.be.equal('星期五');
    });
    it('isTerm should equal false', function () {
      lunar2solarData.isTerm.should.be.false();
    });
    it('term should equal empty string', function () {
      should(lunar2solarData.term).be.exactly('');
    });
  });

  describe('lunar2solar that has term', function () {
    it('isTerm should be true', function () {
      should(lunar2solarData2.isTerm).be.true();
    });
    it('term should not be null', function () {
      should(lunar2solarData2.term).not.be.null();
    });
    it('term should equal 寒露', function () {
      should(lunar2solarData2.term).be.equal('寒露');
    });
  });
  describe('2033/12/23', function () {
    it('should be leap', function () {
      should(solar2lunarData3.isLeap).be.true();
    });
  });
  describe('2017/12/14', function () {
    it('should not be leap', function () {
      should(solar2lunarData4.monthCn).be.equal('十月');
    });
  });
  describe('1949/8/14', function () {
    it('monthCn should equal 七月', function () {
      should(solar2lunarData5.monthCn).be.equal('七月');
    });
    it('isLeap should equal false', function () {
      solar2lunarData5.isLeap.should.be.false();
    });
  });
  describe('1949/9/14', function () {
    it('monthCn should equal 闰七月', function () {
      should(solar2lunarData6.monthCn).be.equal('闰七月');
    });
    it('isLeap should equal true', function () {
      solar2lunarData6.isLeap.should.be.true();
    });
  });
  describe('1949/10/14', function () {
    it('monthCn should equal 八月', function () {
      should(solar2lunarData7.monthCn).be.equal('八月');
    });
    it('isLeap should equal false', function () {
      solar2lunarData7.isLeap.should.be.false();
    });
  });
  describe('2019/2/1', function () {
    it('gzYear should be 戊戌', function () {
      should(solar2lunarData8.gzYear).be.equal('戊戌');
    });
  });
  describe('2019/2/5', function () {
    it('gzYear should be 己亥', function () {
      should(solar2lunarData9.gzYear).be.equal('己亥');
    });
  });
  describe('2018/2/2', function () {
    it('gzYear should be 丁酉', function () {
      should(solar2lunarData10.gzYear).be.equal('丁酉');
    });
  });
});
