package org.jeecg.modules.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.knowledge.entity.KbCategory;
import org.jeecg.modules.knowledge.mapper.KbCategoryMapper;
import org.jeecg.modules.knowledge.service.IKbCategoryService;
import org.jeecg.modules.knowledge.vo.KbCategoryTreeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识分类Service实现类
 * 
 * @author seadragon
 * @since 2025-07-03
 */
@Service
public class KbCategoryServiceImpl extends ServiceImpl<KbCategoryMapper, KbCategory> implements IKbCategoryService {

    @Override
    public List<KbCategoryTreeVO> getCategoryTree() {
        // 获取所有分类
        List<KbCategory> allCategories = baseMapper.selectAllCategories();
        
        // 构建树形结构
        return buildCategoryTree(allCategories, null);
    }

    @Override
    public List<KbCategory> getChildrenByParentId(String parentId) {
        return baseMapper.selectByParentId(parentId);
    }

    @Override
    public KbCategory getByCategoryCode(String categoryCode) {
        return baseMapper.selectByCategoryCode(categoryCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCategory(KbCategory kbCategory) {
        // 设置默认值
        if (kbCategory.getSortNo() == null) {
            kbCategory.setSortNo(0);
        }
        if (kbCategory.getStatus() == null) {
            kbCategory.setStatus(1);
        }
        if (kbCategory.getIsLeaf() == null) {
            kbCategory.setIsLeaf(1);
        }
        
        kbCategory.setCreateTime(new Date());
        
        boolean result = this.save(kbCategory);
        
        // 如果有父分类，更新父分类的叶子节点状态
        if (oConvertUtils.isNotEmpty(kbCategory.getParentId())) {
            updateParentLeafStatus(kbCategory.getParentId());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCategory(KbCategory kbCategory) {
        kbCategory.setUpdateTime(new Date());
        return this.updateById(kbCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCategory(String id) {
        // 检查是否有子分类
        if (hasChildren(id)) {
            throw new RuntimeException("该分类下存在子分类，无法删除");
        }
        
        // 检查是否有知识条目
        if (hasArticles(id)) {
            throw new RuntimeException("该分类下存在知识条目，无法删除");
        }
        
        KbCategory category = this.getById(id);
        if (category == null) {
            return false;
        }
        
        // 逻辑删除
        category.setDelFlag(1);
        category.setUpdateTime(new Date());
        boolean result = this.updateById(category);
        
        // 更新父分类的叶子节点状态
        if (oConvertUtils.isNotEmpty(category.getParentId())) {
            updateParentLeafStatus(category.getParentId());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchCategory(List<String> ids) {
        for (String id : ids) {
            deleteCategory(id);
        }
        return true;
    }

    @Override
    public boolean checkCategoryCodeExists(String categoryCode, String excludeId) {
        LambdaQueryWrapper<KbCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KbCategory::getCategoryCode, categoryCode);
        queryWrapper.eq(KbCategory::getDelFlag, 0);
        
        if (oConvertUtils.isNotEmpty(excludeId)) {
            queryWrapper.ne(KbCategory::getId, excludeId);
        }
        
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean hasChildren(String parentId) {
        LambdaQueryWrapper<KbCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KbCategory::getParentId, parentId);
        queryWrapper.eq(KbCategory::getDelFlag, 0);
        
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean hasArticles(String categoryId) {
        Integer count = baseMapper.countArticlesByCategory(categoryId);
        return count != null && count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateParentLeafStatus(String parentId) {
        if (oConvertUtils.isEmpty(parentId)) {
            return;
        }
        
        boolean hasChildren = hasChildren(parentId);
        int isLeaf = hasChildren ? 0 : 1;
        
        baseMapper.updateLeafStatus(parentId, isLeaf);
    }

    @Override
    public List<String> getAllChildrenIds(String parentId) {
        return baseMapper.selectChildrenIds(parentId);
    }

    /**
     * 构建分类树形结构
     * 
     * @param categories 分类列表
     * @param parentId 父级ID
     * @return 树形结构
     */
    private List<KbCategoryTreeVO> buildCategoryTree(List<KbCategory> categories, String parentId) {
        List<KbCategoryTreeVO> treeList = new ArrayList<>();
        
        for (KbCategory category : categories) {
            String categoryParentId = category.getParentId();
            
            // 判断是否为当前层级的节点
            boolean isCurrentLevel = (parentId == null && categoryParentId == null) || 
                                   (parentId != null && parentId.equals(categoryParentId));
            
            if (isCurrentLevel) {
                KbCategoryTreeVO treeVO = new KbCategoryTreeVO();
                BeanUtils.copyProperties(category, treeVO);
                
                // 设置树组件需要的属性
                treeVO.setKey(category.getId());
                treeVO.setTitle(category.getCategoryName());
                treeVO.setValue(category.getId());
                
                // 查询知识条目数量
                Integer articleCount = baseMapper.countArticlesByCategory(category.getId());
                treeVO.setArticleCount(articleCount != null ? articleCount : 0);
                
                // 递归构建子节点
                List<KbCategoryTreeVO> children = buildCategoryTree(categories, category.getId());
                treeVO.setChildren(children);
                treeVO.setHasChildren(!children.isEmpty());
                
                treeList.add(treeVO);
            }
        }
        
        return treeList;
    }
}
