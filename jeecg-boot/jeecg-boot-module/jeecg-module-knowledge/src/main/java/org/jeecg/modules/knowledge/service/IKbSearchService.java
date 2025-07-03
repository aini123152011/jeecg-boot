package org.jeecg.modules.knowledge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.knowledge.vo.KbArticleVO;
import org.jeecg.modules.knowledge.vo.KbSearchResultVO;

import java.util.List;
import java.util.Map;

/**
 * 知识库搜索服务接口
 * 
 * @author seadragon
 * @since 2025-07-03
 */
public interface IKbSearchService {

    /**
     * 全文搜索知识条目（带高亮）
     *
     * @param page 分页参数
     * @param keyword 搜索关键词
     * @param categoryId 分类ID
     * @param contentType 内容类型
     * @param sourceType 来源类型
     * @return 搜索结果
     */
    IPage<KbSearchResultVO> searchWithHighlight(Page<KbSearchResultVO> page, String keyword, 
                                               String categoryId, String contentType, String sourceType);

    /**
     * 精确搜索知识条目
     *
     * @param page 分页参数
     * @param keyword 搜索关键词
     * @param categoryId 分类ID
     * @param exactMatch 是否精确匹配
     * @return 搜索结果
     */
    IPage<KbArticleVO> exactSearch(Page<KbArticleVO> page, String keyword, String categoryId, boolean exactMatch);

    /**
     * 根据标签搜索知识条目
     *
     * @param page 分页参数
     * @param tagNames 标签名称列表
     * @param categoryId 分类ID
     * @return 搜索结果
     */
    IPage<KbArticleVO> searchByTags(Page<KbArticleVO> page, List<String> tagNames, String categoryId);

    /**
     * 搜索建议（自动补全）
     *
     * @param keyword 关键词前缀
     * @param limit 返回数量限制
     * @return 建议列表
     */
    List<String> searchSuggestions(String keyword, Integer limit);

    /**
     * 热门搜索关键词
     *
     * @param limit 返回数量限制
     * @return 热门关键词列表
     */
    List<Map<String, Object>> getHotSearchKeywords(Integer limit);

    /**
     * 相关知识推荐
     *
     * @param articleId 当前知识条目ID
     * @param limit 推荐数量
     * @return 相关知识列表
     */
    List<KbArticleVO> getRelatedArticles(String articleId, Integer limit);

    /**
     * 高级搜索
     *
     * @param page 分页参数
     * @param title 标题关键词
     * @param content 内容关键词
     * @param keywords 关键词
     * @param categoryId 分类ID
     * @param createBy 创建人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param status 状态
     * @return 搜索结果
     */
    IPage<KbArticleVO> advancedSearch(Page<KbArticleVO> page, String title, String content, 
                                     String keywords, String categoryId, String createBy, 
                                     String startTime, String endTime, String status);

    /**
     * 记录搜索历史
     *
     * @param keyword 搜索关键词
     * @param userId 用户ID
     * @param resultCount 搜索结果数量
     */
    void recordSearchHistory(String keyword, String userId, Integer resultCount);

    /**
     * 获取用户搜索历史
     *
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 搜索历史列表
     */
    List<Map<String, Object>> getUserSearchHistory(String userId, Integer limit);

    /**
     * 清除用户搜索历史
     *
     * @param userId 用户ID
     */
    void clearUserSearchHistory(String userId);

    /**
     * 搜索统计分析
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> getSearchStatistics(String startTime, String endTime);
}
