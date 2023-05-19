-- auto-generated definition
create table gateway_api_register
(
    id             bigint auto_increment comment '主键id'
        primary key,
    api_path       varchar(100)     default ''                not null comment '请求路径',
    service_id     varchar(100)     default ''                not null comment '服务id',
    qualified_name varchar(200)     default ''                not null comment '全限定名',
    method_name    varchar(100)     default ''                not null comment '方法名',
    http_method    varchar(16)      default ''                not null comment 'method类型',
    consume        varchar(64)      default ''                not null comment '提交内容类型（Content-Type）',
    produce        varchar(64)      default ''                not null comment '返回的内容类型',
    env            varchar(8)       default ''                not null comment '环境变量',
    remark         varchar(200)     default ''                not null comment '备注',
    creator        varchar(64)                                not null comment '行记录创建者',
    modifier       varchar(64)                                not null comment '行记录最近更新人',
    created_time   timestamp        default CURRENT_TIMESTAMP not null comment '行记录创建时间',
    modified_time  timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '行记录最近修改时间',
    version        tinyint unsigned default '0'               not null comment '行版本号',
    extension      varchar(2048)    default ''                not null comment '拓展字段',
    deleted        bigint           default 0                 not null comment '是否删除',
    constraint unique_qualified_method
        unique (qualified_name, method_name, deleted),
    constraint unique_service_api
        unique (api_path, service_id, deleted)
)
    comment '网关接口注册表';

-- auto-generated definition
create table gateway_api
(
    id             bigint auto_increment comment '主键id'
        primary key,
    api_code       varchar(64)      default ''                not null comment '接口编码',
    api_name       varchar(50)      default ''                not null comment '接口名称',
    api_path       varchar(100)     default ''                not null comment '请求路径',
    service_id     varchar(100)     default ''                not null comment '服务id',
    qualified_name varchar(200)     default ''                not null comment '全限定名',
    method_name    varchar(100)     default ''                not null comment '方法名',
    api_type       tinyint unsigned default '0'               not null comment '接口类型',
    read_or_write  tinyint unsigned default '0'               not null comment '读写类型',
    api_desc       varchar(200)     default ''                not null comment '接口描述',
    dev            tinyint          default 0                 not null comment '开发环境状态',
    test           tinyint          default 0                 not null comment '联调环境',
    sit            tinyint          default 0                 not null comment '测试环境状态',
    pre            tinyint          default 0                 not null comment '预发环境状态',
    prod           tinyint          default 0                 not null comment '生产环境状态',
    is_auth        tinyint unsigned default '1'               not null comment '是否认证',
    changeable     tinyint(1)       default 1                 null comment '是否可变',
    remark         varchar(200)     default ''                not null comment '备注',
    creator        varchar(64)                                not null comment '行记录创建者',
    modifier       varchar(64)                                not null comment '行记录最近更新人',
    created_time   timestamp        default CURRENT_TIMESTAMP not null comment '行记录创建时间',
    modified_time  timestamp        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '行记录最近修改时间',
    version        tinyint unsigned default '0'               not null comment '行版本号',
    extension      varchar(2048)    default ''                not null comment '拓展字段',
    deleted        bigint           default 0                 not null comment '是否删除',
    constraint unique_api_code
        unique (api_code, deleted)
)
    comment '网关接口表';

