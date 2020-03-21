package com.mashibing.controller;

import com.mashibing.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 如果使用默认的类型转换器，那么在输入日期的时候必须要使用/作为分隔，如果需要自定义实现该如何操作？
 *
 *  如果需要添加日期格式化器的话，只需要在实体类上添加@DataTimeFormat("格式")即可，
 *  但是需要注意的是，如果在配置日期格式化器的时候，通过配置了类型转换器，那么此时就会失效需要使用FormattingConversionServiceFactoryBean对象
 *
 */
@Controller
public class DateConverterController {

    @RequestMapping("/testDateConverter")
    public String testDateConverter(User user){
        System.out.println(user);
        return "success";
    }
}
