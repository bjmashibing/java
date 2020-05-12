package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 员工通讯录类别
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblEmployeeContactCategory implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 排序号
     */
    private Long orderId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 上级类别id
     */
    private String parentCategoryId;

    /**
     * 标记线
     */
    private String line;

    /**
     * 创建人id
     */
    private String createPersonId;

    /**
     * 创建人名称
     */
    private String createPerson;

    /**
     * 权限字符串
     */
    private String privileges;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
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

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "TblEmployeeContactCategory{" +
        "id=" + id +
        ", categoryName=" + categoryName +
        ", orderId=" + orderId +
        ", remark=" + remark +
        ", parentCategoryId=" + parentCategoryId +
        ", line=" + line +
        ", createPersonId=" + createPersonId +
        ", createPerson=" + createPerson +
        ", privileges=" + privileges +
        "}";
    }
}
