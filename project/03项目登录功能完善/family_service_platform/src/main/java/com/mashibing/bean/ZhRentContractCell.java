package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 租赁合同房间
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhRentContractCell implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属合同编号
     */
    private Integer contractId;

    /**
     * 所属档口信息
     */
    private Integer stallMessage;

    /**
     * 操作人
     */
    private String operatePerson;

    /**
     * 操作时间
     */
    private LocalDateTime operateDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getStallMessage() {
        return stallMessage;
    }

    public void setStallMessage(Integer stallMessage) {
        this.stallMessage = stallMessage;
    }

    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

    public LocalDateTime getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(LocalDateTime operateDate) {
        this.operateDate = operateDate;
    }

    @Override
    public String toString() {
        return "ZhRentContractCell{" +
        "id=" + id +
        ", contractId=" + contractId +
        ", stallMessage=" + stallMessage +
        ", operatePerson=" + operatePerson +
        ", operateDate=" + operateDate +
        "}";
    }
}
