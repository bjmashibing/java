package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 费项设置
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyMoneySetting implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 费项编号
     */
    private String moneySettingCode;

    /**
     * 费项名称
     */
    private String moneySettingName;

    /**
     * 收费方式
     */
    private String receiveType;

    /**
     * 单位价格
     */
    private Double priceUnit;

    /**
     * 收费周期
     */
    private Integer receiveCycle;

    /**
     * 费用类型
     */
    private String moneyType;

    /**
     * 是否允许修改单价
     */
    private String isUpdatePrice;

    /**
     * 是否便捷费项
     */
    private String isConvenientMoney;

    /**
     * 最小使用量
     */
    private Double minUsedNumber;

    /**
     * 是否阶梯收费
     */
    private String isStepReceive;

    /**
     * 阶梯条件1
     */
    private Double stepCondition1;

    /**
     * 阶梯单价1
     */
    private Double stepPrice1;

    /**
     * 阶梯条件2
     */
    private Double stepCondition21;

    /**
     * 阶梯条件2
     */
    private Double stepCondition22;

    /**
     * 阶梯单价2
     */
    private Double stepPrice2;

    /**
     * 阶梯条件3
     */
    private Double stepCondition31;

    /**
     * 阶梯条件3
     */
    private Double stepCondition32;

    /**
     * 阶梯单价3
     */
    private Double stepPrice3;

    /**
     * 阶梯条件4
     */
    private Double stepCondition41;

    /**
     * 阶梯条件4
     */
    private Double stepCondition42;

    /**
     * 阶梯单价4
     */
    private Double stepPrice4;

    /**
     * 阶梯条件5
     */
    private Double stepCondition5;

    /**
     * 阶梯单价5
     */
    private Double stepPrice5;

    /**
     * 续费短信
     */
    private String renewMessage;

    /**
     * 从已收费的费用止期后N天发送短信
     */
    private Integer receiveWarnStopDay;

    /**
     * 最多短信重复提醒次数
     */
    private Integer maxWarnNumber;

    /**
     * 催缴短信
     */
    private String askMessage;

    /**
     * 从未收取的缴费限期前N天发送短信
     */
    private Integer noReceiveWarnStopDay;

    /**
     * 关联费项ID
     */
    private Integer associateMoneyId;

    /**
     * 滞纳金比率
     */
    private Double delayRate;

    /**
     * 滞纳金超期天数
     */
    private Integer delayOverDay;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 常规收费打印时隐藏单价
     */
    private String receivePrintHidden;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMoneySettingCode() {
        return moneySettingCode;
    }

    public void setMoneySettingCode(String moneySettingCode) {
        this.moneySettingCode = moneySettingCode;
    }

    public String getMoneySettingName() {
        return moneySettingName;
    }

    public void setMoneySettingName(String moneySettingName) {
        this.moneySettingName = moneySettingName;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public Double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Integer getReceiveCycle() {
        return receiveCycle;
    }

    public void setReceiveCycle(Integer receiveCycle) {
        this.receiveCycle = receiveCycle;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getIsUpdatePrice() {
        return isUpdatePrice;
    }

    public void setIsUpdatePrice(String isUpdatePrice) {
        this.isUpdatePrice = isUpdatePrice;
    }

    public String getIsConvenientMoney() {
        return isConvenientMoney;
    }

    public void setIsConvenientMoney(String isConvenientMoney) {
        this.isConvenientMoney = isConvenientMoney;
    }

    public Double getMinUsedNumber() {
        return minUsedNumber;
    }

    public void setMinUsedNumber(Double minUsedNumber) {
        this.minUsedNumber = minUsedNumber;
    }

    public String getIsStepReceive() {
        return isStepReceive;
    }

    public void setIsStepReceive(String isStepReceive) {
        this.isStepReceive = isStepReceive;
    }

    public Double getStepCondition1() {
        return stepCondition1;
    }

    public void setStepCondition1(Double stepCondition1) {
        this.stepCondition1 = stepCondition1;
    }

    public Double getStepPrice1() {
        return stepPrice1;
    }

    public void setStepPrice1(Double stepPrice1) {
        this.stepPrice1 = stepPrice1;
    }

    public Double getStepCondition21() {
        return stepCondition21;
    }

    public void setStepCondition21(Double stepCondition21) {
        this.stepCondition21 = stepCondition21;
    }

    public Double getStepCondition22() {
        return stepCondition22;
    }

    public void setStepCondition22(Double stepCondition22) {
        this.stepCondition22 = stepCondition22;
    }

    public Double getStepPrice2() {
        return stepPrice2;
    }

    public void setStepPrice2(Double stepPrice2) {
        this.stepPrice2 = stepPrice2;
    }

    public Double getStepCondition31() {
        return stepCondition31;
    }

    public void setStepCondition31(Double stepCondition31) {
        this.stepCondition31 = stepCondition31;
    }

    public Double getStepCondition32() {
        return stepCondition32;
    }

    public void setStepCondition32(Double stepCondition32) {
        this.stepCondition32 = stepCondition32;
    }

    public Double getStepPrice3() {
        return stepPrice3;
    }

    public void setStepPrice3(Double stepPrice3) {
        this.stepPrice3 = stepPrice3;
    }

    public Double getStepCondition41() {
        return stepCondition41;
    }

    public void setStepCondition41(Double stepCondition41) {
        this.stepCondition41 = stepCondition41;
    }

    public Double getStepCondition42() {
        return stepCondition42;
    }

    public void setStepCondition42(Double stepCondition42) {
        this.stepCondition42 = stepCondition42;
    }

    public Double getStepPrice4() {
        return stepPrice4;
    }

    public void setStepPrice4(Double stepPrice4) {
        this.stepPrice4 = stepPrice4;
    }

    public Double getStepCondition5() {
        return stepCondition5;
    }

    public void setStepCondition5(Double stepCondition5) {
        this.stepCondition5 = stepCondition5;
    }

    public Double getStepPrice5() {
        return stepPrice5;
    }

    public void setStepPrice5(Double stepPrice5) {
        this.stepPrice5 = stepPrice5;
    }

    public String getRenewMessage() {
        return renewMessage;
    }

    public void setRenewMessage(String renewMessage) {
        this.renewMessage = renewMessage;
    }

    public Integer getReceiveWarnStopDay() {
        return receiveWarnStopDay;
    }

    public void setReceiveWarnStopDay(Integer receiveWarnStopDay) {
        this.receiveWarnStopDay = receiveWarnStopDay;
    }

    public Integer getMaxWarnNumber() {
        return maxWarnNumber;
    }

    public void setMaxWarnNumber(Integer maxWarnNumber) {
        this.maxWarnNumber = maxWarnNumber;
    }

    public String getAskMessage() {
        return askMessage;
    }

    public void setAskMessage(String askMessage) {
        this.askMessage = askMessage;
    }

    public Integer getNoReceiveWarnStopDay() {
        return noReceiveWarnStopDay;
    }

    public void setNoReceiveWarnStopDay(Integer noReceiveWarnStopDay) {
        this.noReceiveWarnStopDay = noReceiveWarnStopDay;
    }

    public Integer getAssociateMoneyId() {
        return associateMoneyId;
    }

    public void setAssociateMoneyId(Integer associateMoneyId) {
        this.associateMoneyId = associateMoneyId;
    }

    public Double getDelayRate() {
        return delayRate;
    }

    public void setDelayRate(Double delayRate) {
        this.delayRate = delayRate;
    }

    public Integer getDelayOverDay() {
        return delayOverDay;
    }

    public void setDelayOverDay(Integer delayOverDay) {
        this.delayOverDay = delayOverDay;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getReceivePrintHidden() {
        return receivePrintHidden;
    }

    public void setReceivePrintHidden(String receivePrintHidden) {
        this.receivePrintHidden = receivePrintHidden;
    }

    @Override
    public String toString() {
        return "FyMoneySetting{" +
        "id=" + id +
        ", moneySettingCode=" + moneySettingCode +
        ", moneySettingName=" + moneySettingName +
        ", receiveType=" + receiveType +
        ", priceUnit=" + priceUnit +
        ", receiveCycle=" + receiveCycle +
        ", moneyType=" + moneyType +
        ", isUpdatePrice=" + isUpdatePrice +
        ", isConvenientMoney=" + isConvenientMoney +
        ", minUsedNumber=" + minUsedNumber +
        ", isStepReceive=" + isStepReceive +
        ", stepCondition1=" + stepCondition1 +
        ", stepPrice1=" + stepPrice1 +
        ", stepCondition21=" + stepCondition21 +
        ", stepCondition22=" + stepCondition22 +
        ", stepPrice2=" + stepPrice2 +
        ", stepCondition31=" + stepCondition31 +
        ", stepCondition32=" + stepCondition32 +
        ", stepPrice3=" + stepPrice3 +
        ", stepCondition41=" + stepCondition41 +
        ", stepCondition42=" + stepCondition42 +
        ", stepPrice4=" + stepPrice4 +
        ", stepCondition5=" + stepCondition5 +
        ", stepPrice5=" + stepPrice5 +
        ", renewMessage=" + renewMessage +
        ", receiveWarnStopDay=" + receiveWarnStopDay +
        ", maxWarnNumber=" + maxWarnNumber +
        ", askMessage=" + askMessage +
        ", noReceiveWarnStopDay=" + noReceiveWarnStopDay +
        ", associateMoneyId=" + associateMoneyId +
        ", delayRate=" + delayRate +
        ", delayOverDay=" + delayOverDay +
        ", company=" + company +
        ", receivePrintHidden=" + receivePrintHidden +
        "}";
    }
}
