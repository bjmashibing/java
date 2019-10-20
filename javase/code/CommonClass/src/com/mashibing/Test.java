package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-09-07 20:47
 */
public class Test {
    Gender gender = Gender.女;
    Gender gender2 = Gender.男;

    public static void main(String[] args) {
        EventEnum ee = EventEnum.LAUNCH;
        ee.show();
        String name = EventEnum.PAGEVIEW.name();
        System.out.println(name);
    }
}
