package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 车位租赁缴费明细
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyCarSpaceRentDetail implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属租赁id
     */
    private Long rentId;

    /**
     * 缴费类型
     */
    private String payType;

    /**
     * 缴费开始日
     */
    private LocalDateTime payStartDate;

    /**
     * 缴费结束日
     */
    private LocalDateTime payStopDate;

    /**
     * 应收金额
     */
    private Double shouldReceive;

    /**
     * 优惠金额
     */
    private Double discountMoney;

    /**
     * 滞纳金
     */
    private Double delayMoney;

    /**
     * 实收金额
     */
    private Double realReceiveMoney;

    /**
     * 说明
     */
    private String desc;

    /**
     * 收款人id
     */
    private String receiveId;

    /**
     * 收款人名称
     */
    private String receivePersonName;

    /**
     * 收款时间
     */
    private LocalDateTime receiveDate;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 收款状态
     */
    private String receiveStatus;

    /**
     * 作废人id
     */
    private String invalidPersonId;

    /**
     * 作废人名称
     */
    private String invalidPersonName;

    /**
     * 作废原因
     */
    private String invalidReason;

    /**
     * 作废时间
     */
    private LocalDateTime invalidDate;

    /**
     * 单据审核状态
     */
    private String receiptCheckStatus;

    /**
     * 单据审核人
     */
    private String receiptCheckPerson;

    /**
     * 单据审核时间
     */
    private LocalDateTime receiptCheckTime;

    /**
     * 单据审核意见
     */
    private String receiptCheckAdvice;

    /**
     * 现金审核状态
     */
    private String moneyCheckStatus;

    /**
     * 现金审核人
     */
    private String moneyCheckPerson;

    /**
     * 现金审核时间
     */
    private LocalDateTime moneyCheckTime;

    /**
     * 现金审核意见
     */
    private String moneyCheckAdvice;

    /**
     * 作废审核状态
     */
    private String invalidCheckStatus;

    /**
     * 作废审核人
     */
    private String invalidCheckPerson;

    /**
     * 作废审核时间
     */
    private LocalDateTime invalidCheckTime;

    /**
     * 作废审核意见
     */
    private String invalidCheckAdvice;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRentId() {
        return rentId;
    }

    public void setRentId(Long rentId) {
        this.rentId = rentId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public LocalDateTime getPayStartDate() {
        return payStartDate;
    }

    public void setPayStartDate(LocalDateTime payStartDate) {
        this.payStartDate = payStartDate;
    }

    public LocalDateTime getPayStopDate() {
        return payStopDate;
    }

    public void setPayStopDate(LocalDateTime payStopDate) {
        this.payStopDate = payStopDate;
    }

    public Double getShouldReceive() {
        return shouldReceive;
    }

    public void setShouldReceive(Double shouldReceive) {
        this.shouldReceive = shouldReceive;
    }

    public Double getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public Double getDelayMoney() {
        return delayMoney;
    }

    public void setDelayMoney(Double delayMoney) {
        this.delayMoney = delayMoney;
    }

    public Double getRealReceiveMoney() {
        return realReceiveMoney;
    }

    public void setRealReceiveMoney(Double realReceiveMoney) {
        this.realReceiveMoney = realReceiveMoney;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceivePersonName() {
        return receivePersonName;
    }

    public void setReceivePersonName(String receivePersonName) {
        this.receivePersonName = receivePersonName;
    }

    public LocalDateTime getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getInvalidPersonId() {
        return invalidPersonId;
    }

    public void setInvalidPersonId(String invalidPersonId) {
        this.invalidPersonId = invalidPersonId;
    }

    public String getInvalidPersonName() {
        return invalidPersonName;
    }

    public void setInvalidPersonName(String invalidPersonName) {
        this.invalidPersonName = invalidPersonName;
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

    public String getReceiptCheckStatus() {
        return receiptCheckStatus;
    }

    public void setReceiptCheckStatus(String receiptCheckStatus) {
        this.receiptCheckStatus = receiptCheckStatus;
    }

    public String getReceiptCheckPerson() {
        return receiptCheckPerson;
    }

    public void setReceiptCheckPerson(String receiptCheckPerson) {
        this.receiptCheckPerson = receiptCheckPerson;
    }

    public LocalDateTime getReceiptCheckTime() {
        return receiptCheckTime;
    }

    public void setReceiptCheckTime(LocalDateTime receiptCheckTime) {
        this.receiptCheckTime = receiptCheckTime;
    }

    public String getReceiptCheckAdvice() {
        return receiptCheckAdvice;
    }

    public void setReceiptCheckAdvice(String receiptCheckAdvice) {
        this.receiptCheckAdvice = receiptCheckAdvice;
    }

    public String getMoneyCheckStatus() {
        return moneyCheckStatus;
    }

    public void setMoneyCheckStatus(String moneyCheckStatus) {
        this.moneyCheckStatus = moneyCheckStatus;
    }

    public String getMoneyCheckPerson() {
        return moneyCheckPerson;
    }

    public void setMoneyCheckPerson(String moneyCheckPerson) {
        this.moneyCheckPerson = moneyCheckPerson;
    }

    public LocalDateTime getMoneyCheckTime() {
        return moneyCheckTime;
    }

    public void setMoneyCheckTime(LocalDateTime moneyCheckTime) {
        this.moneyCheckTime = moneyCheckTime;
    }

    public String getMoneyCheckAdvice() {
        return moneyCheckAdvice;
    }

    public void setMoneyCheckAdvice(String moneyCheckAdvice) {
        this.moneyCheckAdvice = moneyCheckAdvice;
    }

    public String getInvalidCheckStatus() {
        return invalidCheckStatus;
    }

    public void setInvalidCheckStatus(String invalidCheckStatus) {
        this.invalidCheckStatus = invalidCheckStatus;
    }

    public String getInvalidCheckPerson() {
        return invalidCheckPerson;
    }

    public void setInvalidCheckPerson(String invalidCheckPerson) {
        this.invalidCheckPerson = invalidCheckPerson;
    }

    public LocalDateTime getInvalidCheckTime() {
        return invalidCheckTime;
    }

    public void setInvalidCheckTime(LocalDateTime invalidCheckTime) {
        this.invalidCheckTime = invalidCheckTime;
    }

    public String getInvalidCheckAdvice() {
        return invalidCheckAdvice;
    }

    public void setInvalidCheckAdvice(String invalidCheckAdvice) {
        this.invalidCheckAdvice = invalidCheckAdvice;
    }

    @Override
    public String toString() {
        return "WyCarSpaceRentDetail{" +
        "id=" + id +
        ", rentId=" + rentId +
        ", payType=" + payType +
        ", payStartDate=" + payStartDate +
        ", payStopDate=" + payStopDate +
        ", shouldReceive=" + shouldReceive +
        ", discountMoney=" + discountMoney +
        ", delayMoney=" + delayMoney +
        ", realReceiveMoney=" + realReceiveMoney +
        ", desc=" + desc +
        ", receiveId=" + receiveId +
        ", receivePersonName=" + receivePersonName +
        ", receiveDate=" + receiveDate +
        ", invoiceNumber=" + invoiceNumber +
        ", receiveStatus=" + receiveStatus +
        ", invalidPersonId=" + invalidPersonId +
        ", invalidPersonName=" + invalidPersonName +
        ", invalidReason=" + invalidReason +
        ", invalidDate=" + invalidDate +
        ", receiptCheckStatus=" + receiptCheckStatus +
        ", receiptCheckPerson=" + receiptCheckPerson +
        ", receiptCheckTime=" + receiptCheckTime +
        ", receiptCheckAdvice=" + receiptCheckAdvice +
        ", moneyCheckStatus=" + moneyCheckStatus +
        ", moneyCheckPerson=" + moneyCheckPerson +
        ", moneyCheckTime=" + moneyCheckTime +
        ", moneyCheckAdvice=" + moneyCheckAdvice +
        ", invalidCheckStatus=" + invalidCheckStatus +
        ", invalidCheckPerson=" + invalidCheckPerson +
        ", invalidCheckTime=" + invalidCheckTime +
        ", invalidCheckAdvice=" + invalidCheckAdvice +
        "}";
    }
}
