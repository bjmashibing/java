package com.mashibing.jdk;

public class Test {
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Object proxy = CalculatorProxy.getProxy(new MyCalculator());
        System.out.println(((Calculator)proxy).add(1, 1));
        System.out.println(proxy.getClass());
    }
}
