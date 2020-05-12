package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 收款单主单
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyReceiptMain implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 收款单号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

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
     * 应付合计
     */
    private Double shouldPayTotal;

    /**
     * 本次应收
     */
    private Double currentShouldReceive;

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
     * 本次实收
     */
    private Double currentRealReceive;

    /**
     * 临客费项id
     */
    private Long temporaryMoneyId;

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
     * 标题
     */
    private String title;

    /**
     * 收款类型
     */
    private String receiveType;

    /**
     * 收据号
     */
    private String receiptNumber;

    /**
     * 发票号
     */
    private String invoiceNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收款人
     */
    private String receivePerson;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 操作时间
     */
    private LocalDateTime operateDate;

    /**
     * 修改人
     */
    private String updatePerson;

    /**
     * 修改时间
     */
    private LocalDateTime updateDate;

    /**
     * 修改原因
     */
    private String updateReason;

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getShouldPayTotal() {
        return shouldPayTotal;
    }

    public void setShouldPayTotal(Double shouldPayTotal) {
        this.shouldPayTotal = shouldPayTotal;
    }

    public Double getCurrentShouldReceive() {
        return currentShouldReceive;
    }

    public void setCurrentShouldReceive(Double currentShouldReceive) {
        this.currentShouldReceive = currentShouldReceive;
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

    public Double getCurrentRealReceive() {
        return currentRealReceive;
    }

    public void setCurrentRealReceive(Double currentRealReceive) {
        this.currentRealReceive = currentRealReceive;
    }

    public Long getTemporaryMoneyId() {
        return temporaryMoneyId;
    }

    public void setTemporaryMoneyId(Long temporaryMoneyId) {
        this.temporaryMoneyId = temporaryMoneyId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceivePerson() {
        return receivePerson;
    }

    public void setReceivePerson(String receivePerson) {
        this.receivePerson = receivePerson;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public LocalDateTime getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(LocalDateTime operateDate) {
        this.operateDate = operateDate;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateReason() {
        return updateReason;
    }

    public void setUpdateReason(String updateReason) {
        this.updateReason = updateReason;
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

    @Override
    public String toString() {
        return "FyReceiptMain{" +
        "id=" + id +
        ", cellId=" + cellId +
        ", receiveDate=" + receiveDate +
        ", customerName=" + customerName +
        ", shouldPayTotal=" + shouldPayTotal +
        ", currentShouldReceive=" + currentShouldReceive +
        ", discountMoney=" + discountMoney +
        ", receiveMethod=" + receiveMethod +
        ", isCustomer=" + isCustomer +
        ", currentRealReceive=" + currentRealReceive +
        ", temporaryMoneyId=" + temporaryMoneyId +
        ", estateId=" + estateId +
        ", currentDelayMoney=" + currentDelayMoney +
        ", lastDelayMoney=" + lastDelayMoney +
        ", title=" + title +
        ", receiveType=" + receiveType +
        ", receiptNumber=" + receiptNumber +
        ", invoiceNumber=" + invoiceNumber +
        ", status=" + status +
        ", remark=" + remark +
        ", receivePerson=" + receivePerson +
        ", company=" + company +
        ", operateDate=" + operateDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        ", updateReason=" + updateReason +
        ", receiptCheckStatus=" + receiptCheckStatus +
        ", receiptCheckPerson=" + receiptCheckPerson +
        ", receiptCheckTime=" + receiptCheckTime +
        ", receiptCheckAdvice=" + receiptCheckAdvice +
        ", moneyCheckStatus=" + moneyCheckStatus +
        ", moneyCheckPerson=" + moneyCheckPerson +
        ", moneyCheckTime=" + moneyCheckTime +
        ", moneyCheckAdvice=" + moneyCheckAdvice +
        "}";
    }
}
