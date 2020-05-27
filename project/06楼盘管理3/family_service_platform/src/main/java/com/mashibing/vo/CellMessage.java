package com.mashibing.vo;

public class CellMessage {
    private String unitCode;
    private Integer startFloor;
    private Integer stopFloor;
    private Integer startCellId;
    private Integer stopCellId;

    public CellMessage() {
    }

    public CellMessage(String unitCode, Integer startFloor, Integer stopFloor, Integer startCellId, Integer stopCellId) {
        this.unitCode = unitCode;
        this.startFloor = startFloor;
        this.stopFloor = stopFloor;
        this.startCellId = startCellId;
        this.stopCellId = stopCellId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Integer getStartFloor() {
        return startFloor;
    }

    public void setStartFloor(Integer startFloor) {
        this.startFloor = startFloor;
    }

    public Integer getStopFloor() {
        return stopFloor;
    }

    public void setStopFloor(Integer stopFloor) {
        this.stopFloor = stopFloor;
    }

    public Integer getStartCellId() {
        return startCellId;
    }

    public void setStartCellId(Integer startCellId) {
        this.startCellId = startCellId;
    }

    public Integer getStopCellId() {
        return stopCellId;
    }

    public void setStopCellId(Integer stopCellId) {
        this.stopCellId = stopCellId;
    }

    @Override
    public String toString() {
        return "CellMessage{" +
                "unitCode='" + unitCode + '\'' +
                ", startFloor=" + startFloor +
                ", stopFloor=" + stopFloor +
                ", startCellId=" + startCellId +
                ", stopCellId=" + stopCellId +
                '}';
    }
}
