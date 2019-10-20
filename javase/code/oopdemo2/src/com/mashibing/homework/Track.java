package com.mashibing.homework;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 16:50
 */
public class Track extends MotoVehicle {
    private int weight;

    public Track(){

    }

    public Track(int no,String brand,int weight){
        super(no,brand);
        this.weight = weight;
    }
    @Override
    public int calcRent(int day) {
        return 50*day*weight;
    }
}
