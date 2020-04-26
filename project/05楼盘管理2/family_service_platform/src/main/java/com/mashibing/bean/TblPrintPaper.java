package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 打印纸张宽度设置
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblPrintPaper implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 编码
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String paperName;

    /**
     * 值
     */
    private String paperValue;

    /**
     * 状态
     */
    private Integer paperStatus;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPaperValue() {
        return paperValue;
    }

    public void setPaperValue(String paperValue) {
        this.paperValue = paperValue;
    }

    public Integer getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(Integer paperStatus) {
        this.paperStatus = paperStatus;
    }

    @Override
    public String toString() {
        return "TblPrintPaper{" +
        "id=" + id +
        ", paperName=" + paperName +
        ", paperValue=" + paperValue +
        ", paperStatus=" + paperStatus +
        "}";
    }
}
