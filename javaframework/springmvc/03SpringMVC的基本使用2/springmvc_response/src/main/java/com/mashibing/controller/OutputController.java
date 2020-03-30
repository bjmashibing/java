package com.mashibing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.management.modelmbean.ModelMBeanOperationInfo;
import java.util.Map;

/**
 * 在向页面回显数据的时候，可以在方法的参数中显示的声明
 * Map:
 * ModelMap:
 * Model:
 * 都可以将数据进行回显：回显之后数据是保存在哪个作用域中的？
 * page:  当前页面
 * request： 当前请求
 * session： 当前会话
 * application：当前应用
 *      当使用上述参数传递数据的时候回把数据都放置到Request作用域
 *
 */
@Controller
@SessionAttributes(types = String.class)
public class OutputController {

    @RequestMapping("/output")
    public String output(Map<String,String> map){
        map.put("msg","hello,output");
        System.out.println(map.getClass());
        return "success";
    }


    @RequestMapping("/output2")
    public String output2(Model model){
        model.addAttribute("msg","hello,output2");
        System.out.println(model.getClass());

        return "success";
    }

    @RequestMapping("/output3")
    public String output3(ModelMap modelMap){
        modelMap.addAttribute("msg","hello,output3");
        System.out.println(modelMap.getClass());

        return "success";
    }

    @RequestMapping("/output4")
    public ModelAndView output4(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("success");
        mv.addObject("msg","output4");
        return mv;
    }

    /**
     * 当需要向session中设置数据的时候，可以在当前Controller上添加@SessionAttributes注解，
     * 此注解表示，每次在向request作用中设置属性值的时候，顺带着向session中保存一份
     * value和type都写上之后表示session中可以存储名字为value值的参数以及类型为Integer的参数
     * value和type可以分开单独使用，但是尽量不要使用type类型，因为会把所有符合类型的数据都设置到session中，导致session异常
     * @param model
     * @return
     */
    @RequestMapping("/testSession")
    public String testSession(Model model){
        model.addAttribute("username","zhangsan");
        return "success";
    }
}
