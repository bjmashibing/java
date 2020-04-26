package com.mashibing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.bean.*;
import com.mashibing.mapper.*;
import com.mashibing.vo.CellMessage;
import com.mashibing.vo.UnitMessage;
import javafx.scene.control.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstateService {

    @Autowired
    private TblCompanyMapper tblCompanyMapper;
    @Autowired
    private FcEstateMapper fcEstateMapper;
    @Autowired
    private FcBuildingMapper fcBuildingMapper;
    @Autowired
    private FcUnitMapper fcUnitMapper;
    @Autowired
    private FcCellMapper fcCellMapper;

    public List<TblCompany> selectCompany(){
        List<TblCompany> companys = tblCompanyMapper.selectCompany();
        return companys;
    }

    /**
     * 再插入数据之前，最好对当前信息做判断，判断住宅编码是否存在，如果存在则不允许插入，如果不存在才允许插入
     * @param fcEstate
     * @return
     */
    public Integer insertEstate(FcEstate fcEstate){

        //定义查询包装类
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("estate_code", fcEstate.getEstateCode());
        FcEstate findResult = fcEstateMapper.selectOne(queryWrapper);
        //定义返回的结果
        int result = 0;
        if(findResult!=null){
            return result;
        }else{
            result = fcEstateMapper.insert(fcEstate);
            return result;
        }
    }

    /**
     * 先插入数据，再查询数据
     * @return
     */
    public List<FcBuilding> selectBuilding(Integer buildingNumber,String estateCode){
        List<FcBuilding> fcBuildings = new ArrayList<>();
        for(int i = 0 ;i<buildingNumber;i++){
            FcBuilding fcBuilding = new FcBuilding();
            fcBuilding.setBuildingCode("B"+(i+1));
            fcBuilding.setBuildingName("第"+(i+1)+"号楼");
            fcBuilding.setEstateCode(estateCode);
            fcBuildingMapper.insert(fcBuilding);
            fcBuildings.add(fcBuilding);
        }
        return fcBuildings;
    }

    /**
     * 完成楼宇的更新功能
     * @param fcBuilding
     * @return
     */
    public Integer updateBuilding(FcBuilding fcBuilding){
        int result = fcBuildingMapper.updateById(fcBuilding);
        return result;
    }

    public List<FcUnit> selectUnit(UnitMessage unitMessage){
        //定义返回值集合
        List<FcUnit> fcUnits = new ArrayList<>();
        for(int i = 0;i<unitMessage.getUnitCount();i++){
            FcUnit fcUnit = new FcUnit();
            fcUnit.setBuildingCode(unitMessage.getBuildingCode());
            fcUnit.setUnitCode("U" + (i+1));
            fcUnit.setUnitName("第"+(i+1)+"单元");
            fcUnitMapper.insert(fcUnit);
            fcUnits.add(fcUnit);
        }
        return fcUnits;
    }

    public Integer updateUnit(FcUnit fcUnit){
        int i = fcUnitMapper.updateById(fcUnit);
        return i;
    }

    public List<FcCell> insertCell(CellMessage[] cellMessages){
        List<FcCell> lists = new ArrayList<>();
        for (CellMessage cellMessage : cellMessages) {
            // 楼层
            for(int i = 1;i<=cellMessage.getStopFloor();i++){
                // 房间号
                for(int j = cellMessage.getStartCellId();j<=cellMessage.getStopCellId();j++){
                    FcCell fcCell = new FcCell();
                    fcCell.setUnitCode(cellMessage.getUnitCode());
                    fcCell.setCellName(i+"0"+j);
                    fcCell.setCellCode("C"+i+"0"+j);
                    fcCell.setFloorNumber(i);
                    fcCellMapper.insert(fcCell);
                    lists.add(fcCell);
                }
            }
        }
        return lists;
    }
}
