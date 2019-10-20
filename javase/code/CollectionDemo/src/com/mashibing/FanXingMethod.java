package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-09-21 16:44
 */
public class FanXingMethod<T> {

    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public <Q> void show(Q q){
        System.out.println(q);
        System.out.println(t);
    }
}
