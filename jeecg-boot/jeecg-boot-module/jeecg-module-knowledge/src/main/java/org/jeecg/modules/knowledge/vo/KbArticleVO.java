package org.jeecg.modules.knowledge.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 知识条目VO
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Data
@Schema(description = "知识条目视图对象")
public class KbArticleVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @Schema(description = "主键ID")
    private String id;

    /**知识标题*/
    @Schema(description = "知识标题")
    private String title;

    /**知识摘要*/
    @Schema(description = "知识摘要")
    private String summary;

    /**知识内容*/
    @Schema(description = "知识内容")
    private String content;

    /**内容类型*/
    @Schema(description = "内容类型(markdown,html,text)")
    private String contentType;

    /**分类ID*/
    @Schema(description = "分类ID")
    private String categoryId;

    /**分类名称*/
    @Schema(description = "分类名称")
    private String categoryName;

    /**关键词*/
    @Schema(description = "关键词")
    private String keywords;

    /**关键词列表*/
    @Schema(description = "关键词列表")
    private List<String> keywordList;

    /**来源类型*/
    @Schema(description = "来源类型(manual:手动创建,import:导入,url:网页抓取)")
    private String sourceType;

    /**来源URL*/
    @Schema(description = "来源URL")
    private String sourceUrl;

    /**附件ID列表*/
    @Schema(description = "附件ID列表(逗号分隔)")
    private String attachmentIds;

    /**附件信息列表*/
    @Schema(description = "附件信息列表")
    private List<Object> attachmentList;

    /**浏览次数*/
    @Schema(description = "浏览次数")
    private Integer viewCount;
    
    /**点赞次数*/
    @Schema(description = "点赞次数")
    private Integer likeCount;

    /**收藏次数*/
    @Schema(description = "收藏次数")
    private Integer collectCount;

    /**评论次数*/
    @Schema(description = "评论次数")
    private Integer commentCount;

    /**分享次数*/
    @Schema(description = "分享次数")
    private Integer shareCount;

    /**质量评分*/
    @Schema(description = "质量评分(0-5分)")
    private BigDecimal qualityScore;

    /**状态*/
    @Schema(description = "状态(draft:草稿,reviewing:审核中,published:已发布,archived:已归档)")
    private String status;

    /**状态文本*/
    @Schema(description = "状态文本")
    private String statusText;

    /**发布时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "发布时间")
    private Date publishTime;

    /**是否置顶*/
    @Schema(description = "是否置顶(0:否,1:是)")
    private Integer isTop;

    /**是否推荐*/
    @Schema(description = "是否推荐(0:否,1:是)")
    private Integer isRecommend;

    /**是否公开*/
    @Schema(description = "是否公开(0:私有,1:公开)")
    private Integer isPublic;

    /**版本号*/
    @Schema(description = "版本号")
    private Integer version;

    /**创建人*/
    @Schema(description = "创建人")
    private String createBy;

    /**创建人姓名*/
    @Schema(description = "创建人姓名")
    private String createByName;

    /**创建时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    /**更新人*/
    @Schema(description = "更新人")
    private String updateBy;

    /**更新人姓名*/
    @Schema(description = "更新人姓名")
    private String updateByName;

    /**更新时间*/
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**标签列表*/
    @Schema(description = "标签列表")
    private List<Object> tagList;

    /**是否已点赞*/
    @Schema(description = "是否已点赞")
    private Boolean isLiked;

    /**是否已收藏*/
    @Schema(description = "是否已收藏")
    private Boolean isCollected;

    /**阅读时长（分钟）*/
    @Schema(description = "阅读时长（分钟）")
    private Integer readingTime;
}
