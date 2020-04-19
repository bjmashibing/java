package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 附件
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblAttupload implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 附件id
     */
    @TableId(value = "attID", type = IdType.AUTO)
    private Integer attID;

    /**
     * 附件名称
     */
    @TableField("attName")
    private String attName;

    /**
     * 附件新名称
     */
    @TableField("attNewName")
    private String attNewName;

    /**
     * 唯一key
     */
    @TableField("attKey")
    private String attKey;

    /**
     * 附件分类
     */
    @TableField("attClass")
    private String attClass;


    public Integer getAttID() {
        return attID;
    }

    public void setAttID(Integer attID) {
        this.attID = attID;
    }

    public String getAttName() {
        return attName;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public String getAttNewName() {
        return attNewName;
    }

    public void setAttNewName(String attNewName) {
        this.attNewName = attNewName;
    }

    public String getAttKey() {
        return attKey;
    }

    public void setAttKey(String attKey) {
        this.attKey = attKey;
    }

    public String getAttClass() {
        return attClass;
    }

    public void setAttClass(String attClass) {
        this.attClass = attClass;
    }

    @Override
    public String toString() {
        return "TblAttupload{" +
        "attID=" + attID +
        ", attName=" + attName +
        ", attNewName=" + attNewName +
        ", attKey=" + attKey +
        ", attClass=" + attClass +
        "}";
    }
}
