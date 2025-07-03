package org.jeecg.modules.knowledge.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.knowledge.mapper.KbArticleMapper;
import org.jeecg.modules.knowledge.service.IKbSearchService;
import org.jeecg.modules.knowledge.vo.KbArticleVO;
import org.jeecg.modules.knowledge.vo.KbSearchResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 知识库搜索服务实现
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Slf4j
@Service
public class KbSearchServiceImpl implements IKbSearchService {

    @Autowired
    private KbArticleMapper kbArticleMapper;

    private static final String HIGHLIGHT_START = "<mark>";
    private static final String HIGHLIGHT_END = "</mark>";
    private static final int SNIPPET_LENGTH = 200;

    @Override
    public IPage<KbSearchResultVO> searchWithHighlight(Page<KbSearchResultVO> page, String keyword, 
                                                      String categoryId, String contentType, String sourceType) {
        // 先进行基础搜索
        Page<KbArticleVO> articlePage = new Page<>(page.getCurrent(), page.getSize());
        IPage<KbArticleVO> articleResult = kbArticleMapper.searchArticles(articlePage, keyword, categoryId);
        
        // 转换为搜索结果VO并添加高亮
        List<KbSearchResultVO> searchResults = articleResult.getRecords().stream()
                .map(article -> convertToSearchResult(article, keyword))
                .collect(Collectors.toList());
        
        // 构建返回结果
        Page<KbSearchResultVO> resultPage = new Page<>(page.getCurrent(), page.getSize());
        resultPage.setRecords(searchResults);
        resultPage.setTotal(articleResult.getTotal());
        resultPage.setPages(articleResult.getPages());
        
        return resultPage;
    }

    @Override
    public IPage<KbArticleVO> exactSearch(Page<KbArticleVO> page, String keyword, String categoryId, boolean exactMatch) {
        if (exactMatch) {
            // 精确匹配搜索
            return kbArticleMapper.exactSearchArticles(page, keyword, categoryId);
        } else {
            // 模糊搜索
            return kbArticleMapper.searchArticles(page, keyword, categoryId);
        }
    }

    @Override
    public IPage<KbArticleVO> searchByTags(Page<KbArticleVO> page, List<String> tagNames, String categoryId) {
        return kbArticleMapper.searchArticlesByTags(page, tagNames, categoryId);
    }

    @Override
    public List<String> searchSuggestions(String keyword, Integer limit) {
        if (!StringUtils.hasText(keyword)) {
            return Collections.emptyList();
        }
        
        limit = limit != null && limit > 0 ? limit : 10;
        return kbArticleMapper.getSearchSuggestions(keyword, limit);
    }

    @Override
    public List<Map<String, Object>> getHotSearchKeywords(Integer limit) {
        limit = limit != null && limit > 0 ? limit : 20;
        return kbArticleMapper.getHotSearchKeywords(limit);
    }

    @Override
    public List<KbArticleVO> getRelatedArticles(String articleId, Integer limit) {
        if (!StringUtils.hasText(articleId)) {
            return Collections.emptyList();
        }
        
        limit = limit != null && limit > 0 ? limit : 5;
        return kbArticleMapper.getRelatedArticles(articleId, limit);
    }

    @Override
    public IPage<KbArticleVO> advancedSearch(Page<KbArticleVO> page, String title, String content, 
                                           String keywords, String categoryId, String createBy, 
                                           String startTime, String endTime, String status) {
        return kbArticleMapper.advancedSearchArticles(page, title, content, keywords, 
                categoryId, createBy, startTime, endTime, status);
    }

    @Override
    public void recordSearchHistory(String keyword, String userId, Integer resultCount) {
        if (StringUtils.hasText(keyword) && StringUtils.hasText(userId)) {
            try {
                kbArticleMapper.insertSearchHistory(keyword, userId, resultCount);
            } catch (Exception e) {
                log.error("记录搜索历史失败: keyword={}, userId={}", keyword, userId, e);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getUserSearchHistory(String userId, Integer limit) {
        if (!StringUtils.hasText(userId)) {
            return Collections.emptyList();
        }
        
        limit = limit != null && limit > 0 ? limit : 20;
        return kbArticleMapper.getUserSearchHistory(userId, limit);
    }

    @Override
    public void clearUserSearchHistory(String userId) {
        if (StringUtils.hasText(userId)) {
            kbArticleMapper.clearUserSearchHistory(userId);
        }
    }

    @Override
    public Map<String, Object> getSearchStatistics(String startTime, String endTime) {
        return kbArticleMapper.getSearchStatistics(startTime, endTime);
    }

    /**
     * 转换为搜索结果VO并添加高亮
     */
    private KbSearchResultVO convertToSearchResult(KbArticleVO article, String keyword) {
        KbSearchResultVO result = new KbSearchResultVO();
        BeanUtils.copyProperties(article, result);
        
        if (StringUtils.hasText(keyword)) {
            // 添加高亮
            result.setTitleHighlight(addHighlight(article.getTitle(), keyword));
            result.setSummaryHighlight(addHighlight(article.getSummary(), keyword));
            result.setKeywordsHighlight(addHighlight(article.getKeywords(), keyword));
            
            // 生成内容片段
            result.setContentHighlight(generateContentSnippet(article.getContent(), keyword));
            
            // 计算匹配度
            result.setMatchScore(calculateMatchScore(article, keyword));
            
            // 提取匹配的关键词
            result.setMatchedKeywords(extractMatchedKeywords(article, keyword));
        }
        
        return result;
    }

    /**
     * 添加高亮标记
     */
    private String addHighlight(String text, String keyword) {
        if (!StringUtils.hasText(text) || !StringUtils.hasText(keyword)) {
            return text;
        }
        
        try {
            // 使用正则表达式进行不区分大小写的替换
            Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            return matcher.replaceAll(HIGHLIGHT_START + "$0" + HIGHLIGHT_END);
        } catch (Exception e) {
            log.warn("添加高亮失败: text={}, keyword={}", text, keyword, e);
            return text;
        }
    }

    /**
     * 生成内容片段
     */
    private String generateContentSnippet(String content, String keyword) {
        if (!StringUtils.hasText(content) || !StringUtils.hasText(keyword)) {
            return content != null && content.length() > SNIPPET_LENGTH ? 
                   content.substring(0, SNIPPET_LENGTH) + "..." : content;
        }
        
        try {
            // 查找关键词位置
            int index = content.toLowerCase().indexOf(keyword.toLowerCase());
            if (index == -1) {
                return content.length() > SNIPPET_LENGTH ? 
                       content.substring(0, SNIPPET_LENGTH) + "..." : content;
            }
            
            // 计算片段起始位置
            int start = Math.max(0, index - SNIPPET_LENGTH / 2);
            int end = Math.min(content.length(), start + SNIPPET_LENGTH);
            
            String snippet = content.substring(start, end);
            
            // 添加省略号
            if (start > 0) {
                snippet = "..." + snippet;
            }
            if (end < content.length()) {
                snippet = snippet + "...";
            }
            
            // 添加高亮
            return addHighlight(snippet, keyword);
        } catch (Exception e) {
            log.warn("生成内容片段失败: keyword={}", keyword, e);
            return content != null && content.length() > SNIPPET_LENGTH ? 
                   content.substring(0, SNIPPET_LENGTH) + "..." : content;
        }
    }

    /**
     * 计算匹配度评分
     */
    private Double calculateMatchScore(KbArticleVO article, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return 0.0;
        }
        
        double score = 0.0;
        String lowerKeyword = keyword.toLowerCase();
        
        // 标题匹配权重最高
        if (StringUtils.hasText(article.getTitle()) && 
            article.getTitle().toLowerCase().contains(lowerKeyword)) {
            score += 10.0;
        }
        
        // 摘要匹配
        if (StringUtils.hasText(article.getSummary()) && 
            article.getSummary().toLowerCase().contains(lowerKeyword)) {
            score += 5.0;
        }
        
        // 关键词匹配
        if (StringUtils.hasText(article.getKeywords()) && 
            article.getKeywords().toLowerCase().contains(lowerKeyword)) {
            score += 8.0;
        }
        
        // 内容匹配
        if (StringUtils.hasText(article.getContent()) && 
            article.getContent().toLowerCase().contains(lowerKeyword)) {
            score += 3.0;
        }
        
        // 考虑文章质量和热度
        if (article.getQualityScore() != null) {
            score += article.getQualityScore().doubleValue();
        }
        
        if (article.getViewCount() != null && article.getViewCount() > 0) {
            score += Math.log10(article.getViewCount());
        }
        
        return score;
    }

    /**
     * 提取匹配的关键词
     */
    private List<String> extractMatchedKeywords(KbArticleVO article, String keyword) {
        List<String> matched = new ArrayList<>();
        
        if (StringUtils.hasText(keyword)) {
            String[] keywords = keyword.split("\\s+");
            for (String kw : keywords) {
                if (StringUtils.hasText(kw)) {
                    matched.add(kw);
                }
            }
        }
        
        return matched;
    }
}
