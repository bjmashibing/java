package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 图纸管理
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class WyPictureManage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图纸名称
     */
    private String pictureName;

    /**
     * 图纸类型
     */
    private Long pictureType;

    /**
     * 说明
     */
    private String desc;

    /**
     * 图纸附件
     */
    private String pictureAttach;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 上传人
     */
    private String uploadPerson;

    /**
     * 上传时间
     */
    private LocalDateTime uploadDate;

    /**
     * 修改人
     */
    private String updatePerson;

    /**
     * 修改时间
     */
    private LocalDateTime updateDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public Long getPictureType() {
        return pictureType;
    }

    public void setPictureType(Long pictureType) {
        this.pictureType = pictureType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPictureAttach() {
        return pictureAttach;
    }

    public void setPictureAttach(String pictureAttach) {
        this.pictureAttach = pictureAttach;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUploadPerson() {
        return uploadPerson;
    }

    public void setUploadPerson(String uploadPerson) {
        this.uploadPerson = uploadPerson;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "WyPictureManage{" +
        "id=" + id +
        ", pictureName=" + pictureName +
        ", pictureType=" + pictureType +
        ", desc=" + desc +
        ", pictureAttach=" + pictureAttach +
        ", company=" + company +
        ", uploadPerson=" + uploadPerson +
        ", uploadDate=" + uploadDate +
        ", updatePerson=" + updatePerson +
        ", updateDate=" + updateDate +
        "}";
    }
}
