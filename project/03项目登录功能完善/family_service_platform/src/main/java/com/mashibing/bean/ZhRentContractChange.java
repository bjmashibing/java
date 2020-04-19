package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 租赁合同变更
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhRentContractChange implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属合同编号
     */
    private Integer contractId;

    /**
     * 变更项目
     */
    private String changeProject;

    /**
     * 原值
     */
    private String oldValue;

    /**
     * 新值
     */
    private String newValue;

    /**
     * 说明
     */
    private String desc;

    /**
     * 变更人
     */
    private String changePerson;

    /**
     * 变更时间
     */
    private LocalDateTime changeDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getChangeProject() {
        return changeProject;
    }

    public void setChangeProject(String changeProject) {
        this.changeProject = changeProject;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChangePerson() {
        return changePerson;
    }

    public void setChangePerson(String changePerson) {
        this.changePerson = changePerson;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    @Override
    public String toString() {
        return "ZhRentContractChange{" +
        "id=" + id +
        ", contractId=" + contractId +
        ", changeProject=" + changeProject +
        ", oldValue=" + oldValue +
        ", newValue=" + newValue +
        ", desc=" + desc +
        ", changePerson=" + changePerson +
        ", changeDate=" + changeDate +
        "}";
    }
}
