package org.jeecg.modules.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.knowledge.entity.KbCategory;

import java.util.List;

/**
 * 知识分类Mapper接口
 * 
 * @author seadragon
 * @since 2025-07-03
 */
public interface KbCategoryMapper extends BaseMapper<KbCategory> {

    /**
     * 根据父级ID查询子分类列表
     * 
     * @param parentId 父级ID
     * @return 子分类列表
     */
    @Select("SELECT * FROM kb_category WHERE parent_id = #{parentId} AND del_flag = 0 ORDER BY sort_no ASC, create_time ASC")
    List<KbCategory> selectByParentId(@Param("parentId") String parentId);

    /**
     * 查询根分类列表
     * 
     * @return 根分类列表
     */
    @Select("SELECT * FROM kb_category WHERE parent_id IS NULL AND del_flag = 0 ORDER BY sort_no ASC, create_time ASC")
    List<KbCategory> selectRootCategories();

    /**
     * 查询所有分类树形结构
     * 
     * @return 分类列表
     */
    @Select("SELECT * FROM kb_category WHERE del_flag = 0 ORDER BY sort_no ASC, create_time ASC")
    List<KbCategory> selectAllCategories();

    /**
     * 根据分类编码查询分类
     * 
     * @param categoryCode 分类编码
     * @return 分类信息
     */
    @Select("SELECT * FROM kb_category WHERE category_code = #{categoryCode} AND del_flag = 0")
    KbCategory selectByCategoryCode(@Param("categoryCode") String categoryCode);

    /**
     * 更新叶子节点状态
     * 
     * @param id 分类ID
     * @param isLeaf 是否叶子节点
     */
    @Update("UPDATE kb_category SET is_leaf = #{isLeaf} WHERE id = #{id}")
    void updateLeafStatus(@Param("id") String id, @Param("isLeaf") Integer isLeaf);

    /**
     * 查询分类下的知识条目数量
     * 
     * @param categoryId 分类ID
     * @return 知识条目数量
     */
    @Select("SELECT COUNT(*) FROM kb_article WHERE category_id = #{categoryId} AND del_flag = 0")
    Integer countArticlesByCategory(@Param("categoryId") String categoryId);

    /**
     * 递归查询所有子分类ID
     * 
     * @param parentId 父级ID
     * @return 子分类ID列表
     */
    List<String> selectChildrenIds(@Param("parentId") String parentId);
}
