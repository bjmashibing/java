package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 支出项目
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyOutProject implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 支出项目id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支出项目名称
     */
    private String outProjectName;

    /**
     * 上级支出项目id
     */
    private Integer parentOutProjectId;

    /**
     * 说明
     */
    private String desc;

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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOutProjectName() {
        return outProjectName;
    }

    public void setOutProjectName(String outProjectName) {
        this.outProjectName = outProjectName;
    }

    public Integer getParentOutProjectId() {
        return parentOutProjectId;
    }

    public void setParentOutProjectId(Integer parentOutProjectId) {
        this.parentOutProjectId = parentOutProjectId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    @Override
    public String toString() {
        return "WyOutProject{" +
        "id=" + id +
        ", outProjectName=" + outProjectName +
        ", parentOutProjectId=" + parentOutProjectId +
        ", desc=" + desc +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        "}";
    }
}
