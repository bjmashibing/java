package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 业主信息表
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhCustomer implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 业主编码
     */
    private String customerCode;

    /**
     * 业主密码
     */
    private String customerPwd;

    /**
     * 业主姓名
     */
    private String customerName;

    /**
     * 业主生日
     */
    private String customerBirthday;

    /**
     * 业主性别
     */
    private String customerGender;

    /**
     * 开户行
     */
    private String openBank;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 学历
     */
    private String education;

    /**
     * 证件号码
     */
    private String certificateNumber;

    /**
     * 证件类型
     */
    private String certificateType;

    /**
     * 工作单位
     */
    private String workPlace;

    /**
     * 职务
     */
    private String customerDuty;

    /**
     * 所在派出所
     */
    private String police;

    /**
     * 民族
     */
    private String nation;

    /**
     * 联系电话
     */
    private String phoneNumber;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 紧急联系人姓名
     */
    private String urgencyUserName;

    /**
     * 紧急联系人电话
     */
    private String urgencyUserPhone;

    /**
     * 紧急联系人地址
     */
    private String urgencyUserAddress;

    /**
     * 业主状态
     */
    private String customerStatus;

    /**
     * 业主类型
     */
    private String customerType;

    /**
     * 照片
     */
    private String picture;

    /**
     * 备注
     */
    private String remark;

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
     * 是否银行代扣
     */
    private String isBankWithhold;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerPwd() {
        return customerPwd;
    }

    public void setCustomerPwd(String customerPwd) {
        this.customerPwd = customerPwd;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerBirthday() {
        return customerBirthday;
    }

    public void setCustomerBirthday(String customerBirthday) {
        this.customerBirthday = customerBirthday;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getCustomerDuty() {
        return customerDuty;
    }

    public void setCustomerDuty(String customerDuty) {
        this.customerDuty = customerDuty;
    }

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getUrgencyUserName() {
        return urgencyUserName;
    }

    public void setUrgencyUserName(String urgencyUserName) {
        this.urgencyUserName = urgencyUserName;
    }

    public String getUrgencyUserPhone() {
        return urgencyUserPhone;
    }

    public void setUrgencyUserPhone(String urgencyUserPhone) {
        this.urgencyUserPhone = urgencyUserPhone;
    }

    public String getUrgencyUserAddress() {
        return urgencyUserAddress;
    }

    public void setUrgencyUserAddress(String urgencyUserAddress) {
        this.urgencyUserAddress = urgencyUserAddress;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getIsBankWithhold() {
        return isBankWithhold;
    }

    public void setIsBankWithhold(String isBankWithhold) {
        this.isBankWithhold = isBankWithhold;
    }

    @Override
    public String toString() {
        return "ZhCustomer{" +
        "id=" + id +
        ", customerCode=" + customerCode +
        ", customerPwd=" + customerPwd +
        ", customerName=" + customerName +
        ", customerBirthday=" + customerBirthday +
        ", customerGender=" + customerGender +
        ", openBank=" + openBank +
        ", nationality=" + nationality +
        ", bankAccount=" + bankAccount +
        ", education=" + education +
        ", certificateNumber=" + certificateNumber +
        ", certificateType=" + certificateType +
        ", workPlace=" + workPlace +
        ", customerDuty=" + customerDuty +
        ", police=" + police +
        ", nation=" + nation +
        ", phoneNumber=" + phoneNumber +
        ", nativePlace=" + nativePlace +
        ", address=" + address +
        ", postCode=" + postCode +
        ", urgencyUserName=" + urgencyUserName +
        ", urgencyUserPhone=" + urgencyUserPhone +
        ", urgencyUserAddress=" + urgencyUserAddress +
        ", customerStatus=" + customerStatus +
        ", customerType=" + customerType +
        ", picture=" + picture +
        ", remark=" + remark +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        ", company=" + company +
        ", isBankWithhold=" + isBankWithhold +
        "}";
    }
}
