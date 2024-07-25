package com.github.lovept.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author lovept
 * @date 2024/7/23 14:30
 * @description memos resource 表实体
 */
@Data
@TableName("resource")
public class Resource implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("uid")
    private String uid;

    @TableField("creator_id")
    private Integer creatorId;

    @TableField("created_ts")
    private Timestamp createdTs;

    @TableField("updated_ts")
    private Timestamp updatedTs;

    @TableField("filename")
    private String filename;

    @TableField("`blob`")
    private byte[] blob;

    @TableField("type")
    private String type;

    @TableField("size")
    private Integer size;

    @TableField("memo_id")
    private Integer memoId;

    @TableField("storage_type")
    private String storageType;

    @TableField("reference")
    private String reference;

    @TableField("payload")
    private String payload;
}
