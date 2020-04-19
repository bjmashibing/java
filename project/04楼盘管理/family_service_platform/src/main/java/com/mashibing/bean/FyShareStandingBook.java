package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 公摊费用台账概要
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyShareStandingBook implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 公摊费用编号
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
    private LocalDateTime createDate;

    /**
     * 生成人
     */
    private String createPerson;

    /**
     * 所属公司
     */
    private String company;


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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "FyShareStandingBook{" +
        "id=" + id +
        ", standingBookName=" + standingBookName +
        ", associateCostCode=" + associateCostCode +
        ", remark=" + remark +
        ", createDate=" + createDate +
        ", createPerson=" + createPerson +
        ", company=" + company +
        "}";
    }
}
