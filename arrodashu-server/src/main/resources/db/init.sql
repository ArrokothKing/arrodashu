-- 大树（ArroDashu）数据库初始化脚本
-- MySQL 8.0

CREATE DATABASE IF NOT EXISTS arrodashu CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE arrodashu;

-- 用户表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    avatar VARCHAR(500) COMMENT '头像URL',
    nickname VARCHAR(50) COMMENT '昵称',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(200) COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户角色关联表
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 科表
CREATE TABLE tree_family (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '科ID',
    name VARCHAR(100) NOT NULL COMMENT '中文名',
    latin_name VARCHAR(100) NOT NULL COMMENT '拉丁学名',
    description TEXT COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科表';

-- 属表
CREATE TABLE tree_genus (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '属ID',
    name VARCHAR(100) NOT NULL COMMENT '中文名',
    latin_name VARCHAR(100) NOT NULL COMMENT '拉丁学名',
    family_id BIGINT NOT NULL COMMENT '所属科ID',
    description TEXT COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (family_id) REFERENCES tree_family(id) ON DELETE CASCADE,
    INDEX idx_family_id (family_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='属表';

-- 品种表
CREATE TABLE tree_species (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '品种ID',
    name VARCHAR(100) NOT NULL COMMENT '中文名',
    latin_name VARCHAR(100) NOT NULL COMMENT '拉丁学名',
    alias VARCHAR(200) COMMENT '别名',
    family_id BIGINT NOT NULL COMMENT '科ID',
    genus_id BIGINT NOT NULL COMMENT '属ID',
    -- 形态特征
    tree_type TINYINT COMMENT '树型：1-乔木，2-灌木，3-藤本',
    height_min INT COMMENT '最小高度(cm)',
    height_max INT COMMENT '最大高度(cm)',
    crown_shape VARCHAR(50) COMMENT '树冠形状',
    bark_desc TEXT COMMENT '树皮描述',
    leaf_desc TEXT COMMENT '叶子描述',
    flower_desc TEXT COMMENT '花描述',
    fruit_desc TEXT COMMENT '果实描述',
    -- 生长习性
    light_preference VARCHAR(50) COMMENT '光照需求',
    water_preference VARCHAR(50) COMMENT '水分需求',
    soil_preference VARCHAR(100) COMMENT '土壤要求',
    hardiness_zone VARCHAR(20) COMMENT '耐寒区',
    growth_rate VARCHAR(20) COMMENT '生长速度',
    -- 园林用途
    landscape_use TEXT COMMENT '园林用途',
    -- 其他
    description TEXT COMMENT '详细描述',
    origin VARCHAR(200) COMMENT '原产地',
    create_by BIGINT COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status TINYINT DEFAULT 1 COMMENT '状态：0-删除，1-正常',
    FOREIGN KEY (family_id) REFERENCES tree_family(id),
    FOREIGN KEY (genus_id) REFERENCES tree_genus(id),
    FOREIGN KEY (create_by) REFERENCES sys_user(id),
    INDEX idx_name (name),
    INDEX idx_latin_name (latin_name),
    INDEX idx_family_id (family_id),
    INDEX idx_genus_id (genus_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品种表';

-- 品种图片表
CREATE TABLE tree_image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图片ID',
    species_id BIGINT NOT NULL COMMENT '品种ID',
    url VARCHAR(500) NOT NULL COMMENT '图片URL',
    type TINYINT DEFAULT 1 COMMENT '类型：1-主图，2-细节图，3-花果图，4-叶图',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (species_id) REFERENCES tree_species(id) ON DELETE CASCADE,
    INDEX idx_species_id (species_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品种图片表';

-- 标签表
CREATE TABLE tree_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名',
    color VARCHAR(20) DEFAULT '#409EFF' COMMENT '颜色',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 品种标签关联表
CREATE TABLE tree_species_tag (
    species_id BIGINT NOT NULL COMMENT '品种ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (species_id, tag_id),
    FOREIGN KEY (species_id) REFERENCES tree_species(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tree_tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品种标签关联表';

-- 用户收藏表
CREATE TABLE user_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    species_id BIGINT NOT NULL COMMENT '品种ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_species (user_id, species_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (species_id) REFERENCES tree_species(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- 文章表
CREATE TABLE article (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文章ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT COMMENT '内容',
    summary VARCHAR(500) COMMENT '摘要',
    cover_image VARCHAR(500) COMMENT '封面图',
    category VARCHAR(50) COMMENT '分类：guide-养护指南，encyclopedia-百科，pest-病虫害',
    author_id BIGINT COMMENT '作者ID',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    status TINYINT DEFAULT 1 COMMENT '状态：0-草稿，1-已发布，2-下架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (author_id) REFERENCES sys_user(id),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 操作日志表
CREATE TABLE operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    operation VARCHAR(100) COMMENT '操作描述',
    method VARCHAR(200) COMMENT '请求方法',
    request_url VARCHAR(500) COMMENT '请求URL',
    request_params TEXT COMMENT '请求参数',
    response_data TEXT COMMENT '响应数据',
    ip VARCHAR(50) COMMENT 'IP地址',
    duration INT COMMENT '执行时长(ms)',
    status TINYINT COMMENT '状态：0-失败，1-成功',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 初始化角色数据
INSERT INTO sys_role (role_name, role_code, description) VALUES
('超级管理员', 'ROLE_SUPER_ADMIN', '系统超级管理员，拥有所有权限'),
('管理员', 'ROLE_ADMIN', '系统管理员'),
('普通用户', 'ROLE_USER', '普通注册用户'),
('访客', 'ROLE_GUEST', '未登录访客');

-- 初始化超级管理员账号（密码：admin123，已加密）
INSERT INTO sys_user (username, email, password, nickname, status) VALUES
('admin', 'admin@arrodashu.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '管理员', 1);

-- 关联超级管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1);

-- 初始化示例科数据
INSERT INTO tree_family (name, latin_name, description) VALUES
('松科', 'Pinaceae', '常绿乔木，叶针形或条形，球果'),
('柏科', 'Cupressaceae', '常绿乔木或灌木，叶鳞形或刺形'),
('蔷薇科', 'Rosaceae', '落叶或常绿乔木、灌木，花两性'),
('豆科', 'Fabaceae', '乔木、灌木或草本，蝶形花冠'),
('槭树科', 'Aceraceae', '落叶乔木或灌木，叶对生，翅果');

-- 初始化示例属数据
INSERT INTO tree_genus (name, latin_name, family_id, description) VALUES
('松属', 'Pinus', 1, '松科最大属，叶2-5针一束'),
('云杉属', 'Picea', 1, '叶四棱状条形，球果下垂'),
('侧柏属', 'Platycladus', 2, '鳞形叶，球果种鳞木质'),
('圆柏属', 'Sabina', 2, '叶刺形或鳞形，球果浆果状'),
('苹果属', 'Malus', 3, '落叶乔木，果为梨果'),
('樱属', 'Prunus', 3, '落叶乔木或灌木，核果'),
('槐属', 'Sophora', 4, '落叶乔木，圆锥花序'),
('槭属', 'Acer', 5, '叶对生，翅果具翅');

-- 初始化示例品种数据
INSERT INTO tree_species (name, latin_name, alias, family_id, genus_id, tree_type, height_min, height_max, light_preference, water_preference, soil_preference, landscape_use, description, origin, create_by) VALUES
('油松', 'Pinus tabuliformis', '短叶松、红皮松', 1, 1, 1, 1500, 2500, '阳性', '耐旱', '适应性强', '园林绿化、荒山造林', '常绿乔木，树皮灰褐色，裂成不规则鳞状块片。叶2针一束，粗硬。球果卵形。', '中国', 1),
('雪松', 'Cedrus deodara', '香柏、喜马拉雅雪松', 1, 1, 1, 2000, 3000, '阳性', '适中', '排水良好', '庭园观赏、行道树', '常绿乔木，树冠塔形。叶针形，灰绿色。球果椭圆形。', '喜马拉雅山区', 1),
('银杏', 'Ginkgo biloba', '白果树、公孙树', 3, 6, 1, 2000, 4000, '阳性', '适中', '深厚肥沃', '行道树、庭荫树、观叶树', '落叶大乔木，树皮灰褐色。叶扇形，秋季金黄色。种子椭圆形。', '中国', 1),
('樱花', 'Prunus serrulata', '山樱花、福岛樱', 3, 6, 1, 400, 1200, '阳性', '适中', '疏松肥沃', '观花树、庭园树', '落叶乔木，树皮紫褐色。花粉红色或白色，春季盛开。', '中国、日本', 1),
('红枫', 'Acer palmatum', '鸡爪槭、日本红枫', 5, 8, 1, 200, 500, '半阴', '适中', '湿润肥沃', '庭园观赏、盆景', '落叶小乔木，叶掌状5-7裂，春夏季红色，秋季更艳。', '日本、中国', 1);

-- 初始化标签数据
INSERT INTO tree_tag (name, color) VALUES
('常绿', '#67C23A'),
('落叶', '#E6A23C'),
('观花', '#F56C6C'),
('观叶', '#409EFF'),
('观果', '#909399'),
('耐寒', '#79C6C6'),
('耐旱', '#B88230'),
('速生', '#9B59B6'),
('行道树', '#1ABC9C'),
('庭荫树', '#3498DB'),
('盆景', '#E74C3C'),
('珍稀', '#F39C12');
