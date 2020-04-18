package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 楼盘信息
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FcEstate implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房产编码
     */
    private String estateCode;

    /**
     * 房产名称
     */
    private String estateName;

    /**
     * 房产地址
     */
    private String estateAddr;

    /**
     * 占地面积
     */
    private Double coverArea;

    /**
     * 建筑面积
     */
    private Double buildArea;

    /**
     * 绿地面积
     */
    private Double greenArea;

    /**
     * 道路面积
     */
    private Double roadArea;

    /**
     * 楼宇数量
     */
    private Double buildingNumber;

    /**
     * 负责人
     */
    private String buildingLeader;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 法人代表
     */
    private String companyBehalf;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系地址
     */
    private String contactAddr;

    /**
     * 车位滞纳金比率
     */
    private Double carSpaceDelayRate;

    /**
     * 车位超期天数
     */
    private Integer carSpaceOverDay;

    /**
     * 房产类型
     */
    private String estateType;

    /**
     * 路灯数量
     */
    private Integer streetLampNumber;

    /**
     * 化粪池数量
     */
    @TableField("hfcNum")
    private Integer hfcNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属公司
     */
    private String company;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstateCode() {
        return estateCode;
    }

    public void setEstateCode(String estateCode) {
        this.estateCode = estateCode;
    }

    public String getEstateName() {
        return estateName;
    }

    public void setEstateName(String estateName) {
        this.estateName = estateName;
    }

    public String getEstateAddr() {
        return estateAddr;
    }

    public void setEstateAddr(String estateAddr) {
        this.estateAddr = estateAddr;
    }

    public Double getCoverArea() {
        return coverArea;
    }

    public void setCoverArea(Double coverArea) {
        this.coverArea = coverArea;
    }

    public Double getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(Double buildArea) {
        this.buildArea = buildArea;
    }

    public Double getGreenArea() {
        return greenArea;
    }

    public void setGreenArea(Double greenArea) {
        this.greenArea = greenArea;
    }

    public Double getRoadArea() {
        return roadArea;
    }

    public void setRoadArea(Double roadArea) {
        this.roadArea = roadArea;
    }

    public Double getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(Double buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getBuildingLeader() {
        return buildingLeader;
    }

    public void setBuildingLeader(String buildingLeader) {
        this.buildingLeader = buildingLeader;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyBehalf() {
        return companyBehalf;
    }

    public void setCompanyBehalf(String companyBehalf) {
        this.companyBehalf = companyBehalf;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }

    public Double getCarSpaceDelayRate() {
        return carSpaceDelayRate;
    }

    public void setCarSpaceDelayRate(Double carSpaceDelayRate) {
        this.carSpaceDelayRate = carSpaceDelayRate;
    }

    public Integer getCarSpaceOverDay() {
        return carSpaceOverDay;
    }

    public void setCarSpaceOverDay(Integer carSpaceOverDay) {
        this.carSpaceOverDay = carSpaceOverDay;
    }

    public String getEstateType() {
        return estateType;
    }

    public void setEstateType(String estateType) {
        this.estateType = estateType;
    }

    public Integer getStreetLampNumber() {
        return streetLampNumber;
    }

    public void setStreetLampNumber(Integer streetLampNumber) {
        this.streetLampNumber = streetLampNumber;
    }

    public Integer getHfcNum() {
        return hfcNum;
    }

    public void setHfcNum(Integer hfcNum) {
        this.hfcNum = hfcNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "FcEstate{" +
        "id=" + id +
        ", estateCode=" + estateCode +
        ", estateName=" + estateName +
        ", estateAddr=" + estateAddr +
        ", coverArea=" + coverArea +
        ", buildArea=" + buildArea +
        ", greenArea=" + greenArea +
        ", roadArea=" + roadArea +
        ", buildingNumber=" + buildingNumber +
        ", buildingLeader=" + buildingLeader +
        ", companyName=" + companyName +
        ", companyBehalf=" + companyBehalf +
        ", contact=" + contact +
        ", contactPhone=" + contactPhone +
        ", contactAddr=" + contactAddr +
        ", carSpaceDelayRate=" + carSpaceDelayRate +
        ", carSpaceOverDay=" + carSpaceOverDay +
        ", estateType=" + estateType +
        ", streetLampNumber=" + streetLampNumber +
        ", hfcNum=" + hfcNum +
        ", remark=" + remark +
        ", company=" + company +
        "}";
    }
}
