package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 企业公告
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblComparyNotice implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动增长id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公告主题
     */
    private String noticeTheme;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime stopDate;

    /**
     * 接受类型
     */
    private String receiveType;

    /**
     * 公告分类
     */
    private Long noticeCategory;

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 附件路径
     */
    private String attachPath;

    /**
     * 状态
     */
    private String status;

    /**
     * 公告类别
     */
    private String noticeType;

    /**
     * 公告附件
     */
    private String noticeAttach;

    /**
     * 录入人
     */
    private String inputPerson;

    /**
     * 录入时间
     */
    private LocalDateTime inputDate;

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
     * 允许查看的用户编码
     */
    private String allowUserCode;

    /**
     * 允许查看的用户名称
     */
    private String allowUserName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoticeTheme() {
        return noticeTheme;
    }

    public void setNoticeTheme(String noticeTheme) {
        this.noticeTheme = noticeTheme;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
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

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public Long getNoticeCategory() {
        return noticeCategory;
    }

    public void setNoticeCategory(Long noticeCategory) {
        this.noticeCategory = noticeCategory;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeAttach() {
        return noticeAttach;
    }

    public void setNoticeAttach(String noticeAttach) {
        this.noticeAttach = noticeAttach;
    }

    public String getInputPerson() {
        return inputPerson;
    }

    public void setInputPerson(String inputPerson) {
        this.inputPerson = inputPerson;
    }

    public LocalDateTime getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDateTime inputDate) {
        this.inputDate = inputDate;
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

    public String getAllowUserCode() {
        return allowUserCode;
    }

    public void setAllowUserCode(String allowUserCode) {
        this.allowUserCode = allowUserCode;
    }

    public String getAllowUserName() {
        return allowUserName;
    }

    public void setAllowUserName(String allowUserName) {
        this.allowUserName = allowUserName;
    }

    @Override
    public String toString() {
        return "TblComparyNotice{" +
        "id=" + id +
        ", noticeTheme=" + noticeTheme +
        ", noticeContent=" + noticeContent +
        ", startDate=" + startDate +
        ", stopDate=" + stopDate +
        ", receiveType=" + receiveType +
        ", noticeCategory=" + noticeCategory +
        ", attachName=" + attachName +
        ", attachPath=" + attachPath +
        ", status=" + status +
        ", noticeType=" + noticeType +
        ", noticeAttach=" + noticeAttach +
        ", inputPerson=" + inputPerson +
        ", inputDate=" + inputDate +
        ", checkPerson=" + checkPerson +
        ", checkDate=" + checkDate +
        ", checkAdvice=" + checkAdvice +
        ", allowUserCode=" + allowUserCode +
        ", allowUserName=" + allowUserName +
        "}";
    }
}
