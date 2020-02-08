package com.mashibing;

import com.mashibing.entity.Address;
import com.mashibing.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * EL表达式
 *      概念;
 *          Experssion Language,一种表达式语言，语法简单
 *      作用
 *          方便jsp页面获取作用域中的属性
 *      语法规则：
 *          ${expression},可以使用.或者[]来获取属性值或者指定索引位置的对象
 *          获取值的时候，直接使用作用域中的key即可，然后使用.来引用属性，使用[]来获取指定索引位置的对象
 *      作用域：
 *          pageContext--->request--->session--->application
 *      获取作用域数据的顺序：
 *          从小的作用域开始查询，如果找到则返回对应的值，不接着向大范围寻找数据
 *          当四种作用域中存在相同key的属性的时候，可以通过pageScope，requestScope，sessionScope，applicationScope获取指定作用域的数据
 *      EL表达式可以进行算术运算和关系运算
 *          直接在表达式中写入算法操作即可，如果是关系运算，返回true或者false
 *          注意：在el表达式中的+表示加法操作而不是字符串连接符
 *      EL表达式可以进行逻辑运算
 *          ${true&&false}<br>
 *          ${true&&true}<br>
 *          ${true||false}<br>
 *          ${true||true}<br>
 *      EL表达式获取header信息
 *          ${header}:获取所有请求头信息
 *          ${header[key]}:获取指定key的数据
 *          ${headerValues[key]}:获取key对应的一组数据，返回类型是数组
 *          ${headervalues[key][0]}:获取key对应数组的某一个值
 *      EL表达式获取cookie数据：
 *          ${cookie}:获取cookie中的所有数据
 *          ${cookie.key}：获取cookie中指定key的数据
 *          ${cookie.key.name}：获取cookie指定key数据的name
 *          ${cookie.key.value}：获取cookie指定key数据的value
 *
 *
 */
public class ELServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置请求响应的编码格式
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //从请求中获取数据
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        System.out.println(name);
        System.out.println(pwd);
        //给request对象单独设置属性
        request.setAttribute("aa","aaaaa");
        //给request添加对象
        User user = new User(1,"张三",new Address("北京市","北京市","海淀区"));
        User user2 = new User(2,"张三",new Address("河北省","邯郸市","武安"));

        ArrayList<User> list  =new ArrayList<>();
        list.add(user);
        list.add(user2);
        request.setAttribute("user",user);

        //给request对象设置集合
        request.setAttribute("list",list);

        //给reqeust对象设置map对象
        HashMap<String,String> map = new HashMap<>();
        map.put("china","beijing");
        map.put("hebei","shijiazhuang");

        request.setAttribute("map",map);

        HashMap<String,User> map2 = new HashMap<>();
        map2.put("a1",user);
        map2.put("a2",user2);

        request.setAttribute("map2",map2);

        //通过请求转发的方式跳转到某一个jsp页面
        request.getRequestDispatcher("el.jsp").forward(request,response);
    }
}
