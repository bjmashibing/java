package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 楼宇信息表
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FcBuilding implements Serializable {

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
     * 楼宇编码
     */
    private String buildingCode;

    /**
     * 楼宇名称
     */
    private String buildingName;

    /**
     * 楼宇功能
     */
    private String buildingFunction;

    /**
     * 使用面积
     */
    private Double usedArea;

    /**
     * 建筑面积
     */
    private Double buildArea;

    /**
     * 建设许可证号
     */
    private String buildPermissionId;

    /**
     * 销售许可证号
     */
    private String salePermissionId;

    /**
     * 竣工日期
     */
    private LocalDateTime finishDate;

    /**
     * 封顶日期
     */
    private LocalDateTime overRoofDate;

    /**
     * 装修
     */
    private String decorate;

    /**
     * 结构类别
     */
    private String structType;

    /**
     * 完损等级
     */
    private String damageGrade;

    /**
     * 单元数量
     */
    private Double unitCount;

    /**
     * 楼宇类型
     */
    private String buildingType;

    /**
     * 清扫层数
     */
    private Integer cleanFloor;

    /**
     * 拖洗层数
     */
    private Integer mopFloor;

    /**
     * 楼狼通道地面
     */
    private Double channelArea;

    /**
     * 电梯轿箱
     */
    private Integer elevator;

    /**
     * 通道门
     */
    private Integer channelDoor;

    /**
     * 电梯门
     */
    private Integer evevatorDoor;

    /**
     * 水井门
     */
    private Integer waterWellDoor;

    /**
     * 电井门
     */
    private Integer electricWellDoor;

    /**
     * 百叶窗
     */
    private Integer windowShades;

    /**
     * 消防栓
     */
    private Integer hydrant;

    /**
     * 整容镜
     */
    private Integer mirrors;

    /**
     * 单元门
     */
    private Integer unitDoor;

    /**
     * 硬化地面
     */
    private Double hardenGroundArea;

    /**
     * 提纯绿地
     */
    private Double greenArea;

    /**
     * 不提纯绿地
     */
    private Double noGreenArea;

    /**
     * 人工水面
     */
    private Double waterByPerson;

    /**
     * 是否使用电梯
     */
    private String isElevator;

    /**
     * 是否需要二次水电
     */
    private String isSecondWaterElectric;

    /**
     * 随机标识码
     */
    private String randomIdentify;

    /**
     * 备注
     */
    private String remark;


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

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingFunction() {
        return buildingFunction;
    }

    public void setBuildingFunction(String buildingFunction) {
        this.buildingFunction = buildingFunction;
    }

    public Double getUsedArea() {
        return usedArea;
    }

    public void setUsedArea(Double usedArea) {
        this.usedArea = usedArea;
    }

    public Double getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(Double buildArea) {
        this.buildArea = buildArea;
    }

    public String getBuildPermissionId() {
        return buildPermissionId;
    }

    public void setBuildPermissionId(String buildPermissionId) {
        this.buildPermissionId = buildPermissionId;
    }

    public String getSalePermissionId() {
        return salePermissionId;
    }

    public void setSalePermissionId(String salePermissionId) {
        this.salePermissionId = salePermissionId;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public LocalDateTime getOverRoofDate() {
        return overRoofDate;
    }

    public void setOverRoofDate(LocalDateTime overRoofDate) {
        this.overRoofDate = overRoofDate;
    }

    public String getDecorate() {
        return decorate;
    }

    public void setDecorate(String decorate) {
        this.decorate = decorate;
    }

    public String getStructType() {
        return structType;
    }

    public void setStructType(String structType) {
        this.structType = structType;
    }

    public String getDamageGrade() {
        return damageGrade;
    }

    public void setDamageGrade(String damageGrade) {
        this.damageGrade = damageGrade;
    }

    public Double getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Double unitCount) {
        this.unitCount = unitCount;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public Integer getCleanFloor() {
        return cleanFloor;
    }

    public void setCleanFloor(Integer cleanFloor) {
        this.cleanFloor = cleanFloor;
    }

    public Integer getMopFloor() {
        return mopFloor;
    }

    public void setMopFloor(Integer mopFloor) {
        this.mopFloor = mopFloor;
    }

    public Double getChannelArea() {
        return channelArea;
    }

    public void setChannelArea(Double channelArea) {
        this.channelArea = channelArea;
    }

    public Integer getElevator() {
        return elevator;
    }

    public void setElevator(Integer elevator) {
        this.elevator = elevator;
    }

    public Integer getChannelDoor() {
        return channelDoor;
    }

    public void setChannelDoor(Integer channelDoor) {
        this.channelDoor = channelDoor;
    }

    public Integer getEvevatorDoor() {
        return evevatorDoor;
    }

    public void setEvevatorDoor(Integer evevatorDoor) {
        this.evevatorDoor = evevatorDoor;
    }

    public Integer getWaterWellDoor() {
        return waterWellDoor;
    }

    public void setWaterWellDoor(Integer waterWellDoor) {
        this.waterWellDoor = waterWellDoor;
    }

    public Integer getElectricWellDoor() {
        return electricWellDoor;
    }

    public void setElectricWellDoor(Integer electricWellDoor) {
        this.electricWellDoor = electricWellDoor;
    }

    public Integer getWindowShades() {
        return windowShades;
    }

    public void setWindowShades(Integer windowShades) {
        this.windowShades = windowShades;
    }

    public Integer getHydrant() {
        return hydrant;
    }

    public void setHydrant(Integer hydrant) {
        this.hydrant = hydrant;
    }

    public Integer getMirrors() {
        return mirrors;
    }

    public void setMirrors(Integer mirrors) {
        this.mirrors = mirrors;
    }

    public Integer getUnitDoor() {
        return unitDoor;
    }

    public void setUnitDoor(Integer unitDoor) {
        this.unitDoor = unitDoor;
    }

    public Double getHardenGroundArea() {
        return hardenGroundArea;
    }

    public void setHardenGroundArea(Double hardenGroundArea) {
        this.hardenGroundArea = hardenGroundArea;
    }

    public Double getGreenArea() {
        return greenArea;
    }

    public void setGreenArea(Double greenArea) {
        this.greenArea = greenArea;
    }

    public Double getNoGreenArea() {
        return noGreenArea;
    }

    public void setNoGreenArea(Double noGreenArea) {
        this.noGreenArea = noGreenArea;
    }

    public Double getWaterByPerson() {
        return waterByPerson;
    }

    public void setWaterByPerson(Double waterByPerson) {
        this.waterByPerson = waterByPerson;
    }

    public String getIsElevator() {
        return isElevator;
    }

    public void setIsElevator(String isElevator) {
        this.isElevator = isElevator;
    }

    public String getIsSecondWaterElectric() {
        return isSecondWaterElectric;
    }

    public void setIsSecondWaterElectric(String isSecondWaterElectric) {
        this.isSecondWaterElectric = isSecondWaterElectric;
    }

    public String getRandomIdentify() {
        return randomIdentify;
    }

    public void setRandomIdentify(String randomIdentify) {
        this.randomIdentify = randomIdentify;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "FcBuilding{" +
        "id=" + id +
        ", estateCode=" + estateCode +
        ", buildingCode=" + buildingCode +
        ", buildingName=" + buildingName +
        ", buildingFunction=" + buildingFunction +
        ", usedArea=" + usedArea +
        ", buildArea=" + buildArea +
        ", buildPermissionId=" + buildPermissionId +
        ", salePermissionId=" + salePermissionId +
        ", finishDate=" + finishDate +
        ", overRoofDate=" + overRoofDate +
        ", decorate=" + decorate +
        ", structType=" + structType +
        ", damageGrade=" + damageGrade +
        ", unitCount=" + unitCount +
        ", buildingType=" + buildingType +
        ", cleanFloor=" + cleanFloor +
        ", mopFloor=" + mopFloor +
        ", channelArea=" + channelArea +
        ", elevator=" + elevator +
        ", channelDoor=" + channelDoor +
        ", evevatorDoor=" + evevatorDoor +
        ", waterWellDoor=" + waterWellDoor +
        ", electricWellDoor=" + electricWellDoor +
        ", windowShades=" + windowShades +
        ", hydrant=" + hydrant +
        ", mirrors=" + mirrors +
        ", unitDoor=" + unitDoor +
        ", hardenGroundArea=" + hardenGroundArea +
        ", greenArea=" + greenArea +
        ", noGreenArea=" + noGreenArea +
        ", waterByPerson=" + waterByPerson +
        ", isElevator=" + isElevator +
        ", isSecondWaterElectric=" + isSecondWaterElectric +
        ", randomIdentify=" + randomIdentify +
        ", remark=" + remark +
        "}";
    }
}
