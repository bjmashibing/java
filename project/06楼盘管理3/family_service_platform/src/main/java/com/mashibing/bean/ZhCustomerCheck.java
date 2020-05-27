package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 业主验房
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhCustomerCheck implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房间编号
     */
    private String cellId;

    /**
     * 验收日期
     */
    private LocalDateTime checkDate;

    /**
     * 确认日期
     */
    private LocalDateTime confirmDate;

    /**
     * 验收项目编号
     */
    private Long checkItemId;

    /**
     * 验收项目名称
     */
    private String checkItemName;

    /**
     * 是否合格
     */
    private String isPass;

    /**
     * 业主意见
     */
    private String consumerAdvice;

    /**
     * 房管员意见
     */
    private String houseKeeperAdvice;

    /**
     * 验收人员
     */
    private String checkPerson;

    /**
     * 备注
     */
    private String remark;

    /**
     * 录入人
     */
    private String inputPerson;

    /**
     * 录入时间
     */
    private LocalDateTime inputDate;

    /**
     * 修改人
     */
    private String updatePerson;

    /**
     * 修改时间
     */
    private LocalDateTime updateDate;

    /**
     * 验房类型
     */
    private String checkHouseType;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public LocalDateTime getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(LocalDateTime confirmDate) {
        this.confirmDate = confirmDate;
    }

    public Long getCheckItemId() {
        return checkItemId;
    }

    public void setCheckItemId(Long checkItemId) {
        this.checkItemId = checkItemId;
    }

    public String getCheckItemName() {
        return checkItemName;
    }

    public void setCheckItemName(String checkItemName) {
        this.checkItemName = checkItemName;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public String getConsumerAdvice() {
        return consumerAdvice;
    }

    public void setConsumerAdvice(String consumerAdvice) {
        this.consumerAdvice = consumerAdvice;
    }

    public String getHouseKeeperAdvice() {
        return houseKeeperAdvice;
    }

    public void setHouseKeeperAdvice(String houseKeeperAdvice) {
        this.houseKeeperAdvice = houseKeeperAdvice;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInputPerson() {
        return inputPerson;
    }

    public void setInputPerson(String inputPerson) {
        this.inputPerson = inputPerson;
    }

    public LocalDateTime getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDateTime inputDate) {
        this.inputDate = inputDate;
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

    public String getCheckHouseType() {
        return checkHouseType;
    }

    public void setCheckHouseType(String checkHouseType) {
        this.checkHouseType = checkHouseType;
    }

    @Override
    public String toString() {
        return "ZhCustomerCheck{" +
        "id=" + id +
        ", cellId=" + cellId +
        ", checkDate=" + checkDate +
        ", confirmDate=" + confirmDate +
        ", checkItemId=" + checkItemId +
        ", checkItemName=" + checkItemName +
        ", isPass=" + isPass +
        ", consumerAdvice=" + consumerAdvice +
        ", houseKeeperAdvice=" + houseKeeperAdvice +
        ", checkPerson=" + checkPerson +
        ", remark=" + remark +
        ", inputPerson=" + inputPerson +
        ", inputDate=" + inputDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        ", checkHouseType=" + checkHouseType +
        "}";
    }
}
