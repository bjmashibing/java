package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 社区活动
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyCommunityEvent implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 活动日期
     */
    private LocalDateTime eventDate;

    /**
     * 活动内容
     */
    private String eventContent;

    /**
     * 主持者
     */
    private String hoster;

    /**
     * 参加人员
     */
    private String joinPerson;

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

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getHoster() {
        return hoster;
    }

    public void setHoster(String hoster) {
        this.hoster = hoster;
    }

    public String getJoinPerson() {
        return joinPerson;
    }

    public void setJoinPerson(String joinPerson) {
        this.joinPerson = joinPerson;
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
        return "WyCommunityEvent{" +
        "id=" + id +
        ", eventDate=" + eventDate +
        ", eventContent=" + eventContent +
        ", hoster=" + hoster +
        ", joinPerson=" + joinPerson +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
