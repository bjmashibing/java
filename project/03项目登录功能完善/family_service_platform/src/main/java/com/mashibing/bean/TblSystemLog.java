package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblSystemLog implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日志内容
     */
    private String logContent;

    /**
     * 模块编码
     */
    private String modelId;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * 部门权限
     */
    private String deptPrivileges;

    /**
     * 操作人编码
     */
    private String operateId;

    /**
     * 操作人名称
     */
    private String operateName;

    /**
     * 部门编码
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 操作时间
     */
    private LocalDateTime operateDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getDeptPrivileges() {
        return deptPrivileges;
    }

    public void setDeptPrivileges(String deptPrivileges) {
        this.deptPrivileges = deptPrivileges;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public LocalDateTime getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(LocalDateTime operateDate) {
        this.operateDate = operateDate;
    }

    @Override
    public String toString() {
        return "TblSystemLog{" +
        "id=" + id +
        ", logContent=" + logContent +
        ", modelId=" + modelId +
        ", ipAddr=" + ipAddr +
        ", deptPrivileges=" + deptPrivileges +
        ", operateId=" + operateId +
        ", operateName=" + operateName +
        ", deptId=" + deptId +
        ", deptName=" + deptName +
        ", operateDate=" + operateDate +
        "}";
    }
}
