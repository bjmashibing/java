package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 网络硬盘_文件夹
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblNetdiskDir implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件夹名称
     */
    private String name;

    /**
     * 上级文件夹
     */
    private Integer parentDir;

    /**
     * 是否共享
     */
    private String isShare;

    /**
     * 创建用户编码
     */
    private String userId;

    /**
     * 创建日期
     */
    private LocalDateTime createDate;


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

    public Integer getParentDir() {
        return parentDir;
    }

    public void setParentDir(Integer parentDir) {
        this.parentDir = parentDir;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "TblNetdiskDir{" +
        "id=" + id +
        ", name=" + name +
        ", parentDir=" + parentDir +
        ", isShare=" + isShare +
        ", userId=" + userId +
        ", createDate=" + createDate +
        "}";
    }
}
