package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 作废单子单
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyInvalidSub implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 作废明细单号
     */
    @TableId(value = "invalid_detail_id", type = IdType.AUTO)
    private String invalidDetailId;

    /**
     * 所属作废单号
     */
    private String invalidId;

    /**
     * 费项名称
     */
    private String moneySettingName;

    /**
     * 计费单价
     */
    private Double chargeUnit;

    /**
     * 上次读数
     */
    private Double lastReadNumber;

    /**
     * 本次读数
     */
    private Double currentReadNumber;

    /**
     * 实际用量
     */
    private Double realUsed;

    /**
     * 费用金额
     */
    private Double money;

    /**
     * 滞纳金
     */
    private Double delayMoney;

    /**
     * 本次应付
     */
    private Double currentShouldPay;

    /**
     * 超期天数
     */
    private Integer overDay;

    /**
     * 费用起期
     */
    private LocalDateTime moneyStartDate;

    /**
     * 费用止期
     */
    private LocalDateTime moneyStopDate;

    /**
     * 缴费限期
     */
    private LocalDateTime payLimitDay;

    /**
     * 记录人
     */
    private String inputPerson;

    /**
     * 所属台账id
     */
    private String standingBookId;

    /**
     * 收费周期
     */
    private Integer receiveCycle;

    /**
     * 减免金额
     */
    private Double derateMoney;

    /**
     * 费项id
     */
    private Integer moneyId;

    /**
     * 滞纳金减免金额
     */
    private Double delayDerateMoney;

    /**
     * 楼层系数
     */
    private Double mopFloor;

    /**
     * 费用倍数
     */
    private Integer moneyMult;


    public String getInvalidDetailId() {
        return invalidDetailId;
    }

    public void setInvalidDetailId(String invalidDetailId) {
        this.invalidDetailId = invalidDetailId;
    }

    public String getInvalidId() {
        return invalidId;
    }

    public void setInvalidId(String invalidId) {
        this.invalidId = invalidId;
    }

    public String getMoneySettingName() {
        return moneySettingName;
    }

    public void setMoneySettingName(String moneySettingName) {
        this.moneySettingName = moneySettingName;
    }

    public Double getChargeUnit() {
        return chargeUnit;
    }

    public void setChargeUnit(Double chargeUnit) {
        this.chargeUnit = chargeUnit;
    }

    public Double getLastReadNumber() {
        return lastReadNumber;
    }

    public void setLastReadNumber(Double lastReadNumber) {
        this.lastReadNumber = lastReadNumber;
    }

    public Double getCurrentReadNumber() {
        return currentReadNumber;
    }

    public void setCurrentReadNumber(Double currentReadNumber) {
        this.currentReadNumber = currentReadNumber;
    }

    public Double getRealUsed() {
        return realUsed;
    }

    public void setRealUsed(Double realUsed) {
        this.realUsed = realUsed;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getDelayMoney() {
        return delayMoney;
    }

    public void setDelayMoney(Double delayMoney) {
        this.delayMoney = delayMoney;
    }

    public Double getCurrentShouldPay() {
        return currentShouldPay;
    }

    public void setCurrentShouldPay(Double currentShouldPay) {
        this.currentShouldPay = currentShouldPay;
    }

    public Integer getOverDay() {
        return overDay;
    }

    public void setOverDay(Integer overDay) {
        this.overDay = overDay;
    }

    public LocalDateTime getMoneyStartDate() {
        return moneyStartDate;
    }

    public void setMoneyStartDate(LocalDateTime moneyStartDate) {
        this.moneyStartDate = moneyStartDate;
    }

    public LocalDateTime getMoneyStopDate() {
        return moneyStopDate;
    }

    public void setMoneyStopDate(LocalDateTime moneyStopDate) {
        this.moneyStopDate = moneyStopDate;
    }

    public LocalDateTime getPayLimitDay() {
        return payLimitDay;
    }

    public void setPayLimitDay(LocalDateTime payLimitDay) {
        this.payLimitDay = payLimitDay;
    }

    public String getInputPerson() {
        return inputPerson;
    }

    public void setInputPerson(String inputPerson) {
        this.inputPerson = inputPerson;
    }

    public String getStandingBookId() {
        return standingBookId;
    }

    public void setStandingBookId(String standingBookId) {
        this.standingBookId = standingBookId;
    }

    public Integer getReceiveCycle() {
        return receiveCycle;
    }

    public void setReceiveCycle(Integer receiveCycle) {
        this.receiveCycle = receiveCycle;
    }

    public Double getDerateMoney() {
        return derateMoney;
    }

    public void setDerateMoney(Double derateMoney) {
        this.derateMoney = derateMoney;
    }

    public Integer getMoneyId() {
        return moneyId;
    }

    public void setMoneyId(Integer moneyId) {
        this.moneyId = moneyId;
    }

    public Double getDelayDerateMoney() {
        return delayDerateMoney;
    }

    public void setDelayDerateMoney(Double delayDerateMoney) {
        this.delayDerateMoney = delayDerateMoney;
    }

    public Double getMopFloor() {
        return mopFloor;
    }

    public void setMopFloor(Double mopFloor) {
        this.mopFloor = mopFloor;
    }

    public Integer getMoneyMult() {
        return moneyMult;
    }

    public void setMoneyMult(Integer moneyMult) {
        this.moneyMult = moneyMult;
    }

    @Override
    public String toString() {
        return "FyInvalidSub{" +
        "invalidDetailId=" + invalidDetailId +
        ", invalidId=" + invalidId +
        ", moneySettingName=" + moneySettingName +
        ", chargeUnit=" + chargeUnit +
        ", lastReadNumber=" + lastReadNumber +
        ", currentReadNumber=" + currentReadNumber +
        ", realUsed=" + realUsed +
        ", money=" + money +
        ", delayMoney=" + delayMoney +
        ", currentShouldPay=" + currentShouldPay +
        ", overDay=" + overDay +
        ", moneyStartDate=" + moneyStartDate +
        ", moneyStopDate=" + moneyStopDate +
        ", payLimitDay=" + payLimitDay +
        ", inputPerson=" + inputPerson +
        ", standingBookId=" + standingBookId +
        ", receiveCycle=" + receiveCycle +
        ", derateMoney=" + derateMoney +
        ", moneyId=" + moneyId +
        ", delayDerateMoney=" + delayDerateMoney +
        ", mopFloor=" + mopFloor +
        ", moneyMult=" + moneyMult +
        "}";
    }
}
