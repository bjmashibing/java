package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 个人设置
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblMyset implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户编码
     */
    private String username;

    /**
     * 是否需要提醒
     */
    private String idRemain;

    /**
     * 提醒间隔时间
     */
    private String remainInterval;

    /**
     * 弹出提醒窗口
     */
    private String remainWindowOpen;

    /**
     * 消息提醒
     */
    private String messageRemain;

    /**
     * 默认主页面
     */
    private String defaultMain;

    /**
     * 邮箱全称
     */
    private String emailAll;

    /**
     * smtp地址
     */
    private String smtpAddr;

    /**
     * 登录用户
     */
    private String loginUser;

    /**
     * 登录密码
     */
    private String loginPwd;

    /**
     * 邮件端口
     */
    private String mailPort;

    /**
     * 发送人名称
     */
    private String sendPerson;

    /**
     * 分页行数
     */
    private Integer pageCount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdRemain() {
        return idRemain;
    }

    public void setIdRemain(String idRemain) {
        this.idRemain = idRemain;
    }

    public String getRemainInterval() {
        return remainInterval;
    }

    public void setRemainInterval(String remainInterval) {
        this.remainInterval = remainInterval;
    }

    public String getRemainWindowOpen() {
        return remainWindowOpen;
    }

    public void setRemainWindowOpen(String remainWindowOpen) {
        this.remainWindowOpen = remainWindowOpen;
    }

    public String getMessageRemain() {
        return messageRemain;
    }

    public void setMessageRemain(String messageRemain) {
        this.messageRemain = messageRemain;
    }

    public String getDefaultMain() {
        return defaultMain;
    }

    public void setDefaultMain(String defaultMain) {
        this.defaultMain = defaultMain;
    }

    public String getEmailAll() {
        return emailAll;
    }

    public void setEmailAll(String emailAll) {
        this.emailAll = emailAll;
    }

    public String getSmtpAddr() {
        return smtpAddr;
    }

    public void setSmtpAddr(String smtpAddr) {
        this.smtpAddr = smtpAddr;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getMailPort() {
        return mailPort;
    }

    public void setMailPort(String mailPort) {
        this.mailPort = mailPort;
    }

    public String getSendPerson() {
        return sendPerson;
    }

    public void setSendPerson(String sendPerson) {
        this.sendPerson = sendPerson;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "TblMyset{" +
        "id=" + id +
        ", username=" + username +
        ", idRemain=" + idRemain +
        ", remainInterval=" + remainInterval +
        ", remainWindowOpen=" + remainWindowOpen +
        ", messageRemain=" + messageRemain +
        ", defaultMain=" + defaultMain +
        ", emailAll=" + emailAll +
        ", smtpAddr=" + smtpAddr +
        ", loginUser=" + loginUser +
        ", loginPwd=" + loginPwd +
        ", mailPort=" + mailPort +
        ", sendPerson=" + sendPerson +
        ", pageCount=" + pageCount +
        "}";
    }
}
