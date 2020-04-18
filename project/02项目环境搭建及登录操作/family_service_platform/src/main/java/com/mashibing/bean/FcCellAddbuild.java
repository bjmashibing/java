package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 房间加建信息
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FcCellAddbuild implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属房间id
     */
    private Integer cellId;

    /**
     * 加建面积
     */
    private Double addbuildArea;

    /**
     * 加建时间
     */
    private LocalDateTime addbuildTime;

    /**
     * 加建状态
     */
    private String addbuildState;

    /**
     * 加建说明
     */
    private String addbuildDesc;

    /**
     * 加建附件
     */
    private String addbuildAccessory;

    /**
     * 操作人
     */
    private String operatePerson;

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

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public Double getAddbuildArea() {
        return addbuildArea;
    }

    public void setAddbuildArea(Double addbuildArea) {
        this.addbuildArea = addbuildArea;
    }

    public LocalDateTime getAddbuildTime() {
        return addbuildTime;
    }

    public void setAddbuildTime(LocalDateTime addbuildTime) {
        this.addbuildTime = addbuildTime;
    }

    public String getAddbuildState() {
        return addbuildState;
    }

    public void setAddbuildState(String addbuildState) {
        this.addbuildState = addbuildState;
    }

    public String getAddbuildDesc() {
        return addbuildDesc;
    }

    public void setAddbuildDesc(String addbuildDesc) {
        this.addbuildDesc = addbuildDesc;
    }

    public String getAddbuildAccessory() {
        return addbuildAccessory;
    }

    public void setAddbuildAccessory(String addbuildAccessory) {
        this.addbuildAccessory = addbuildAccessory;
    }

    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

    public LocalDateTime getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(LocalDateTime operateDate) {
        this.operateDate = operateDate;
    }

    @Override
    public String toString() {
        return "FcCellAddbuild{" +
        "id=" + id +
        ", cellId=" + cellId +
        ", addbuildArea=" + addbuildArea +
        ", addbuildTime=" + addbuildTime +
        ", addbuildState=" + addbuildState +
        ", addbuildDesc=" + addbuildDesc +
        ", addbuildAccessory=" + addbuildAccessory +
        ", operatePerson=" + operatePerson +
        ", operateDate=" + operateDate +
        "}";
    }
}
