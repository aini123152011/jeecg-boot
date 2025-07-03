package org.jeecg.modules.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.knowledge.entity.KbArticle;
import org.jeecg.modules.knowledge.vo.KbArticleVO;

import java.util.List;
import java.util.Map;

/**
 * 知识条目Mapper接口
 * 
 * @author seadragon
 * @since 2025-07-03
 */
public interface KbArticleMapper extends BaseMapper<KbArticle> {

    /**
     * 分页查询知识条目（包含分类名称）
     * 
     * @param page 分页对象
     * @param categoryId 分类ID
     * @param title 标题关键词
     * @param status 状态
     * @return 知识条目分页列表
     */
    IPage<KbArticleVO> selectArticlePageWithCategory(Page<KbArticleVO> page, 
                                                    @Param("categoryId") String categoryId,
                                                    @Param("title") String title,
                                                    @Param("status") String status);

    /**
     * 根据分类ID查询知识条目列表
     * 
     * @param categoryId 分类ID
     * @return 知识条目列表
     */
    @Select("SELECT * FROM kb_article WHERE category_id = #{categoryId} AND del_flag = 0 ORDER BY is_top DESC, publish_time DESC")
    List<KbArticle> selectByCategoryId(@Param("categoryId") String categoryId);

    /**
     * 查询推荐知识条目
     * 
     * @param limit 限制数量
     * @return 推荐知识条目列表
     */
    @Select("SELECT * FROM kb_article WHERE is_recommend = 1 AND status = 'published' AND del_flag = 0 ORDER BY quality_score DESC, view_count DESC LIMIT #{limit}")
    List<KbArticle> selectRecommendArticles(@Param("limit") Integer limit);

    /**
     * 查询热门知识条目
     * 
     * @param limit 限制数量
     * @return 热门知识条目列表
     */
    @Select("SELECT * FROM kb_article WHERE status = 'published' AND del_flag = 0 ORDER BY view_count DESC, like_count DESC LIMIT #{limit}")
    List<KbArticle> selectHotArticles(@Param("limit") Integer limit);

    /**
     * 查询最新知识条目
     * 
     * @param limit 限制数量
     * @return 最新知识条目列表
     */
    @Select("SELECT * FROM kb_article WHERE status = 'published' AND del_flag = 0 ORDER BY publish_time DESC LIMIT #{limit}")
    List<KbArticle> selectLatestArticles(@Param("limit") Integer limit);

    /**
     * 全文搜索知识条目
     * 
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @param categoryId 分类ID
     * @return 搜索结果
     */
    IPage<KbArticleVO> searchArticles(Page<KbArticleVO> page, 
                                     @Param("keyword") String keyword,
                                     @Param("categoryId") String categoryId);

    /**
     * 增加浏览次数
     * 
     * @param id 知识条目ID
     */
    @Update("UPDATE kb_article SET view_count = view_count + 1 WHERE id = #{id}")
    void increaseViewCount(@Param("id") String id);

    /**
     * 增加点赞次数
     * 
     * @param id 知识条目ID
     */
    @Update("UPDATE kb_article SET like_count = like_count + 1 WHERE id = #{id}")
    void increaseLikeCount(@Param("id") String id);

    /**
     * 减少点赞次数
     * 
     * @param id 知识条目ID
     */
    @Update("UPDATE kb_article SET like_count = GREATEST(like_count - 1, 0) WHERE id = #{id}")
    void decreaseLikeCount(@Param("id") String id);

    /**
     * 增加收藏次数
     * 
     * @param id 知识条目ID
     */
    @Update("UPDATE kb_article SET collect_count = collect_count + 1 WHERE id = #{id}")
    void increaseCollectCount(@Param("id") String id);

    /**
     * 减少收藏次数
     * 
     * @param id 知识条目ID
     */
    @Update("UPDATE kb_article SET collect_count = GREATEST(collect_count - 1, 0) WHERE id = #{id}")
    void decreaseCollectCount(@Param("id") String id);

    /**
     * 增加分享次数
     * 
     * @param id 知识条目ID
     */
    @Update("UPDATE kb_article SET share_count = share_count + 1 WHERE id = #{id}")
    void increaseShareCount(@Param("id") String id);

    /**
     * 统计各状态的知识条目数量
     * 
     * @return 统计结果
     */
    @Select("SELECT status, COUNT(*) as count FROM kb_article WHERE del_flag = 0 GROUP BY status")
    List<Object> countByStatus();

    /**
     * 统计各分类的知识条目数量
     *
     * @return 统计结果
     */
    @Select("SELECT c.category_name, COUNT(a.id) as count FROM kb_category c LEFT JOIN kb_article a ON c.id = a.category_id AND a.del_flag = 0 WHERE c.del_flag = 0 GROUP BY c.id, c.category_name")
    List<Object> countByCategory();

    /**
     * 精确搜索知识条目
     *
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @param categoryId 分类ID
     * @return 搜索结果
     */
    IPage<KbArticleVO> exactSearchArticles(Page<KbArticleVO> page,
                                          @Param("keyword") String keyword,
                                          @Param("categoryId") String categoryId);

    /**
     * 根据标签搜索知识条目
     *
     * @param page 分页对象
     * @param tagNames 标签名称列表
     * @param categoryId 分类ID
     * @return 搜索结果
     */
    IPage<KbArticleVO> searchArticlesByTags(Page<KbArticleVO> page,
                                           @Param("tagNames") List<String> tagNames,
                                           @Param("categoryId") String categoryId);

    /**
     * 获取搜索建议
     *
     * @param keyword 关键词前缀
     * @param limit 返回数量限制
     * @return 建议列表
     */
    List<String> getSearchSuggestions(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取热门搜索关键词
     *
     * @param limit 返回数量限制
     * @return 热门关键词列表
     */
    List<Map<String, Object>> getHotSearchKeywords(@Param("limit") Integer limit);

    /**
     * 获取相关知识推荐
     *
     * @param articleId 当前知识条目ID
     * @param limit 推荐数量
     * @return 相关知识列表
     */
    List<KbArticleVO> getRelatedArticles(@Param("articleId") String articleId, @Param("limit") Integer limit);

    /**
     * 高级搜索
     *
     * @param page 分页对象
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
    IPage<KbArticleVO> advancedSearchArticles(Page<KbArticleVO> page,
                                             @Param("title") String title,
                                             @Param("content") String content,
                                             @Param("keywords") String keywords,
                                             @Param("categoryId") String categoryId,
                                             @Param("createBy") String createBy,
                                             @Param("startTime") String startTime,
                                             @Param("endTime") String endTime,
                                             @Param("status") String status);

    /**
     * 记录搜索历史
     *
     * @param keyword 搜索关键词
     * @param userId 用户ID
     * @param resultCount 搜索结果数量
     */
    void insertSearchHistory(@Param("keyword") String keyword,
                           @Param("userId") String userId,
                           @Param("resultCount") Integer resultCount);

    /**
     * 获取用户搜索历史
     *
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 搜索历史列表
     */
    List<Map<String, Object>> getUserSearchHistory(@Param("userId") String userId, @Param("limit") Integer limit);

    /**
     * 清除用户搜索历史
     *
     * @param userId 用户ID
     */
    void clearUserSearchHistory(@Param("userId") String userId);

    /**
     * 搜索统计分析
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> getSearchStatistics(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
