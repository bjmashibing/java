package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 植被信息
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyVegetationInformation implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 植被编号
     */
    @TableId(value = "vegetation_id", type = IdType.AUTO)
    private String vegetationId;

    /**
     * 植被名称
     */
    private String vegetationName;

    /**
     * 种类
     */
    private String vegetationType;

    /**
     * 树龄
     */
    private Integer vegetationAge;

    /**
     * 数量
     */
    private Integer vegetationNumber;

    /**
     * 单位
     */
    private String vegetationUnit;

    /**
     * 习性
     */
    private String vegetationHabit;

    /**
     * 特点
     */
    private String vegetationFeature;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属公司
     */
    private String company;


    public String getVegetationId() {
        return vegetationId;
    }

    public void setVegetationId(String vegetationId) {
        this.vegetationId = vegetationId;
    }

    public String getVegetationName() {
        return vegetationName;
    }

    public void setVegetationName(String vegetationName) {
        this.vegetationName = vegetationName;
    }

    public String getVegetationType() {
        return vegetationType;
    }

    public void setVegetationType(String vegetationType) {
        this.vegetationType = vegetationType;
    }

    public Integer getVegetationAge() {
        return vegetationAge;
    }

    public void setVegetationAge(Integer vegetationAge) {
        this.vegetationAge = vegetationAge;
    }

    public Integer getVegetationNumber() {
        return vegetationNumber;
    }

    public void setVegetationNumber(Integer vegetationNumber) {
        this.vegetationNumber = vegetationNumber;
    }

    public String getVegetationUnit() {
        return vegetationUnit;
    }

    public void setVegetationUnit(String vegetationUnit) {
        this.vegetationUnit = vegetationUnit;
    }

    public String getVegetationHabit() {
        return vegetationHabit;
    }

    public void setVegetationHabit(String vegetationHabit) {
        this.vegetationHabit = vegetationHabit;
    }

    public String getVegetationFeature() {
        return vegetationFeature;
    }

    public void setVegetationFeature(String vegetationFeature) {
        this.vegetationFeature = vegetationFeature;
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
        return "WyVegetationInformation{" +
        "vegetationId=" + vegetationId +
        ", vegetationName=" + vegetationName +
        ", vegetationType=" + vegetationType +
        ", vegetationAge=" + vegetationAge +
        ", vegetationNumber=" + vegetationNumber +
        ", vegetationUnit=" + vegetationUnit +
        ", vegetationHabit=" + vegetationHabit +
        ", vegetationFeature=" + vegetationFeature +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
