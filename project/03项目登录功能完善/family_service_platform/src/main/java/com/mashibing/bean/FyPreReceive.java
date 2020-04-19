package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 预收款管理
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyPreReceive implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 凭证号
     */
    private String voucherNumber;

    /**
     * 房间号
     */
    private Integer cellId;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 金额
     */
    private Double money;

    /**
     * 经手人
     */
    private String handlerPerson;

    /**
     * 收款日期
     */
    private LocalDateTime receiveDate;

    /**
     * 录入人
     */
    private String inputPerson;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 收款方式
     */
    private String receiveMethod;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 修改人
     */
    private String updatePerson;

    /**
     * 修改时间
     */
    private LocalDateTime updateDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getHandlerPerson() {
        return handlerPerson;
    }

    public void setHandlerPerson(String handlerPerson) {
        this.handlerPerson = handlerPerson;
    }

    public LocalDateTime getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getInputPerson() {
        return inputPerson;
    }

    public void setInputPerson(String inputPerson) {
        this.inputPerson = inputPerson;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getReceiveMethod() {
        return receiveMethod;
    }

    public void setReceiveMethod(String receiveMethod) {
        this.receiveMethod = receiveMethod;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    @Override
    public String toString() {
        return "FyPreReceive{" +
        "id=" + id +
        ", voucherNumber=" + voucherNumber +
        ", cellId=" + cellId +
        ", summary=" + summary +
        ", money=" + money +
        ", handlerPerson=" + handlerPerson +
        ", receiveDate=" + receiveDate +
        ", inputPerson=" + inputPerson +
        ", company=" + company +
        ", receiveMethod=" + receiveMethod +
        ", dataSource=" + dataSource +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        "}";
    }
}
