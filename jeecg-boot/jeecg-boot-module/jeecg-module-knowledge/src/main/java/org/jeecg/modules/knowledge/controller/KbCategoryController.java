package org.jeecg.modules.knowledge.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.knowledge.entity.KbCategory;
import org.jeecg.modules.knowledge.service.IKbCategoryService;
import org.jeecg.modules.knowledge.vo.KbCategoryTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 知识分类Controller
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Tag(name = "知识分类管理", description = "知识分类管理相关接口")
@RestController
@RequestMapping("/knowledge/kbCategory")
@Slf4j
public class KbCategoryController extends JeecgController<KbCategory, IKbCategoryService> {

    @Autowired
    private IKbCategoryService kbCategoryService;

    /**
     * 分页列表查询
     *
     * @param kbCategory 查询条件
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param req 请求对象
     * @return 分页结果
     */
    @AutoLog(value = "知识分类-分页列表查询")
    @Operation(summary = "知识分类-分页列表查询", description = "知识分类-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<KbCategory>> queryPageList(KbCategory kbCategory,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   HttpServletRequest req) {
        QueryWrapper<KbCategory> queryWrapper = QueryGenerator.initQueryWrapper(kbCategory, req.getParameterMap());
        queryWrapper.orderByAsc("sort_no").orderByAsc("create_time");
        
        Page<KbCategory> page = new Page<KbCategory>(pageNo, pageSize);
        IPage<KbCategory> pageList = kbCategoryService.page(page, queryWrapper);
        
        return Result.OK(pageList);
    }

    /**
     * 获取分类树形结构
     *
     * @return 分类树
     */
    @AutoLog(value = "知识分类-获取树形结构")
    @Operation(summary = "知识分类-获取树形结构", description = "知识分类-获取树形结构")
    @GetMapping(value = "/tree")
    public Result<List<KbCategoryTreeVO>> getCategoryTree() {
        List<KbCategoryTreeVO> treeList = kbCategoryService.getCategoryTree();
        return Result.OK(treeList);
    }

    /**
     * 根据父级ID获取子分类
     *
     * @param parentId 父级ID
     * @return 子分类列表
     */
    @AutoLog(value = "知识分类-获取子分类")
    @Operation(summary = "知识分类-获取子分类", description = "知识分类-获取子分类")
    @GetMapping(value = "/children")
    public Result<List<KbCategory>> getChildren(@RequestParam(name = "parentId", required = false) String parentId) {
        List<KbCategory> children = kbCategoryService.getChildrenByParentId(parentId);
        return Result.OK(children);
    }

    /**
     * 添加分类
     *
     * @param kbCategory 分类信息
     * @return 添加结果
     */
    @AutoLog(value = "知识分类-添加")
    @Operation(summary = "知识分类-添加", description = "知识分类-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody KbCategory kbCategory) {
        // 检查分类编码是否存在
        if (kbCategoryService.checkCategoryCodeExists(kbCategory.getCategoryCode(), null)) {
            return Result.error("分类编码已存在");
        }
        
        kbCategoryService.saveCategory(kbCategory);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑分类
     *
     * @param kbCategory 分类信息
     * @return 编辑结果
     */
    @AutoLog(value = "知识分类-编辑")
    @Operation(summary = "知识分类-编辑", description = "知识分类-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody KbCategory kbCategory) {
        // 检查分类编码是否存在
        if (kbCategoryService.checkCategoryCodeExists(kbCategory.getCategoryCode(), kbCategory.getId())) {
            return Result.error("分类编码已存在");
        }
        
        kbCategoryService.updateCategory(kbCategory);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除分类
     *
     * @param id 分类ID
     * @return 删除结果
     */
    @AutoLog(value = "知识分类-通过id删除")
    @Operation(summary = "知识分类-通过id删除", description = "知识分类-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            kbCategoryService.deleteCategory(id);
            return Result.OK("删除成功!");
        } catch (Exception e) {
            log.error("删除分类失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除分类
     *
     * @param ids 分类ID列表
     * @return 删除结果
     */
    @AutoLog(value = "知识分类-批量删除")
    @Operation(summary = "知识分类-批量删除", description = "知识分类-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        try {
            List<String> idList = Arrays.asList(ids.split(","));
            kbCategoryService.deleteBatchCategory(idList);
            return Result.OK("批量删除成功!");
        } catch (Exception e) {
            log.error("批量删除分类失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 通过id查询分类
     *
     * @param id 分类ID
     * @return 分类信息
     */
    @AutoLog(value = "知识分类-通过id查询")
    @Operation(summary = "知识分类-通过id查询", description = "知识分类-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<KbCategory> queryById(@RequestParam(name = "id", required = true) String id) {
        KbCategory kbCategory = kbCategoryService.getById(id);
        if (kbCategory == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(kbCategory);
    }

    /**
     * 检查分类编码是否存在
     *
     * @param categoryCode 分类编码
     * @param id 排除的ID
     * @return 检查结果
     */
    @AutoLog(value = "知识分类-检查编码是否存在")
    @Operation(summary = "知识分类-检查编码是否存在", description = "知识分类-检查编码是否存在")
    @GetMapping(value = "/checkCode")
    public Result<Boolean> checkCategoryCode(@RequestParam(name = "categoryCode") String categoryCode,
                                             @RequestParam(name = "id", required = false) String id) {
        boolean exists = kbCategoryService.checkCategoryCodeExists(categoryCode, id);
        return Result.OK(exists);
    }

    /**
     * 导出excel
     *
     * @param request 请求对象
     * @param kbCategory 查询条件
     */
    @RequestMapping(value = "/exportXls")
    public void exportXls(HttpServletRequest request, KbCategory kbCategory, HttpServletResponse response) {
        super.exportXls(request, kbCategory, KbCategory.class, "知识分类");
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
        return super.importExcel(request, response, KbCategory.class);
    }
}
