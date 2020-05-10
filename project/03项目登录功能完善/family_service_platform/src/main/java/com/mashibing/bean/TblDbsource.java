package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 数据库
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblDbsource implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String sourceName;

    /**
     * 中文解释
     */
    private String sourceDesc;

    /**
     * 类型
     */
    private String sourceType;

    /**
     * 分类
     */
    private String sourceClass;

    /**
     * 是否可以清空
     */
    private String idClear;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;

    /**
     * 所属产品
     */
    private String belongProduct;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
    }

    public String getIdClear() {
        return idClear;
    }

    public void setIdClear(String idClear) {
        this.idClear = idClear;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getBelongProduct() {
        return belongProduct;
    }

    public void setBelongProduct(String belongProduct) {
        this.belongProduct = belongProduct;
    }

    @Override
    public String toString() {
        return "TblDbsource{" +
        "id=" + id +
        ", sourceName=" + sourceName +
        ", sourceDesc=" + sourceDesc +
        ", sourceType=" + sourceType +
        ", sourceClass=" + sourceClass +
        ", idClear=" + idClear +
        ", updateDate=" + updateDate +
        ", belongProduct=" + belongProduct +
        "}";
    }
}
