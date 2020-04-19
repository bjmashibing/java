package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 规章制度
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblRule implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动增长id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 适用范围
     */
    private String useRange;

    /**
     * 分类
     */
    private Long category;

    /**
     * 文号
     */
    private String articleNumber;

    /**
     * 制度等级
     */
    private String level;

    /**
     * 保密等级
     */
    private String secretLevel;

    /**
     * 主题词
     */
    private String titleWord;

    /**
     * 发文单位
     */
    private String publishCompany;

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 附件路径
     */
    private String attachPath;

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
     * 审批人
     */
    private String checkPerson;

    /**
     * 审批时间
     */
    private LocalDateTime checkDate;

    /**
     * 审批意见
     */
    private String checkAdvice;

    /**
     * 允许查看的用户编码
     */
    private String allowUserCode;

    /**
     * 允许查看的用户名称
     */
    private String allowUserName;

    /**
     * 规章制度附件
     */
    private String ruleAttach;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUseRange() {
        return useRange;
    }

    public void setUseRange(String useRange) {
        this.useRange = useRange;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    public String getTitleWord() {
        return titleWord;
    }

    public void setTitleWord(String titleWord) {
        this.titleWord = titleWord;
    }

    public String getPublishCompany() {
        return publishCompany;
    }

    public void setPublishCompany(String publishCompany) {
        this.publishCompany = publishCompany;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
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

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
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

    public String getAllowUserCode() {
        return allowUserCode;
    }

    public void setAllowUserCode(String allowUserCode) {
        this.allowUserCode = allowUserCode;
    }

    public String getAllowUserName() {
        return allowUserName;
    }

    public void setAllowUserName(String allowUserName) {
        this.allowUserName = allowUserName;
    }

    public String getRuleAttach() {
        return ruleAttach;
    }

    public void setRuleAttach(String ruleAttach) {
        this.ruleAttach = ruleAttach;
    }

    @Override
    public String toString() {
        return "TblRule{" +
        "id=" + id +
        ", title=" + title +
        ", content=" + content +
        ", useRange=" + useRange +
        ", category=" + category +
        ", articleNumber=" + articleNumber +
        ", level=" + level +
        ", secretLevel=" + secretLevel +
        ", titleWord=" + titleWord +
        ", publishCompany=" + publishCompany +
        ", attachName=" + attachName +
        ", attachPath=" + attachPath +
        ", status=" + status +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        ", checkPerson=" + checkPerson +
        ", checkDate=" + checkDate +
        ", checkAdvice=" + checkAdvice +
        ", allowUserCode=" + allowUserCode +
        ", allowUserName=" + allowUserName +
        ", ruleAttach=" + ruleAttach +
        "}";
    }
}
