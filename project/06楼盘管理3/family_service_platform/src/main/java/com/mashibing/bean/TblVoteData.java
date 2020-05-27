package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 投票数据表
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblVoteData implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 投票编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 投票项目编号
     */
    private Integer voteProjectId;

    /**
     * 投票用户编码
     */
    private String voteUserId;

    /**
     * 投票用户名称
     */
    private String voteUserName;

    /**
     * 投票时间
     */
    private LocalDateTime voteDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVoteProjectId() {
        return voteProjectId;
    }

    public void setVoteProjectId(Integer voteProjectId) {
        this.voteProjectId = voteProjectId;
    }

    public String getVoteUserId() {
        return voteUserId;
    }

    public void setVoteUserId(String voteUserId) {
        this.voteUserId = voteUserId;
    }

    public String getVoteUserName() {
        return voteUserName;
    }

    public void setVoteUserName(String voteUserName) {
        this.voteUserName = voteUserName;
    }

    public LocalDateTime getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDateTime voteDate) {
        this.voteDate = voteDate;
    }

    @Override
    public String toString() {
        return "TblVoteData{" +
        "id=" + id +
        ", voteProjectId=" + voteProjectId +
        ", voteUserId=" + voteUserId +
        ", voteUserName=" + voteUserName +
        ", voteDate=" + voteDate +
        "}";
    }
}
