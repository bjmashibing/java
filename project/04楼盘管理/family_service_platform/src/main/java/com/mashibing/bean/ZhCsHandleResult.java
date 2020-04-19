package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 业主服务_办理结果
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhCsHandleResult implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属服务单id
     */
    private Integer serviceId;

    /**
     * 办理人id
     */
    private String transactorId;

    /**
     * 办理人名称
     */
    private String transactorName;

    /**
     * 是否负责人
     */
    private String isLeader;

    /**
     * 相关单位
     */
    private String relationCompany;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 开始办理时间
     */
    private LocalDateTime handleStartDate;

    /**
     * 完成办理时间
     */
    private LocalDateTime handleStopDate;

    /**
     * 办理结果
     */
    private String handleResult;

    /**
     * 办理完成附件
     */
    private String handleFinishAttach;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getTransactorId() {
        return transactorId;
    }

    public void setTransactorId(String transactorId) {
        this.transactorId = transactorId;
    }

    public String getTransactorName() {
        return transactorName;
    }

    public void setTransactorName(String transactorName) {
        this.transactorName = transactorName;
    }

    public String getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(String isLeader) {
        this.isLeader = isLeader;
    }

    public String getRelationCompany() {
        return relationCompany;
    }

    public void setRelationCompany(String relationCompany) {
        this.relationCompany = relationCompany;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getHandleStartDate() {
        return handleStartDate;
    }

    public void setHandleStartDate(LocalDateTime handleStartDate) {
        this.handleStartDate = handleStartDate;
    }

    public LocalDateTime getHandleStopDate() {
        return handleStopDate;
    }

    public void setHandleStopDate(LocalDateTime handleStopDate) {
        this.handleStopDate = handleStopDate;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getHandleFinishAttach() {
        return handleFinishAttach;
    }

    public void setHandleFinishAttach(String handleFinishAttach) {
        this.handleFinishAttach = handleFinishAttach;
    }

    @Override
    public String toString() {
        return "ZhCsHandleResult{" +
        "id=" + id +
        ", serviceId=" + serviceId +
        ", transactorId=" + transactorId +
        ", transactorName=" + transactorName +
        ", isLeader=" + isLeader +
        ", relationCompany=" + relationCompany +
        ", phoneNumber=" + phoneNumber +
        ", handleStartDate=" + handleStartDate +
        ", handleStopDate=" + handleStopDate +
        ", handleResult=" + handleResult +
        ", handleFinishAttach=" + handleFinishAttach +
        "}";
    }
}
