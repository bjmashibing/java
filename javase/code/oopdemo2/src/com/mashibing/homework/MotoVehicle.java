package com.mashibing.homework;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 15:33
 */
public abstract class MotoVehicle {

    private int no;
    private String brand;

    public MotoVehicle(){

    }

    public MotoVehicle(int no,String brand){
        this.no = no;
        this.brand = brand;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public abstract int calcRent(int day);
}
