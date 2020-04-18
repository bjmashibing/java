package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 租赁合同退款
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhRentContractRefund implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属合同编号
     */
    private Integer contractId;

    /**
     * 所属租户id
     */
    private Integer rentId;

    /**
     * 租户名称
     */
    private String rentName;

    /**
     * 退款日期
     */
    private LocalDateTime refundTime;

    /**
     * 退款金额
     */
    private Double refundMoney;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 退款说明
     */
    private String refundDesc;

    /**
     * 操作人id
     */
    private String operateId;

    /**
     * 操作人名称
     */
    private String operatePerson;

    /**
     * 操作时间
     */
    private LocalDateTime operateDate;

    /**
     * 作废人id
     */
    private String invalidId;

    /**
     * 作废人名称
     */
    private String invalidPerson;

    /**
     * 作废原因
     */
    private LocalDateTime invalidReason;

    /**
     * 作废时间
     */
    private String invalidDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getRentId() {
        return rentId;
    }

    public void setRentId(Integer rentId) {
        this.rentId = rentId;
    }

    public String getRentName() {
        return rentName;
    }

    public void setRentName(String rentName) {
        this.rentName = rentName;
    }

    public LocalDateTime getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(LocalDateTime refundTime) {
        this.refundTime = refundTime;
    }

    public Double getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(Double refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

    public LocalDateTime getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(LocalDateTime operateDate) {
        this.operateDate = operateDate;
    }

    public String getInvalidId() {
        return invalidId;
    }

    public void setInvalidId(String invalidId) {
        this.invalidId = invalidId;
    }

    public String getInvalidPerson() {
        return invalidPerson;
    }

    public void setInvalidPerson(String invalidPerson) {
        this.invalidPerson = invalidPerson;
    }

    public LocalDateTime getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(LocalDateTime invalidReason) {
        this.invalidReason = invalidReason;
    }

    public String getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
    }

    @Override
    public String toString() {
        return "ZhRentContractRefund{" +
        "id=" + id +
        ", contractId=" + contractId +
        ", rentId=" + rentId +
        ", rentName=" + rentName +
        ", refundTime=" + refundTime +
        ", refundMoney=" + refundMoney +
        ", refundStatus=" + refundStatus +
        ", refundDesc=" + refundDesc +
        ", operateId=" + operateId +
        ", operatePerson=" + operatePerson +
        ", operateDate=" + operateDate +
        ", invalidId=" + invalidId +
        ", invalidPerson=" + invalidPerson +
        ", invalidReason=" + invalidReason +
        ", invalidDate=" + invalidDate +
        "}";
    }
}
