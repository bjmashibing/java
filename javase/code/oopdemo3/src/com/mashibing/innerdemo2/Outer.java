package com.mashibing.innerdemo2;

/**
 * @author: 马士兵教育
 * @create: 2019-09-01 15:22
 */
public class Outer {

    private String name = "zhangsan";

    class Inner{
        private String name = "lisi";

        public void show(){
            System.out.println(name);
            System.out.println(this.name);
            System.out.println(Outer.this.name);
        }
    }

    public static void main(String[] args) {
        Outer.Inner inner = new Outer().new Inner();
        inner.show();
    }
}
