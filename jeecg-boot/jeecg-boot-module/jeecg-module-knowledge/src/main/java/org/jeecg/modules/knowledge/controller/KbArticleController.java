package org.jeecg.modules.knowledge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.knowledge.entity.KbArticle;
import org.jeecg.modules.knowledge.service.IKbArticleService;
import org.jeecg.modules.knowledge.vo.KbArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 知识条目Controller
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Tag(name = "知识条目管理", description = "知识条目管理相关接口")
@RestController
@RequestMapping("/knowledge/kbArticle")
@Slf4j
public class KbArticleController extends JeecgController<KbArticle, IKbArticleService> {

    @Autowired
    private IKbArticleService kbArticleService;

    /**
     * 分页列表查询
     *
     * @param categoryId 分类ID
     * @param title 标题关键词
     * @param status 状态
     * @param pageNo 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    @AutoLog(value = "知识条目-分页列表查询")
    @Operation(summary = "知识条目-分页列表查询", description = "知识条目-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<KbArticleVO>> queryPageList(@RequestParam(name = "categoryId", required = false) String categoryId,
                                                    @RequestParam(name = "title", required = false) String title,
                                                    @RequestParam(name = "status", required = false) String status,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<KbArticleVO> page = new Page<KbArticleVO>(pageNo, pageSize);
        IPage<KbArticleVO> pageList = kbArticleService.getArticlePageWithCategory(page, categoryId, title, status);

        return Result.OK(pageList);
    }

    /**
     * 根据分类ID查询知识条目列表
     *
     * @param categoryId 分类ID
     * @return 知识条目列表
     */
    @AutoLog(value = "知识条目-根据分类查询")
    @Operation(summary = "知识条目-根据分类查询", description = "知识条目-根据分类查询")
    @GetMapping(value = "/listByCategory")
    public Result<List<KbArticle>> getArticlesByCategory(@RequestParam(name = "categoryId") String categoryId) {
        List<KbArticle> articles = kbArticleService.getArticlesByCategoryId(categoryId);
        return Result.OK(articles);
    }

    /**
     * 查询推荐知识条目
     *
     * @param limit 限制数量
     * @return 推荐知识条目列表
     */
    @AutoLog(value = "知识条目-查询推荐")
    @Operation(summary = "知识条目-查询推荐", description = "知识条目-查询推荐")
    @GetMapping(value = "/recommend")
    public Result<List<KbArticle>> getRecommendArticles(@RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        List<KbArticle> articles = kbArticleService.getRecommendArticles(limit);
        return Result.OK(articles);
    }

    /**
     * 查询热门知识条目
     *
     * @param limit 限制数量
     * @return 热门知识条目列表
     */
    @AutoLog(value = "知识条目-查询热门")
    @Operation(summary = "知识条目-查询热门", description = "知识条目-查询热门")
    @GetMapping(value = "/hot")
    public Result<List<KbArticle>> getHotArticles(@RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        List<KbArticle> articles = kbArticleService.getHotArticles(limit);
        return Result.OK(articles);
    }

    /**
     * 查询最新知识条目
     *
     * @param limit 限制数量
     * @return 最新知识条目列表
     */
    @AutoLog(value = "知识条目-查询最新")
    @Operation(summary = "知识条目-查询最新", description = "知识条目-查询最新")
    @GetMapping(value = "/latest")
    public Result<List<KbArticle>> getLatestArticles(@RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        List<KbArticle> articles = kbArticleService.getLatestArticles(limit);
        return Result.OK(articles);
    }

    /**
     * 全文搜索知识条目
     *
     * @param keyword 搜索关键词
     * @param categoryId 分类ID
     * @param pageNo 页码
     * @param pageSize 页大小
     * @return 搜索结果
     */
    @AutoLog(value = "知识条目-全文搜索")
    @Operation(summary = "知识条目-全文搜索", description = "知识条目-全文搜索")
    @GetMapping(value = "/search")
    public Result<IPage<KbArticleVO>> searchArticles(@RequestParam(name = "keyword") String keyword,
                                                     @RequestParam(name = "categoryId", required = false) String categoryId,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<KbArticleVO> page = new Page<KbArticleVO>(pageNo, pageSize);
        IPage<KbArticleVO> pageList = kbArticleService.searchArticles(page, keyword, categoryId);
        
        return Result.OK(pageList);
    }

    /**
     * 添加知识条目
     *
     * @param kbArticle 知识条目信息
     * @return 添加结果
     */
    @AutoLog(value = "知识条目-添加")
    @Operation(summary = "知识条目-添加", description = "知识条目-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody KbArticle kbArticle) {
        kbArticleService.saveArticle(kbArticle);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑知识条目
     *
     * @param kbArticle 知识条目信息
     * @return 编辑结果
     */
    @AutoLog(value = "知识条目-编辑")
    @Operation(summary = "知识条目-编辑", description = "知识条目-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody KbArticle kbArticle) {
        kbArticleService.updateArticle(kbArticle);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除知识条目
     *
     * @param id 知识条目ID
     * @return 删除结果
     */
    @AutoLog(value = "知识条目-通过id删除")
    @Operation(summary = "知识条目-通过id删除", description = "知识条目-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            kbArticleService.deleteArticle(id);
            return Result.OK("删除成功!");
        } catch (Exception e) {
            log.error("删除知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除知识条目
     *
     * @param ids 知识条目ID列表
     * @return 删除结果
     */
    @AutoLog(value = "知识条目-批量删除")
    @Operation(summary = "知识条目-批量删除", description = "知识条目-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        try {
            List<String> idList = Arrays.asList(ids.split(","));
            kbArticleService.deleteBatchArticle(idList);
            return Result.OK("批量删除成功!");
        } catch (Exception e) {
            log.error("批量删除知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 通过id查询知识条目
     *
     * @param id 知识条目ID
     * @return 知识条目信息
     */
    @AutoLog(value = "知识条目-通过id查询")
    @Operation(summary = "知识条目-通过id查询", description = "知识条目-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<KbArticle> queryById(@RequestParam(name = "id", required = true) String id) {
        KbArticle kbArticle = kbArticleService.getById(id);
        if (kbArticle == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(kbArticle);
    }

    /**
     * 获取知识条目详情
     *
     * @param id 知识条目ID
     * @return 知识条目详情
     */
    @AutoLog(value = "知识条目-获取详情")
    @Operation(summary = "知识条目-获取详情", description = "知识条目-获取详情")
    @GetMapping(value = "/detail")
    public Result<KbArticleVO> getDetail(@RequestParam(name = "id", required = true) String id) {
        KbArticleVO detail = kbArticleService.getArticleDetail(id);
        if (detail == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(detail);
    }

    /**
     * 发布知识条目
     *
     * @param id 知识条目ID
     * @return 发布结果
     */
    @AutoLog(value = "知识条目-发布")
    @Operation(summary = "知识条目-发布", description = "知识条目-发布")
    @PostMapping(value = "/publish")
    public Result<String> publish(@RequestParam(name = "id", required = true) String id) {
        try {
            kbArticleService.publishArticle(id);
            return Result.OK("发布成功!");
        } catch (Exception e) {
            log.error("发布知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量发布知识条目
     *
     * @param ids 知识条目ID列表
     * @return 发布结果
     */
    @AutoLog(value = "知识条目-批量发布")
    @Operation(summary = "知识条目-批量发布", description = "知识条目-批量发布")
    @PostMapping(value = "/publishBatch")
    public Result<String> publishBatch(@RequestParam(name = "ids", required = true) String ids) {
        try {
            List<String> idList = Arrays.asList(ids.split(","));
            kbArticleService.publishBatchArticle(idList);
            return Result.OK("批量发布成功!");
        } catch (Exception e) {
            log.error("批量发布知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 归档知识条目
     *
     * @param id 知识条目ID
     * @return 归档结果
     */
    @AutoLog(value = "知识条目-归档")
    @Operation(summary = "知识条目-归档", description = "知识条目-归档")
    @PostMapping(value = "/archive")
    public Result<String> archive(@RequestParam(name = "id", required = true) String id) {
        try {
            kbArticleService.archiveArticle(id);
            return Result.OK("归档成功!");
        } catch (Exception e) {
            log.error("归档知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量归档知识条目
     *
     * @param ids 知识条目ID列表
     * @return 归档结果
     */
    @AutoLog(value = "知识条目-批量归档")
    @Operation(summary = "知识条目-批量归档", description = "知识条目-批量归档")
    @PostMapping(value = "/archiveBatch")
    public Result<String> archiveBatch(@RequestParam(name = "ids", required = true) String ids) {
        try {
            List<String> idList = Arrays.asList(ids.split(","));
            kbArticleService.archiveBatchArticle(idList);
            return Result.OK("批量归档成功!");
        } catch (Exception e) {
            log.error("批量归档知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 点赞知识条目
     *
     * @param id 知识条目ID
     * @param request 请求对象
     * @return 点赞结果
     */
    @AutoLog(value = "知识条目-点赞")
    @Operation(summary = "知识条目-点赞", description = "知识条目-点赞")
    @PostMapping(value = "/like")
    public Result<String> like(@RequestParam(name = "id", required = true) String id, HttpServletRequest request) {
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute("loginUser");
            String userId = loginUser != null ? loginUser.getId() : "anonymous";
            kbArticleService.likeArticle(id, userId);
            return Result.OK("点赞成功!");
        } catch (Exception e) {
            log.error("点赞知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消点赞知识条目
     *
     * @param id 知识条目ID
     * @param request 请求对象
     * @return 取消点赞结果
     */
    @AutoLog(value = "知识条目-取消点赞")
    @Operation(summary = "知识条目-取消点赞", description = "知识条目-取消点赞")
    @PostMapping(value = "/unlike")
    public Result<String> unlike(@RequestParam(name = "id", required = true) String id, HttpServletRequest request) {
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute("loginUser");
            String userId = loginUser != null ? loginUser.getId() : "anonymous";
            kbArticleService.unlikeArticle(id, userId);
            return Result.OK("取消点赞成功!");
        } catch (Exception e) {
            log.error("取消点赞知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 收藏知识条目
     *
     * @param id 知识条目ID
     * @param request 请求对象
     * @return 收藏结果
     */
    @AutoLog(value = "知识条目-收藏")
    @Operation(summary = "知识条目-收藏", description = "知识条目-收藏")
    @PostMapping(value = "/collect")
    public Result<String> collect(@RequestParam(name = "id", required = true) String id, HttpServletRequest request) {
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute("loginUser");
            String userId = loginUser != null ? loginUser.getId() : "anonymous";
            kbArticleService.collectArticle(id, userId);
            return Result.OK("收藏成功!");
        } catch (Exception e) {
            log.error("收藏知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消收藏知识条目
     *
     * @param id 知识条目ID
     * @param request 请求对象
     * @return 取消收藏结果
     */
    @AutoLog(value = "知识条目-取消收藏")
    @Operation(summary = "知识条目-取消收藏", description = "知识条目-取消收藏")
    @PostMapping(value = "/uncollect")
    public Result<String> uncollect(@RequestParam(name = "id", required = true) String id, HttpServletRequest request) {
        try {
            LoginUser loginUser = (LoginUser) request.getAttribute("loginUser");
            String userId = loginUser != null ? loginUser.getId() : "anonymous";
            kbArticleService.uncollectArticle(id, userId);
            return Result.OK("取消收藏成功!");
        } catch (Exception e) {
            log.error("取消收藏知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分享知识条目
     *
     * @param id 知识条目ID
     * @return 分享结果
     */
    @AutoLog(value = "知识条目-分享")
    @Operation(summary = "知识条目-分享", description = "知识条目-分享")
    @PostMapping(value = "/share")
    public Result<String> share(@RequestParam(name = "id", required = true) String id) {
        try {
            kbArticleService.shareArticle(id);
            return Result.OK("分享成功!");
        } catch (Exception e) {
            log.error("分享知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 复制知识条目
     *
     * @param id 知识条目ID
     * @return 复制结果
     */
    @AutoLog(value = "知识条目-复制")
    @Operation(summary = "知识条目-复制", description = "知识条目-复制")
    @PostMapping(value = "/copy")
    public Result<String> copy(@RequestParam(name = "id", required = true) String id) {
        try {
            kbArticleService.copyArticle(id);
            return Result.OK("复制成功!");
        } catch (Exception e) {
            log.error("复制知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 移动知识条目到指定分类
     *
     * @param ids 知识条目ID列表
     * @param categoryId 目标分类ID
     * @return 移动结果
     */
    @AutoLog(value = "知识条目-移动")
    @Operation(summary = "知识条目-移动", description = "知识条目-移动")
    @PostMapping(value = "/move")
    public Result<String> move(@RequestParam(name = "ids", required = true) String ids,
                               @RequestParam(name = "categoryId", required = true) String categoryId) {
        try {
            List<String> idList = Arrays.asList(ids.split(","));
            kbArticleService.moveArticles(idList, categoryId);
            return Result.OK("移动成功!");
        } catch (Exception e) {
            log.error("移动知识条目失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 统计各状态的知识条目数量
     *
     * @return 统计结果
     */
    @AutoLog(value = "知识条目-状态统计")
    @Operation(summary = "知识条目-状态统计", description = "知识条目-状态统计")
    @GetMapping(value = "/countByStatus")
    public Result<List<Object>> countByStatus() {
        List<Object> result = kbArticleService.countByStatus();
        return Result.OK(result);
    }

    /**
     * 统计各分类的知识条目数量
     *
     * @return 统计结果
     */
    @AutoLog(value = "知识条目-分类统计")
    @Operation(summary = "知识条目-分类统计", description = "知识条目-分类统计")
    @GetMapping(value = "/countByCategory")
    public Result<List<Object>> countByCategory() {
        List<Object> result = kbArticleService.countByCategory();
        return Result.OK(result);
    }

    /**
     * 导出excel
     *
     * @param request 请求对象
     * @param kbArticle 查询条件
     */
    @RequestMapping(value = "/exportXls")
    public void exportXls(HttpServletRequest request, KbArticle kbArticle, HttpServletResponse response) {
        super.exportXls(request, kbArticle, KbArticle.class, "知识条目");
    }

    /**
     * 通过excel导入数据
     *
     * @param request 请求对象
     * @param response 响应对象
     * @return 导入结果
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, KbArticle.class);
    }
}
