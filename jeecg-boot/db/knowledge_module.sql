-- =============================================
-- 知识库管理模块数据库脚本
-- 作者: seadragon
-- 日期: 2025-07-03
-- 描述: 知识库管理系统的完整数据库结构和初始化数据
-- =============================================

-- 设置字符集和外键检查
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =============================================
-- 1. 数据表结构
-- =============================================

-- 1.1 知识分类表
DROP TABLE IF EXISTS `kb_category`;
CREATE TABLE `kb_category` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父级ID',
  `category_name` varchar(100) NOT NULL COMMENT '分类名称',
  `category_code` varchar(50) NOT NULL COMMENT '分类编码',
  `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
  `icon` varchar(100) DEFAULT NULL COMMENT '分类图标',
  `sort_no` int(11) DEFAULT '0' COMMENT '排序号',
  `is_leaf` tinyint(1) DEFAULT '1' COMMENT '是否叶子节点(0:否,1:是)',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除状态(0:正常,1:已删除)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_code` (`category_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort_no` (`sort_no`),
  KEY `idx_status` (`status`),
  KEY `idx_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识分类表';

-- 1.2 知识条目表
DROP TABLE IF EXISTS `kb_article`;
CREATE TABLE `kb_article` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `title` varchar(200) NOT NULL COMMENT '知识标题',
  `summary` varchar(1000) DEFAULT NULL COMMENT '知识摘要',
  `content` longtext COMMENT '知识内容',
  `content_type` varchar(20) DEFAULT 'markdown' COMMENT '内容类型(markdown,html,text)',
  `category_id` varchar(36) DEFAULT NULL COMMENT '分类ID',
  `keywords` varchar(500) DEFAULT NULL COMMENT '关键词',
  `source_type` varchar(20) DEFAULT 'manual' COMMENT '来源类型(manual:手动创建,import:导入,url:网页抓取)',
  `source_url` varchar(500) DEFAULT NULL COMMENT '来源URL',
  `attachment_ids` varchar(1000) DEFAULT NULL COMMENT '附件ID列表(逗号分隔)',
  `view_count` int(11) DEFAULT '0' COMMENT '浏览次数',
  `like_count` int(11) DEFAULT '0' COMMENT '点赞次数',
  `collect_count` int(11) DEFAULT '0' COMMENT '收藏次数',
  `comment_count` int(11) DEFAULT '0' COMMENT '评论次数',
  `share_count` int(11) DEFAULT '0' COMMENT '分享次数',
  `quality_score` decimal(3,2) DEFAULT '0.00' COMMENT '质量评分(0-5分)',
  `status` varchar(20) DEFAULT 'draft' COMMENT '状态(draft:草稿,reviewing:审核中,published:已发布,archived:已归档)',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `is_top` tinyint(1) DEFAULT '0' COMMENT '是否置顶(0:否,1:是)',
  `is_recommend` tinyint(1) DEFAULT '0' COMMENT '是否推荐(0:否,1:是)',
  `is_public` tinyint(1) DEFAULT '1' COMMENT '是否公开(0:私有,1:公开)',
  `version` int(11) DEFAULT '1' COMMENT '版本号',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除状态(0:正常,1:已删除)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_title` (`title`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_is_top` (`is_top`),
  KEY `idx_is_recommend` (`is_recommend`),
  KEY `idx_view_count` (`view_count`),
  KEY `idx_like_count` (`like_count`),
  KEY `idx_del_flag` (`del_flag`),
  KEY `idx_create_time` (`create_time`),
  FULLTEXT KEY `ft_content` (`title`,`summary`,`content`,`keywords`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识条目表';

-- 1.3 用户点赞记录表
DROP TABLE IF EXISTS `kb_user_like`;
CREATE TABLE `kb_user_like` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `article_id` varchar(36) NOT NULL COMMENT '知识条目ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_article_like` (`user_id`,`article_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户点赞记录表';

-- 1.4 用户收藏记录表
DROP TABLE IF EXISTS `kb_user_collect`;
CREATE TABLE `kb_user_collect` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `article_id` varchar(36) NOT NULL COMMENT '知识条目ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_article_collect` (`user_id`,`article_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏记录表';

-- 1.5 知识评论表
DROP TABLE IF EXISTS `kb_comment`;
CREATE TABLE `kb_comment` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `article_id` varchar(36) NOT NULL COMMENT '知识条目ID',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父评论ID',
  `user_id` varchar(32) NOT NULL COMMENT '评论用户ID',
  `content` text NOT NULL COMMENT '评论内容',
  `like_count` int(11) DEFAULT '0' COMMENT '点赞次数',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除状态(0:正常,1:已删除)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识评论表';

-- 1.6 知识标签表
DROP TABLE IF EXISTS `kb_tag`;
CREATE TABLE `kb_tag` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称',
  `tag_color` varchar(20) DEFAULT NULL COMMENT '标签颜色',
  `description` varchar(200) DEFAULT NULL COMMENT '标签描述',
  `use_count` int(11) DEFAULT '0' COMMENT '使用次数',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0:禁用,1:启用)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tag_name` (`tag_name`),
  KEY `idx_use_count` (`use_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识标签表';

-- 1.7 知识条目标签关联表
DROP TABLE IF EXISTS `kb_article_tag`;
CREATE TABLE `kb_article_tag` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `article_id` varchar(36) NOT NULL COMMENT '知识条目ID',
  `tag_id` varchar(36) NOT NULL COMMENT '标签ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_tag` (`article_id`,`tag_id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识条目标签关联表';

-- 1.8 知识访问日志表
DROP TABLE IF EXISTS `kb_access_log`;
CREATE TABLE `kb_access_log` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `article_id` varchar(36) NOT NULL COMMENT '知识条目ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `access_time` datetime DEFAULT NULL COMMENT '访问时间',
  `reading_time` int(11) DEFAULT '0' COMMENT '阅读时长(秒)',
  PRIMARY KEY (`id`),
  KEY `idx_article_id` (`article_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_access_time` (`access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识访问日志表';

-- 1.9 搜索历史表
DROP TABLE IF EXISTS `kb_search_history`;
CREATE TABLE `kb_search_history` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `keyword` varchar(200) NOT NULL COMMENT '搜索关键词',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `result_count` int(11) DEFAULT '0' COMMENT '搜索结果数量',
  `search_time` datetime DEFAULT NULL COMMENT '搜索时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_keyword` (`user_id`,`keyword`),
  KEY `idx_keyword` (`keyword`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_search_time` (`search_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索历史表';

-- 1.9 搜索历史表
DROP TABLE IF EXISTS `kb_search_history`;
CREATE TABLE `kb_search_history` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `keyword` varchar(200) NOT NULL COMMENT '搜索关键词',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `result_count` int(11) DEFAULT '0' COMMENT '搜索结果数量',
  `search_time` datetime DEFAULT NULL COMMENT '搜索时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_keyword` (`user_id`,`keyword`),
  KEY `idx_keyword` (`keyword`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_search_time` (`search_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索历史表';

-- =============================================
-- 2. 数据字典配置
-- =============================================

-- 2.1 添加字典类型
INSERT IGNORE INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`) VALUES
('knowledge_content_type', '知识内容类型', 'kb_content_type', '知识条目内容类型', 0, 'admin', NOW(), 0),
('knowledge_source_type', '知识来源类型', 'kb_source_type', '知识条目来源类型', 0, 'admin', NOW(), 0),
('knowledge_article_status', '知识条目状态', 'kb_article_status', '知识条目状态', 0, 'admin', NOW(), 0);

-- 2.2 添加字典项
INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`) VALUES
-- 内容类型
('kb_content_type_markdown', 'knowledge_content_type', 'Markdown', 'markdown', 'Markdown格式', 1, 1, 'admin', NOW()),
('kb_content_type_html', 'knowledge_content_type', 'HTML', 'html', 'HTML格式', 2, 1, 'admin', NOW()),
('kb_content_type_text', 'knowledge_content_type', '纯文本', 'text', '纯文本格式', 3, 1, 'admin', NOW()),
-- 来源类型
('kb_source_type_manual', 'knowledge_source_type', '手动创建', 'manual', '手动创建', 1, 1, 'admin', NOW()),
('kb_source_type_import', 'knowledge_source_type', '文件导入', 'import', '从文件导入', 2, 1, 'admin', NOW()),
('kb_source_type_url', 'knowledge_source_type', '网页抓取', 'url', '从网页抓取', 3, 1, 'admin', NOW()),
-- 文章状态
('kb_article_status_draft', 'knowledge_article_status', '草稿', 'draft', '草稿状态', 1, 1, 'admin', NOW()),
('kb_article_status_reviewing', 'knowledge_article_status', '审核中', 'reviewing', '审核中', 2, 1, 'admin', NOW()),
('kb_article_status_published', 'knowledge_article_status', '已发布', 'published', '已发布', 3, 1, 'admin', NOW()),
('kb_article_status_archived', 'knowledge_article_status', '已归档', 'archived', '已归档', 4, 1, 'admin', NOW());

-- =============================================
-- 3. 菜单权限配置
-- =============================================

-- 3.1 添加知识库主菜单
INSERT IGNORE INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`) VALUES
('knowledge_main', NULL, '知识库', '/knowledge', 'layouts/RouteView', NULL, NULL, 0, NULL, '1', 5, 0, 'book', 1, 0, 0, 0, 0, '知识库管理系统', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0);

-- 3.2 添加子菜单
INSERT IGNORE INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`) VALUES
('knowledge_category', 'knowledge_main', '知识分类', '/knowledge/category', 'knowledge/category/KbCategoryList', NULL, NULL, 1, NULL, '1', 1, 0, 'folder', 1, 1, 0, 0, 0, '知识分类管理', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article', 'knowledge_main', '知识条目', '/knowledge/article', 'knowledge/article/KbArticleList', NULL, NULL, 1, NULL, '1', 2, 0, 'file-text', 1, 1, 0, 0, 0, '知识条目管理', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_search', 'knowledge_main', '知识搜索', '/knowledge/search', 'knowledge/search/KbSearchList', NULL, NULL, 1, NULL, '1', 3, 0, 'search', 1, 1, 0, 0, 0, '知识搜索', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_statistics', 'knowledge_main', '知识统计', '/knowledge/statistics', 'knowledge/statistics/KbStatistics', NULL, NULL, 1, NULL, '1', 4, 0, 'bar-chart', 1, 1, 0, 0, 0, '知识统计分析', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0);

-- 3.3 添加知识分类管理按钮权限
INSERT IGNORE INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`) VALUES
('knowledge_category_add', 'knowledge_category', '添加', NULL, NULL, NULL, NULL, 2, 'knowledge:kbCategory:add', '1', 1, 0, NULL, 1, 1, 0, 0, 0, '知识分类添加', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_category_edit', 'knowledge_category', '编辑', NULL, NULL, NULL, NULL, 2, 'knowledge:kbCategory:edit', '1', 2, 0, NULL, 1, 1, 0, 0, 0, '知识分类编辑', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_category_delete', 'knowledge_category', '删除', NULL, NULL, NULL, NULL, 2, 'knowledge:kbCategory:delete', '1', 3, 0, NULL, 1, 1, 0, 0, 0, '知识分类删除', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_category_batch_delete', 'knowledge_category', '批量删除', NULL, NULL, NULL, NULL, 2, 'knowledge:kbCategory:deleteBatch', '1', 4, 0, NULL, 1, 1, 0, 0, 0, '知识分类批量删除', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_category_export', 'knowledge_category', '导出', NULL, NULL, NULL, NULL, 2, 'knowledge:kbCategory:exportXls', '1', 5, 0, NULL, 1, 1, 0, 0, 0, '知识分类导出', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_category_import', 'knowledge_category', '导入', NULL, NULL, NULL, NULL, 2, 'knowledge:kbCategory:importExcel', '1', 6, 0, NULL, 1, 1, 0, 0, 0, '知识分类导入', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0);

-- 3.4 添加知识条目管理按钮权限
INSERT IGNORE INTO `sys_permission` (`id`, `parent_id`, `name`, `url`, `component`, `component_name`, `redirect`, `menu_type`, `perms`, `perms_type`, `sort_no`, `always_show`, `icon`, `is_route`, `is_leaf`, `keep_alive`, `hidden`, `hide_tab`, `description`, `status`, `del_flag`, `rule_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `internal_or_external`) VALUES
('knowledge_article_add', 'knowledge_article', '添加', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:add', '1', 1, 0, NULL, 1, 1, 0, 0, 0, '知识条目添加', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_edit', 'knowledge_article', '编辑', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:edit', '1', 2, 0, NULL, 1, 1, 0, 0, 0, '知识条目编辑', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_delete', 'knowledge_article', '删除', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:delete', '1', 3, 0, NULL, 1, 1, 0, 0, 0, '知识条目删除', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_batch_delete', 'knowledge_article', '批量删除', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:deleteBatch', '1', 4, 0, NULL, 1, 1, 0, 0, 0, '知识条目批量删除', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_publish', 'knowledge_article', '发布', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:publish', '1', 5, 0, NULL, 1, 1, 0, 0, 0, '知识条目发布', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_batch_publish', 'knowledge_article', '批量发布', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:publishBatch', '1', 6, 0, NULL, 1, 1, 0, 0, 0, '知识条目批量发布', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_archive', 'knowledge_article', '归档', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:archive', '1', 7, 0, NULL, 1, 1, 0, 0, 0, '知识条目归档', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_batch_archive', 'knowledge_article', '批量归档', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:archiveBatch', '1', 8, 0, NULL, 1, 1, 0, 0, 0, '知识条目批量归档', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_copy', 'knowledge_article', '复制', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:copy', '1', 9, 0, NULL, 1, 1, 0, 0, 0, '知识条目复制', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_move', 'knowledge_article', '移动', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:move', '1', 10, 0, NULL, 1, 1, 0, 0, 0, '知识条目移动', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_export', 'knowledge_article', '导出', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:exportXls', '1', 11, 0, NULL, 1, 1, 0, 0, 0, '知识条目导出', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0),
('knowledge_article_import', 'knowledge_article', '导入', NULL, NULL, NULL, NULL, 2, 'knowledge:kbArticle:importExcel', '1', 12, 0, NULL, 1, 1, 0, 0, 0, '知识条目导入', '1', 0, 0, 'admin', NOW(), NULL, NULL, 0);

-- 3.5 为管理员角色分配知识库权限
INSERT IGNORE INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `data_rule_ids`, `operate_date`, `operate_ip`)
SELECT CONCAT('kb_perm_', UNIX_TIMESTAMP(), '_', (@row_number:=@row_number+1)), 'f6817f48af4fb3af11b9e8bf182f618b', `id`, NULL, NOW(), '127.0.0.1'
FROM `sys_permission`
CROSS JOIN (SELECT @row_number:=0) r
WHERE `id` LIKE 'knowledge%';

-- =============================================
-- 4. 初始化数据
-- =============================================

-- 4.1 插入根分类
INSERT IGNORE INTO `kb_category` (`id`, `parent_id`, `category_name`, `category_code`, `description`, `icon`, `sort_no`, `is_leaf`, `status`, `del_flag`, `create_by`, `create_time`) VALUES
('1', NULL, '技术文档', 'tech_doc', '技术相关的文档资料', 'file-text', 1, 0, 1, 0, 'admin', NOW()),
('2', NULL, '业务知识', 'business', '业务流程和规范', 'briefcase', 2, 0, 1, 0, 'admin', NOW()),
('3', NULL, '常见问题', 'faq', '常见问题解答', 'question-circle', 3, 0, 1, 0, 'admin', NOW());

-- 4.2 插入子分类
INSERT IGNORE INTO `kb_category` (`id`, `parent_id`, `category_name`, `category_code`, `description`, `icon`, `sort_no`, `is_leaf`, `status`, `del_flag`, `create_by`, `create_time`) VALUES
('11', '1', 'Java开发', 'java_dev', 'Java开发相关文档', 'coffee', 1, 1, 1, 0, 'admin', NOW()),
('12', '1', '前端开发', 'frontend_dev', '前端开发相关文档', 'html5', 2, 1, 1, 0, 'admin', NOW()),
('13', '1', '数据库', 'database', '数据库相关文档', 'database', 3, 1, 1, 0, 'admin', NOW()),
('21', '2', '产品需求', 'product_req', '产品需求文档', 'file-search', 1, 1, 1, 0, 'admin', NOW()),
('22', '2', '操作手册', 'operation_manual', '系统操作手册', 'book', 2, 1, 1, 0, 'admin', NOW()),
('31', '3', '系统问题', 'system_issue', '系统相关问题', 'bug', 1, 1, 1, 0, 'admin', NOW()),
('32', '3', '使用问题', 'usage_issue', '使用相关问题', 'user', 2, 1, 1, 0, 'admin', NOW());

-- 4.3 插入示例标签
INSERT IGNORE INTO `kb_tag` (`id`, `tag_name`, `tag_color`, `description`, `use_count`, `status`, `create_by`, `create_time`) VALUES
('tag1', 'Java', '#f50', 'Java相关', 0, 1, 'admin', NOW()),
('tag2', 'Spring', '#2db7f5', 'Spring框架', 0, 1, 'admin', NOW()),
('tag3', 'Vue', '#87d068', 'Vue.js', 0, 1, 'admin', NOW()),
('tag4', 'MySQL', '#108ee9', 'MySQL数据库', 0, 1, 'admin', NOW()),
('tag5', '教程', '#f56a00', '教程类文档', 0, 1, 'admin', NOW());

-- =============================================
-- 5. 恢复外键检查
-- =============================================
SET FOREIGN_KEY_CHECKS = 1;
