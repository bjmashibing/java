package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 催缴短信提醒日志
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyAskMsgRemindLog implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房间id
     */
    private Integer cellId;

    /**
     * 费项id
     */
    private Integer moneyId;

    /**
     * 接受号码
     */
    private String receivePhone;

    /**
     * 缴费限期
     */
    private LocalDateTime payLimitDay;

    /**
     * 提醒天数
     */
    private Integer remindDays;

    /**
     * 房产名称
     */
    private String cellName;

    /**
     * 发送人id
     */
    private String sendPersonId;

    /**
     * 发送人名称
     */
    private String sendPersonName;

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

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public Integer getMoneyId() {
        return moneyId;
    }

    public void setMoneyId(Integer moneyId) {
        this.moneyId = moneyId;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public LocalDateTime getPayLimitDay() {
        return payLimitDay;
    }

    public void setPayLimitDay(LocalDateTime payLimitDay) {
        this.payLimitDay = payLimitDay;
    }

    public Integer getRemindDays() {
        return remindDays;
    }

    public void setRemindDays(Integer remindDays) {
        this.remindDays = remindDays;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
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

    @Override
    public String toString() {
        return "WyAskMsgRemindLog{" +
        "id=" + id +
        ", cellId=" + cellId +
        ", moneyId=" + moneyId +
        ", receivePhone=" + receivePhone +
        ", payLimitDay=" + payLimitDay +
        ", remindDays=" + remindDays +
        ", cellName=" + cellName +
        ", sendPersonId=" + sendPersonId +
        ", sendPersonName=" + sendPersonName +
        ", sendDate=" + sendDate +
        "}";
    }
}
