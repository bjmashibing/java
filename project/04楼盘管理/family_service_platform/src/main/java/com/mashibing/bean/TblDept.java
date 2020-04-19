package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 部门信息表
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblDept implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 部门id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门负责人
     */
    private String deptLeader;

    /**
     * 部门电话
     */
    private String deptPhone;

    /**
     * 部门类型
     */
    private Long deptType;

    /**
     * 部门传真
     */
    private String deptFax;

    /**
     * 部门上级编号
     */
    private Integer deptParent;

    /**
     * 部门层级线
     */
    private String deptLine;

    /**
     * 部门权限
     */
    private String deptPrivileges;

    /**
     * 部门管理权限
     */
    private String deptManagePrivileges;

    /**
     * 机构类别
     */
    private String organCategory;

    /**
     * 岗位编制数
     */
    private Integer deptPersonNumber;

    /**
     * 建档人
     */
    private String inputPerson;

    /**
     * 建档时间
     */
    private LocalDateTime inputTime;

    /**
     * 部门备注
     */
    private String deptRemark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptLeader() {
        return deptLeader;
    }

    public void setDeptLeader(String deptLeader) {
        this.deptLeader = deptLeader;
    }

    public String getDeptPhone() {
        return deptPhone;
    }

    public void setDeptPhone(String deptPhone) {
        this.deptPhone = deptPhone;
    }

    public Long getDeptType() {
        return deptType;
    }

    public void setDeptType(Long deptType) {
        this.deptType = deptType;
    }

    public String getDeptFax() {
        return deptFax;
    }

    public void setDeptFax(String deptFax) {
        this.deptFax = deptFax;
    }

    public Integer getDeptParent() {
        return deptParent;
    }

    public void setDeptParent(Integer deptParent) {
        this.deptParent = deptParent;
    }

    public String getDeptLine() {
        return deptLine;
    }

    public void setDeptLine(String deptLine) {
        this.deptLine = deptLine;
    }

    public String getDeptPrivileges() {
        return deptPrivileges;
    }

    public void setDeptPrivileges(String deptPrivileges) {
        this.deptPrivileges = deptPrivileges;
    }

    public String getDeptManagePrivileges() {
        return deptManagePrivileges;
    }

    public void setDeptManagePrivileges(String deptManagePrivileges) {
        this.deptManagePrivileges = deptManagePrivileges;
    }

    public String getOrganCategory() {
        return organCategory;
    }

    public void setOrganCategory(String organCategory) {
        this.organCategory = organCategory;
    }

    public Integer getDeptPersonNumber() {
        return deptPersonNumber;
    }

    public void setDeptPersonNumber(Integer deptPersonNumber) {
        this.deptPersonNumber = deptPersonNumber;
    }

    public String getInputPerson() {
        return inputPerson;
    }

    public void setInputPerson(String inputPerson) {
        this.inputPerson = inputPerson;
    }

    public LocalDateTime getInputTime() {
        return inputTime;
    }

    public void setInputTime(LocalDateTime inputTime) {
        this.inputTime = inputTime;
    }

    public String getDeptRemark() {
        return deptRemark;
    }

    public void setDeptRemark(String deptRemark) {
        this.deptRemark = deptRemark;
    }

    @Override
    public String toString() {
        return "TblDept{" +
        "id=" + id +
        ", deptCode=" + deptCode +
        ", deptName=" + deptName +
        ", deptLeader=" + deptLeader +
        ", deptPhone=" + deptPhone +
        ", deptType=" + deptType +
        ", deptFax=" + deptFax +
        ", deptParent=" + deptParent +
        ", deptLine=" + deptLine +
        ", deptPrivileges=" + deptPrivileges +
        ", deptManagePrivileges=" + deptManagePrivileges +
        ", organCategory=" + organCategory +
        ", deptPersonNumber=" + deptPersonNumber +
        ", inputPerson=" + inputPerson +
        ", inputTime=" + inputTime +
        ", deptRemark=" + deptRemark +
        "}";
    }
}
