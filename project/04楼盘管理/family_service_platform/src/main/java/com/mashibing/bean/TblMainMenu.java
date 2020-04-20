package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 主菜单
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblMainMenu implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主菜单编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 主菜单名称
     */
    private String mainMenuName;

    /**
     * 主菜单文件路径
     */
    private String mainMenuUrl;

    /**
     * 主菜单图标
     */
    private String mainMenuIcon;

    /**
     * 主菜单状态
     */
    private String mainMenuStatus;

    /**
     * 菜单key
     */
    private String mainMenuKey;

    /**
     * 排序号
     */
    private Double mainMenuOrder;

    /**
     * 产品id
     */
    private String belongProduct;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMainMenuName() {
        return mainMenuName;
    }

    public void setMainMenuName(String mainMenuName) {
        this.mainMenuName = mainMenuName;
    }

    public String getMainMenuUrl() {
        return mainMenuUrl;
    }

    public void setMainMenuUrl(String mainMenuUrl) {
        this.mainMenuUrl = mainMenuUrl;
    }

    public String getMainMenuIcon() {
        return mainMenuIcon;
    }

    public void setMainMenuIcon(String mainMenuIcon) {
        this.mainMenuIcon = mainMenuIcon;
    }

    public String getMainMenuStatus() {
        return mainMenuStatus;
    }

    public void setMainMenuStatus(String mainMenuStatus) {
        this.mainMenuStatus = mainMenuStatus;
    }

    public String getMainMenuKey() {
        return mainMenuKey;
    }

    public void setMainMenuKey(String mainMenuKey) {
        this.mainMenuKey = mainMenuKey;
    }

    public Double getMainMenuOrder() {
        return mainMenuOrder;
    }

    public void setMainMenuOrder(Double mainMenuOrder) {
        this.mainMenuOrder = mainMenuOrder;
    }

    public String getBelongProduct() {
        return belongProduct;
    }

    public void setBelongProduct(String belongProduct) {
        this.belongProduct = belongProduct;
    }

    @Override
    public String toString() {
        return "TblMainMenu{" +
        "id=" + id +
        ", mainMenuName=" + mainMenuName +
        ", mainMenuUrl=" + mainMenuUrl +
        ", mainMenuIcon=" + mainMenuIcon +
        ", mainMenuStatus=" + mainMenuStatus +
        ", mainMenuKey=" + mainMenuKey +
        ", mainMenuOrder=" + mainMenuOrder +
        ", belongProduct=" + belongProduct +
        "}";
    }
}
