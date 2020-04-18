package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 我的日程
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblMyplan implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 主题
     */
    private String planTheme;

    /**
     * 地点
     */
    private String planAddr;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime stopDate;

    /**
     * 分类
     */
    private String planType;

    /**
     * 状态
     */
    private String planStatus;

    /**
     * 优先级
     */
    private String planPrior;

    /**
     * 备用字段
     */
    private String fieldBak;

    /**
     * 日程描述
     */
    private String planDesc;

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 附件路径
     */
    private String attachUrl;

    /**
     * 所有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 日程附件
     */
    private String planAttach;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanTheme() {
        return planTheme;
    }

    public void setPlanTheme(String planTheme) {
        this.planTheme = planTheme;
    }

    public String getPlanAddr() {
        return planAddr;
    }

    public void setPlanAddr(String planAddr) {
        this.planAddr = planAddr;
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

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getPlanPrior() {
        return planPrior;
    }

    public void setPlanPrior(String planPrior) {
        this.planPrior = planPrior;
    }

    public String getFieldBak() {
        return fieldBak;
    }

    public void setFieldBak(String fieldBak) {
        this.fieldBak = fieldBak;
    }

    public String getPlanDesc() {
        return planDesc;
    }

    public void setPlanDesc(String planDesc) {
        this.planDesc = planDesc;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachUrl() {
        return attachUrl;
    }

    public void setAttachUrl(String attachUrl) {
        this.attachUrl = attachUrl;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getPlanAttach() {
        return planAttach;
    }

    public void setPlanAttach(String planAttach) {
        this.planAttach = planAttach;
    }

    @Override
    public String toString() {
        return "TblMyplan{" +
        "id=" + id +
        ", planTheme=" + planTheme +
        ", planAddr=" + planAddr +
        ", startDate=" + startDate +
        ", stopDate=" + stopDate +
        ", planType=" + planType +
        ", planStatus=" + planStatus +
        ", planPrior=" + planPrior +
        ", fieldBak=" + fieldBak +
        ", planDesc=" + planDesc +
        ", attachName=" + attachName +
        ", attachUrl=" + attachUrl +
        ", owner=" + owner +
        ", createDate=" + createDate +
        ", planAttach=" + planAttach +
        "}";
    }
}
