package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 作废单主单
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyInvalidMain implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 作废单号
     */
    @TableId(value = "invalid_id", type = IdType.AUTO)
    private String invalidId;

    /**
     * 所属收款单号
     */
    private String receiveId;

    /**
     * 房间号
     */
    private Integer cellId;

    /**
     * 收费日期
     */
    private LocalDateTime receiveDate;

    /**
     * 业主姓名
     */
    private String customerName;

    /**
     * 费用金额
     */
    private Double money;

    /**
     * 实收金额
     */
    private Double realReceiveMoney;

    /**
     * 优惠金额
     */
    private Double discountMoney;

    /**
     * 收款方式
     */
    private String receiveMethod;

    /**
     * 是否业主
     */
    private String isCustomer;

    /**
     * 收费金额
     */
    private Double receiveMoney;

    /**
     * 费项id
     */
    private Long moneyId;

    /**
     * 所属楼盘id
     */
    private Integer estateId;

    /**
     * 本次欠缴
     */
    private Double currentDelayMoney;

    /**
     * 上次欠缴
     */
    private Double lastDelayMoney;

    /**
     * 作废类型
     */
    private String invalidType;

    /**
     * 收据号
     */
    private String receiptNumber;

    /**
     * 发票号
     */
    private String invoiceNumber;

    /**
     * 收款人
     */
    private String receivePerson;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 作废原因
     */
    private String invalidReason;

    /**
     * 作废时间
     */
    private LocalDateTime invalidDate;

    /**
     * 作废人
     */
    private String invalidPerson;


    public String getInvalidId() {
        return invalidId;
    }

    public void setInvalidId(String invalidId) {
        this.invalidId = invalidId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public LocalDateTime getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getRealReceiveMoney() {
        return realReceiveMoney;
    }

    public void setRealReceiveMoney(Double realReceiveMoney) {
        this.realReceiveMoney = realReceiveMoney;
    }

    public Double getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public String getReceiveMethod() {
        return receiveMethod;
    }

    public void setReceiveMethod(String receiveMethod) {
        this.receiveMethod = receiveMethod;
    }

    public String getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(String isCustomer) {
        this.isCustomer = isCustomer;
    }

    public Double getReceiveMoney() {
        return receiveMoney;
    }

    public void setReceiveMoney(Double receiveMoney) {
        this.receiveMoney = receiveMoney;
    }

    public Long getMoneyId() {
        return moneyId;
    }

    public void setMoneyId(Long moneyId) {
        this.moneyId = moneyId;
    }

    public Integer getEstateId() {
        return estateId;
    }

    public void setEstateId(Integer estateId) {
        this.estateId = estateId;
    }

    public Double getCurrentDelayMoney() {
        return currentDelayMoney;
    }

    public void setCurrentDelayMoney(Double currentDelayMoney) {
        this.currentDelayMoney = currentDelayMoney;
    }

    public Double getLastDelayMoney() {
        return lastDelayMoney;
    }

    public void setLastDelayMoney(Double lastDelayMoney) {
        this.lastDelayMoney = lastDelayMoney;
    }

    public String getInvalidType() {
        return invalidType;
    }

    public void setInvalidType(String invalidType) {
        this.invalidType = invalidType;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getReceivePerson() {
        return receivePerson;
    }

    public void setReceivePerson(String receivePerson) {
        this.receivePerson = receivePerson;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public LocalDateTime getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(LocalDateTime invalidDate) {
        this.invalidDate = invalidDate;
    }

    public String getInvalidPerson() {
        return invalidPerson;
    }

    public void setInvalidPerson(String invalidPerson) {
        this.invalidPerson = invalidPerson;
    }

    @Override
    public String toString() {
        return "FyInvalidMain{" +
        "invalidId=" + invalidId +
        ", receiveId=" + receiveId +
        ", cellId=" + cellId +
        ", receiveDate=" + receiveDate +
        ", customerName=" + customerName +
        ", money=" + money +
        ", realReceiveMoney=" + realReceiveMoney +
        ", discountMoney=" + discountMoney +
        ", receiveMethod=" + receiveMethod +
        ", isCustomer=" + isCustomer +
        ", receiveMoney=" + receiveMoney +
        ", moneyId=" + moneyId +
        ", estateId=" + estateId +
        ", currentDelayMoney=" + currentDelayMoney +
        ", lastDelayMoney=" + lastDelayMoney +
        ", invalidType=" + invalidType +
        ", receiptNumber=" + receiptNumber +
        ", invoiceNumber=" + invoiceNumber +
        ", receivePerson=" + receivePerson +
        ", remark=" + remark +
        ", company=" + company +
        ", invalidReason=" + invalidReason +
        ", invalidDate=" + invalidDate +
        ", invalidPerson=" + invalidPerson +
        "}";
    }
}
