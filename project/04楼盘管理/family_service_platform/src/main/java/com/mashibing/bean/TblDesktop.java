package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 桌面
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblDesktop implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编码
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 更多地址
     */
    private String morePath;

    /**
     * 权限
     */
    private String privileges;

    /**
     * 状态
     */
    private String status;

    /**
     * 所属产品
     */
    private String belongProduct;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMorePath() {
        return morePath;
    }

    public void setMorePath(String morePath) {
        this.morePath = morePath;
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

    public String getBelongProduct() {
        return belongProduct;
    }

    public void setBelongProduct(String belongProduct) {
        this.belongProduct = belongProduct;
    }

    @Override
    public String toString() {
        return "TblDesktop{" +
        "id=" + id +
        ", name=" + name +
        ", morePath=" + morePath +
        ", privileges=" + privileges +
        ", status=" + status +
        ", belongProduct=" + belongProduct +
        "}";
    }
}
