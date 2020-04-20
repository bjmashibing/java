package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 临客费项设置
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyTemporaryMoneySetting implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 临客费项id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 费项名称
     */
    private String temporaryMoneyName;

    /**
     * 上级费项id
     */
    private Integer upperMoneyId;

    /**
     * 单位价格
     */
    private Double priceUnit;

    /**
     * 费项说明
     */
    private String moneyDescription;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
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

    public String getTemporaryMoneyName() {
        return temporaryMoneyName;
    }

    public void setTemporaryMoneyName(String temporaryMoneyName) {
        this.temporaryMoneyName = temporaryMoneyName;
    }

    public Integer getUpperMoneyId() {
        return upperMoneyId;
    }

    public void setUpperMoneyId(Integer upperMoneyId) {
        this.upperMoneyId = upperMoneyId;
    }

    public Double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getMoneyDescription() {
        return moneyDescription;
    }

    public void setMoneyDescription(String moneyDescription) {
        this.moneyDescription = moneyDescription;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "FyTemporaryMoneySetting{" +
        "id=" + id +
        ", temporaryMoneyName=" + temporaryMoneyName +
        ", upperMoneyId=" + upperMoneyId +
        ", priceUnit=" + priceUnit +
        ", moneyDescription=" + moneyDescription +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        ", company=" + company +
        "}";
    }
}
