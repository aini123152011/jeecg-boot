package org.jeecg.modules.knowledge.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 知识分类树形结构VO
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Data
@Schema(description = "知识分类树形结构")
public class KbCategoryTreeVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @Schema(description = "主键ID")
    private String id;

    /**父级ID*/
    @Schema(description = "父级ID")
    private String parentId;

    /**分类名称*/
    @Schema(description = "分类名称")
    private String categoryName;

    /**分类编码*/
    @Schema(description = "分类编码")
    private String categoryCode;

    /**分类描述*/
    @Schema(description = "分类描述")
    private String description;

    /**分类图标*/
    @Schema(description = "分类图标")
    private String icon;

    /**排序号*/
    @Schema(description = "排序号")
    private Integer sortNo;

    /**是否叶子节点*/
    @Schema(description = "是否叶子节点(0:否,1:是)")
    private Integer isLeaf;

    /**状态*/
    @Schema(description = "状态(0:禁用,1:启用)")
    private Integer status;

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

    /**子分类列表*/
    @Schema(description = "子分类列表")
    private List<KbCategoryTreeVO> children;

    /**知识条目数量*/
    @Schema(description = "知识条目数量")
    private Integer articleCount;

    /**是否有子分类*/
    @Schema(description = "是否有子分类")
    private Boolean hasChildren;

    /**树节点的key，用于前端树组件*/
    @Schema(description = "树节点key")
    private String key;

    /**树节点的title，用于前端树组件*/
    @Schema(description = "树节点title")
    private String title;

    /**树节点的value，用于前端树组件*/
    @Schema(description = "树节点value")
    private String value;
}
