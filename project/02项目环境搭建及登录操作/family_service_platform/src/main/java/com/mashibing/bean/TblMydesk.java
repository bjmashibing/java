package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 我的桌面
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblMydesk implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属模块
     */
    private String belongModel;

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

    public String getBelongModel() {
        return belongModel;
    }

    public void setBelongModel(String belongModel) {
        this.belongModel = belongModel;
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
        return "TblMydesk{" +
        "id=" + id +
        ", belongModel=" + belongModel +
        ", orderId=" + orderId +
        ", username=" + username +
        ", showNum=" + showNum +
        "}";
    }
}
