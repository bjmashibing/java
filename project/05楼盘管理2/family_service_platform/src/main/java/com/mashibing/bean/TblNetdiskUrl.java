package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 网络硬盘路径
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblNetdiskUrl implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件夹id
     */
    private Integer dirId;

    /**
     * 文件原名称
     */
    private String fileName;

    /**
     * 新名称
     */
    private String newName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文档大小
     */
    private Integer fileSize;

    /**
     * 上传时间
     */
    private LocalDateTime createDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "TblNetdiskUrl{" +
        "id=" + id +
        ", dirId=" + dirId +
        ", fileName=" + fileName +
        ", newName=" + newName +
        ", fileType=" + fileType +
        ", fileSize=" + fileSize +
        ", createDate=" + createDate +
        "}";
    }
}
