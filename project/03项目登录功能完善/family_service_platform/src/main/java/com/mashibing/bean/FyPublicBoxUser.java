package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 公表关联用户
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FyPublicBoxUser implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动增长id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 表号
     */
    private String publicBoxId;

    /**
     * 房间号
     */
    private Integer cellId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPublicBoxId() {
        return publicBoxId;
    }

    public void setPublicBoxId(String publicBoxId) {
        this.publicBoxId = publicBoxId;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    @Override
    public String toString() {
        return "FyPublicBoxUser{" +
        "id=" + id +
        ", publicBoxId=" + publicBoxId +
        ", cellId=" + cellId +
        "}";
    }
}
