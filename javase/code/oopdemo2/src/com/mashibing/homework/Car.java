package com.mashibing.homework;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 15:34
 */
public class Car  extends MotoVehicle{

    private String type;

    public Car(){

    }

    public Car(String type) {
        this.type = type;
    }

    public Car(int no,String brand,String type){
        super(no,brand);
        this.type= type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int calcRent(int day) {

        if(type.equals("0")){
            return 600*day;
        }else if(type.equals("1")){
            return 500*day;
        }else if(type.equals("2")){
            return 300*day;
        }else{
            System.out.println("类型不匹配");
            return 0;
        }
    }
}
