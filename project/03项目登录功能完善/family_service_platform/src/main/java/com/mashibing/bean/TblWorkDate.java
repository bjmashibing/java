package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 工作日期
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblWorkDate implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    private LocalDateTime dt;

    /**
     * 星期
     */
    private Integer weekday;

    /**
     * 是否上班
     */
    private Integer isWork;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDt() {
        return dt;
    }

    public void setDt(LocalDateTime dt) {
        this.dt = dt;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Integer getIsWork() {
        return isWork;
    }

    public void setIsWork(Integer isWork) {
        this.isWork = isWork;
    }

    @Override
    public String toString() {
        return "TblWorkDate{" +
        "id=" + id +
        ", dt=" + dt +
        ", weekday=" + weekday +
        ", isWork=" + isWork +
        "}";
    }
}
