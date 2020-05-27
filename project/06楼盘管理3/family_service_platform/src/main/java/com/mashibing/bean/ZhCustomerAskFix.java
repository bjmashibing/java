package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 业主请修
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class ZhCustomerAskFix implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房间编码
     */
    private String cellId;

    /**
     * 申请人
     */
    private String proposer;

    /**
     * 申请时间
     */
    private LocalDateTime proposerDate;

    /**
     * 请修内容
     */
    private String askFixContent;

    /**
     * 审批人
     */
    private String checkPerson;

    /**
     * 修理费用
     */
    private Double fixMoney;

    /**
     * 审批日期
     */
    private LocalDateTime checkDate;

    /**
     * 审批意见
     */
    private String checkAdvice;

    /**
     * 负责人电话
     */
    private String leaderPhone;

    /**
     * 施工单位
     */
    private String executeCompany;

    /**
     * 施工开始日期
     */
    private LocalDateTime executeStartDate;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 验收人
     */
    private String checkedPerson;

    /**
     * 施工结束日期
     */
    private LocalDateTime executeStopDate;

    /**
     * 验收日期
     */
    private LocalDateTime checkedDate;

    /**
     * 验收意见
     */
    private String checkedAdvice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 标识
     */
    private String identify;

    /**
     * 确认人
     */
    private String confirmPerson;

    /**
     * 确认时间
     */
    private LocalDateTime confirmDate;

    /**
     * 验收附件
     */
    private String checkedAttach;

    /**
     * 参考附件
     */
    private String referAttach;

    /**
     * 联系电话
     */
    private String phoneNumber;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public LocalDateTime getProposerDate() {
        return proposerDate;
    }

    public void setProposerDate(LocalDateTime proposerDate) {
        this.proposerDate = proposerDate;
    }

    public String getAskFixContent() {
        return askFixContent;
    }

    public void setAskFixContent(String askFixContent) {
        this.askFixContent = askFixContent;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public Double getFixMoney() {
        return fixMoney;
    }

    public void setFixMoney(Double fixMoney) {
        this.fixMoney = fixMoney;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckAdvice() {
        return checkAdvice;
    }

    public void setCheckAdvice(String checkAdvice) {
        this.checkAdvice = checkAdvice;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public String getExecuteCompany() {
        return executeCompany;
    }

    public void setExecuteCompany(String executeCompany) {
        this.executeCompany = executeCompany;
    }

    public LocalDateTime getExecuteStartDate() {
        return executeStartDate;
    }

    public void setExecuteStartDate(LocalDateTime executeStartDate) {
        this.executeStartDate = executeStartDate;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getCheckedPerson() {
        return checkedPerson;
    }

    public void setCheckedPerson(String checkedPerson) {
        this.checkedPerson = checkedPerson;
    }

    public LocalDateTime getExecuteStopDate() {
        return executeStopDate;
    }

    public void setExecuteStopDate(LocalDateTime executeStopDate) {
        this.executeStopDate = executeStopDate;
    }

    public LocalDateTime getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(LocalDateTime checkedDate) {
        this.checkedDate = checkedDate;
    }

    public String getCheckedAdvice() {
        return checkedAdvice;
    }

    public void setCheckedAdvice(String checkedAdvice) {
        this.checkedAdvice = checkedAdvice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getConfirmPerson() {
        return confirmPerson;
    }

    public void setConfirmPerson(String confirmPerson) {
        this.confirmPerson = confirmPerson;
    }

    public LocalDateTime getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(LocalDateTime confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getCheckedAttach() {
        return checkedAttach;
    }

    public void setCheckedAttach(String checkedAttach) {
        this.checkedAttach = checkedAttach;
    }

    public String getReferAttach() {
        return referAttach;
    }

    public void setReferAttach(String referAttach) {
        this.referAttach = referAttach;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "ZhCustomerAskFix{" +
        "id=" + id +
        ", cellId=" + cellId +
        ", proposer=" + proposer +
        ", proposerDate=" + proposerDate +
        ", askFixContent=" + askFixContent +
        ", checkPerson=" + checkPerson +
        ", fixMoney=" + fixMoney +
        ", checkDate=" + checkDate +
        ", checkAdvice=" + checkAdvice +
        ", leaderPhone=" + leaderPhone +
        ", executeCompany=" + executeCompany +
        ", executeStartDate=" + executeStartDate +
        ", leader=" + leader +
        ", checkedPerson=" + checkedPerson +
        ", executeStopDate=" + executeStopDate +
        ", checkedDate=" + checkedDate +
        ", checkedAdvice=" + checkedAdvice +
        ", remark=" + remark +
        ", status=" + status +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", identify=" + identify +
        ", confirmPerson=" + confirmPerson +
        ", confirmDate=" + confirmDate +
        ", checkedAttach=" + checkedAttach +
        ", referAttach=" + referAttach +
        ", phoneNumber=" + phoneNumber +
        "}";
    }
}
