package com.mashibing.proxy;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 21:33
 */
public class XiMenQing {
    public static void main(String[] args) {

//        WangPo wangPo = new WangPo();
//        wangPo.playWithMen();
//        wangPo.makeEyesWithMen();

        JiaShi jiaShi = new JiaShi();
        WangPo wangPo = new WangPo(jiaShi);
        wangPo.makeEyesWithMen();
        wangPo.playWithMen();

    }
}
