package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 群组档案
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblGroupRecord implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动增长id
     */
    private Integer groupRecordId;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 群组类型
     */
    private String groupType;

    /**
     * 群组说明
     */
    private String groupDesc;

    /**
     * 组内成员id
     */
    private String groupMemberId;

    /**
     * 组内成员名称
     */
    private String groupMemberName;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


    public Integer getGroupRecordId() {
        return groupRecordId;
    }

    public void setGroupRecordId(Integer groupRecordId) {
        this.groupRecordId = groupRecordId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(String groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    public String getGroupMemberName() {
        return groupMemberName;
    }

    public void setGroupMemberName(String groupMemberName) {
        this.groupMemberName = groupMemberName;
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
        return "TblGroupRecord{" +
        "groupRecordId=" + groupRecordId +
        ", groupName=" + groupName +
        ", groupType=" + groupType +
        ", groupDesc=" + groupDesc +
        ", groupMemberId=" + groupMemberId +
        ", groupMemberName=" + groupMemberName +
        ", createPerson=" + createPerson +
        ", createDate=" + createDate +
        "}";
    }
}
