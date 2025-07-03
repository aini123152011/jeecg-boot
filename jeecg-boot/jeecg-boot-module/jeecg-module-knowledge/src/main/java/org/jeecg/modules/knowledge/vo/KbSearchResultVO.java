package org.jeecg.modules.knowledge.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 知识搜索结果VO
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "知识搜索结果")
public class KbSearchResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "知识标题（高亮）")
    private String titleHighlight;

    @Schema(description = "知识标题（原始）")
    private String title;

    @Schema(description = "知识摘要（高亮）")
    private String summaryHighlight;

    @Schema(description = "知识摘要（原始）")
    private String summary;

    @Schema(description = "内容片段（高亮）")
    private String contentHighlight;

    @Schema(description = "内容类型")
    private String contentType;

    @Schema(description = "分类ID")
    private String categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "关键词（高亮）")
    private String keywordsHighlight;

    @Schema(description = "关键词（原始）")
    private String keywords;

    @Schema(description = "来源类型")
    private String sourceType;

    @Schema(description = "来源URL")
    private String sourceUrl;

    @Schema(description = "附件ID列表")
    private String attachmentIds;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "收藏次数")
    private Integer collectCount;

    @Schema(description = "评论次数")
    private Integer commentCount;

    @Schema(description = "分享次数")
    private Integer shareCount;

    @Schema(description = "质量评分")
    private BigDecimal qualityScore;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @Schema(description = "是否公开")
    private Boolean isPublic;

    @Schema(description = "版本号")
    private Integer version;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建人姓名")
    private String createByName;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新人姓名")
    private String updateByName;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Schema(description = "匹配度评分")
    private Double matchScore;

    @Schema(description = "匹配的关键词列表")
    private List<String> matchedKeywords;

    @Schema(description = "标签列表")
    private List<String> tags;

    @Schema(description = "是否已点赞")
    private Boolean isLiked;

    @Schema(description = "是否已收藏")
    private Boolean isCollected;

    @Schema(description = "搜索片段列表")
    private List<String> searchSnippets;
}
