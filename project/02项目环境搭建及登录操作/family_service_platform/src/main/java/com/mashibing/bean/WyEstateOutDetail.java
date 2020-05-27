package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 楼盘经费支出明细
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyEstateOutDetail implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 记账日期
     */
    private LocalDateTime chargeDate;

    /**
     * 所属楼盘id
     */
    private Integer estateId;

    /**
     * 支出主项目
     */
    private String outputMainProject;

    /**
     * 支出子项目
     */
    private Integer outputSubProject;

    /**
     * 支出金额
     */
    private Double outputMoney;

    /**
     * 年累计支出金额
     */
    private Double outputMoneyYear;

    /**
     * 主项累计支出金额
     */
    private Double outputMoneyMain;

    /**
     * 状态
     */
    private String status;

    /**
     * 说明
     */
    private String desc;

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
     * 下一步接收人id
     */
    private String nextReceivePersonId;

    /**
     * 下一步接收人姓名
     */
    private String nextReceivePersonName;

    /**
     * 送审时间
     */
    private LocalDateTime sendCheckDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(LocalDateTime chargeDate) {
        this.chargeDate = chargeDate;
    }

    public Integer getEstateId() {
        return estateId;
    }

    public void setEstateId(Integer estateId) {
        this.estateId = estateId;
    }

    public String getOutputMainProject() {
        return outputMainProject;
    }

    public void setOutputMainProject(String outputMainProject) {
        this.outputMainProject = outputMainProject;
    }

    public Integer getOutputSubProject() {
        return outputSubProject;
    }

    public void setOutputSubProject(Integer outputSubProject) {
        this.outputSubProject = outputSubProject;
    }

    public Double getOutputMoney() {
        return outputMoney;
    }

    public void setOutputMoney(Double outputMoney) {
        this.outputMoney = outputMoney;
    }

    public Double getOutputMoneyYear() {
        return outputMoneyYear;
    }

    public void setOutputMoneyYear(Double outputMoneyYear) {
        this.outputMoneyYear = outputMoneyYear;
    }

    public Double getOutputMoneyMain() {
        return outputMoneyMain;
    }

    public void setOutputMoneyMain(Double outputMoneyMain) {
        this.outputMoneyMain = outputMoneyMain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getNextReceivePersonId() {
        return nextReceivePersonId;
    }

    public void setNextReceivePersonId(String nextReceivePersonId) {
        this.nextReceivePersonId = nextReceivePersonId;
    }

    public String getNextReceivePersonName() {
        return nextReceivePersonName;
    }

    public void setNextReceivePersonName(String nextReceivePersonName) {
        this.nextReceivePersonName = nextReceivePersonName;
    }

    public LocalDateTime getSendCheckDate() {
        return sendCheckDate;
    }

    public void setSendCheckDate(LocalDateTime sendCheckDate) {
        this.sendCheckDate = sendCheckDate;
    }

    @Override
    public String toString() {
        return "WyEstateOutDetail{" +
        "id=" + id +
        ", chargeDate=" + chargeDate +
        ", estateId=" + estateId +
        ", outputMainProject=" + outputMainProject +
        ", outputSubProject=" + outputSubProject +
        ", outputMoney=" + outputMoney +
        ", outputMoneyYear=" + outputMoneyYear +
        ", outputMoneyMain=" + outputMoneyMain +
        ", status=" + status +
        ", desc=" + desc +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        ", nextReceivePersonId=" + nextReceivePersonId +
        ", nextReceivePersonName=" + nextReceivePersonName +
        ", sendCheckDate=" + sendCheckDate +
        "}";
    }
}
