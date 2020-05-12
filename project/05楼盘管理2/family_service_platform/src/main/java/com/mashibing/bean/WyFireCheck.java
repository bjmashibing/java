package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 消防巡查
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyFireCheck implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 巡视日期
     */
    private LocalDateTime checkDate;

    /**
     * 巡视地点
     */
    private String checkPlace;

    /**
     * 巡视人
     */
    private String checkPerson;

    /**
     * 巡视情况
     */
    private String checkCondition;

    /**
     * 处理意见
     */
    private String handleAdvice;

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

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckPlace() {
        return checkPlace;
    }

    public void setCheckPlace(String checkPlace) {
        this.checkPlace = checkPlace;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getCheckCondition() {
        return checkCondition;
    }

    public void setCheckCondition(String checkCondition) {
        this.checkCondition = checkCondition;
    }

    public String getHandleAdvice() {
        return handleAdvice;
    }

    public void setHandleAdvice(String handleAdvice) {
        this.handleAdvice = handleAdvice;
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
        return "WyFireCheck{" +
        "id=" + id +
        ", checkDate=" + checkDate +
        ", checkPlace=" + checkPlace +
        ", checkPerson=" + checkPerson +
        ", checkCondition=" + checkCondition +
        ", handleAdvice=" + handleAdvice +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
