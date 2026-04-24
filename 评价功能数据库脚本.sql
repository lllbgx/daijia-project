-- 订单评价表
CREATE TABLE `order_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `customer_id` bigint NOT NULL COMMENT '顾客ID',
  `rate` int NOT NULL COMMENT '评分，1星~5星',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态，1未申诉，2已申诉，3申诉失败，4申诉成功',
  `instance_id` varchar(100) DEFAULT NULL COMMENT '申诉工作流ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识，0未删除，1已删除',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_driver_id` (`driver_id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单评价表';

-- 添加一些测试数据（可选）
INSERT INTO `order_comment` (
  `order_id`, `driver_id`, `customer_id`, `rate`, `remark`, `status`,
  `create_time`, `update_time`
) VALUES
(1, 1001, 2001, 5, '服务很好，司机很专业！', 1, NOW(), NOW()),
(2, 1002, 2001, 4, '整体不错，等待时间有点长', 1, NOW(), NOW()),
(3, 1001, 2003, 3, '一般般', 1, NOW(), NOW());

-- 更新订单表的状态字段（如果尚未完成）
-- 假设订单ID为1,2,3的订单已完成
UPDATE `order_info`
SET `order_status` = '已完成',
    `update_time` = NOW()
WHERE `id` IN (1, 2, 3);