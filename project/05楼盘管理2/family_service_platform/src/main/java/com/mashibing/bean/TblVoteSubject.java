package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 投票题目表
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblVoteSubject implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 题目编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属项目编号
     */
    private Integer projectId;

    /**
     * 题目名称
     */
    private String subjectName;

    /**
     * 建档人
     */
    private String inputRecordPerson;

    /**
     * 建档时间
     */
    private LocalDateTime inputRecordDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getInputRecordPerson() {
        return inputRecordPerson;
    }

    public void setInputRecordPerson(String inputRecordPerson) {
        this.inputRecordPerson = inputRecordPerson;
    }

    public LocalDateTime getInputRecordDate() {
        return inputRecordDate;
    }

    public void setInputRecordDate(LocalDateTime inputRecordDate) {
        this.inputRecordDate = inputRecordDate;
    }

    @Override
    public String toString() {
        return "TblVoteSubject{" +
        "id=" + id +
        ", projectId=" + projectId +
        ", subjectName=" + subjectName +
        ", inputRecordPerson=" + inputRecordPerson +
        ", inputRecordDate=" + inputRecordDate +
        "}";
    }
}
