package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 公摊费用台账用户明细
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyShareUserDetail implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户明细id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属台账编号
     */
    private Double standingBookId;

    /**
     * 所属房间编码
     */
    private Integer cellId;

    /**
     * 业主姓名
     */
    private String customerName;

    /**
     * 表编号
     */
    private String boxId;

    /**
     * 分摊金额
     */
    private Double shareMoney;

    /**
     * 本次分摊量
     */
    private Double currentShareNumber;

    /**
     * 本次费用起期
     */
    private LocalDateTime currentPayStartDate;

    /**
     * 本次费用止期
     */
    private LocalDateTime currentPayStopDate;

    /**
     * 本次缴费限期
     */
    private LocalDateTime currentPayLimitDate;

    /**
     * 收费标识
     */
    private String receiveIdentify;

    /**
     * 单位价格
     */
    private Double priceUnit;

    /**
     * 费用标识
     */
    private String costIdentify;

    /**
     * 收费单号
     */
    private String receiveId;

    /**
     * 退款次数
     */
    private Integer refundNumber;

    /**
     * 收费周期
     */
    private Integer receiveCycle;

    /**
     * 减免金额
     */
    private Double derateMoney;

    /**
     * 应收金额
     */
    private Double shouldPay;

    /**
     * 作废次数
     */
    private Integer invalidNumber;

    /**
     * 减免滞纳金
     */
    private Double derateDelayMoney;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getStandingBookId() {
        return standingBookId;
    }

    public void setStandingBookId(Double standingBookId) {
        this.standingBookId = standingBookId;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public Double getShareMoney() {
        return shareMoney;
    }

    public void setShareMoney(Double shareMoney) {
        this.shareMoney = shareMoney;
    }

    public Double getCurrentShareNumber() {
        return currentShareNumber;
    }

    public void setCurrentShareNumber(Double currentShareNumber) {
        this.currentShareNumber = currentShareNumber;
    }

    public LocalDateTime getCurrentPayStartDate() {
        return currentPayStartDate;
    }

    public void setCurrentPayStartDate(LocalDateTime currentPayStartDate) {
        this.currentPayStartDate = currentPayStartDate;
    }

    public LocalDateTime getCurrentPayStopDate() {
        return currentPayStopDate;
    }

    public void setCurrentPayStopDate(LocalDateTime currentPayStopDate) {
        this.currentPayStopDate = currentPayStopDate;
    }

    public LocalDateTime getCurrentPayLimitDate() {
        return currentPayLimitDate;
    }

    public void setCurrentPayLimitDate(LocalDateTime currentPayLimitDate) {
        this.currentPayLimitDate = currentPayLimitDate;
    }

    public String getReceiveIdentify() {
        return receiveIdentify;
    }

    public void setReceiveIdentify(String receiveIdentify) {
        this.receiveIdentify = receiveIdentify;
    }

    public Double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getCostIdentify() {
        return costIdentify;
    }

    public void setCostIdentify(String costIdentify) {
        this.costIdentify = costIdentify;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public Integer getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(Integer refundNumber) {
        this.refundNumber = refundNumber;
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

    public Double getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(Double shouldPay) {
        this.shouldPay = shouldPay;
    }

    public Integer getInvalidNumber() {
        return invalidNumber;
    }

    public void setInvalidNumber(Integer invalidNumber) {
        this.invalidNumber = invalidNumber;
    }

    public Double getDerateDelayMoney() {
        return derateDelayMoney;
    }

    public void setDerateDelayMoney(Double derateDelayMoney) {
        this.derateDelayMoney = derateDelayMoney;
    }

    @Override
    public String toString() {
        return "FyShareUserDetail{" +
        "id=" + id +
        ", standingBookId=" + standingBookId +
        ", cellId=" + cellId +
        ", customerName=" + customerName +
        ", boxId=" + boxId +
        ", shareMoney=" + shareMoney +
        ", currentShareNumber=" + currentShareNumber +
        ", currentPayStartDate=" + currentPayStartDate +
        ", currentPayStopDate=" + currentPayStopDate +
        ", currentPayLimitDate=" + currentPayLimitDate +
        ", receiveIdentify=" + receiveIdentify +
        ", priceUnit=" + priceUnit +
        ", costIdentify=" + costIdentify +
        ", receiveId=" + receiveId +
        ", refundNumber=" + refundNumber +
        ", receiveCycle=" + receiveCycle +
        ", derateMoney=" + derateMoney +
        ", shouldPay=" + shouldPay +
        ", invalidNumber=" + invalidNumber +
        ", derateDelayMoney=" + derateDelayMoney +
        "}";
    }
}
