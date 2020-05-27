package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 清洁记录
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyCleanRecord implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目编号
     */
    private String projectId;

    /**
     * 清洁情况
     */
    private String cleanCondition;

    /**
     * 清洁时间
     */
    private String cleanDate;

    /**
     * 清洁人
     */
    private String cleanPerson;

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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCleanCondition() {
        return cleanCondition;
    }

    public void setCleanCondition(String cleanCondition) {
        this.cleanCondition = cleanCondition;
    }

    public String getCleanDate() {
        return cleanDate;
    }

    public void setCleanDate(String cleanDate) {
        this.cleanDate = cleanDate;
    }

    public String getCleanPerson() {
        return cleanPerson;
    }

    public void setCleanPerson(String cleanPerson) {
        this.cleanPerson = cleanPerson;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "WyCleanRecord{" +
        "id=" + id +
        ", projectId=" + projectId +
        ", cleanCondition=" + cleanCondition +
        ", cleanDate=" + cleanDate +
        ", cleanPerson=" + cleanPerson +
        ", remark=" + remark +
        "}";
    }
}
