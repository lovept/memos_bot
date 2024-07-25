package com.github.lovept.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lovept
 * @date 2024/7/23 14:30
 * @description memos 实体类
 */
@Data
@TableName("memo")
public class Memo {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 唯一标识符
     */
    @TableField("uid")
    private String uid;

    /**
     * 创建者ID
     */
    @TableField("creator_id")
    private Integer creatorId;

    /**
     * 创建时间戳
     */
    @TableField("created_ts")
    private LocalDateTime createdTs;

    /**
     * 更新时间戳
     */
    @TableField("updated_ts")
    private LocalDateTime updatedTs;

    /**
     * 行状态
     */
    @TableField("row_status")
    private String rowStatus;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 可见性
     */
    @TableField("visibility")
    private String visibility;

    /**
     * 标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 负载
     */
    @TableField("payload")
    private String payload;

}
