package com.mashibing.proxy;

import javax.tools.Diagnostic;

/**
 * @author: 马士兵教育
 * @create: 2019-09-28 21:30
 */

/*
*
* 代理人
* */
public class WangPo implements KindWomen {

    private KindWomen kindWomen;

    public WangPo(){
        this.kindWomen = new PanJinLian();
    }

    public WangPo(KindWomen kindWomen){
        this.kindWomen = kindWomen;
    }

    @Override
    public void makeEyesWithMen() {
        this.kindWomen.makeEyesWithMen();
    }

    @Override
    public void playWithMen() {
        this.kindWomen.playWithMen();
    }
}
