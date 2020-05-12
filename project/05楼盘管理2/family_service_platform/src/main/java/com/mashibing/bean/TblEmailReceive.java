package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 邮件接受
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblEmailReceive implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 接受id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属邮件id
     */
    private Long emailSendId;

    /**
     * 单个接收人id
     */
    private String receiveId;

    /**
     * 接受人群编码
     */
    private String receivePersonCode;

    /**
     * 接受人群名称
     */
    private String receivePersonName;

    /**
     * 邮件标题
     */
    private String emailTitle;

    /**
     * 邮件内容
     */
    private String emailContent;

    /**
     * 重要级别
     */
    private String importantGrade;

    /**
     * 状态
     */
    private String status;

    /**
     * 删除标志
     */
    private String isDelete;

    /**
     * 密送标志
     */
    private String isSecretSend;

    /**
     * 邮件附件
     */
    private String emailAttach;

    /**
     * 接受类型
     */
    private String receiveType;

    /**
     * 发送人id
     */
    private String sendPersonId;

    /**
     * 发送人姓名
     */
    private String sendPersonName;

    /**
     * 发送时间
     */
    private LocalDateTime sendDate;

    /**
     * 接受时间
     */
    private LocalDateTime receiveDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getEmailSendId() {
        return emailSendId;
    }

    public void setEmailSendId(Long emailSendId) {
        this.emailSendId = emailSendId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceivePersonCode() {
        return receivePersonCode;
    }

    public void setReceivePersonCode(String receivePersonCode) {
        this.receivePersonCode = receivePersonCode;
    }

    public String getReceivePersonName() {
        return receivePersonName;
    }

    public void setReceivePersonName(String receivePersonName) {
        this.receivePersonName = receivePersonName;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getImportantGrade() {
        return importantGrade;
    }

    public void setImportantGrade(String importantGrade) {
        this.importantGrade = importantGrade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsSecretSend() {
        return isSecretSend;
    }

    public void setIsSecretSend(String isSecretSend) {
        this.isSecretSend = isSecretSend;
    }

    public String getEmailAttach() {
        return emailAttach;
    }

    public void setEmailAttach(String emailAttach) {
        this.emailAttach = emailAttach;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getSendPersonId() {
        return sendPersonId;
    }

    public void setSendPersonId(String sendPersonId) {
        this.sendPersonId = sendPersonId;
    }

    public String getSendPersonName() {
        return sendPersonName;
    }

    public void setSendPersonName(String sendPersonName) {
        this.sendPersonName = sendPersonName;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public LocalDateTime getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    @Override
    public String toString() {
        return "TblEmailReceive{" +
        "id=" + id +
        ", emailSendId=" + emailSendId +
        ", receiveId=" + receiveId +
        ", receivePersonCode=" + receivePersonCode +
        ", receivePersonName=" + receivePersonName +
        ", emailTitle=" + emailTitle +
        ", emailContent=" + emailContent +
        ", importantGrade=" + importantGrade +
        ", status=" + status +
        ", isDelete=" + isDelete +
        ", isSecretSend=" + isSecretSend +
        ", emailAttach=" + emailAttach +
        ", receiveType=" + receiveType +
        ", sendPersonId=" + sendPersonId +
        ", sendPersonName=" + sendPersonName +
        ", sendDate=" + sendDate +
        ", receiveDate=" + receiveDate +
        "}";
    }
}
