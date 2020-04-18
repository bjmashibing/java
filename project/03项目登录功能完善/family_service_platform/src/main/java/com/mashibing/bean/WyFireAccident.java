package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 消防事故
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyFireAccident implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 事故日期
     */
    private LocalDateTime accidentDate;

    /**
     * 事故地点
     */
    private String accidentPlace;

    /**
     * 发生原因
     */
    private String occurReason;

    /**
     * 相关人员
     */
    private String relatedPerson;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 损失程度
     */
    private String loss;

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

    public LocalDateTime getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(LocalDateTime accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getAccidentPlace() {
        return accidentPlace;
    }

    public void setAccidentPlace(String accidentPlace) {
        this.accidentPlace = accidentPlace;
    }

    public String getOccurReason() {
        return occurReason;
    }

    public void setOccurReason(String occurReason) {
        this.occurReason = occurReason;
    }

    public String getRelatedPerson() {
        return relatedPerson;
    }

    public void setRelatedPerson(String relatedPerson) {
        this.relatedPerson = relatedPerson;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
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
        return "WyFireAccident{" +
        "id=" + id +
        ", accidentDate=" + accidentDate +
        ", accidentPlace=" + accidentPlace +
        ", occurReason=" + occurReason +
        ", relatedPerson=" + relatedPerson +
        ", handleResult=" + handleResult +
        ", loss=" + loss +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
