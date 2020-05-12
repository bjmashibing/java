package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 房间信息表
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class FcCell implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 房间编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 单元编码
     */
    private Integer unitCode;

    /**
     * 房间编码
     */
    private String cellCode;

    /**
     * 房间名称
     */
    private String cellName;

    /**
     * 户型编码
     */
    private String cellHouseCode;

    /**
     * 朝向编码
     */
    private String cellTowardCode;

    /**
     *  功能
     */
    private String cellFunction;

    /**
     * 装修编码
     */
    private String cellDecorateCode;

    /**
     * 使用面积
     */
    private Double cellUsedArea;

    /**
     * 建筑面积
     */
    private Double cellBuildArea;

    /**
     * 车库面积
     */
    private Double carportArea;

    /**
     * 车位面积
     */
    private Double carArea;

    /**
     * 阁楼面积
     */
    private Double loftArea;

    /**
     * 储藏室面积
     */
    private Double storeArea;

    /**
     * 楼层数
     */
    private Integer floorNumber;

    /**
     * 上次欠缴
     */
    private Double lastDebt;

    /**
     * 物业类型
     */
    private Long propertyType;

    /**
     * 租金
     */
    private Double rentMoney;

    /**
     * 长度
     */
    private Double length;

    /**
     * 宽度
     */
    private Double width;

    /**
     * 档口用途
     */
    private Long stallUse;

    /**
     * 档口区域
     */
    private Long stallArea;

    /**
     * 是否已售
     */
    private String isSale;

    /**
     * 是否已租
     */
    private String isRent;

    /**
     * 销售合同编号
     */
    private String saleContractId;

    /**
     * 租赁合同编号
     */
    private String rentContractId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 系数
     */
    private String factor;

    /**
     * 房间性质
     */
    private Integer cellProperty;

    /**
     * 储藏室号
     */
    private String storeId;

    /**
     * 车牌号
     */
    private String carLicenceId;

    /**
     * 车位号
     */
    private String carSpaceId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(Integer unitCode) {
        this.unitCode = unitCode;
    }

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getCellHouseCode() {
        return cellHouseCode;
    }

    public void setCellHouseCode(String cellHouseCode) {
        this.cellHouseCode = cellHouseCode;
    }

    public String getCellTowardCode() {
        return cellTowardCode;
    }

    public void setCellTowardCode(String cellTowardCode) {
        this.cellTowardCode = cellTowardCode;
    }

    public String getCellFunction() {
        return cellFunction;
    }

    public void setCellFunction(String cellFunction) {
        this.cellFunction = cellFunction;
    }

    public String getCellDecorateCode() {
        return cellDecorateCode;
    }

    public void setCellDecorateCode(String cellDecorateCode) {
        this.cellDecorateCode = cellDecorateCode;
    }

    public Double getCellUsedArea() {
        return cellUsedArea;
    }

    public void setCellUsedArea(Double cellUsedArea) {
        this.cellUsedArea = cellUsedArea;
    }

    public Double getCellBuildArea() {
        return cellBuildArea;
    }

    public void setCellBuildArea(Double cellBuildArea) {
        this.cellBuildArea = cellBuildArea;
    }

    public Double getCarportArea() {
        return carportArea;
    }

    public void setCarportArea(Double carportArea) {
        this.carportArea = carportArea;
    }

    public Double getCarArea() {
        return carArea;
    }

    public void setCarArea(Double carArea) {
        this.carArea = carArea;
    }

    public Double getLoftArea() {
        return loftArea;
    }

    public void setLoftArea(Double loftArea) {
        this.loftArea = loftArea;
    }

    public Double getStoreArea() {
        return storeArea;
    }

    public void setStoreArea(Double storeArea) {
        this.storeArea = storeArea;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Double getLastDebt() {
        return lastDebt;
    }

    public void setLastDebt(Double lastDebt) {
        this.lastDebt = lastDebt;
    }

    public Long getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Long propertyType) {
        this.propertyType = propertyType;
    }

    public Double getRentMoney() {
        return rentMoney;
    }

    public void setRentMoney(Double rentMoney) {
        this.rentMoney = rentMoney;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Long getStallUse() {
        return stallUse;
    }

    public void setStallUse(Long stallUse) {
        this.stallUse = stallUse;
    }

    public Long getStallArea() {
        return stallArea;
    }

    public void setStallArea(Long stallArea) {
        this.stallArea = stallArea;
    }

    public String getIsSale() {
        return isSale;
    }

    public void setIsSale(String isSale) {
        this.isSale = isSale;
    }

    public String getIsRent() {
        return isRent;
    }

    public void setIsRent(String isRent) {
        this.isRent = isRent;
    }

    public String getSaleContractId() {
        return saleContractId;
    }

    public void setSaleContractId(String saleContractId) {
        this.saleContractId = saleContractId;
    }

    public String getRentContractId() {
        return rentContractId;
    }

    public void setRentContractId(String rentContractId) {
        this.rentContractId = rentContractId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public Integer getCellProperty() {
        return cellProperty;
    }

    public void setCellProperty(Integer cellProperty) {
        this.cellProperty = cellProperty;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCarLicenceId() {
        return carLicenceId;
    }

    public void setCarLicenceId(String carLicenceId) {
        this.carLicenceId = carLicenceId;
    }

    public String getCarSpaceId() {
        return carSpaceId;
    }

    public void setCarSpaceId(String carSpaceId) {
        this.carSpaceId = carSpaceId;
    }

    @Override
    public String toString() {
        return "FcCell{" +
        "id=" + id +
        ", unitCode=" + unitCode +
        ", cellCode=" + cellCode +
        ", cellName=" + cellName +
        ", cellHouseCode=" + cellHouseCode +
        ", cellTowardCode=" + cellTowardCode +
        ", cellFunction=" + cellFunction +
        ", cellDecorateCode=" + cellDecorateCode +
        ", cellUsedArea=" + cellUsedArea +
        ", cellBuildArea=" + cellBuildArea +
        ", carportArea=" + carportArea +
        ", carArea=" + carArea +
        ", loftArea=" + loftArea +
        ", storeArea=" + storeArea +
        ", floorNumber=" + floorNumber +
        ", lastDebt=" + lastDebt +
        ", propertyType=" + propertyType +
        ", rentMoney=" + rentMoney +
        ", length=" + length +
        ", width=" + width +
        ", stallUse=" + stallUse +
        ", stallArea=" + stallArea +
        ", isSale=" + isSale +
        ", isRent=" + isRent +
        ", saleContractId=" + saleContractId +
        ", rentContractId=" + rentContractId +
        ", remark=" + remark +
        ", factor=" + factor +
        ", cellProperty=" + cellProperty +
        ", storeId=" + storeId +
        ", carLicenceId=" + carLicenceId +
        ", carSpaceId=" + carSpaceId +
        "}";
    }
}
