package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 物品出入
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyGoodsInout implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 出入时间
     */
    private LocalDateTime inoutDate;

    /**
     * 携带人
     */
    private String carryPerson;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 出入类型
     */
    private String inputType;

    /**
     * 居住地址
     */
    private String liveAddr;

    /**
     * 出入单元
     */
    private String inoutUnit;

    /**
     * 户主姓名
     */
    private String customerName;

    /**
     * 出入物品
     */
    private String inoutGoods;

    /**
     * 值班人
     */
    private String agent;

    /**
     * 备注
     */
    private String remark;

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

    public LocalDateTime getInoutDate() {
        return inoutDate;
    }

    public void setInoutDate(LocalDateTime inoutDate) {
        this.inoutDate = inoutDate;
    }

    public String getCarryPerson() {
        return carryPerson;
    }

    public void setCarryPerson(String carryPerson) {
        this.carryPerson = carryPerson;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getLiveAddr() {
        return liveAddr;
    }

    public void setLiveAddr(String liveAddr) {
        this.liveAddr = liveAddr;
    }

    public String getInoutUnit() {
        return inoutUnit;
    }

    public void setInoutUnit(String inoutUnit) {
        this.inoutUnit = inoutUnit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInoutGoods() {
        return inoutGoods;
    }

    public void setInoutGoods(String inoutGoods) {
        this.inoutGoods = inoutGoods;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "WyGoodsInout{" +
        "id=" + id +
        ", inoutDate=" + inoutDate +
        ", carryPerson=" + carryPerson +
        ", idCard=" + idCard +
        ", inputType=" + inputType +
        ", liveAddr=" + liveAddr +
        ", inoutUnit=" + inoutUnit +
        ", customerName=" + customerName +
        ", inoutGoods=" + inoutGoods +
        ", agent=" + agent +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
