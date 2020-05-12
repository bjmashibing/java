package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 业主成员
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhCustomerMembers implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属业主编码
     */
    private Integer belongCustomerId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 出生日期
     */
    private LocalDateTime birdate;

    /**
     * 性别
     */
    private String gender;

    /**
     * 与业主关系
     */
    private String ration;

    /**
     * 证件类型
     */
    private String certificateType;

    /**
     * 证件号码
     */
    private String certificateNumber;

    /**
     * 学历
     */
    private String education;

    /**
     * 备注
     */
    private String remark;

    /**
     * 工作单位
     */
    private String workPlace;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 照片
     */
    private String picture;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBelongCustomerId() {
        return belongCustomerId;
    }

    public void setBelongCustomerId(Integer belongCustomerId) {
        this.belongCustomerId = belongCustomerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getBirdate() {
        return birdate;
    }

    public void setBirdate(LocalDateTime birdate) {
        this.birdate = birdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRation() {
        return ration;
    }

    public void setRation(String ration) {
        this.ration = ration;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "ZhCustomerMembers{" +
        "id=" + id +
        ", belongCustomerId=" + belongCustomerId +
        ", name=" + name +
        ", birdate=" + birdate +
        ", gender=" + gender +
        ", ration=" + ration +
        ", certificateType=" + certificateType +
        ", certificateNumber=" + certificateNumber +
        ", education=" + education +
        ", remark=" + remark +
        ", workPlace=" + workPlace +
        ", phoneNumber=" + phoneNumber +
        ", picture=" + picture +
        "}";
    }
}
