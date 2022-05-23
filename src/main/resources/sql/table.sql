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
    tenant_id bigint null comment '租户id',
    data_create_time timestamp null,
    data_update_time timestamp null,
    data_is_deleted tinyint default 0 null,
    constraint account
        primary key (id)
);