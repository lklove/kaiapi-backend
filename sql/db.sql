-- 接口信息
create table if not exists kaiapi.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestParams` text  comment '请求参数',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态 （0 - 关闭，1 - 开启）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
    ) comment '接口信息';

insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('徐潇然', '丁健雄', 'www.irwin-gulgowski.io', '秦梓晨', '秦弘文', 0, '彭苑博', 712);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('尹鸿煊', '邱峻熙', 'www.dee-heathcote.biz', '蒋浩然', '袁彬', 0, '石伟诚', 51);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('许煜城', '卢煜祺', 'www.lakeesha-hand.info', '曹君浩', '莫鸿煊', 0, '赖瑞霖', 226627);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('傅立诚', '郑烨霖', 'www.dottie-hamill.biz', '卢明哲', '戴峻熙', 0, '范懿轩', 1574498);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('戴楷瑞', '孙哲瀚', 'www.ellis-wunsch.net', '韦天宇', '石旭尧', 0, '熊立辉', 72);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('谢明辉', '赖耀杰', 'www.eun-jacobi.info', '洪明杰', '侯果', 0, '史晟睿', 722);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('周健雄', '萧志泽', 'www.monte-satterfield.info', '万明辉', '覃健雄', 0, '袁致远', 1373831);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('孙黎昕', '蔡鸿涛', 'www.yen-macejkovic.co', '袁鹏飞', '郭烨霖', 0, '龚擎苍', 2240123);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('龚明哲', '石昊然', 'www.jami-franecki.com', '程晓啸', '莫子骞', 0, '魏弘文', 443408712);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('陶鹏煊', '石浩宇', 'www.leigh-becker.net', '魏弘文', '蔡子涵', 0, '罗凯瑞', 869);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('雷浩然', '赵子默', 'www.raymond-kuhic.info', '方君浩', '熊瑾瑜', 0, '朱智渊', 713991);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('田哲瀚', '张浩宇', 'www.dana-corkery.name', '邹志泽', '曾钰轩', 0, '唐风华', 1455);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('钱烨霖', '程懿轩', 'www.chong-leuschke.net', '彭哲瀚', '姚鑫磊', 0, '任潇然', 7685528);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('江思远', '姜金鑫', 'www.quinn-nitzsche.net', '薛子骞', '苏思源', 0, '曹烨霖', 136537);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('韦越泽', '杨鑫磊', 'www.mary-metz.biz', '邱胤祥', '田耀杰', 0, '覃峻熙', 8575);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('杨睿渊', '郭远航', 'www.brenton-senger.info', '蒋立辉', '朱文昊', 0, '董天翊', 748);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('赖明轩', '廖浩然', 'www.ula-schuster.info', '范天宇', '尹展鹏', 0, '曾子轩', 317630676);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('邱明', '胡浩轩', 'www.latisha-connelly.io', '陈伟宸', '王明哲', 0, '谭鸿煊', 47222648);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('叶天翊', '高昊天', 'www.michel-connelly.co', '周文轩', '梁建辉', 0, '刘浩', 9);
insert into kaiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('贾伟宸', '秦绍齐', 'www.rafael-hill.net', '宋晓博', '邵志泽', 0, '孔瑞霖', 28843);

-- 用户接口信息关系表
create table if not exists kaiapi.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '用户id',
    `interfaceInfoId` bigint not null comment '接口id',
    `totalNum`int default 0 not null comment '总的调用次数）',
    `leftNum`int default 0 not null comment '剩余调用次数）',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户接口信息关系表';