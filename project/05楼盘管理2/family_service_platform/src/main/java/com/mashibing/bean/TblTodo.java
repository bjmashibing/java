package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 待办事项
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblTodo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 权限
     */
    private String privileges;

    /**
     * 状态
     */
    private String status;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 显示行数
     */
    private Integer showNumber;

    /**
     * 天数
     */
    private Integer days;

    /**
     * 所属产品
     */
    private String belongProduct;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getShowNumber() {
        return showNumber;
    }

    public void setShowNumber(Integer showNumber) {
        this.showNumber = showNumber;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getBelongProduct() {
        return belongProduct;
    }

    public void setBelongProduct(String belongProduct) {
        this.belongProduct = belongProduct;
    }

    @Override
    public String toString() {
        return "TblTodo{" +
        "id=" + id +
        ", name=" + name +
        ", privileges=" + privileges +
        ", status=" + status +
        ", url=" + url +
        ", showNumber=" + showNumber +
        ", days=" + days +
        ", belongProduct=" + belongProduct +
        "}";
    }
}
