package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 信件收取
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyEmailReceive implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 收件日期
     */
    private LocalDateTime receiveDate;

    /**
     * 领件日期
     */
    private LocalDateTime getDate;

    /**
     * 邮件类别
     */
    private String emailType;

    /**
     * 收件单位
     */
    private String receiveUnit;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 领件人
     */
    private String getPerson;

    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件
     */
    private String card;

    /**
     * 经手人
     */
    private String agent;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属公司
     */
    private String company;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    public LocalDateTime getGetDate() {
        return getDate;
    }

    public void setGetDate(LocalDateTime getDate) {
        this.getDate = getDate;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getReceiveUnit() {
        return receiveUnit;
    }

    public void setReceiveUnit(String receiveUnit) {
        this.receiveUnit = receiveUnit;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getGetPerson() {
        return getPerson;
    }

    public void setGetPerson(String getPerson) {
        this.getPerson = getPerson;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "WyEmailReceive{" +
        "id=" + id +
        ", receiveDate=" + receiveDate +
        ", getDate=" + getDate +
        ", emailType=" + emailType +
        ", receiveUnit=" + receiveUnit +
        ", number=" + number +
        ", getPerson=" + getPerson +
        ", cardType=" + cardType +
        ", card=" + card +
        ", agent=" + agent +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
