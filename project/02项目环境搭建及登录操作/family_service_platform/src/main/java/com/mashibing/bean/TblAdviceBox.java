package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 意见箱
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblAdviceBox implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 管理员id
     */
    private String adminId;

    /**
     * 用户范围id
     */
    private String userRangeId;

    /**
     * 用户范围姓名
     */
    @TableField("User_range_name")
    private String userRangeName;

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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUserRangeId() {
        return userRangeId;
    }

    public void setUserRangeId(String userRangeId) {
        this.userRangeId = userRangeId;
    }

    public String getUserRangeName() {
        return userRangeName;
    }

    public void setUserRangeName(String userRangeName) {
        this.userRangeName = userRangeName;
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

    @Override
    public String toString() {
        return "TblAdviceBox{" +
        "id=" + id +
        ", name=" + name +
        ", type=" + type +
        ", status=" + status +
        ", adminId=" + adminId +
        ", userRangeId=" + userRangeId +
        ", userRangeName=" + userRangeName +
        ", remark=" + remark +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        "}";
    }
}
