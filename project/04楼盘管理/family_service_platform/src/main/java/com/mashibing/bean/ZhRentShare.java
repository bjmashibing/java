package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 租赁分租信息
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhRentShare implements Serializable {

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
     * 租户名称
     */
    private String rentName;

    /**
     * 分租人
     */
    private String shareRentPerson;

    /**
     * 分租房间id
     */
    private String shareCellId;

    /**
     * 分租房间名称
     */
    private String shareCellName;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 起始日期
     */
    private LocalDateTime startDate;

    /**
     * 截止日期
     */
    private LocalDateTime stopDate;

    /**
     * 经营范围
     */
    private String saleRange;

    /**
     * 备注
     */
    private String remark;

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

    public String getRentName() {
        return rentName;
    }

    public void setRentName(String rentName) {
        this.rentName = rentName;
    }

    public String getShareRentPerson() {
        return shareRentPerson;
    }

    public void setShareRentPerson(String shareRentPerson) {
        this.shareRentPerson = shareRentPerson;
    }

    public String getShareCellId() {
        return shareCellId;
    }

    public void setShareCellId(String shareCellId) {
        this.shareCellId = shareCellId;
    }

    public String getShareCellName() {
        return shareCellName;
    }

    public void setShareCellName(String shareCellName) {
        this.shareCellName = shareCellName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getSaleRange() {
        return saleRange;
    }

    public void setSaleRange(String saleRange) {
        this.saleRange = saleRange;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "ZhRentShare{" +
        "id=" + id +
        ", contractId=" + contractId +
        ", rentName=" + rentName +
        ", shareRentPerson=" + shareRentPerson +
        ", shareCellId=" + shareCellId +
        ", shareCellName=" + shareCellName +
        ", contact=" + contact +
        ", phoneNumber=" + phoneNumber +
        ", startDate=" + startDate +
        ", stopDate=" + stopDate +
        ", saleRange=" + saleRange +
        ", remark=" + remark +
        ", operateId=" + operateId +
        ", operatePerson=" + operatePerson +
        ", operateDate=" + operateDate +
        ", updatePersonId=" + updatePersonId +
        ", updatePersonName=" + updatePersonName +
        ", updateDate=" + updateDate +
        ", updateReason=" + updateReason +
        "}";
    }
}
