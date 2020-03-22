package com.mashibing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 *
 * springmvc处理过程
 *  1、浏览器要发送一个请求 http://localhost:8080/springmvc_helloworld_war_exploded/hello
 *  2、首先交给tomcat容器
 *  3、在web.xml文件中配置了DispatcherServlet的类，所以此时会由当前的DispatcherServlet来接受请求
 *  4、接受到请求之后找到对应的Controller，去Controller中寻找@RequestMapping注解标识的方法
 *  5、找到匹配的方法之后，执行方法的逻辑
 *  6、处理完成之后需要返回一个前端页面的名称，
 *  7、有视图处理器来根据名称映射到对应的jsp页面的路径
 *  8、DispatcherServlet拿到对应的路径地址之后返回给浏览器
 *
 */
@Controller
@RequestMapping("/hello")
public class HelloController{

    /*
    * @RequestMapping表示用来匹配当前方法要处理的请求，其中/可以写也可以不写，一般推荐协商
    *
    * @RequestMapping可以添加在类上，也可以添加在方法上
    *      方法：http://localhost:8080/springmvc_helloworld_war_exploded/hello
    *       类：http://localhost:8080/springmvc_helloworld_war_exploded/hello/hello
    * 当添加在类上的时候表示给所有的当前类的方法钱添加一个访问路径
    *     什么时候需要在类上添加此注解？
    *       当包含多个Controller，通过在不同的Controller中包含同名的请求的时候，需要添加
    *
    * @RequestMapping配置的参数
    *   value:表示要匹配的请求
    *   method:表示请求的方式，post  get
    *   params:表示要求请求中必须要包含的参数
    *           必须要包含username的属性值
    *           @RequestMapping( value = "/hello",params = {"username"})
    *           不能包含的参数名称
                @RequestMapping( value = "/hello",params = {"!username"})
                必须要包含username,age两个属性值，并且username的值为zhangsan
                @RequestMapping( value = "/hello",params = {"username=zhangsan","age"})
     *  headers:表示限制请求头中的相关属性值，用来做请求的限制
     * produces：限制请求中的Content-Type
     * consumers：限制响应中的Content-Type
     *
     *
     * @RequestMapping可以进行模糊匹配
     *      ？：替代任意一个字符
     *      *：替代多个字符
     *      **：替代多层路径
     * 如果能匹配到多个请求，那么优先是精准匹配，其次是模糊匹配
    * */
    @RequestMapping( value = "/hello",headers = {"User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36"})
    public String hello(Map<String,String> map){
        map.put("hello","hello,Springmvc");
        return "hello";
    }

    @RequestMapping( value = "/hello*")
    public String hello2(Map<String,String> map){
        map.put("hello","hello,heihei");
        return "hello";
    }
}
