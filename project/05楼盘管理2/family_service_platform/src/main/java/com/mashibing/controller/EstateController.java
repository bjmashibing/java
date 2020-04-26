package com.mashibing.controller;

import com.alibaba.fastjson.JSONObject;
import com.mashibing.bean.*;
import com.mashibing.returnJson.ReturnObject;
import com.mashibing.service.EstateService;
import com.mashibing.vo.CellMessage;
import com.mashibing.vo.UnitMessage;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*",methods = {},allowCredentials = "true")
public class EstateController {

    @Autowired
    private EstateService estateService;

    @RequestMapping("/estate/selectCompany")
    public String selectCompany(){
        System.out.println("selectCompany");
        List<TblCompany> companies = estateService.selectCompany();
        return JSONObject.toJSONString(new ReturnObject(companies));
    }

    @RequestMapping("/estate/insertEstate")
    public String insertEstate(FcEstate fcEstate){
        System.out.println(fcEstate);
        System.out.println("insert estate");
        Integer result = estateService.insertEstate(fcEstate);
        if (result == 0){
            return JSONObject.toJSONString(new ReturnObject("0","房产编码已经存在"));
        }else{
            return JSONObject.toJSONString(new ReturnObject("1","插入房产成功"));
        }
    }

    /**
     * 此处应该完成的是楼宇的查询功能，但是大家会发现，现在数据表中没有任何楼宇的数据，
     * 因此再辨析的时候需要进行插入且返回插入的数据
     * @param buildingNumber
     * @param estateCode
     * @return
     */
    @RequestMapping("/estate/selectBuilding")
    public String selectBuilding(Integer buildingNumber,String estateCode){
        System.out.println("select building");
        List<FcBuilding> fcBuildings = estateService.selectBuilding(buildingNumber, estateCode);
        System.out.println(fcBuildings);
        return JSONObject.toJSONString(new ReturnObject(fcBuildings));
    }

    @RequestMapping("/estate/updateBuilding")
    public String updateBuilding(FcBuilding fcBuilding){
        System.out.println("update building");
        Integer result = estateService.updateBuilding(fcBuilding);
        if(result == 1){
            return JSONObject.toJSONString(new ReturnObject("更新楼宇成功"));
        }else{
            return JSONObject.toJSONString(new ReturnObject("更新楼宇失败"));
        }
    }

    @RequestMapping("/estate/selectUnit")
    public String selectUnit(@RequestBody UnitMessage[] unitMessages){
        System.out.println("estate selectUnit");
        List<FcUnit> allUnit = new ArrayList<>();
        for (UnitMessage unitMessage : unitMessages) {
            allUnit.addAll(estateService.selectUnit(unitMessage));
        }
        return  JSONObject.toJSONString(new ReturnObject(allUnit));
    }

    @RequestMapping("/estate/updateUnit")
    public String updateUnit(FcUnit fcUnit){
        Integer result = estateService.updateUnit(fcUnit);
        if(result ==1 ){
            return JSONObject.toJSONString(new ReturnObject("更新单元成功"));
        }else{
            return JSONObject.toJSONString(new ReturnObject("更新单元失败"));
        }
    }

    @RequestMapping("/estate/insertCell")
    public String insertCell(@RequestBody CellMessage[] cellMessages){
        System.out.println("insert cell");
        List<FcCell> fcCells = estateService.insertCell(cellMessages);
        return JSONObject.toJSONString(new ReturnObject(fcCells));
    }
}
