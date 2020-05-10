package com.mashibing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 分组待办事项
 * </p>
 *
 * @author lian
 * @since 2020-04-18
 */
public class TblGroupsTodo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 分组id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 待办事项id
     */
    private String todoId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    @Override
    public String toString() {
        return "TblGroupsTodo{" +
        "id=" + id +
        ", todoId=" + todoId +
        "}";
    }
}
