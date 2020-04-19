package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 邮件发送
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblEmailSend implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 邮件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 是否草稿
     */
    private String isDraft;

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
     * 发送类型
     */
    private String sendType;

    /**
     * 发送人id
     */
    private String sendPerson;

    /**
     * 发送人姓名
     */
    private String sendName;

    /**
     * 发送时间
     */
    private LocalDateTime sendDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(String isDraft) {
        this.isDraft = isDraft;
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

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getSendPerson() {
        return sendPerson;
    }

    public void setSendPerson(String sendPerson) {
        this.sendPerson = sendPerson;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    @Override
    public String toString() {
        return "TblEmailSend{" +
        "id=" + id +
        ", receivePersonCode=" + receivePersonCode +
        ", receivePersonName=" + receivePersonName +
        ", emailTitle=" + emailTitle +
        ", emailContent=" + emailContent +
        ", importantGrade=" + importantGrade +
        ", isDraft=" + isDraft +
        ", isDelete=" + isDelete +
        ", isSecretSend=" + isSecretSend +
        ", emailAttach=" + emailAttach +
        ", sendType=" + sendType +
        ", sendPerson=" + sendPerson +
        ", sendName=" + sendName +
        ", sendDate=" + sendDate +
        "}";
    }
}
