package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 自定义类型
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblCustomType implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 类型编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 类型状态
     */
    private String status;

    /**
     * 类型分类
     */
    private String category;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "TblCustomType{" +
        "id=" + id +
        ", name=" + name +
        ", status=" + status +
        ", category=" + category +
        "}";
    }
}
