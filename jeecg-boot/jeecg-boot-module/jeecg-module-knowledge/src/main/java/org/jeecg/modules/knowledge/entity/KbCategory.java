package org.jeecg.modules.knowledge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 知识分类实体类
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Data
@TableName("kb_category")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "知识分类表")
public class KbCategory implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**父级ID*/
    @Excel(name = "父级ID", width = 15)
    @Schema(description = "父级ID")
    private String parentId;

    /**分类名称*/
    @Excel(name = "分类名称", width = 15)
    @Schema(description = "分类名称")
    private String categoryName;

    /**分类编码*/
    @Excel(name = "分类编码", width = 15)
    @Schema(description = "分类编码")
    private String categoryCode;

    /**分类描述*/
    @Excel(name = "分类描述", width = 15)
    @Schema(description = "分类描述")
    private String description;

    /**分类图标*/
    @Excel(name = "分类图标", width = 15)
    @Schema(description = "分类图标")
    private String icon;

    /**排序号*/
    @Excel(name = "排序号", width = 15)
    @Schema(description = "排序号")
    private Integer sortNo;
    
    /**是否叶子节点*/
    @Excel(name = "是否叶子节点", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @Schema(description = "是否叶子节点(0:否,1:是)")
    private Integer isLeaf;

    /**状态*/
    @Excel(name = "状态", width = 15, dicCode = "valid_status")
    @Dict(dicCode = "valid_status")
    @Schema(description = "状态(0:禁用,1:启用)")
    private Integer status;

    /**删除状态*/
    @Excel(name = "删除状态", width = 15)
    @Schema(description = "删除状态(0:正常,1:已删除)")
    @TableLogic
    private Integer delFlag;

    /**创建人*/
    @Schema(description = "创建人")
    private String createBy;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    /**更新人*/
    @Schema(description = "更新人")
    private String updateBy;

    /**更新时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**所属部门*/
    @Schema(description = "所属部门")
    private String sysOrgCode;

    /**租户ID*/
    @Schema(description = "租户ID")
    private String tenantId;
}
