package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 楼盘经费支出项目
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyEstateOutProject implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 项目id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 上级支出项目id
     */
    private Integer parentOutProjectId;

    /**
     * 所属主项目
     */
    private String belongMainProjecct;

    /**
     * 说明
     */
    private String desc;

    /**
     * 建档人
     */
    private String createPerson;

    /**
     * 建档时间
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getParentOutProjectId() {
        return parentOutProjectId;
    }

    public void setParentOutProjectId(Integer parentOutProjectId) {
        this.parentOutProjectId = parentOutProjectId;
    }

    public String getBelongMainProjecct() {
        return belongMainProjecct;
    }

    public void setBelongMainProjecct(String belongMainProjecct) {
        this.belongMainProjecct = belongMainProjecct;
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
        return "WyEstateOutProject{" +
        "id=" + id +
        ", projectName=" + projectName +
        ", parentOutProjectId=" + parentOutProjectId +
        ", belongMainProjecct=" + belongMainProjecct +
        ", desc=" + desc +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        "}";
    }
}
