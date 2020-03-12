package com.mashibing.jdk;

public class MyCalculator implements Calculator{
    @Override
    public int add(int i, int j) {
        int result = i + j;
        return result;

    }

    @Override
    public int sub(int i, int j) {
        int result = i - j;
        return result;
    }

    @Override
    public int div(int i, int j) {
        int result = i / j;
        return result;
    }

    @Override
    public int mul(int i, int j) {
        int result = i * j;
        return result;
    }
}
