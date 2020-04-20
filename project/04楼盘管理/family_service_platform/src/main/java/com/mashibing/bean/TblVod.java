package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 视频点播
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblVod implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 视频编码
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 来源
     */
    private String videoSource;

    /**
     * 视频类型
     */
    private Long videlType;

    /**
     * 节目名称
     */
    private String programName;

    /**
     * 节目路径
     */
    private String programUrl;

    /**
     * 简介
     */
    private String simpleIntro;

    /**
     * 是否在首页显示
     */
    private String isFirst;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    public Long getVidelType() {
        return videlType;
    }

    public void setVidelType(Long videlType) {
        this.videlType = videlType;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramUrl() {
        return programUrl;
    }

    public void setProgramUrl(String programUrl) {
        this.programUrl = programUrl;
    }

    public String getSimpleIntro() {
        return simpleIntro;
    }

    public void setSimpleIntro(String simpleIntro) {
        this.simpleIntro = simpleIntro;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
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

    @Override
    public String toString() {
        return "TblVod{" +
        "id=" + id +
        ", videoName=" + videoName +
        ", videoSource=" + videoSource +
        ", videlType=" + videlType +
        ", programName=" + programName +
        ", programUrl=" + programUrl +
        ", simpleIntro=" + simpleIntro +
        ", isFirst=" + isFirst +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        "}";
    }
}
