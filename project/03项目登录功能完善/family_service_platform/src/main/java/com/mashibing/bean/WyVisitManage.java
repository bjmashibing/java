package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 来访管理
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyVisitManage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 来访时间
     */
    private LocalDateTime visitDate;

    /**
     * 离开时间
     */
    private LocalDateTime leaveDate;

    /**
     * 来访人
     */
    private String visitPerson;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 来访人住址
     */
    private String visitAddr;

    /**
     * 来访事由
     */
    private String visitReason;

    /**
     * 被访人
     */
    private String visitedPerson;

    /**
     * 所住地址
     */
    private String visitedReason;

    /**
     * 值班人
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

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public LocalDateTime getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDateTime leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getVisitPerson() {
        return visitPerson;
    }

    public void setVisitPerson(String visitPerson) {
        this.visitPerson = visitPerson;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getVisitAddr() {
        return visitAddr;
    }

    public void setVisitAddr(String visitAddr) {
        this.visitAddr = visitAddr;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    public String getVisitedPerson() {
        return visitedPerson;
    }

    public void setVisitedPerson(String visitedPerson) {
        this.visitedPerson = visitedPerson;
    }

    public String getVisitedReason() {
        return visitedReason;
    }

    public void setVisitedReason(String visitedReason) {
        this.visitedReason = visitedReason;
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
        return "WyVisitManage{" +
        "id=" + id +
        ", visitDate=" + visitDate +
        ", leaveDate=" + leaveDate +
        ", visitPerson=" + visitPerson +
        ", idCard=" + idCard +
        ", visitAddr=" + visitAddr +
        ", visitReason=" + visitReason +
        ", visitedPerson=" + visitedPerson +
        ", visitedReason=" + visitedReason +
        ", agent=" + agent +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
