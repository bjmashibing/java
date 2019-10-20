package com.mashibing.homework;

/**
 * @author: 马士兵教育
 * @create: 2019-08-31 15:44
 */
public class TestMotoVehicle {
    public static void main(String[] args) {
//        MotoVehicle moto = new MotoVehicle();
//    Car car = new Car(1,"宝马","1");
//    System.out.println("租金是："+car.calcRent(5));
//    Bus bus = new Bus(2,"金龙",20);
//        System.out.println("租金是："+bus.calcRent(5));
        MotoVehicle[] moto = new MotoVehicle[5];
        moto[0] = new Car(1,"宝马","1");
        moto[1] = new Car(1,"宝马","1");
        moto[2] = new Car(2,"别克","2");
        moto[3] = new Bus(3,"金龙",34);
        moto[4] = new Track(4,"解放",50);
        int totalMoney = calcTotal(moto);
        System.out.println("总租金是："+totalMoney);
    }

    public static int calcTotal(MotoVehicle[] moto){
        int totalMoney = 0;
        for(int i = 0;i<moto.length;i++){
            totalMoney+=moto[i].calcRent(5);
        }
        return totalMoney;
    }
}
