package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 功能模块
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblFunctionModel implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 模块编码
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模块名称
     */
    private String modelName;

    /**
     * 模块类型
     */
    private String modelType;

    /**
     * 上级模块编码
     */
    private Long modelParent;

    /**
     * 状态
     */
    private String modelStatus;

    /**
     * 文件路径
     */
    private String modelUrl;

    /**
     * 分析参考
     */
    private String modelAnalyseRef;

    /**
     * 报表分析
     */
    private Integer modelReportAnalyse;

    /**
     * 图标名称
     */
    private String modelIcon;

    /**
     * 模块性质
     */
    private String modelProperty;

    /**
     * 模块描述
     */
    private String modelDesc;

    /**
     * 是否控制操作权限
     */
    private String isControl;

    /**
     * 全部
     */
    private String mFull;

    /**
     * 新增
     */
    private String mAdd;

    /**
     * 修改
     */
    private String mMod;

    /**
     * 删除
     */
    private String mDel;

    /**
     * 导出
     */
    private String mExp;

    /**
     * 审批
     */
    private String mAud;

    /**
     * 执行
     */
    private String mExe;

    /**
     * 查询
     */
    private String mQue;

    /**
     * 个人
     */
    private String dPerson;

    /**
     * 部门
     */
    private String dDept;

    /**
     * 公司
     */
    private String dCompany;

    /**
     * 排序字段
     */
    private Double orderid;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public Long getModelParent() {
        return modelParent;
    }

    public void setModelParent(Long modelParent) {
        this.modelParent = modelParent;
    }

    public String getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(String modelStatus) {
        this.modelStatus = modelStatus;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public String getModelAnalyseRef() {
        return modelAnalyseRef;
    }

    public void setModelAnalyseRef(String modelAnalyseRef) {
        this.modelAnalyseRef = modelAnalyseRef;
    }

    public Integer getModelReportAnalyse() {
        return modelReportAnalyse;
    }

    public void setModelReportAnalyse(Integer modelReportAnalyse) {
        this.modelReportAnalyse = modelReportAnalyse;
    }

    public String getModelIcon() {
        return modelIcon;
    }

    public void setModelIcon(String modelIcon) {
        this.modelIcon = modelIcon;
    }

    public String getModelProperty() {
        return modelProperty;
    }

    public void setModelProperty(String modelProperty) {
        this.modelProperty = modelProperty;
    }

    public String getModelDesc() {
        return modelDesc;
    }

    public void setModelDesc(String modelDesc) {
        this.modelDesc = modelDesc;
    }

    public String getIsControl() {
        return isControl;
    }

    public void setIsControl(String isControl) {
        this.isControl = isControl;
    }

    public String getmFull() {
        return mFull;
    }

    public void setmFull(String mFull) {
        this.mFull = mFull;
    }

    public String getmAdd() {
        return mAdd;
    }

    public void setmAdd(String mAdd) {
        this.mAdd = mAdd;
    }

    public String getmMod() {
        return mMod;
    }

    public void setmMod(String mMod) {
        this.mMod = mMod;
    }

    public String getmDel() {
        return mDel;
    }

    public void setmDel(String mDel) {
        this.mDel = mDel;
    }

    public String getmExp() {
        return mExp;
    }

    public void setmExp(String mExp) {
        this.mExp = mExp;
    }

    public String getmAud() {
        return mAud;
    }

    public void setmAud(String mAud) {
        this.mAud = mAud;
    }

    public String getmExe() {
        return mExe;
    }

    public void setmExe(String mExe) {
        this.mExe = mExe;
    }

    public String getmQue() {
        return mQue;
    }

    public void setmQue(String mQue) {
        this.mQue = mQue;
    }

    public String getdPerson() {
        return dPerson;
    }

    public void setdPerson(String dPerson) {
        this.dPerson = dPerson;
    }

    public String getdDept() {
        return dDept;
    }

    public void setdDept(String dDept) {
        this.dDept = dDept;
    }

    public String getdCompany() {
        return dCompany;
    }

    public void setdCompany(String dCompany) {
        this.dCompany = dCompany;
    }

    public Double getOrderid() {
        return orderid;
    }

    public void setOrderid(Double orderid) {
        this.orderid = orderid;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "TblFunctionModel{" +
        "id=" + id +
        ", modelName=" + modelName +
        ", modelType=" + modelType +
        ", modelParent=" + modelParent +
        ", modelStatus=" + modelStatus +
        ", modelUrl=" + modelUrl +
        ", modelAnalyseRef=" + modelAnalyseRef +
        ", modelReportAnalyse=" + modelReportAnalyse +
        ", modelIcon=" + modelIcon +
        ", modelProperty=" + modelProperty +
        ", modelDesc=" + modelDesc +
        ", isControl=" + isControl +
        ", mFull=" + mFull +
        ", mAdd=" + mAdd +
        ", mMod=" + mMod +
        ", mDel=" + mDel +
        ", mExp=" + mExp +
        ", mAud=" + mAud +
        ", mExe=" + mExe +
        ", mQue=" + mQue +
        ", dPerson=" + dPerson +
        ", dDept=" + dDept +
        ", dCompany=" + dCompany +
        ", orderid=" + orderid +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        "}";
    }
}
