package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 我的意见
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblMyadvice implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 意见箱
     */
    private Integer adviceBox;

    /**
     * 状态
     */
    private String status;

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 发表人id
     */
    private String publisherId;

    /**
     * 发表人名称
     */
    private String publisherName;

    /**
     * 发表时间
     */
    private LocalDateTime publisherDate;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复人id
     */
    private String replyId;

    /**
     * 回复人名称
     */
    private String replyName;

    /**
     * 回复时间
     */
    private LocalDateTime replyDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAdviceBox() {
        return adviceBox;
    }

    public void setAdviceBox(Integer adviceBox) {
        this.adviceBox = adviceBox;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public LocalDateTime getPublisherDate() {
        return publisherDate;
    }

    public void setPublisherDate(LocalDateTime publisherDate) {
        this.publisherDate = publisherDate;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }

    @Override
    public String toString() {
        return "TblMyadvice{" +
        "id=" + id +
        ", title=" + title +
        ", content=" + content +
        ", adviceBox=" + adviceBox +
        ", status=" + status +
        ", attachName=" + attachName +
        ", publisherId=" + publisherId +
        ", publisherName=" + publisherName +
        ", publisherDate=" + publisherDate +
        ", replyContent=" + replyContent +
        ", replyId=" + replyId +
        ", replyName=" + replyName +
        ", replyDate=" + replyDate +
        "}";
    }
}
