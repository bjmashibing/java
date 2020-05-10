package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 参数档案
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblArgRecord implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 参数编码
     */
    @TableId(value = "arg_code", type = IdType.AUTO)
    private String argCode;

    /**
     * 参数名称
     */
    private String argName;

    /**
     * 参数值
     */
    private String argValue;

    /**
     * 说明
     */
    private String argDesc;

    /**
     * 排序号
     */
    private Integer argOrder;

    /**
     * 所属产品
     */
    private String belongProduct;


    public String getArgCode() {
        return argCode;
    }

    public void setArgCode(String argCode) {
        this.argCode = argCode;
    }

    public String getArgName() {
        return argName;
    }

    public void setArgName(String argName) {
        this.argName = argName;
    }

    public String getArgValue() {
        return argValue;
    }

    public void setArgValue(String argValue) {
        this.argValue = argValue;
    }

    public String getArgDesc() {
        return argDesc;
    }

    public void setArgDesc(String argDesc) {
        this.argDesc = argDesc;
    }

    public Integer getArgOrder() {
        return argOrder;
    }

    public void setArgOrder(Integer argOrder) {
        this.argOrder = argOrder;
    }

    public String getBelongProduct() {
        return belongProduct;
    }

    public void setBelongProduct(String belongProduct) {
        this.belongProduct = belongProduct;
    }

    @Override
    public String toString() {
        return "TblArgRecord{" +
        "argCode=" + argCode +
        ", argName=" + argName +
        ", argValue=" + argValue +
        ", argDesc=" + argDesc +
        ", argOrder=" + argOrder +
        ", belongProduct=" + belongProduct +
        "}";
    }
}
