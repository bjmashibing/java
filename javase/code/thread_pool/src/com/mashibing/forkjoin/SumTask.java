package com.mashibing.forkjoin;

import java.util.concurrent.RecursiveTask;

class SumTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 20; //每个小任务 最多只累加20个数
    private int arry[];
    private int start;
    private int end;
    
    

    /**
     * Creates a new instance of SumTask.
     * 累加从start到end的arry数组
     * @param arry
     * @param start
     * @param end
     */
    public SumTask(int[] arry, int start, int end) {
        super();
        this.arry = arry;
        this.start = start;
        this.end = end;
    }



    @Override
    protected Integer compute() {
        int sum =0;
        //当end与start之间的差小于threshold时，开始进行实际的累加
        if(end - start <THRESHOLD){
            for(int i= start;i<end;i++){
                System.out.println(Thread.currentThread().getName()+"的i值："+arry[i]);
                sum += arry[i];
            }
            return sum;
        }else {//当end与start之间的差大于threshold，即要累加的数超过20个时候，将大任务分解成小任务
            int middle = (start+ end)/2;
            SumTask left = new SumTask(arry, start, middle);
            SumTask right = new SumTask(arry, middle, end);
            //并行执行两个 小任务
            left.fork();
            right.fork();
            //把两个小任务累加的结果合并起来
            return left.join()+right.join();
        }
        
    }
    
}