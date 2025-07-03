package org.jeecg.modules.knowledge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.knowledge.entity.KbArticle;
import org.jeecg.modules.knowledge.vo.KbArticleVO;

import java.util.List;

/**
 * 知识条目Service接口
 * 
 * @author seadragon
 * @since 2025-07-03
 */
public interface IKbArticleService extends IService<KbArticle> {

    /**
     * 分页查询知识条目（包含分类名称）
     * 
     * @param page 分页对象
     * @param categoryId 分类ID
     * @param title 标题关键词
     * @param status 状态
     * @return 知识条目分页列表
     */
    IPage<KbArticleVO> getArticlePageWithCategory(Page<KbArticleVO> page, String categoryId, String title, String status);

    /**
     * 根据分类ID查询知识条目列表
     * 
     * @param categoryId 分类ID
     * @return 知识条目列表
     */
    List<KbArticle> getArticlesByCategoryId(String categoryId);

    /**
     * 查询推荐知识条目
     * 
     * @param limit 限制数量
     * @return 推荐知识条目列表
     */
    List<KbArticle> getRecommendArticles(Integer limit);

    /**
     * 查询热门知识条目
     * 
     * @param limit 限制数量
     * @return 热门知识条目列表
     */
    List<KbArticle> getHotArticles(Integer limit);

    /**
     * 查询最新知识条目
     * 
     * @param limit 限制数量
     * @return 最新知识条目列表
     */
    List<KbArticle> getLatestArticles(Integer limit);

    /**
     * 全文搜索知识条目
     * 
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @param categoryId 分类ID
     * @return 搜索结果
     */
    IPage<KbArticleVO> searchArticles(Page<KbArticleVO> page, String keyword, String categoryId);

    /**
     * 保存知识条目
     * 
     * @param kbArticle 知识条目
     * @return 保存结果
     */
    boolean saveArticle(KbArticle kbArticle);

    /**
     * 更新知识条目
     * 
     * @param kbArticle 知识条目
     * @return 更新结果
     */
    boolean updateArticle(KbArticle kbArticle);

    /**
     * 删除知识条目（逻辑删除）
     * 
     * @param id 知识条目ID
     * @return 删除结果
     */
    boolean deleteArticle(String id);

    /**
     * 批量删除知识条目
     * 
     * @param ids 知识条目ID列表
     * @return 删除结果
     */
    boolean deleteBatchArticle(List<String> ids);

    /**
     * 发布知识条目
     * 
     * @param id 知识条目ID
     * @return 发布结果
     */
    boolean publishArticle(String id);

    /**
     * 批量发布知识条目
     * 
     * @param ids 知识条目ID列表
     * @return 发布结果
     */
    boolean publishBatchArticle(List<String> ids);

    /**
     * 归档知识条目
     * 
     * @param id 知识条目ID
     * @return 归档结果
     */
    boolean archiveArticle(String id);

    /**
     * 批量归档知识条目
     * 
     * @param ids 知识条目ID列表
     * @return 归档结果
     */
    boolean archiveBatchArticle(List<String> ids);

    /**
     * 增加浏览次数
     * 
     * @param id 知识条目ID
     */
    void increaseViewCount(String id);

    /**
     * 点赞知识条目
     * 
     * @param id 知识条目ID
     * @param userId 用户ID
     * @return 点赞结果
     */
    boolean likeArticle(String id, String userId);

    /**
     * 取消点赞知识条目
     * 
     * @param id 知识条目ID
     * @param userId 用户ID
     * @return 取消点赞结果
     */
    boolean unlikeArticle(String id, String userId);

    /**
     * 收藏知识条目
     * 
     * @param id 知识条目ID
     * @param userId 用户ID
     * @return 收藏结果
     */
    boolean collectArticle(String id, String userId);

    /**
     * 取消收藏知识条目
     * 
     * @param id 知识条目ID
     * @param userId 用户ID
     * @return 取消收藏结果
     */
    boolean uncollectArticle(String id, String userId);

    /**
     * 分享知识条目
     * 
     * @param id 知识条目ID
     */
    void shareArticle(String id);

    /**
     * 获取知识条目详情（包含分类信息）
     * 
     * @param id 知识条目ID
     * @return 知识条目详情
     */
    KbArticleVO getArticleDetail(String id);

    /**
     * 复制知识条目
     * 
     * @param id 原知识条目ID
     * @return 复制结果
     */
    boolean copyArticle(String id);

    /**
     * 移动知识条目到指定分类
     * 
     * @param ids 知识条目ID列表
     * @param categoryId 目标分类ID
     * @return 移动结果
     */
    boolean moveArticles(List<String> ids, String categoryId);

    /**
     * 统计各状态的知识条目数量
     * 
     * @return 统计结果
     */
    List<Object> countByStatus();

    /**
     * 统计各分类的知识条目数量
     * 
     * @return 统计结果
     */
    List<Object> countByCategory();
}
