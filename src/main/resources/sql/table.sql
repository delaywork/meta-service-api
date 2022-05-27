CREATE TABLE if not exists tenant
(
    id bigint not null,
    data_create_time timestamp null,
    data_update_time timestamp null,
    data_is_deleted tinyint default 0 null,
    constraint tenant
        primary key (id)
);

CREATE TABLE if not exists account
(
    id bigint not null,
    openid varchar(64) null comment '微信 openid',
    unionid varchar(64) null comment '微信 unionid',
    name varchar(255) null comment '账户名称',
    email varchar(255) null comment '邮箱',
    area_code varchar(128) null comment '手机前缀',
    phone varchar(128) null comment '手机',
    password varchar(255) null comment '用户密码',
    avatar_url varchar(255) null comment '用户头像',
    time_zone int null comment '时区配置',
    time_zone_text varchar(128) null comment '时区名称',
    language_type varchar(128) null comment '语言配置',
    tenant_id bigint null comment '租户id',
    data_create_time timestamp null,
    data_update_time timestamp null,
    data_is_deleted tinyint default 0 null,
    constraint account
        primary key (id)
);

CREATE TABLE if not exists account_authorization
(
    id bigint not null,
    account_id bigint not null comment '账户 id',
    type varchar(64) not null comment '授权类型',
    context text null comment '授权内容',
    tenant_id bigint null comment '租户id',
    data_create_time timestamp null comment '授权时间',
    data_update_time timestamp null,
    data_is_deleted tinyint default 0 null,
    constraint account_authorization
        primary key (id)
);

CREATE TABLE if not exists data_room
(
    id bigint not null,
    parent_id bigint null comment '父节点 id',
    type varchar(64) null comment '类型',
    name varchar(255) null comment '名称',
    comments varchar(2048) null comment '描述',
    cloud varchar(255) null comment '云',
    url varchar(255) null comment '文件地址（类型为"文件夹"则为空）',
    tenant_id bigint null comment '租户id',
    operation_account_id bigint null comment '操作人员id',
    data_create_time timestamp null,
    data_update_time timestamp null,
    data_is_deleted tinyint default 0 null,
    constraint data_room
        primary key (id)
);

CREATE TABLE if not exists meta
(
    id bigint not null,
    about text null comment '关于 meta',
    terms_conditions text null comment '条款',
    policy text null comment '策略',
    data_create_time timestamp null,
    data_update_time timestamp null,
    data_is_deleted tinyint default 0 null,
    constraint meta
        primary key (id)
);

