package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 企业档案
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblCompany implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 企业编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 企业全称
     */
    private String companyFullName;

    /**
     * 企业简称
     */
    private String companySimpleName;

    /**
     * 英文名称
     */
    private String companyEnglishName;

    /**
     * 企业品牌
     */
    private String companyBrand;

    /**
     * 企业类型
     */
    private String companyType;

    /**
     * 所属行业
     */
    private String companyTrade;

    /**
     * 企业地址
     */
    private String companyAddr;

    /**
     * 邮政编码
     */
    private String postCode;

    /**
     * 企业电话
     */
    private String companyPhone;

    /**
     * 企业传真
     */
    private String companyFax;

    /**
     * 企业网站
     */
    private String companyWebsite;

    /**
     * 企业邮箱
     */
    private String companyEmail;

    /**
     * 国税号
     */
    private String companyNational;

    /**
     * 地税号
     */
    private String companyLand;

    /**
     * 开户银行
     */
    private String openBank;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 法人代表
     */
    private String companyLeader;

    /**
     * 注册时间
     */
    private LocalDateTime registerDate;

    /**
     * 注册资金
     */
    private Double registerMoney;

    /**
     * 员工人数
     */
    private String employeeNumber;

    /**
     * 企业简介
     */
    private String companyIntro;

    /**
     * 备注
     */
    private String remark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyFullName() {
        return companyFullName;
    }

    public void setCompanyFullName(String companyFullName) {
        this.companyFullName = companyFullName;
    }

    public String getCompanySimpleName() {
        return companySimpleName;
    }

    public void setCompanySimpleName(String companySimpleName) {
        this.companySimpleName = companySimpleName;
    }

    public String getCompanyEnglishName() {
        return companyEnglishName;
    }

    public void setCompanyEnglishName(String companyEnglishName) {
        this.companyEnglishName = companyEnglishName;
    }

    public String getCompanyBrand() {
        return companyBrand;
    }

    public void setCompanyBrand(String companyBrand) {
        this.companyBrand = companyBrand;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyTrade() {
        return companyTrade;
    }

    public void setCompanyTrade(String companyTrade) {
        this.companyTrade = companyTrade;
    }

    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(String companyAddr) {
        this.companyAddr = companyAddr;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyFax() {
        return companyFax;
    }

    public void setCompanyFax(String companyFax) {
        this.companyFax = companyFax;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyNational() {
        return companyNational;
    }

    public void setCompanyNational(String companyNational) {
        this.companyNational = companyNational;
    }

    public String getCompanyLand() {
        return companyLand;
    }

    public void setCompanyLand(String companyLand) {
        this.companyLand = companyLand;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getCompanyLeader() {
        return companyLeader;
    }

    public void setCompanyLeader(String companyLeader) {
        this.companyLeader = companyLeader;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public Double getRegisterMoney() {
        return registerMoney;
    }

    public void setRegisterMoney(Double registerMoney) {
        this.registerMoney = registerMoney;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getCompanyIntro() {
        return companyIntro;
    }

    public void setCompanyIntro(String companyIntro) {
        this.companyIntro = companyIntro;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TblCompany{" +
        "id=" + id +
        ", companyFullName=" + companyFullName +
        ", companySimpleName=" + companySimpleName +
        ", companyEnglishName=" + companyEnglishName +
        ", companyBrand=" + companyBrand +
        ", companyType=" + companyType +
        ", companyTrade=" + companyTrade +
        ", companyAddr=" + companyAddr +
        ", postCode=" + postCode +
        ", companyPhone=" + companyPhone +
        ", companyFax=" + companyFax +
        ", companyWebsite=" + companyWebsite +
        ", companyEmail=" + companyEmail +
        ", companyNational=" + companyNational +
        ", companyLand=" + companyLand +
        ", openBank=" + openBank +
        ", bankAccount=" + bankAccount +
        ", companyLeader=" + companyLeader +
        ", registerDate=" + registerDate +
        ", registerMoney=" + registerMoney +
        ", employeeNumber=" + employeeNumber +
        ", companyIntro=" + companyIntro +
        ", remark=" + remark +
        "}";
    }
}
