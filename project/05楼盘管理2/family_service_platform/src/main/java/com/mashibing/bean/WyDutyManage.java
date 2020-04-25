package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 执勤管理
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyDutyManage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 执勤日期
     */
    private LocalDateTime dutyDate;

    /**
     * 执勤人
     */
    private String dutyPerson;

    /**
     * 执勤类型
     */
    private String dutyType;

    /**
     * 执勤地点
     */
    private String dutyPlace;

    /**
     * 执勤记录
     */
    private String dutyRecord;

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

    public LocalDateTime getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(LocalDateTime dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getDutyPerson() {
        return dutyPerson;
    }

    public void setDutyPerson(String dutyPerson) {
        this.dutyPerson = dutyPerson;
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public String getDutyPlace() {
        return dutyPlace;
    }

    public void setDutyPlace(String dutyPlace) {
        this.dutyPlace = dutyPlace;
    }

    public String getDutyRecord() {
        return dutyRecord;
    }

    public void setDutyRecord(String dutyRecord) {
        this.dutyRecord = dutyRecord;
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
        return "WyDutyManage{" +
        "id=" + id +
        ", dutyDate=" + dutyDate +
        ", dutyPerson=" + dutyPerson +
        ", dutyType=" + dutyType +
        ", dutyPlace=" + dutyPlace +
        ", dutyRecord=" + dutyRecord +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
