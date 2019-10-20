package com.mashibing;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author: 马士兵教育
 * @create: 2019-09-01 16:20
 */
/*
 * 异常:
 *       在程序运行过程中，出现的不正常情况叫做异常
 *
 *       注意：
 *           1、相同的代码在运行的时候，根据输入的参数或者操作的不同，有可能会发生异常，有可能不会发生异常
 *               应该在写代码的过程中尽可能的保证代码的正确性，不要到处都bug
 *           2、如果要解决代码中出现的异常，需要添加非常复杂的代码逻辑来进行判断，会使代码变得非常臃肿，不利于维护，可读性比较差
 *               因此，推荐大家使用异常机制来处理程序运行过程中出现的问题
 *           3、程序在运行过程中如果出现了问题，会导致后面的代码无法正常执行，而使用异常机制之后，可以对异常情况进行处理
 *              同时后续的代码会继续执行，不会中断整个程序
 *           4、在异常的处理过程中，不要只是简单的输出错误，要尽可能的讲详细的异常信息进行输出
 *                  e.printStackTrace():打印异常的堆栈信息，可以从异常信息的最后一行开始追踪，寻找自己编写的java类
 *
 *      异常处理的方式：
 *          1、捕获异常
 *              try{代码逻辑}catch(Exception e){异常处理逻辑}
 *              try{代码逻辑}catch(具体的异常Exception e){异常处理逻辑}catch(具体的异常)：
 *                    可以针对每一种具体的异常做相应的更丰富的处理
 *                      注意：当使用多重的catch的时候一定要注意相关异常的顺序，将子类放在最前面的catch，父类放在后面的catch
 *              执行过程中可能存在的情况：
 *                  1、正常执行，只执行try中的代码
 *                  2、遇到异常情况，会处理try中异常代码之前的逻辑，后面的逻辑不会执行，最后会执行catch中的代码
 *                  3、使用多重catch的时候，会遇到异常子类不匹配的情况，此时依然会报错，因此建议在catch的最后将所有的异常的父类写上
 *InputMismatchException
 * ArithmeticException
 * */
public class TestException {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            System.out.print("请输入被除数:");
            int num1 = in.nextInt();
            System.out.print("请输入除数:");
            int num2 = in.nextInt();
            System.out.println(String.format("%d / %d = %d",
                    num1, num2, num1 / num2));
            System.out.println("前面没有出现异常");
        /*}catch(Exception e){
//            System.out.println("出现异常");
            e.printStackTrace();
//            System.out.println("--------");
            System.out.println(e.getMessage());
        }*/
       }catch(ArithmeticException e){
            System.out.println("数学异常，除数不能是0");
            e.printStackTrace();
        }catch (InputMismatchException e){
            System.out.println("输入的参数值类型不匹配");
            e.printStackTrace();
        }catch (NullPointerException e){
            System.out.println("空指针异常");
            e.printStackTrace();
        }
        System.out.println("感谢使用本程序！");

    }
}
