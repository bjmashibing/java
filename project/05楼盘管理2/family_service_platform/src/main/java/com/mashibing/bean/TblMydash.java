package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 我的驾驶舱
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblMydash implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属驾驶舱id
     */
    private Integer dashId;

    /**
     * 排序号
     */
    private Long orderId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 显示条数
     */
    private String showNum;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDashId() {
        return dashId;
    }

    public void setDashId(Integer dashId) {
        this.dashId = dashId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShowNum() {
        return showNum;
    }

    public void setShowNum(String showNum) {
        this.showNum = showNum;
    }

    @Override
    public String toString() {
        return "TblMydash{" +
        "id=" + id +
        ", dashId=" + dashId +
        ", orderId=" + orderId +
        ", username=" + username +
        ", showNum=" + showNum +
        "}";
    }
}
