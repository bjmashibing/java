package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 消防演练
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyFireExercise implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 单位
     */
    private String unit;

    /**
     * 开始日期
     */
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    private LocalDateTime stopDate;

    /**
     * 演练目的
     */
    private String exercisePurpose;

    /**
     * 参与人数
     */
    private Integer joinPersons;

    /**
     * 协助单位
     */
    private String assistantUnit;

    /**
     * 演练内容
     */
    private String exerciseContent;

    /**
     * 演练效果
     */
    private String exerciseResult;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getStopDate() {
        return stopDate;
    }

    public void setStopDate(LocalDateTime stopDate) {
        this.stopDate = stopDate;
    }

    public String getExercisePurpose() {
        return exercisePurpose;
    }

    public void setExercisePurpose(String exercisePurpose) {
        this.exercisePurpose = exercisePurpose;
    }

    public Integer getJoinPersons() {
        return joinPersons;
    }

    public void setJoinPersons(Integer joinPersons) {
        this.joinPersons = joinPersons;
    }

    public String getAssistantUnit() {
        return assistantUnit;
    }

    public void setAssistantUnit(String assistantUnit) {
        this.assistantUnit = assistantUnit;
    }

    public String getExerciseContent() {
        return exerciseContent;
    }

    public void setExerciseContent(String exerciseContent) {
        this.exerciseContent = exerciseContent;
    }

    public String getExerciseResult() {
        return exerciseResult;
    }

    public void setExerciseResult(String exerciseResult) {
        this.exerciseResult = exerciseResult;
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
        return "WyFireExercise{" +
        "id=" + id +
        ", unit=" + unit +
        ", startDate=" + startDate +
        ", stopDate=" + stopDate +
        ", exercisePurpose=" + exercisePurpose +
        ", joinPersons=" + joinPersons +
        ", assistantUnit=" + assistantUnit +
        ", exerciseContent=" + exerciseContent +
        ", exerciseResult=" + exerciseResult +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
