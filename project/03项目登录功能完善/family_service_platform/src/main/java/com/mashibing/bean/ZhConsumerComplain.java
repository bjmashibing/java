package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 业主投诉
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhConsumerComplain implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房间编号
     */
    private String cellId;

    /**
     * 投诉人
     */
    private String complainPerson;

    /**
     * 投诉电话
     */
    private String complainPhone;

    /**
     * 投诉日期
     */
    private LocalDateTime complainDate;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 接待人
     */
    private String receptionPerson;

    /**
     * 投诉类别
     */
    private String complainType;

    /**
     * 状态
     */
    private String status;

    /**
     * 开始受理人
     */
    private String startAcceptPerson;

    /**
     * 开始受理时间
     */
    private LocalDateTime startAcceptDate;

    /**
     * 受理结果
     */
    private String acceptResult;

    /**
     * 受理完成人
     */
    private String acceptFinishPerson;

    /**
     * 受理完成时间
     */
    private LocalDateTime acceptFinishDate;

    /**
     * 数据来源
     */
    private String datasource;

    /**
     * 参考附件
     */
    private String referAttach;

    /**
     * 回访人
     */
    private String returnVisitPerson;

    /**
     * 回访时间
     */
    private LocalDateTime returnVisitDate;

    /**
     * 是否满意
     */
    private String isSatisfy;

    /**
     * 业主评价
     */
    private String customerEvaluate;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getComplainPerson() {
        return complainPerson;
    }

    public void setComplainPerson(String complainPerson) {
        this.complainPerson = complainPerson;
    }

    public String getComplainPhone() {
        return complainPhone;
    }

    public void setComplainPhone(String complainPhone) {
        this.complainPhone = complainPhone;
    }

    public LocalDateTime getComplainDate() {
        return complainDate;
    }

    public void setComplainDate(LocalDateTime complainDate) {
        this.complainDate = complainDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getReceptionPerson() {
        return receptionPerson;
    }

    public void setReceptionPerson(String receptionPerson) {
        this.receptionPerson = receptionPerson;
    }

    public String getComplainType() {
        return complainType;
    }

    public void setComplainType(String complainType) {
        this.complainType = complainType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartAcceptPerson() {
        return startAcceptPerson;
    }

    public void setStartAcceptPerson(String startAcceptPerson) {
        this.startAcceptPerson = startAcceptPerson;
    }

    public LocalDateTime getStartAcceptDate() {
        return startAcceptDate;
    }

    public void setStartAcceptDate(LocalDateTime startAcceptDate) {
        this.startAcceptDate = startAcceptDate;
    }

    public String getAcceptResult() {
        return acceptResult;
    }

    public void setAcceptResult(String acceptResult) {
        this.acceptResult = acceptResult;
    }

    public String getAcceptFinishPerson() {
        return acceptFinishPerson;
    }

    public void setAcceptFinishPerson(String acceptFinishPerson) {
        this.acceptFinishPerson = acceptFinishPerson;
    }

    public LocalDateTime getAcceptFinishDate() {
        return acceptFinishDate;
    }

    public void setAcceptFinishDate(LocalDateTime acceptFinishDate) {
        this.acceptFinishDate = acceptFinishDate;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getReferAttach() {
        return referAttach;
    }

    public void setReferAttach(String referAttach) {
        this.referAttach = referAttach;
    }

    public String getReturnVisitPerson() {
        return returnVisitPerson;
    }

    public void setReturnVisitPerson(String returnVisitPerson) {
        this.returnVisitPerson = returnVisitPerson;
    }

    public LocalDateTime getReturnVisitDate() {
        return returnVisitDate;
    }

    public void setReturnVisitDate(LocalDateTime returnVisitDate) {
        this.returnVisitDate = returnVisitDate;
    }

    public String getIsSatisfy() {
        return isSatisfy;
    }

    public void setIsSatisfy(String isSatisfy) {
        this.isSatisfy = isSatisfy;
    }

    public String getCustomerEvaluate() {
        return customerEvaluate;
    }

    public void setCustomerEvaluate(String customerEvaluate) {
        this.customerEvaluate = customerEvaluate;
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

    @Override
    public String toString() {
        return "ZhConsumerComplain{" +
        "id=" + id +
        ", cellId=" + cellId +
        ", complainPerson=" + complainPerson +
        ", complainPhone=" + complainPhone +
        ", complainDate=" + complainDate +
        ", phoneNumber=" + phoneNumber +
        ", receptionPerson=" + receptionPerson +
        ", complainType=" + complainType +
        ", status=" + status +
        ", startAcceptPerson=" + startAcceptPerson +
        ", startAcceptDate=" + startAcceptDate +
        ", acceptResult=" + acceptResult +
        ", acceptFinishPerson=" + acceptFinishPerson +
        ", acceptFinishDate=" + acceptFinishDate +
        ", datasource=" + datasource +
        ", referAttach=" + referAttach +
        ", returnVisitPerson=" + returnVisitPerson +
        ", returnVisitDate=" + returnVisitDate +
        ", isSatisfy=" + isSatisfy +
        ", customerEvaluate=" + customerEvaluate +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        "}";
    }
}
