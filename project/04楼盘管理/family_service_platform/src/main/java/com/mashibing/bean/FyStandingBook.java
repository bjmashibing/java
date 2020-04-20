package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 费用台账概要
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyStandingBook implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 台账编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 台账名称
     */
    private String standingBookName;

    /**
     * 关联费用编码
     */
    private Integer associateCostCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 生成日期
     */
    private LocalDateTime creationDate;

    /**
     * 生成人
     */
    private String creationPerson;

    /**
     * 关联台账账号
     */
    private String associateStandingBookId;

    /**
     * 所属公司
     */
    private Integer company;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStandingBookName() {
        return standingBookName;
    }

    public void setStandingBookName(String standingBookName) {
        this.standingBookName = standingBookName;
    }

    public Integer getAssociateCostCode() {
        return associateCostCode;
    }

    public void setAssociateCostCode(Integer associateCostCode) {
        this.associateCostCode = associateCostCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationPerson() {
        return creationPerson;
    }

    public void setCreationPerson(String creationPerson) {
        this.creationPerson = creationPerson;
    }

    public String getAssociateStandingBookId() {
        return associateStandingBookId;
    }

    public void setAssociateStandingBookId(String associateStandingBookId) {
        this.associateStandingBookId = associateStandingBookId;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "FyStandingBook{" +
        "id=" + id +
        ", standingBookName=" + standingBookName +
        ", associateCostCode=" + associateCostCode +
        ", remark=" + remark +
        ", creationDate=" + creationDate +
        ", creationPerson=" + creationPerson +
        ", associateStandingBookId=" + associateStandingBookId +
        ", company=" + company +
        "}";
    }
}
