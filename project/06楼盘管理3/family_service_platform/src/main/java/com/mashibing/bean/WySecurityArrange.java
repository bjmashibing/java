package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 保安安排
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WySecurityArrange implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 开始日期
     */
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    private LocalDateTime stopDate;

    /**
     * 班次
     */
    private String classes;

    /**
     * 时段
     */
    private String timeFrame;

    /**
     * 地段
     */
    private String district;

    /**
     * 值班人员
     */
    private String waterkeeper;

    /**
     * 岗位
     */
    private String job;

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

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWaterkeeper() {
        return waterkeeper;
    }

    public void setWaterkeeper(String waterkeeper) {
        this.waterkeeper = waterkeeper;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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
        return "WySecurityArrange{" +
        "id=" + id +
        ", startDate=" + startDate +
        ", stopDate=" + stopDate +
        ", classes=" + classes +
        ", timeFrame=" + timeFrame +
        ", district=" + district +
        ", waterkeeper=" + waterkeeper +
        ", job=" + job +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
