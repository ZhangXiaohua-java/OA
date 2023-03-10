-- ----------------------------
-- 1.顾客个人信息表
-- ----------------------------
create table t_customer(

                           id char(19) primary key comment "使用雪花id作为主键",

                           phone char(11) not null comment "顾客联系手机号",

                           password char(60) not null comment "顾客登陆密码",

                           gender char(1) default '1' comment "0女,1男",

                           customer_name varchar(50) comment "顾客的真实姓名",

                           id_num char(18) comment "用户身份证号,如果为空则不允许邮寄任何物品",

                           avatar varchar(128) comment "用户头像地址",

                           login_ip varchar(11) comment "用户上次登陆的ip地址,后续可做异常登陆处理等.",

                           login_date datetime comment "上次登陆时间",

                           register_date datetime comment "注册时间",

                           level char(1) default 0 comment "会员等级,0-9,后面再处理"

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '顾客个人信息表';

-- ----------------------------
-- 2.顾客积分表
-- ----------------------------
create table t_integral(

                           id char(19) primary key comment "雪花id做主键",

                           customer_id char(19) not null comment "顾客id",

                           current_integral bigint(10) default 0 "本次消费后的积分",

                           last_integral bigint(10) default 0 "本次消费前的积分",

                           order_id not null comment "对应的订单号id"

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '顾客积分表';

-- ----------------------------
-- 3.顾客收货地址列表
-- ----------------------------
create table t_post_address(

                               id char(21) primary key comment "使用nanoid作为主键",

                               post_code char(6) comment "收货地址的邮编",

                               is_default char(1) comment "是否是默认收货地址",

                               recevier_name varchar(128) not null comment "收货人姓名",

                               phone char(11) not null  comment "收货人手机号"

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '顾客收货地址列表';


-- ----------------------------
-- 4.全国地理位置信息表
-- ----------------------------
create table t_area (
                        `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                        `pid` int(11) DEFAULT NULL COMMENT '父id',
                        `shortname` varchar(100) DEFAULT NULL COMMENT '简称',
                        `name` varchar(100) DEFAULT NULL COMMENT '名称',
                        `merger_name` varchar(255) DEFAULT NULL COMMENT '全称',
                        `level` tinyint(4) DEFAULT NULL COMMENT '层级 0 1 2 省市区县',
                        `pinyin` varchar(100) DEFAULT NULL COMMENT '拼音',
                        `code` varchar(100) DEFAULT NULL COMMENT '长途区号',
                        `zip_code` varchar(100) DEFAULT NULL COMMENT '邮编',
                        `first` varchar(50) DEFAULT NULL COMMENT '首字母',
                        `lng` varchar(100) DEFAULT NULL COMMENT '经度',
                        `lat` varchar(100) DEFAULT NULL COMMENT '纬度',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3750 DEFAULT CHARSET=utf8mb4 comment '全国地理位置信息表';


-- ----------------------------
-- 5.邮寄货品历史表
-- ----------------------------
create table t_post_history(
                               id char(19) primary key not null comment "使用雪花id",

                               customer_id char(19) not null comment "顾客id",

                               order_id char(19) not null comment "订单号",

                               status char(1) default 0 comment "货品邮寄状态,0下单未发出,1已发出,2运输中,3已运达,4已签收,5售后中,6为止状态,7丢失",

                               update_time datetime not null comment "本次记录的更新时间,只保留三个月内的记录,超时系统自动删除"

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '邮寄货品历史表';


-- ----------------------------
-- 6.订单表
-- ----------------------------
create table t_post_order(
                             id char(19) primary key comment "雪花算法生成id",

                             customer_id not null comment "顾客id",

                             pay double(9,3) not null comment "支付金额",

                             discount double(9,3) default 0 comment "积分抵扣金额,可以选择是否使用",

                             use_discount char(1) default 0 comment "本次支付是否使用了积分,0未使用,1使用"

                                 order_time datetime not null comment "支付时间,下单即支付",

                             remark varchar(255) comment "订单备注信息",

                             create_by bigint(20) not null comment "创建订单的员工id",

                             goods_type int not null comment "运输商品的类型,后续会设计出一个字典表出来",

                             employee_name varchar(30) not null comment "创建订单的员工姓名,冗余信息"

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '订单表';


-- ----------------------------
-- 7.运输历史表
-- ----------------------------
create table t_transport(

                            id char(19) primary key comment "雪花id",

                            customer_id char(19) not null comment "顾客id",

                            order_id char(19) not null comment "订单号,订单号就作为用户查询物流信息的唯一凭证",

                            current_position char(6) not null comment "货品所在地址的邮编",

                            update_time datetime not null comment "每次物流信息更新的时间",

                            employee_id bigint(20) not null comment "操作的货品的员工id",

                            employee_name varchar(30) not null comment "员工姓名,冗余信息,减少查表次数",

                            remark varchar(255) comment "备注信息"


)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '运输历史表';


-- ----------------------------
-- 8.赔付信息表
-- ----------------------------
create table t_compensate (

                              id char(21) primary key comment "使用nanoid作为主键",

                              order_id char(19) not null comment "订单号",

                              customer_id char(19) not null comment "顾客id",

                              pay int  comment "赔付金额",

                              status char(1) not null comment "0未赔付,1沟通中,2已完成,3为止",

                              update_time datetime comment "更新时间",

                              create_by bigint(20) not null comment "记录创建者id",

                              employee_name varchar(30) comment "赔付最终处理人姓名",

                              responsibile_employee bigint(20) not null comment "责任人id"

)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment = '赔付信息表';


-- ----------------------------
-- 9.部门表
-- ----------------------------
create table sys_dept (
                          dept_id           bigint(20)      not null auto_increment    comment '部门id',
                          parent_id         bigint(20)      default 0                  comment '父部门id',
                          ancestors         varchar(50)     default ''                 comment '祖级列表',
                          dept_name         varchar(30)     default ''                 comment '部门名称',
                          order_num         int(4)          default 0                  comment '显示顺序',
                          leader            varchar(20)     default null               comment '负责人',
                          phone             varchar(11)     default null               comment '联系电话',
                          email             varchar(50)     default null               comment '邮箱',
                          status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
                          del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          create_by         varchar(64)     default ''                 comment '创建者',
                          create_time       datetime                                   comment '创建时间',
                          update_by         varchar(64)     default ''                 comment '更新者',
                          update_time       datetime                                   comment '更新时间',
                          primary key (dept_id)
) engine=innodb auto_increment=200 comment = '部门表';


-- ----------------------------
-- 10.员工信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
                          user_id           bigint(20)      not null auto_increment    comment '用户ID',
                          dept_id           bigint(20)      default null               comment '部门ID',
                          user_name         varchar(30)     not null                   comment '用户账号',
                          nick_name         varchar(30)     not null                   comment '用户昵称',
                          user_type         varchar(2)      default '00'               comment '用户类型（00系统用户）',
                          email             varchar(50)     default ''                 comment '用户邮箱',
                          phonenumber       varchar(11)     default ''                 comment '手机号码',
                          sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
                          avatar            varchar(100)    default ''                 comment '头像地址',
                          password          varchar(100)    default ''                 comment '密码',
                          status            char(1)         default '0'                comment '帐号状态（0正常 1停用）',
                          del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          login_ip          varchar(128)    default ''                 comment '最后登录IP',
                          login_date        datetime                                   comment '最后登录时间',
                          create_by         varchar(64)     default ''                 comment '创建者',
                          create_time       datetime                                   comment '创建时间',
                          update_by         varchar(64)     default ''                 comment '更新者',
                          update_time       datetime                                   comment '更新时间',
                          remark            varchar(500)    default null               comment '备注',
                          primary key (user_id)
) engine=innodb comment = '用户信息表';


-- ----------------------------
-- 11.岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
    post_id       bigint(20)      not null auto_increment    comment '岗位ID',
    post_code     varchar(64)     not null                   comment '岗位编码',
    post_name     varchar(50)     not null                   comment '岗位名称',
    post_sort     int(4)          not null                   comment '显示顺序',
    status        char(1)         not null                   comment '状态（0正常 1停用）',
    create_by     varchar(64)     default ''                 comment '创建者',
    create_time   datetime                                   comment '创建时间',
    update_by     varchar(64)     default ''                 comment '更新者',
    update_time   datetime                                   comment '更新时间',
    remark        varchar(500)    default null               comment '备注',
    primary key (post_id)
) engine=innodb comment = '岗位信息表';



-- ----------------------------
-- 12.用户与岗位关联表  用户1-N岗位
-- ----------------------------
create table sys_user_post
(
    user_id   bigint(20) not null comment '用户ID',
    post_id   bigint(20) not null comment '岗位ID',
    primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';
