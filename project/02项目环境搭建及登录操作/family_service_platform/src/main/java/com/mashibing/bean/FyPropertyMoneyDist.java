package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 物业费分布
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyPropertyMoneyDist implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房间编号
     */
    private Integer cellId;

    /**
     * 费项编号
     */
    private Integer moneyId;

    /**
     * 是否共有费用
     */
    private String isPublicMoney;

    /**
     * 当前读数
     */
    private Double currentReadNumber;

    /**
     * 上次计费单位
     */
    private Double lastChargeUnit;

    /**
     * 楼层系数
     */
    private Double floorFactor;

    /**
     * 使用量倍数
     */
    private Integer useNumberMult;


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

    public Integer getMoneyId() {
        return moneyId;
    }

    public void setMoneyId(Integer moneyId) {
        this.moneyId = moneyId;
    }

    public String getIsPublicMoney() {
        return isPublicMoney;
    }

    public void setIsPublicMoney(String isPublicMoney) {
        this.isPublicMoney = isPublicMoney;
    }

    public Double getCurrentReadNumber() {
        return currentReadNumber;
    }

    public void setCurrentReadNumber(Double currentReadNumber) {
        this.currentReadNumber = currentReadNumber;
    }

    public Double getLastChargeUnit() {
        return lastChargeUnit;
    }

    public void setLastChargeUnit(Double lastChargeUnit) {
        this.lastChargeUnit = lastChargeUnit;
    }

    public Double getFloorFactor() {
        return floorFactor;
    }

    public void setFloorFactor(Double floorFactor) {
        this.floorFactor = floorFactor;
    }

    public Integer getUseNumberMult() {
        return useNumberMult;
    }

    public void setUseNumberMult(Integer useNumberMult) {
        this.useNumberMult = useNumberMult;
    }

    @Override
    public String toString() {
        return "FyPropertyMoneyDist{" +
        "id=" + id +
        ", cellId=" + cellId +
        ", moneyId=" + moneyId +
        ", isPublicMoney=" + isPublicMoney +
        ", currentReadNumber=" + currentReadNumber +
        ", lastChargeUnit=" + lastChargeUnit +
        ", floorFactor=" + floorFactor +
        ", useNumberMult=" + useNumberMult +
        "}";
    }
}
