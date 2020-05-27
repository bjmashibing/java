package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 租户信息
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhRentInformation implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户编码
     */
    private String rentCode;

    /**
     * 租户名称
     */
    private String rentName;

    /**
     * 法定代表
     */
    private String memberOfRight;

    /**
     * 租户类型
     */
    private Long rentType;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 性别
     */
    private String gender;

    /**
     * 联系电话
     */
    private String homeNumber;

    /**
     * 手机
     */
    private String phoneNumber;

    /**
     * 地址
     */
    private String addr;

    /**
     * 证件类型
     */
    private Long certificateType;

    /**
     * 主营商品
     */
    private Long mainSale;

    /**
     * 证件号码
     */
    private String certificateNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 照片地址
     */
    private String pictureUrl;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 修改人
     */
    private String updatePerson;

    /**
     * 修改时间
     */
    private LocalDateTime updateDate;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 登陆密码
     */
    private String pwd;

    /**
     * 租户附件
     */
    private String rentAttach;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRentCode() {
        return rentCode;
    }

    public void setRentCode(String rentCode) {
        this.rentCode = rentCode;
    }

    public String getRentName() {
        return rentName;
    }

    public void setRentName(String rentName) {
        this.rentName = rentName;
    }

    public String getMemberOfRight() {
        return memberOfRight;
    }

    public void setMemberOfRight(String memberOfRight) {
        this.memberOfRight = memberOfRight;
    }

    public Long getRentType() {
        return rentType;
    }

    public void setRentType(Long rentType) {
        this.rentType = rentType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Long getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(Long certificateType) {
        this.certificateType = certificateType;
    }

    public Long getMainSale() {
        return mainSale;
    }

    public void setMainSale(Long mainSale) {
        this.mainSale = mainSale;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRentAttach() {
        return rentAttach;
    }

    public void setRentAttach(String rentAttach) {
        this.rentAttach = rentAttach;
    }

    @Override
    public String toString() {
        return "ZhRentInformation{" +
        "id=" + id +
        ", rentCode=" + rentCode +
        ", rentName=" + rentName +
        ", memberOfRight=" + memberOfRight +
        ", rentType=" + rentType +
        ", contact=" + contact +
        ", gender=" + gender +
        ", homeNumber=" + homeNumber +
        ", phoneNumber=" + phoneNumber +
        ", addr=" + addr +
        ", certificateType=" + certificateType +
        ", mainSale=" + mainSale +
        ", certificateNumber=" + certificateNumber +
        ", status=" + status +
        ", remark=" + remark +
        ", pictureUrl=" + pictureUrl +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        ", company=" + company +
        ", pwd=" + pwd +
        ", rentAttach=" + rentAttach +
        "}";
    }
}
