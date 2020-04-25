package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 绿化检查
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyGreenCheck implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 绿化编码
     */
    private String greenCode;

    /**
     * 检查时间
     */
    private LocalDateTime checkDate;

    /**
     * 检查情况
     */
    private String checkCondition;

    /**
     * 处理情况
     */
    private String handleCondition;

    /**
     * 检查人
     */
    private String checkPerson;

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

    public String getGreenCode() {
        return greenCode;
    }

    public void setGreenCode(String greenCode) {
        this.greenCode = greenCode;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckCondition() {
        return checkCondition;
    }

    public void setCheckCondition(String checkCondition) {
        this.checkCondition = checkCondition;
    }

    public String getHandleCondition() {
        return handleCondition;
    }

    public void setHandleCondition(String handleCondition) {
        this.handleCondition = handleCondition;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "WyGreenCheck{" +
        "id=" + id +
        ", greenCode=" + greenCode +
        ", checkDate=" + checkDate +
        ", checkCondition=" + checkCondition +
        ", handleCondition=" + handleCondition +
        ", checkPerson=" + checkPerson +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
