package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 短信充值单
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblMessageCharge implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 充值单号
     */
    private String chargeNumber;

    /**
     * 充值账户
     */
    private String chargeAccount;

    /**
     * 充值金额
     */
    private Double chargeMoney;

    /**
     * 充值说明
     */
    private String chargeDesc;

    /**
     * 充值人
     */
    private String chargePerson;

    /**
     * 充值日期
     */
    private LocalDateTime chargeDate;


    public String getChargeNumber() {
        return chargeNumber;
    }

    public void setChargeNumber(String chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    public String getChargeAccount() {
        return chargeAccount;
    }

    public void setChargeAccount(String chargeAccount) {
        this.chargeAccount = chargeAccount;
    }

    public Double getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(Double chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc;
    }

    public String getChargePerson() {
        return chargePerson;
    }

    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson;
    }

    public LocalDateTime getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(LocalDateTime chargeDate) {
        this.chargeDate = chargeDate;
    }

    @Override
    public String toString() {
        return "TblMessageCharge{" +
        "chargeNumber=" + chargeNumber +
        ", chargeAccount=" + chargeAccount +
        ", chargeMoney=" + chargeMoney +
        ", chargeDesc=" + chargeDesc +
        ", chargePerson=" + chargePerson +
        ", chargeDate=" + chargeDate +
        "}";
    }
}
