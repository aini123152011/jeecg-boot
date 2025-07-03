package org.jeecg.modules.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.knowledge.entity.KbArticle;
import org.jeecg.modules.knowledge.mapper.KbArticleMapper;
import org.jeecg.modules.knowledge.service.IKbArticleService;
import org.jeecg.modules.knowledge.vo.KbArticleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 知识条目Service实现类
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Service
public class KbArticleServiceImpl extends ServiceImpl<KbArticleMapper, KbArticle> implements IKbArticleService {

    @Override
    public IPage<KbArticleVO> getArticlePageWithCategory(Page<KbArticleVO> page, String categoryId, String title, String status) {
        return baseMapper.selectArticlePageWithCategory(page, categoryId, title, status);
    }

    @Override
    public List<KbArticle> getArticlesByCategoryId(String categoryId) {
        return baseMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<KbArticle> getRecommendArticles(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return baseMapper.selectRecommendArticles(limit);
    }

    @Override
    public List<KbArticle> getHotArticles(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return baseMapper.selectHotArticles(limit);
    }

    @Override
    public List<KbArticle> getLatestArticles(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return baseMapper.selectLatestArticles(limit);
    }

    @Override
    public IPage<KbArticleVO> searchArticles(Page<KbArticleVO> page, String keyword, String categoryId) {
        return baseMapper.searchArticles(page, keyword, categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveArticle(KbArticle kbArticle) {
        // 设置默认值
        if (kbArticle.getViewCount() == null) {
            kbArticle.setViewCount(0);
        }
        if (kbArticle.getLikeCount() == null) {
            kbArticle.setLikeCount(0);
        }
        if (kbArticle.getCollectCount() == null) {
            kbArticle.setCollectCount(0);
        }
        if (kbArticle.getCommentCount() == null) {
            kbArticle.setCommentCount(0);
        }
        if (kbArticle.getShareCount() == null) {
            kbArticle.setShareCount(0);
        }
        if (kbArticle.getIsTop() == null) {
            kbArticle.setIsTop(0);
        }
        if (kbArticle.getIsRecommend() == null) {
            kbArticle.setIsRecommend(0);
        }
        if (kbArticle.getIsPublic() == null) {
            kbArticle.setIsPublic(1);
        }
        if (kbArticle.getVersion() == null) {
            kbArticle.setVersion(1);
        }
        if (oConvertUtils.isEmpty(kbArticle.getStatus())) {
            kbArticle.setStatus("draft");
        }
        if (oConvertUtils.isEmpty(kbArticle.getContentType())) {
            kbArticle.setContentType("markdown");
        }
        if (oConvertUtils.isEmpty(kbArticle.getSourceType())) {
            kbArticle.setSourceType("manual");
        }
        
        kbArticle.setCreateTime(new Date());
        
        return this.save(kbArticle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArticle(KbArticle kbArticle) {
        kbArticle.setUpdateTime(new Date());
        // 更新版本号
        if (kbArticle.getVersion() != null) {
            kbArticle.setVersion(kbArticle.getVersion() + 1);
        }
        return this.updateById(kbArticle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteArticle(String id) {
        KbArticle article = this.getById(id);
        if (article == null) {
            return false;
        }
        
        // 逻辑删除
        article.setDelFlag(1);
        article.setUpdateTime(new Date());
        return this.updateById(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchArticle(List<String> ids) {
        for (String id : ids) {
            deleteArticle(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishArticle(String id) {
        KbArticle article = this.getById(id);
        if (article == null) {
            return false;
        }
        
        article.setStatus("published");
        article.setPublishTime(new Date());
        article.setUpdateTime(new Date());
        
        return this.updateById(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishBatchArticle(List<String> ids) {
        for (String id : ids) {
            publishArticle(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean archiveArticle(String id) {
        KbArticle article = this.getById(id);
        if (article == null) {
            return false;
        }
        
        article.setStatus("archived");
        article.setUpdateTime(new Date());
        
        return this.updateById(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean archiveBatchArticle(List<String> ids) {
        for (String id : ids) {
            archiveArticle(id);
        }
        return true;
    }

    @Override
    public void increaseViewCount(String id) {
        baseMapper.increaseViewCount(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likeArticle(String id, String userId) {
        // TODO: 实现用户点赞记录表的逻辑
        baseMapper.increaseLikeCount(id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unlikeArticle(String id, String userId) {
        // TODO: 实现用户点赞记录表的逻辑
        baseMapper.decreaseLikeCount(id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean collectArticle(String id, String userId) {
        // TODO: 实现用户收藏记录表的逻辑
        baseMapper.increaseCollectCount(id);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uncollectArticle(String id, String userId) {
        // TODO: 实现用户收藏记录表的逻辑
        baseMapper.decreaseCollectCount(id);
        return true;
    }

    @Override
    public void shareArticle(String id) {
        baseMapper.increaseShareCount(id);
    }

    @Override
    public KbArticleVO getArticleDetail(String id) {
        // 增加浏览次数
        increaseViewCount(id);

        // 查询详情 - 需要根据ID查询特定文章
        KbArticle article = this.getById(id);
        if (article == null) {
            return null;
        }

        // 转换为VO对象
        KbArticleVO vo = new KbArticleVO();
        BeanUtils.copyProperties(article, vo);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyArticle(String id) {
        KbArticle original = this.getById(id);
        if (original == null) {
            return false;
        }
        
        KbArticle copy = new KbArticle();
        BeanUtils.copyProperties(original, copy);
        copy.setId(null);
        copy.setTitle(original.getTitle() + " - 副本");
        copy.setStatus("draft");
        copy.setPublishTime(null);
        copy.setViewCount(0);
        copy.setLikeCount(0);
        copy.setCollectCount(0);
        copy.setCommentCount(0);
        copy.setShareCount(0);
        copy.setVersion(1);
        copy.setCreateTime(new Date());
        copy.setUpdateTime(null);
        
        return this.save(copy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveArticles(List<String> ids, String categoryId) {
        for (String id : ids) {
            KbArticle article = this.getById(id);
            if (article != null) {
                article.setCategoryId(categoryId);
                article.setUpdateTime(new Date());
                this.updateById(article);
            }
        }
        return true;
    }

    @Override
    public List<Object> countByStatus() {
        return baseMapper.countByStatus();
    }

    @Override
    public List<Object> countByCategory() {
        return baseMapper.countByCategory();
    }
}
