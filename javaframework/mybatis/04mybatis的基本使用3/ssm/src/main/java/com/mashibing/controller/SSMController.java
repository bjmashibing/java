package com.mashibing.controller;

import com.mashibing.bean.Emp;
import com.mashibing.dao.EmpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SSMController {

    @Autowired
    EmpDao empDao;

    @RequestMapping("/test")
    public String test(Model model){
        System.out.println("test");
        Emp emp = empDao.selectEmpByEmpno(7369);
        System.out.println(emp);
        model.addAttribute("emp",emp.getEname());
        return "success";
    }
}
