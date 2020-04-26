package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 我的记事本
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblMyNote implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建人员编码
     */
    private String createPersonId;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private String type;

    /**
     * 地点
     */
    private String place;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否私人性质
     */
    private Integer isPrivate;

    /**
     * 是否重复
     */
    private Integer isRepeat;

    /**
     * 重复
     */
    private String repeat;

    /**
     * 重复至日结束
     */
    private LocalDateTime repeatStop;

    /**
     * 是否提醒
     */
    private Integer isRemain;

    /**
     * 提前N天提醒
     */
    private Integer remainDay;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime stopDate;

    /**
     * 预约人员
     */
    private String orderPerson;

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

    public String getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(String createPersonId) {
        this.createPersonId = createPersonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Integer getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(Integer isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public LocalDateTime getRepeatStop() {
        return repeatStop;
    }

    public void setRepeatStop(LocalDateTime repeatStop) {
        this.repeatStop = repeatStop;
    }

    public Integer getIsRemain() {
        return isRemain;
    }

    public void setIsRemain(Integer isRemain) {
        this.isRemain = isRemain;
    }

    public Integer getRemainDay() {
        return remainDay;
    }

    public void setRemainDay(Integer remainDay) {
        this.remainDay = remainDay;
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

    public String getOrderPerson() {
        return orderPerson;
    }

    public void setOrderPerson(String orderPerson) {
        this.orderPerson = orderPerson;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "TblMyNote{" +
        "id=" + id +
        ", createPersonId=" + createPersonId +
        ", title=" + title +
        ", type=" + type +
        ", place=" + place +
        ", content=" + content +
        ", isPrivate=" + isPrivate +
        ", isRepeat=" + isRepeat +
        ", repeat=" + repeat +
        ", repeatStop=" + repeatStop +
        ", isRemain=" + isRemain +
        ", remainDay=" + remainDay +
        ", startDate=" + startDate +
        ", stopDate=" + stopDate +
        ", orderPerson=" + orderPerson +
        ", createDate=" + createDate +
        "}";
    }
}
