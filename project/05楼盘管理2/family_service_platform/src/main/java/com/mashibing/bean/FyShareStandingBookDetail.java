package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 公摊费用台账公表明细
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyShareStandingBookDetail implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 公表明细id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属台账编号
     */
    private Double standingBookId;

    /**
     * 公表名称
     */
    private String publicBoxName;

    /**
     * 单位价格
     */
    private Double priceUnit;

    /**
     * 分摊户数
     */
    private Double shareNumber;

    /**
     * 上次读数
     */
    private Double lastReadNumber;

    /**
     * 本次读数
     */
    private Double currentReadNumber;

    /**
     * 本次用量
     */
    private Double currentUseNumber;

    /**
     * 应缴费用
     */
    private Double shouldPay;

    /**
     * 上次费用止期
     */
    private LocalDateTime lastPayStopDate;

    /**
     * 本次费用起期
     */
    private LocalDateTime currentPayStartDate;

    /**
     * 本次费用止期
     */
    private LocalDateTime currentPayStopDate;

    /**
     * 本次缴费限期
     */
    private LocalDateTime currentPayLimitDate;

    /**
     * 收费周期
     */
    private Integer receiveCycle;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getStandingBookId() {
        return standingBookId;
    }

    public void setStandingBookId(Double standingBookId) {
        this.standingBookId = standingBookId;
    }

    public String getPublicBoxName() {
        return publicBoxName;
    }

    public void setPublicBoxName(String publicBoxName) {
        this.publicBoxName = publicBoxName;
    }

    public Double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Double getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(Double shareNumber) {
        this.shareNumber = shareNumber;
    }

    public Double getLastReadNumber() {
        return lastReadNumber;
    }

    public void setLastReadNumber(Double lastReadNumber) {
        this.lastReadNumber = lastReadNumber;
    }

    public Double getCurrentReadNumber() {
        return currentReadNumber;
    }

    public void setCurrentReadNumber(Double currentReadNumber) {
        this.currentReadNumber = currentReadNumber;
    }

    public Double getCurrentUseNumber() {
        return currentUseNumber;
    }

    public void setCurrentUseNumber(Double currentUseNumber) {
        this.currentUseNumber = currentUseNumber;
    }

    public Double getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(Double shouldPay) {
        this.shouldPay = shouldPay;
    }

    public LocalDateTime getLastPayStopDate() {
        return lastPayStopDate;
    }

    public void setLastPayStopDate(LocalDateTime lastPayStopDate) {
        this.lastPayStopDate = lastPayStopDate;
    }

    public LocalDateTime getCurrentPayStartDate() {
        return currentPayStartDate;
    }

    public void setCurrentPayStartDate(LocalDateTime currentPayStartDate) {
        this.currentPayStartDate = currentPayStartDate;
    }

    public LocalDateTime getCurrentPayStopDate() {
        return currentPayStopDate;
    }

    public void setCurrentPayStopDate(LocalDateTime currentPayStopDate) {
        this.currentPayStopDate = currentPayStopDate;
    }

    public LocalDateTime getCurrentPayLimitDate() {
        return currentPayLimitDate;
    }

    public void setCurrentPayLimitDate(LocalDateTime currentPayLimitDate) {
        this.currentPayLimitDate = currentPayLimitDate;
    }

    public Integer getReceiveCycle() {
        return receiveCycle;
    }

    public void setReceiveCycle(Integer receiveCycle) {
        this.receiveCycle = receiveCycle;
    }

    @Override
    public String toString() {
        return "FyShareStandingBookDetail{" +
        "id=" + id +
        ", standingBookId=" + standingBookId +
        ", publicBoxName=" + publicBoxName +
        ", priceUnit=" + priceUnit +
        ", shareNumber=" + shareNumber +
        ", lastReadNumber=" + lastReadNumber +
        ", currentReadNumber=" + currentReadNumber +
        ", currentUseNumber=" + currentUseNumber +
        ", shouldPay=" + shouldPay +
        ", lastPayStopDate=" + lastPayStopDate +
        ", currentPayStartDate=" + currentPayStartDate +
        ", currentPayStopDate=" + currentPayStopDate +
        ", currentPayLimitDate=" + currentPayLimitDate +
        ", receiveCycle=" + receiveCycle +
        "}";
    }
}
