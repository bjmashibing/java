package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 租赁合同返利
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhRentContractReturn implements Serializable {

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
     * 返利日期起
     */
    private LocalDateTime returnMoneyStart;

    /**
     * 返利日期终
     */
    private LocalDateTime returnMoneyStop;

    /**
     * 返利基数金额
     */
    private Double returnCardinalMoney;

    /**
     * 返利比例
     */
    private Double returnRate;

    /**
     * 本次返利金额
     */
    private Double currentReturnMoney;

    /**
     * 返利状态
     */
    private String returnMoneyStatus;

    /**
     * 返利说明
     */
    private String returnMoneyDesc;

    /**
     * 操作人id
     */
    private String operateId;

    /**
     * 操作人名称
     */
    private String operateName;

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
     * 作废时间
     */
    private LocalDateTime invalidDate;

    /**
     * 作废原因
     */
    private String invalidReason;

    /**
     * 修改人id
     */
    private String updatePersonId;

    /**
     * 修改人名称
     */
    private String updatePersonName;

    /**
     * 修改时间
     */
    private LocalDateTime updateDate;

    /**
     * 修改原因
     */
    private String updateReason;


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

    public LocalDateTime getReturnMoneyStart() {
        return returnMoneyStart;
    }

    public void setReturnMoneyStart(LocalDateTime returnMoneyStart) {
        this.returnMoneyStart = returnMoneyStart;
    }

    public LocalDateTime getReturnMoneyStop() {
        return returnMoneyStop;
    }

    public void setReturnMoneyStop(LocalDateTime returnMoneyStop) {
        this.returnMoneyStop = returnMoneyStop;
    }

    public Double getReturnCardinalMoney() {
        return returnCardinalMoney;
    }

    public void setReturnCardinalMoney(Double returnCardinalMoney) {
        this.returnCardinalMoney = returnCardinalMoney;
    }

    public Double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(Double returnRate) {
        this.returnRate = returnRate;
    }

    public Double getCurrentReturnMoney() {
        return currentReturnMoney;
    }

    public void setCurrentReturnMoney(Double currentReturnMoney) {
        this.currentReturnMoney = currentReturnMoney;
    }

    public String getReturnMoneyStatus() {
        return returnMoneyStatus;
    }

    public void setReturnMoneyStatus(String returnMoneyStatus) {
        this.returnMoneyStatus = returnMoneyStatus;
    }

    public String getReturnMoneyDesc() {
        return returnMoneyDesc;
    }

    public void setReturnMoneyDesc(String returnMoneyDesc) {
        this.returnMoneyDesc = returnMoneyDesc;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
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

    public LocalDateTime getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(LocalDateTime invalidDate) {
        this.invalidDate = invalidDate;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }

    public String getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(String updatePersonId) {
        this.updatePersonId = updatePersonId;
    }

    public String getUpdatePersonName() {
        return updatePersonName;
    }

    public void setUpdatePersonName(String updatePersonName) {
        this.updatePersonName = updatePersonName;
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

    @Override
    public String toString() {
        return "ZhRentContractReturn{" +
        "id=" + id +
        ", contractId=" + contractId +
        ", rentId=" + rentId +
        ", rentName=" + rentName +
        ", returnMoneyStart=" + returnMoneyStart +
        ", returnMoneyStop=" + returnMoneyStop +
        ", returnCardinalMoney=" + returnCardinalMoney +
        ", returnRate=" + returnRate +
        ", currentReturnMoney=" + currentReturnMoney +
        ", returnMoneyStatus=" + returnMoneyStatus +
        ", returnMoneyDesc=" + returnMoneyDesc +
        ", operateId=" + operateId +
        ", operateName=" + operateName +
        ", operateDate=" + operateDate +
        ", invalidId=" + invalidId +
        ", invalidPerson=" + invalidPerson +
        ", invalidDate=" + invalidDate +
        ", invalidReason=" + invalidReason +
        ", updatePersonId=" + updatePersonId +
        ", updatePersonName=" + updatePersonName +
        ", updateDate=" + updateDate +
        ", updateReason=" + updateReason +
        "}";
    }
}
