package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 业主房产对照表
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhCustomerEstate implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业主id
     */
    private Integer customerId;

    /**
     * 业主姓名
     */
    private String customerName;

    /**
     * 房间id
     */
    private Integer cellId;

    /**
     * 使用状态
     */
    private String useStatus;

    /**
     * 入住日期
     */
    private LocalDateTime liveDate;

    /**
     * 装修时间
     */
    private LocalDateTime decorateDate;

    /**
     * 认购证号
     */
    private String subscriptionCardNumber;

    /**
     * 房产证号
     */
    private String houseCode;

    /**
     * 是否缴纳维修金
     */
    private String isPayDecorateMoney;

    /**
     * 预缴维修金
     */
    private Double prePayDecorateMoney;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序号
     */
    private Integer orderid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public LocalDateTime getLiveDate() {
        return liveDate;
    }

    public void setLiveDate(LocalDateTime liveDate) {
        this.liveDate = liveDate;
    }

    public LocalDateTime getDecorateDate() {
        return decorateDate;
    }

    public void setDecorateDate(LocalDateTime decorateDate) {
        this.decorateDate = decorateDate;
    }

    public String getSubscriptionCardNumber() {
        return subscriptionCardNumber;
    }

    public void setSubscriptionCardNumber(String subscriptionCardNumber) {
        this.subscriptionCardNumber = subscriptionCardNumber;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getIsPayDecorateMoney() {
        return isPayDecorateMoney;
    }

    public void setIsPayDecorateMoney(String isPayDecorateMoney) {
        this.isPayDecorateMoney = isPayDecorateMoney;
    }

    public Double getPrePayDecorateMoney() {
        return prePayDecorateMoney;
    }

    public void setPrePayDecorateMoney(Double prePayDecorateMoney) {
        this.prePayDecorateMoney = prePayDecorateMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    @Override
    public String toString() {
        return "ZhCustomerEstate{" +
        "id=" + id +
        ", customerId=" + customerId +
        ", customerName=" + customerName +
        ", cellId=" + cellId +
        ", useStatus=" + useStatus +
        ", liveDate=" + liveDate +
        ", decorateDate=" + decorateDate +
        ", subscriptionCardNumber=" + subscriptionCardNumber +
        ", houseCode=" + houseCode +
        ", isPayDecorateMoney=" + isPayDecorateMoney +
        ", prePayDecorateMoney=" + prePayDecorateMoney +
        ", remark=" + remark +
        ", orderid=" + orderid +
        "}";
    }
}
