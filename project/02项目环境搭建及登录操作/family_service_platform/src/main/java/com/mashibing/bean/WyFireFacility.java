package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 消防设施
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyFireFacility implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设施名称
     */
    private String facilityName;

    /**
     * 规格型号
     */
    private String specifications;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 放置地点
     */
    private String place;

    /**
     * 负责人
     */
    private String leader;

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

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
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
        return "WyFireFacility{" +
        "id=" + id +
        ", facilityName=" + facilityName +
        ", specifications=" + specifications +
        ", unit=" + unit +
        ", number=" + number +
        ", place=" + place +
        ", leader=" + leader +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
