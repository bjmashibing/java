package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 员工通讯录
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblEmployeeContact implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 排序号
     */
    private Integer orderId;

    /**
     * 所属类别名称
     */
    private String categoryName;

    /**
     * 所属类别id
     */
    private String categoryId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    private String workNum;

    /**
     * 部门
     */
    private String dept;

    /**
     * 角色
     */
    private String role;

    /**
     * 职位
     */
    private String position;

    /**
     * 性别
     */
    private String gender;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 办公电话
     */
    private String officePhone;

    /**
     * 传真
     */
    private String fax;

    /**
     * 移动电话
     */
    private String movePhone;

    /**
     * 家庭电话
     */
    private String homePhone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * QQ号
     */
    private String qq;

    /**
     * 微信号
     */
    private String wchat;

    /**
     * 内部即时通
     */
    private String innerMsn;

    /**
     * 地址
     */
    private String addr;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人id
     */
    private String createPersonId;

    /**
     * 创建人名称
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMovePhone() {
        return movePhone;
    }

    public void setMovePhone(String movePhone) {
        this.movePhone = movePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWchat() {
        return wchat;
    }

    public void setWchat(String wchat) {
        this.wchat = wchat;
    }

    public String getInnerMsn() {
        return innerMsn;
    }

    public void setInnerMsn(String innerMsn) {
        this.innerMsn = innerMsn;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(String createPersonId) {
        this.createPersonId = createPersonId;
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

    @Override
    public String toString() {
        return "TblEmployeeContact{" +
        "id=" + id +
        ", orderId=" + orderId +
        ", categoryName=" + categoryName +
        ", categoryId=" + categoryId +
        ", name=" + name +
        ", workNum=" + workNum +
        ", dept=" + dept +
        ", role=" + role +
        ", position=" + position +
        ", gender=" + gender +
        ", birthday=" + birthday +
        ", officePhone=" + officePhone +
        ", fax=" + fax +
        ", movePhone=" + movePhone +
        ", homePhone=" + homePhone +
        ", email=" + email +
        ", qq=" + qq +
        ", wchat=" + wchat +
        ", innerMsn=" + innerMsn +
        ", addr=" + addr +
        ", postCode=" + postCode +
        ", remark=" + remark +
        ", createPersonId=" + createPersonId +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        "}";
    }
}
