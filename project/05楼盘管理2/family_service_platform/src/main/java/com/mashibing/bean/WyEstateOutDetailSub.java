package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 楼盘经费支出明细_审批子表
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyEstateOutDetailSub implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属主单id
     */
    private Long belongOutProjectId;

    /**
     * 接受时间
     */
    private LocalDateTime receiveDate;

    /**
     * 审批意见
     */
    private String checkAdvice;

    /**
     * 审批人id
     */
    private String checkPersonId;

    /**
     * 审批人名称
     */
    private String checkPersonName;

    /**
     * 审批时间
     */
    private LocalDateTime checkDate;

    /**
     * 审批状态
     */
    private String checkStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBelongOutProjectId() {
        return belongOutProjectId;
    }

    public void setBelongOutProjectId(Long belongOutProjectId) {
        this.belongOutProjectId = belongOutProjectId;
    }

    public LocalDateTime getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDateTime receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getCheckAdvice() {
        return checkAdvice;
    }

    public void setCheckAdvice(String checkAdvice) {
        this.checkAdvice = checkAdvice;
    }

    public String getCheckPersonId() {
        return checkPersonId;
    }

    public void setCheckPersonId(String checkPersonId) {
        this.checkPersonId = checkPersonId;
    }

    public String getCheckPersonName() {
        return checkPersonName;
    }

    public void setCheckPersonName(String checkPersonName) {
        this.checkPersonName = checkPersonName;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    @Override
    public String toString() {
        return "WyEstateOutDetailSub{" +
        "id=" + id +
        ", belongOutProjectId=" + belongOutProjectId +
        ", receiveDate=" + receiveDate +
        ", checkAdvice=" + checkAdvice +
        ", checkPersonId=" + checkPersonId +
        ", checkPersonName=" + checkPersonName +
        ", checkDate=" + checkDate +
        ", checkStatus=" + checkStatus +
        "}";
    }
}
