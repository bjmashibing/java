package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 退款单主单
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyRefundMain implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 退款单号
     */
    @TableId(value = "refund_id", type = IdType.AUTO)
    private String refundId;

    /**
     * 所属收款单号
     */
    private String receiptId;

    /**
     * 房间号
     */
    private Integer cellId;

    /**
     * 收费日期
     */
    private LocalDateTime receiveCycle;

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
     * 收款金额
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
     * 退款类型
     */
    private String refundType;

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
     * 退款原因
     */
    private String refundReason;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 退款人
     */
    private String refundPerson;

    /**
     * 审核状态
     */
    private String checkStatus;

    /**
     * 审核人
     */
    private String checkPerson;

    /**
     * 审核时间
     */
    private LocalDateTime checkTime;

    /**
     * 审核意见
     */
    private String checkAdvice;


    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public LocalDateTime getReceiveCycle() {
        return receiveCycle;
    }

    public void setReceiveCycle(LocalDateTime receiveCycle) {
        this.receiveCycle = receiveCycle;
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

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
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

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public LocalDateTime getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(LocalDateTime refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundPerson() {
        return refundPerson;
    }

    public void setRefundPerson(String refundPerson) {
        this.refundPerson = refundPerson;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public LocalDateTime getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(LocalDateTime checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckAdvice() {
        return checkAdvice;
    }

    public void setCheckAdvice(String checkAdvice) {
        this.checkAdvice = checkAdvice;
    }

    @Override
    public String toString() {
        return "FyRefundMain{" +
        "refundId=" + refundId +
        ", receiptId=" + receiptId +
        ", cellId=" + cellId +
        ", receiveCycle=" + receiveCycle +
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
        ", refundType=" + refundType +
        ", receiptNumber=" + receiptNumber +
        ", invoiceNumber=" + invoiceNumber +
        ", receivePerson=" + receivePerson +
        ", remark=" + remark +
        ", company=" + company +
        ", refundReason=" + refundReason +
        ", refundTime=" + refundTime +
        ", refundPerson=" + refundPerson +
        ", checkStatus=" + checkStatus +
        ", checkPerson=" + checkPerson +
        ", checkTime=" + checkTime +
        ", checkAdvice=" + checkAdvice +
        "}";
    }
}
