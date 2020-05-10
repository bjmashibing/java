package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 车位租赁
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyCarSpaceRent implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合同编号
     */
    private String constractId;

    /**
     * 所属车位编号
     */
    private String carSpaceId;

    /**
     * 租期开始日
     */
    private LocalDateTime rentStartDate;

    /**
     * 租期结束日
     */
    private LocalDateTime rentStopDate;

    /**
     * 承租月数
     */
    private Double rentMonth;

    /**
     * 使用人id
     */
    private Integer userId;

    /**
     * 使用人名称
     */
    private String userName;

    /**
     * 车牌号码
     */
    private String carLicenceId;

    /**
     * 停车证号
     */
    private String stopCarLicence;

    /**
     * 月租金
     */
    private Double rentPerMonth;

    /**
     * 月服务费
     */
    private Double serviceMoneyPerMonth;

    /**
     * 签订日期
     */
    private LocalDateTime signDate;

    /**
     * 生效日期
     */
    private LocalDateTime startDate;

    /**
     * 终止日期
     */
    private LocalDateTime stopDate;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 中介费
     */
    private Double agentMoney;

    /**
     * 是否收取租金
     */
    private String isRentMoney;

    /**
     * 合同附件
     */
    private String contractAttach;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 修改人
     */
    private String updatePerson;

    /**
     * 修改时间
     */
    private LocalDateTime updateDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConstractId() {
        return constractId;
    }

    public void setConstractId(String constractId) {
        this.constractId = constractId;
    }

    public String getCarSpaceId() {
        return carSpaceId;
    }

    public void setCarSpaceId(String carSpaceId) {
        this.carSpaceId = carSpaceId;
    }

    public LocalDateTime getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(LocalDateTime rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public LocalDateTime getRentStopDate() {
        return rentStopDate;
    }

    public void setRentStopDate(LocalDateTime rentStopDate) {
        this.rentStopDate = rentStopDate;
    }

    public Double getRentMonth() {
        return rentMonth;
    }

    public void setRentMonth(Double rentMonth) {
        this.rentMonth = rentMonth;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCarLicenceId() {
        return carLicenceId;
    }

    public void setCarLicenceId(String carLicenceId) {
        this.carLicenceId = carLicenceId;
    }

    public String getStopCarLicence() {
        return stopCarLicence;
    }

    public void setStopCarLicence(String stopCarLicence) {
        this.stopCarLicence = stopCarLicence;
    }

    public Double getRentPerMonth() {
        return rentPerMonth;
    }

    public void setRentPerMonth(Double rentPerMonth) {
        this.rentPerMonth = rentPerMonth;
    }

    public Double getServiceMoneyPerMonth() {
        return serviceMoneyPerMonth;
    }

    public void setServiceMoneyPerMonth(Double serviceMoneyPerMonth) {
        this.serviceMoneyPerMonth = serviceMoneyPerMonth;
    }

    public LocalDateTime getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDateTime signDate) {
        this.signDate = signDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getStopDate() {
        return stopDate;
    }

    public void setStopDate(LocalDateTime stopDate) {
        this.stopDate = stopDate;
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

    public Double getAgentMoney() {
        return agentMoney;
    }

    public void setAgentMoney(Double agentMoney) {
        this.agentMoney = agentMoney;
    }

    public String getIsRentMoney() {
        return isRentMoney;
    }

    public void setIsRentMoney(String isRentMoney) {
        this.isRentMoney = isRentMoney;
    }

    public String getContractAttach() {
        return contractAttach;
    }

    public void setContractAttach(String contractAttach) {
        this.contractAttach = contractAttach;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
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

    @Override
    public String toString() {
        return "WyCarSpaceRent{" +
        "id=" + id +
        ", constractId=" + constractId +
        ", carSpaceId=" + carSpaceId +
        ", rentStartDate=" + rentStartDate +
        ", rentStopDate=" + rentStopDate +
        ", rentMonth=" + rentMonth +
        ", userId=" + userId +
        ", userName=" + userName +
        ", carLicenceId=" + carLicenceId +
        ", stopCarLicence=" + stopCarLicence +
        ", rentPerMonth=" + rentPerMonth +
        ", serviceMoneyPerMonth=" + serviceMoneyPerMonth +
        ", signDate=" + signDate +
        ", startDate=" + startDate +
        ", stopDate=" + stopDate +
        ", status=" + status +
        ", remark=" + remark +
        ", agentMoney=" + agentMoney +
        ", isRentMoney=" + isRentMoney +
        ", contractAttach=" + contractAttach +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        "}";
    }
}
