package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 物业接管资料明细
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyTakeoverDataDetail implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 接管资料id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属接管id
     */
    private Integer takeoverId;

    /**
     * 资料名称
     */
    private String dataName;

    /**
     * 资料份数
     */
    private Integer dataCopies;

    /**
     * 资料页数
     */
    private Integer dataPages;

    /**
     * 资料分类
     */
    private Long dataType;

    /**
     * 档案号
     */
    private String fileNumber;

    /**
     * 交出人
     */
    private String handoverPerson;

    /**
     * 接收人
     */
    private String receivePerson;

    /**
     * 接受日期
     */
    private LocalDateTime receiveDate;

    /**
     * 备注
     */
    private String remark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTakeoverId() {
        return takeoverId;
    }

    public void setTakeoverId(Integer takeoverId) {
        this.takeoverId = takeoverId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public Integer getDataCopies() {
        return dataCopies;
    }

    public void setDataCopies(Integer dataCopies) {
        this.dataCopies = dataCopies;
    }

    public Integer getDataPages() {
        return dataPages;
    }

    public void setDataPages(Integer dataPages) {
        this.dataPages = dataPages;
    }

    public Long getDataType() {
        return dataType;
    }

    public void setDataType(Long dataType) {
        this.dataType = dataType;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getHandoverPerson() {
        return handoverPerson;
    }

    public void setHandoverPerson(String handoverPerson) {
        this.handoverPerson = handoverPerson;
    }

    public String getReceivePerson() {
        return receivePerson;
    }

    public void setReceivePerson(String receivePerson) {
        this.receivePerson = receivePerson;
    }

    public LocalDateTime getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "WyTakeoverDataDetail{" +
        "id=" + id +
        ", takeoverId=" + takeoverId +
        ", dataName=" + dataName +
        ", dataCopies=" + dataCopies +
        ", dataPages=" + dataPages +
        ", dataType=" + dataType +
        ", fileNumber=" + fileNumber +
        ", handoverPerson=" + handoverPerson +
        ", receivePerson=" + receivePerson +
        ", receiveDate=" + receiveDate +
        ", remark=" + remark +
        "}";
    }
}
