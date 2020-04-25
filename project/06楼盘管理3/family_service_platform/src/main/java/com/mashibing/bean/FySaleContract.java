package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 销售合同
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FySaleContract implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 合同编号
     */
    @TableId(value = "sale_contract_id", type = IdType.AUTO)
    private String saleContractId;

    /**
     * 所属房间编号
     */
    private Integer cellId;

    /**
     * 合同金额
     */
    private Double contractMoney;

    /**
     * 合同日期
     */
    private LocalDateTime contractDate;

    /**
     * 付款方式
     */
    private String payMethod;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 业主姓名
     */
    private String customerName;

    /**
     * 固定电话
     */
    private String onlinePhone;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 合同附件
     */
    private String contractAttach;


    public String getSaleContractId() {
        return saleContractId;
    }

    public void setSaleContractId(String saleContractId) {
        this.saleContractId = saleContractId;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public Double getContractMoney() {
        return contractMoney;
    }

    public void setContractMoney(Double contractMoney) {
        this.contractMoney = contractMoney;
    }

    public LocalDateTime getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDateTime contractDate) {
        this.contractDate = contractDate;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOnlinePhone() {
        return onlinePhone;
    }

    public void setOnlinePhone(String onlinePhone) {
        this.onlinePhone = onlinePhone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContractAttach() {
        return contractAttach;
    }

    public void setContractAttach(String contractAttach) {
        this.contractAttach = contractAttach;
    }

    @Override
    public String toString() {
        return "FySaleContract{" +
        "saleContractId=" + saleContractId +
        ", cellId=" + cellId +
        ", contractMoney=" + contractMoney +
        ", contractDate=" + contractDate +
        ", payMethod=" + payMethod +
        ", idNumber=" + idNumber +
        ", customerName=" + customerName +
        ", onlinePhone=" + onlinePhone +
        ", phoneNumber=" + phoneNumber +
        ", remark=" + remark +
        ", contractAttach=" + contractAttach +
        "}";
    }
}
