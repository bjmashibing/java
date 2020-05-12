package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 租赁合同
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhRentContract implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合同编号
     */
    private String contractId;

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
     * 所属租户id
     */
    private Integer rentId;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 起租日期
     */
    private LocalDateTime startRentDate;

    /**
     * 停租日期
     */
    private LocalDateTime stopRentDate;

    /**
     * 合同租金金额
     */
    private Double contractRentMoney;

    /**
     * 收费面积
     */
    private Double receiveArea;

    /**
     * 合同状态
     */
    private String contractStatus;

    /**
     * 保证金
     */
    private Double ensureMoney;

    /**
     * 保证金说明
     */
    private String ensureMoneyDesc;

    /**
     * 合同附件
     */
    private String contractAttach;

    /**
     * 租期
     */
    private Integer rentDays;

    /**
     * 管理费
     */
    private Double adminMoney;

    /**
     * 是否收取租金
     */
    private String isRentMoney;

    /**
     * 缴纳方式
     */
    private Long payMethod;

    /**
     * 备注
     */
    private String remark;

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

    /**
     * 招商人员id
     */
    private String attractPersonId;

    /**
     * 招商人员姓名
     */
    private String attractPersonName;

    /**
     * 所属公司
     */
    private String company;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
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

    public Integer getRentId() {
        return rentId;
    }

    public void setRentId(Integer rentId) {
        this.rentId = rentId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDateTime getStartRentDate() {
        return startRentDate;
    }

    public void setStartRentDate(LocalDateTime startRentDate) {
        this.startRentDate = startRentDate;
    }

    public LocalDateTime getStopRentDate() {
        return stopRentDate;
    }

    public void setStopRentDate(LocalDateTime stopRentDate) {
        this.stopRentDate = stopRentDate;
    }

    public Double getContractRentMoney() {
        return contractRentMoney;
    }

    public void setContractRentMoney(Double contractRentMoney) {
        this.contractRentMoney = contractRentMoney;
    }

    public Double getReceiveArea() {
        return receiveArea;
    }

    public void setReceiveArea(Double receiveArea) {
        this.receiveArea = receiveArea;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Double getEnsureMoney() {
        return ensureMoney;
    }

    public void setEnsureMoney(Double ensureMoney) {
        this.ensureMoney = ensureMoney;
    }

    public String getEnsureMoneyDesc() {
        return ensureMoneyDesc;
    }

    public void setEnsureMoneyDesc(String ensureMoneyDesc) {
        this.ensureMoneyDesc = ensureMoneyDesc;
    }

    public String getContractAttach() {
        return contractAttach;
    }

    public void setContractAttach(String contractAttach) {
        this.contractAttach = contractAttach;
    }

    public Integer getRentDays() {
        return rentDays;
    }

    public void setRentDays(Integer rentDays) {
        this.rentDays = rentDays;
    }

    public Double getAdminMoney() {
        return adminMoney;
    }

    public void setAdminMoney(Double adminMoney) {
        this.adminMoney = adminMoney;
    }

    public String getIsRentMoney() {
        return isRentMoney;
    }

    public void setIsRentMoney(String isRentMoney) {
        this.isRentMoney = isRentMoney;
    }

    public Long getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Long payMethod) {
        this.payMethod = payMethod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getAttractPersonId() {
        return attractPersonId;
    }

    public void setAttractPersonId(String attractPersonId) {
        this.attractPersonId = attractPersonId;
    }

    public String getAttractPersonName() {
        return attractPersonName;
    }

    public void setAttractPersonName(String attractPersonName) {
        this.attractPersonName = attractPersonName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "ZhRentContract{" +
        "id=" + id +
        ", contractId=" + contractId +
        ", signDate=" + signDate +
        ", startDate=" + startDate +
        ", stopDate=" + stopDate +
        ", rentId=" + rentId +
        ", contact=" + contact +
        ", startRentDate=" + startRentDate +
        ", stopRentDate=" + stopRentDate +
        ", contractRentMoney=" + contractRentMoney +
        ", receiveArea=" + receiveArea +
        ", contractStatus=" + contractStatus +
        ", ensureMoney=" + ensureMoney +
        ", ensureMoneyDesc=" + ensureMoneyDesc +
        ", contractAttach=" + contractAttach +
        ", rentDays=" + rentDays +
        ", adminMoney=" + adminMoney +
        ", isRentMoney=" + isRentMoney +
        ", payMethod=" + payMethod +
        ", remark=" + remark +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        ", attractPersonId=" + attractPersonId +
        ", attractPersonName=" + attractPersonName +
        ", company=" + company +
        "}";
    }
}
