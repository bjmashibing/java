package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 物业接管概要
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyPropertyTakeoverSchema implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 接管单号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 接管标题
     */
    private String takeoverTitle;

    /**
     * 所属楼盘
     */
    private Integer estateId;

    /**
     * 接管备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建日期
     */
    private LocalDateTime createDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTakeoverTitle() {
        return takeoverTitle;
    }

    public void setTakeoverTitle(String takeoverTitle) {
        this.takeoverTitle = takeoverTitle;
    }

    public Integer getEstateId() {
        return estateId;
    }

    public void setEstateId(Integer estateId) {
        this.estateId = estateId;
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
        return "WyPropertyTakeoverSchema{" +
        "id=" + id +
        ", takeoverTitle=" + takeoverTitle +
        ", estateId=" + estateId +
        ", remark=" + remark +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        "}";
    }
}
