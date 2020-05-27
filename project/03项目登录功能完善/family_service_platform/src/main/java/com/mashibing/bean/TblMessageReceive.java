package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 短信接受表
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblMessageReceive implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 记录编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 拓展号码
     */
    private String extendPhone;

    /**
     * 短信内容
     */
    private String messageContent;

    /**
     * 回复时间
     */
    private LocalDateTime replyDate;

    /**
     * 位置序号
     */
    private String positionOrder;

    /**
     * 接受时间
     */
    private LocalDateTime receiveDate;

    /**
     * 读取标记
     */
    private Integer readTag;

    /**
     * 读取时间
     */
    private LocalDateTime readDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExtendPhone() {
        return extendPhone;
    }

    public void setExtendPhone(String extendPhone) {
        this.extendPhone = extendPhone;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }

    public String getPositionOrder() {
        return positionOrder;
    }

    public void setPositionOrder(String positionOrder) {
        this.positionOrder = positionOrder;
    }

    public LocalDateTime getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Integer getReadTag() {
        return readTag;
    }

    public void setReadTag(Integer readTag) {
        this.readTag = readTag;
    }

    public LocalDateTime getReadDate() {
        return readDate;
    }

    public void setReadDate(LocalDateTime readDate) {
        this.readDate = readDate;
    }

    @Override
    public String toString() {
        return "TblMessageReceive{" +
        "id=" + id +
        ", phone=" + phone +
        ", extendPhone=" + extendPhone +
        ", messageContent=" + messageContent +
        ", replyDate=" + replyDate +
        ", positionOrder=" + positionOrder +
        ", receiveDate=" + receiveDate +
        ", readTag=" + readTag +
        ", readDate=" + readDate +
        "}";
    }
}
