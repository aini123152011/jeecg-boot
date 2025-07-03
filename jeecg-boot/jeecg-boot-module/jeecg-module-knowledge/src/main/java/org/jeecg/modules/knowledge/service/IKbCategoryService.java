package org.jeecg.modules.knowledge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.knowledge.entity.KbCategory;
import org.jeecg.modules.knowledge.vo.KbCategoryTreeVO;

import java.util.List;

/**
 * 知识分类Service接口
 * 
 * @author seadragon
 * @since 2025-07-03
 */
public interface IKbCategoryService extends IService<KbCategory> {

    /**
     * 获取分类树形结构
     * 
     * @return 分类树
     */
    List<KbCategoryTreeVO> getCategoryTree();

    /**
     * 根据父级ID获取子分类列表
     * 
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<KbCategory> getChildrenByParentId(String parentId);

    /**
     * 根据分类编码获取分类信息
     * 
     * @param categoryCode 分类编码
     * @return 分类信息
     */
    KbCategory getByCategoryCode(String categoryCode);

    /**
     * 保存分类信息
     * 
     * @param kbCategory 分类信息
     * @return 保存结果
     */
    boolean saveCategory(KbCategory kbCategory);

    /**
     * 更新分类信息
     * 
     * @param kbCategory 分类信息
     * @return 更新结果
     */
    boolean updateCategory(KbCategory kbCategory);

    /**
     * 删除分类（逻辑删除）
     * 
     * @param id 分类ID
     * @return 删除结果
     */
    boolean deleteCategory(String id);

    /**
     * 批量删除分类
     * 
     * @param ids 分类ID列表
     * @return 删除结果
     */
    boolean deleteBatchCategory(List<String> ids);

    /**
     * 检查分类编码是否存在
     * 
     * @param categoryCode 分类编码
     * @param excludeId 排除的ID（用于更新时检查）
     * @return 是否存在
     */
    boolean checkCategoryCodeExists(String categoryCode, String excludeId);

    /**
     * 检查分类是否有子分类
     * 
     * @param parentId 父级ID
     * @return 是否有子分类
     */
    boolean hasChildren(String parentId);

    /**
     * 检查分类下是否有知识条目
     * 
     * @param categoryId 分类ID
     * @return 是否有知识条目
     */
    boolean hasArticles(String categoryId);

    /**
     * 更新父分类的叶子节点状态
     * 
     * @param parentId 父级ID
     */
    void updateParentLeafStatus(String parentId);

    /**
     * 获取分类的所有子分类ID（递归）
     * 
     * @param parentId 父级ID
     * @return 子分类ID列表
     */
    List<String> getAllChildrenIds(String parentId);
}
