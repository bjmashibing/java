package com.mashibing.streamtype;

import java.io.Serializable;

/**
 * @author: 马士兵教育
 * @create: 2019-09-22 21:47
 */

/**
 * 1、如果需要将对象通过io流进行传输，那么必须要实现序列化接口
 * 2、当前类中必须声明一个serialVersionUID的值，值为多少无所谓，但是要有
 * 3、transient:使用此关键字修饰的变量，在进行序列化的时候，不会别序列化
 *
 */
public class Person implements Serializable {
    long serialVersionUID = 1L;

    private int id;
    private String name;
    transient private String pwd;

    public Person() {
    }

    public Person(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
