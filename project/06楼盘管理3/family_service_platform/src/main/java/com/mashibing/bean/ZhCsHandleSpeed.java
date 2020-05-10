package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 业主服务_办理进度
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhCsHandleSpeed implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务单id
     */
    private Integer serviceId;

    /**
     * 办理人
     */
    private String transactorName;

    /**
     * 办理时间
     */
    private LocalDateTime transactorDate;

    /**
     * 办理内容
     */
    private String transactorContent;

    /**
     * 记录人id
     */
    private String recorderId;

    /**
     * 记录人名称
     */
    private String recorderName;

    /**
     * 记录时间
     */
    private LocalDateTime recorderDate;


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

    public String getTransactorName() {
        return transactorName;
    }

    public void setTransactorName(String transactorName) {
        this.transactorName = transactorName;
    }

    public LocalDateTime getTransactorDate() {
        return transactorDate;
    }

    public void setTransactorDate(LocalDateTime transactorDate) {
        this.transactorDate = transactorDate;
    }

    public String getTransactorContent() {
        return transactorContent;
    }

    public void setTransactorContent(String transactorContent) {
        this.transactorContent = transactorContent;
    }

    public String getRecorderId() {
        return recorderId;
    }

    public void setRecorderId(String recorderId) {
        this.recorderId = recorderId;
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    public LocalDateTime getRecorderDate() {
        return recorderDate;
    }

    public void setRecorderDate(LocalDateTime recorderDate) {
        this.recorderDate = recorderDate;
    }

    @Override
    public String toString() {
        return "ZhCsHandleSpeed{" +
        "id=" + id +
        ", serviceId=" + serviceId +
        ", transactorName=" + transactorName +
        ", transactorDate=" + transactorDate +
        ", transactorContent=" + transactorContent +
        ", recorderId=" + recorderId +
        ", recorderName=" + recorderName +
        ", recorderDate=" + recorderDate +
        "}";
    }
}
