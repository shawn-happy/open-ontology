package com.prism.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 * 包含：id、created_at、updated_at、created_by、updated_by、is_deleted
 */
@Data
@Schema(description = "实体基类")
public abstract class BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    @TableField("id")
    @Schema(description = "主键 ID", example = "1")
    private Long id;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    @Schema(description = "创建人 ID")
    private Long createdBy;

    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新人 ID")
    private Long updatedBy;

    @TableField("is_deleted")
    @TableLogic
    @Schema(description = "逻辑删除（0=未删除，1=已删除）")
    private Integer deleted;
}
