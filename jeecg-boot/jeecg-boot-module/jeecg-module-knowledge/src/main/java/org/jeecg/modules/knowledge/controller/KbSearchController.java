package org.jeecg.modules.knowledge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.knowledge.service.IKbSearchService;
import org.jeecg.modules.knowledge.vo.KbArticleVO;
import org.jeecg.modules.knowledge.vo.KbSearchResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 知识库搜索Controller
 *
 * @author seadragon
 * @since 2025-07-03
 */
@Tag(name = "知识库搜索", description = "知识库搜索相关接口")
@RestController
@RequestMapping("/knowledge/search")
@Slf4j
public class KbSearchController {

    @Autowired
    private IKbSearchService kbSearchService;

    /**
     * 全文搜索（带高亮）
     *
     * @param keyword 搜索关键词
     * @param categoryId 分类ID
     * @param contentType 内容类型
     * @param sourceType 来源类型
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param request 请求对象
     * @return 搜索结果
     */
    @AutoLog(value = "知识搜索-全文搜索")
    @Operation(summary = "知识搜索-全文搜索", description = "知识搜索-全文搜索")
    @GetMapping(value = "/fulltext")
    public Result<IPage<KbSearchResultVO>> searchWithHighlight(@RequestParam(name = "keyword") String keyword,
                                                              @RequestParam(name = "categoryId", required = false) String categoryId,
                                                              @RequestParam(name = "contentType", required = false) String contentType,
                                                              @RequestParam(name = "sourceType", required = false) String sourceType,
                                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                              HttpServletRequest request) {
        Page<KbSearchResultVO> page = new Page<>(pageNo, pageSize);
        IPage<KbSearchResultVO> pageList = kbSearchService.searchWithHighlight(page, keyword, categoryId, contentType, sourceType);
        
        // 记录搜索历史
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute("loginUser");
            String userId = loginUser != null ? loginUser.getId() : "anonymous";
            kbSearchService.recordSearchHistory(keyword, userId, (int) pageList.getTotal());
        } catch (Exception e) {
            log.warn("记录搜索历史失败", e);
        }
        
        return Result.OK(pageList);
    }

    /**
     * 精确搜索
     *
     * @param keyword 搜索关键词
     * @param categoryId 分类ID
     * @param exactMatch 是否精确匹配
     * @param pageNo 页码
     * @param pageSize 页大小
     * @return 搜索结果
     */
    @AutoLog(value = "知识搜索-精确搜索")
    @Operation(summary = "知识搜索-精确搜索", description = "知识搜索-精确搜索")
    @GetMapping(value = "/exact")
    public Result<IPage<KbArticleVO>> exactSearch(@RequestParam(name = "keyword") String keyword,
                                                 @RequestParam(name = "categoryId", required = false) String categoryId,
                                                 @RequestParam(name = "exactMatch", defaultValue = "false") Boolean exactMatch,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<KbArticleVO> page = new Page<>(pageNo, pageSize);
        IPage<KbArticleVO> pageList = kbSearchService.exactSearch(page, keyword, categoryId, exactMatch);
        
        return Result.OK(pageList);
    }

    /**
     * 根据标签搜索
     *
     * @param tags 标签名称列表（逗号分隔）
     * @param categoryId 分类ID
     * @param pageNo 页码
     * @param pageSize 页大小
     * @return 搜索结果
     */
    @AutoLog(value = "知识搜索-标签搜索")
    @Operation(summary = "知识搜索-标签搜索", description = "知识搜索-标签搜索")
    @GetMapping(value = "/tags")
    public Result<IPage<KbArticleVO>> searchByTags(@RequestParam(name = "tags") String tags,
                                                  @RequestParam(name = "categoryId", required = false) String categoryId,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<KbArticleVO> page = new Page<>(pageNo, pageSize);
        List<String> tagNames = Arrays.asList(tags.split(","));
        IPage<KbArticleVO> pageList = kbSearchService.searchByTags(page, tagNames, categoryId);
        
        return Result.OK(pageList);
    }

    /**
     * 搜索建议
     *
     * @param keyword 关键词前缀
     * @param limit 返回数量限制
     * @return 建议列表
     */
    @AutoLog(value = "知识搜索-搜索建议")
    @Operation(summary = "知识搜索-搜索建议", description = "知识搜索-搜索建议")
    @GetMapping(value = "/suggestions")
    public Result<List<String>> searchSuggestions(@RequestParam(name = "keyword") String keyword,
                                                 @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        List<String> suggestions = kbSearchService.searchSuggestions(keyword, limit);
        return Result.OK(suggestions);
    }

    /**
     * 热门搜索关键词
     *
     * @param limit 返回数量限制
     * @return 热门关键词列表
     */
    @AutoLog(value = "知识搜索-热门关键词")
    @Operation(summary = "知识搜索-热门关键词", description = "知识搜索-热门关键词")
    @GetMapping(value = "/hot")
    public Result<List<Map<String, Object>>> getHotSearchKeywords(@RequestParam(name = "limit", defaultValue = "20") Integer limit) {
        List<Map<String, Object>> hotKeywords = kbSearchService.getHotSearchKeywords(limit);
        return Result.OK(hotKeywords);
    }

    /**
     * 相关知识推荐
     *
     * @param articleId 当前知识条目ID
     * @param limit 推荐数量
     * @return 相关知识列表
     */
    @AutoLog(value = "知识搜索-相关推荐")
    @Operation(summary = "知识搜索-相关推荐", description = "知识搜索-相关推荐")
    @GetMapping(value = "/related")
    public Result<List<KbArticleVO>> getRelatedArticles(@RequestParam(name = "articleId") String articleId,
                                                       @RequestParam(name = "limit", defaultValue = "5") Integer limit) {
        List<KbArticleVO> relatedArticles = kbSearchService.getRelatedArticles(articleId, limit);
        return Result.OK(relatedArticles);
    }

    /**
     * 高级搜索
     *
     * @param title 标题关键词
     * @param content 内容关键词
     * @param keywords 关键词
     * @param categoryId 分类ID
     * @param createBy 创建人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param status 状态
     * @param pageNo 页码
     * @param pageSize 页大小
     * @return 搜索结果
     */
    @AutoLog(value = "知识搜索-高级搜索")
    @Operation(summary = "知识搜索-高级搜索", description = "知识搜索-高级搜索")
    @GetMapping(value = "/advanced")
    public Result<IPage<KbArticleVO>> advancedSearch(@RequestParam(name = "title", required = false) String title,
                                                    @RequestParam(name = "content", required = false) String content,
                                                    @RequestParam(name = "keywords", required = false) String keywords,
                                                    @RequestParam(name = "categoryId", required = false) String categoryId,
                                                    @RequestParam(name = "createBy", required = false) String createBy,
                                                    @RequestParam(name = "startTime", required = false) String startTime,
                                                    @RequestParam(name = "endTime", required = false) String endTime,
                                                    @RequestParam(name = "status", required = false) String status,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<KbArticleVO> page = new Page<>(pageNo, pageSize);
        IPage<KbArticleVO> pageList = kbSearchService.advancedSearch(page, title, content, keywords, 
                categoryId, createBy, startTime, endTime, status);
        
        return Result.OK(pageList);
    }

    /**
     * 获取用户搜索历史
     *
     * @param limit 返回数量限制
     * @param request 请求对象
     * @return 搜索历史列表
     */
    @AutoLog(value = "知识搜索-搜索历史")
    @Operation(summary = "知识搜索-搜索历史", description = "知识搜索-搜索历史")
    @GetMapping(value = "/history")
    public Result<List<Map<String, Object>>> getUserSearchHistory(@RequestParam(name = "limit", defaultValue = "20") Integer limit,
                                                                 HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) request.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.error("用户未登录");
        }
        
        List<Map<String, Object>> history = kbSearchService.getUserSearchHistory(loginUser.getId(), limit);
        return Result.OK(history);
    }

    /**
     * 清除用户搜索历史
     *
     * @param request 请求对象
     * @return 清除结果
     */
    @AutoLog(value = "知识搜索-清除历史")
    @Operation(summary = "知识搜索-清除历史", description = "知识搜索-清除历史")
    @DeleteMapping(value = "/history")
    public Result<String> clearUserSearchHistory(HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) request.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.error("用户未登录");
        }
        
        kbSearchService.clearUserSearchHistory(loginUser.getId());
        return Result.OK("清除成功");
    }

    /**
     * 搜索统计分析
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    @AutoLog(value = "知识搜索-统计分析")
    @Operation(summary = "知识搜索-统计分析", description = "知识搜索-统计分析")
    @GetMapping(value = "/statistics")
    public Result<Map<String, Object>> getSearchStatistics(@RequestParam(name = "startTime", required = false) String startTime,
                                                          @RequestParam(name = "endTime", required = false) String endTime) {
        // 默认查询最近30天的数据
        if (startTime == null) {
            startTime = "DATE_SUB(NOW(), INTERVAL 30 DAY)";
        }
        if (endTime == null) {
            endTime = "NOW()";
        }
        
        Map<String, Object> statistics = kbSearchService.getSearchStatistics(startTime, endTime);
        return Result.OK(statistics);
    }
}
