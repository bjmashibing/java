package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 公表信息
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyPublicBox implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 公表编号
     */
    private String publicBoxId;

    /**
     * 所属费项
     */
    private Integer moneyId;

    /**
     * 公表读数
     */
    private Double publicBoxReadNumber;

    /**
     * 分摊方法
     */
    private String shareMethod;

    /**
     * 公表状态
     */
    private String publicBoxState;


    public String getPublicBoxId() {
        return publicBoxId;
    }

    public void setPublicBoxId(String publicBoxId) {
        this.publicBoxId = publicBoxId;
    }

    public Integer getMoneyId() {
        return moneyId;
    }

    public void setMoneyId(Integer moneyId) {
        this.moneyId = moneyId;
    }

    public Double getPublicBoxReadNumber() {
        return publicBoxReadNumber;
    }

    public void setPublicBoxReadNumber(Double publicBoxReadNumber) {
        this.publicBoxReadNumber = publicBoxReadNumber;
    }

    public String getShareMethod() {
        return shareMethod;
    }

    public void setShareMethod(String shareMethod) {
        this.shareMethod = shareMethod;
    }

    public String getPublicBoxState() {
        return publicBoxState;
    }

    public void setPublicBoxState(String publicBoxState) {
        this.publicBoxState = publicBoxState;
    }

    @Override
    public String toString() {
        return "FyPublicBox{" +
        "publicBoxId=" + publicBoxId +
        ", moneyId=" + moneyId +
        ", publicBoxReadNumber=" + publicBoxReadNumber +
        ", shareMethod=" + shareMethod +
        ", publicBoxState=" + publicBoxState +
        "}";
    }
}
