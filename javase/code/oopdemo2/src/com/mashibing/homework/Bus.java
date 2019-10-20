package com.mashibing.homework;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 15:34
 */
public class Bus extends MotoVehicle {

    private int seatcount;

    public Bus(){

    }

    public Bus(int no,String brand,int seatcount){
        super(no,brand);
        this.seatcount = seatcount;
    }

    public int getSeatcount() {
        return seatcount;
    }

    public void setSeatcount(int seatcount) {
        this.seatcount = seatcount;
    }

    @Override
    public int calcRent(int day) {
        if(seatcount>16){
            return 1500*day;
        }else{
            return 800*day;
        }
    }
}
