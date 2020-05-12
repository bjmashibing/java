package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 业主服务
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhCustomerService implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房间编号
     */
    private Integer cellId;

    /**
     * 申请人姓名
     */
    private String proposer;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 诉求时间
     */
    private LocalDateTime appealDate;

    /**
     * 诉求事项
     */
    private String appealEvent;

    /**
     * 状态
     */
    private String status;

    /**
     * 服务类型
     */
    private Long serviceType;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 标识
     */
    private String identify;

    /**
     * 审批人
     */
    private String checkPerson;

    /**
     * 审批时间
     */
    private LocalDateTime checkDate;

    /**
     * 审批意见
     */
    private String checkAdvice;

    /**
     * 服务费用
     */
    private Double serviceMoney;

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
     * 参考附件
     */
    private String referAttach;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getAppealDate() {
        return appealDate;
    }

    public void setAppealDate(LocalDateTime appealDate) {
        this.appealDate = appealDate;
    }

    public String getAppealEvent() {
        return appealEvent;
    }

    public void setAppealEvent(String appealEvent) {
        this.appealEvent = appealEvent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getServiceType() {
        return serviceType;
    }

    public void setServiceType(Long serviceType) {
        this.serviceType = serviceType;
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

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckAdvice() {
        return checkAdvice;
    }

    public void setCheckAdvice(String checkAdvice) {
        this.checkAdvice = checkAdvice;
    }

    public Double getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(Double serviceMoney) {
        this.serviceMoney = serviceMoney;
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

    public String getReferAttach() {
        return referAttach;
    }

    public void setReferAttach(String referAttach) {
        this.referAttach = referAttach;
    }

    @Override
    public String toString() {
        return "ZhCustomerService{" +
        "id=" + id +
        ", cellId=" + cellId +
        ", proposer=" + proposer +
        ", phoneNumber=" + phoneNumber +
        ", appealDate=" + appealDate +
        ", appealEvent=" + appealEvent +
        ", status=" + status +
        ", serviceType=" + serviceType +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", identify=" + identify +
        ", checkPerson=" + checkPerson +
        ", checkDate=" + checkDate +
        ", checkAdvice=" + checkAdvice +
        ", serviceMoney=" + serviceMoney +
        ", returnVisitPerson=" + returnVisitPerson +
        ", returnVisitDate=" + returnVisitDate +
        ", isSatisfy=" + isSatisfy +
        ", customerEvaluate=" + customerEvaluate +
        ", referAttach=" + referAttach +
        "}";
    }
}
