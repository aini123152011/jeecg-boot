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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 知识条目实体类
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Data
@TableName("kb_article")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "知识条目表")
public class KbArticle implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**知识标题*/
    @Excel(name = "知识标题", width = 20)
    @Schema(description = "知识标题")
    private String title;

    /**知识摘要*/
    @Excel(name = "知识摘要", width = 30)
    @Schema(description = "知识摘要")
    private String summary;

    /**知识内容*/
    @Schema(description = "知识内容")
    private String content;

    /**内容类型*/
    @Excel(name = "内容类型", width = 15, dicCode = "kb_content_type")
    @Dict(dicCode = "kb_content_type")
    @Schema(description = "内容类型(markdown,html,text)")
    private String contentType;

    /**分类ID*/
    @Excel(name = "分类ID", width = 15)
    @Schema(description = "分类ID")
    private String categoryId;

    /**关键词*/
    @Excel(name = "关键词", width = 20)
    @Schema(description = "关键词")
    private String keywords;

    /**来源类型*/
    @Excel(name = "来源类型", width = 15, dicCode = "kb_source_type")
    @Dict(dicCode = "kb_source_type")
    @Schema(description = "来源类型(manual:手动创建,import:导入,url:网页抓取)")
    private String sourceType;

    /**来源URL*/
    @Excel(name = "来源URL", width = 30)
    @Schema(description = "来源URL")
    private String sourceUrl;

    /**附件ID列表*/
    @Excel(name = "附件ID列表", width = 20)
    @Schema(description = "附件ID列表(逗号分隔)")
    private String attachmentIds;

    /**浏览次数*/
    @Excel(name = "浏览次数", width = 15)
    @Schema(description = "浏览次数")
    private Integer viewCount;
    
    /**点赞次数*/
    @Excel(name = "点赞次数", width = 15)
    @Schema(description = "点赞次数")
    private Integer likeCount;

    /**收藏次数*/
    @Excel(name = "收藏次数", width = 15)
    @Schema(description = "收藏次数")
    private Integer collectCount;

    /**评论次数*/
    @Excel(name = "评论次数", width = 15)
    @Schema(description = "评论次数")
    private Integer commentCount;

    /**分享次数*/
    @Excel(name = "分享次数", width = 15)
    @Schema(description = "分享次数")
    private Integer shareCount;

    /**质量评分*/
    @Excel(name = "质量评分", width = 15)
    @Schema(description = "质量评分(0-5分)")
    private BigDecimal qualityScore;

    /**状态*/
    @Excel(name = "状态", width = 15, dicCode = "kb_article_status")
    @Dict(dicCode = "kb_article_status")
    @Schema(description = "状态(draft:草稿,reviewing:审核中,published:已发布,archived:已归档)")
    private String status;

    /**发布时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发布时间")
    private Date publishTime;

    /**是否置顶*/
    @Excel(name = "是否置顶", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @Schema(description = "是否置顶(0:否,1:是)")
    private Integer isTop;

    /**是否推荐*/
    @Excel(name = "是否推荐", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @Schema(description = "是否推荐(0:否,1:是)")
    private Integer isRecommend;

    /**是否公开*/
    @Excel(name = "是否公开", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @Schema(description = "是否公开(0:私有,1:公开)")
    private Integer isPublic;

    /**版本号*/
    @Excel(name = "版本号", width = 15)
    @Schema(description = "版本号")
    private Integer version;

    /**删除状态*/
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
