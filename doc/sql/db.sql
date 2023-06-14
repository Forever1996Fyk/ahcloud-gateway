-- auto-generated definition
create table gateway_route_definition
(
    id                          bigint auto_increment comment '主键id'
        primary key,
    app_name                    varchar(128)  default ''                not null comment 'app名称',
    route_id                    varchar(64)   default ''                not null comment '路由id',
    service_id                  varchar(128)  default ''                not null comment '服务id',
    uri                         varchar(128)  default ''                not null comment '路由uri',
    rpc_type                    varchar(16)   default ''                not null comment 'rpc类型',
    context_path                varchar(256)  default ''                not null comment '上下文路径',
    predicate_definition_config varchar(2048) default ''                not null comment '断言定义配置',
    filter_definition_config    varchar(2048) default ''                not null invisible comment '过滤器定义配置',
    env                         varchar(8)    default ''                not null comment '环境变量',
    route_type                  tinyint unsigned default 1 not null comment '路由类型',
    remark                      varchar(256)  default ''                not null comment '备注',
    creator                     varchar(64)                             not null comment '行记录创建者',
    modifier                    varchar(64)                             not null comment '行记录最近更新人',
    created_time                timestamp     default CURRENT_TIMESTAMP not null comment '行记录创建时间',
    modified_time               timestamp     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '行记录最近修改时间',
    version                     tinyint unsigned default '0' not null comment '行版本号',
    extension                   varchar(2048) default ''                not null comment '拓展字段',
    deleted                     bigint        default 0                 not null comment '是否删除',
    constraint uniq_route
        unique (env, app_name, route_id, service_id, deleted)
) comment '网关路由定义表';

-- auto-generated definition
create table gateway_api_meta_data
(
    id             bigint auto_increment comment '主键id'
        primary key,
    app_name       varchar(128)  default ''                not null comment 'app名称',
    api_path       varchar(128)  default ''                not null comment '请求路径',
    service_id     varchar(128)  default ''                not null comment '服务id',
    qualified_name varchar(256)  default ''                not null comment '全限定名',
    method_name    varchar(128)  default ''                not null comment '方法名',
    http_method    varchar(16)   default ''                not null comment 'method类型',
    consume        varchar(64)   default ''                not null comment '提交内容类型（Content-Type）',
    produce        varchar(64)   default ''                not null comment '返回的内容类型',
    rpc_type       varchar(16)   default ''                not null comment 'rpc类型',
    env            varchar(8)    default ''                not null comment '环境变量',
    remark         varchar(200)  default ''                not null comment '备注',
    creator        varchar(64)                             not null comment '行记录创建者',
    modifier       varchar(64)                             not null comment '行记录最近更新人',
    created_time   timestamp     default CURRENT_TIMESTAMP not null comment '行记录创建时间',
    modified_time  timestamp     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '行记录最近修改时间',
    version        tinyint unsigned default '0' not null comment '行版本号',
    extension      varchar(2048) default ''                not null comment '拓展字段',
    deleted        bigint        default 0                 not null comment '是否删除',
    constraint unique_qualified_method
        unique (env, app_name, qualified_name, method_name, deleted),
    constraint unique_service_api
        unique (env, app_name, api_path, service_id, deleted)
) comment '网关接口元数据表';

-- auto-generated definition
create table gateway_api
(
    id             bigint auto_increment comment '主键id'
        primary key,
    api_code       varchar(64)   default ''                not null comment '接口编码',
    api_name       varchar(50)   default ''                not null comment '接口名称',
    api_path       varchar(100)  default ''                not null comment '请求路径',
    service_id     varchar(100)  default ''                not null comment '服务id',
    qualified_name varchar(200)  default ''                not null comment '全限定名',
    method_name    varchar(100)  default ''                not null comment '方法名',
    http_method    varchar(16)   default ''                not null comment 'method类型',
    api_type       tinyint unsigned default '0' not null comment '接口类型',
    read_or_write  tinyint unsigned default '0' not null comment '读写类型',
    api_desc       varchar(200)  default ''                not null comment '接口描述',
    dev            tinyint       default 0                 not null comment '开发环境',
    test           tinyint       default 0                 not null comment '联调环境',
    sit            tinyint       default 0                 not null comment '测试环境',
    pre            tinyint       default 0                 not null comment '预发环境',
    prod           tinyint       default 0                 not null comment '生产环境',
    is_auth        tinyint unsigned default '1' not null comment '是否认证',
    changeable     tinyint(1) default 1 null comment '是否可变',
    remark         varchar(200)  default ''                not null comment '备注',
    creator        varchar(64)                             not null comment '行记录创建者',
    modifier       varchar(64)                             not null comment '行记录最近更新人',
    created_time   timestamp     default CURRENT_TIMESTAMP not null comment '行记录创建时间',
    modified_time  timestamp     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '行记录最近修改时间',
    version        tinyint unsigned default '0' not null comment '行版本号',
    extension      varchar(2048) default ''                not null comment '拓展字段',
    deleted        bigint        default 0                 not null comment '是否删除',
    constraint unique_api_code
        unique (api_code, deleted)
) comment '网关接口表';

insert into gateway_api (api_code, api_name, api_path, service_id, qualified_name, method_name, http_method, api_type, read_or_write, api_desc, dev, test, sit, pre, prod, is_auth, changeable, creator, modifier)
VALUES
    ('gateway.api.add', '新增api接口', '/ahcloud-gateway-api/api/add', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.ApiController', 'addApi', 'POST', 1, 2, '新增api接口', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM'),
    ('gateway.api.update', '更新api接口', '/ahcloud-gateway-api/api/update', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.ApiController', 'updateApi', 'POST', 1, 2, '更新api接口', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM'),
    ('gateway.api.deleteById', '根据id删除api接口', '/ahcloud-gateway-api/api/deleteById/**', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.ApiController', 'deleteApiById', 'POST', 1, 2, '根据id删除api接口', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM'),
    ('gateway.api.findById', '根据id获取api接口', '/ahcloud-gateway-api/api/findById/**', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.ApiController', 'findById', 'GET', 1, 1, '根据id获取api接口', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM'),
    ('gateway.api.page', '分页查询api接口', '/ahcloud-gateway-api/api/page', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.ApiController', 'pageApiList', 'GET', 1, 1, '分页查询api接口', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM'),
    ('gateway.api.offlineApi', '下线api接口', '/ahcloud-gateway-api/api/offlineApi/**', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.ApiController', 'offlineApi', 'POST', 1, 2, '下线api接口', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM'),
    ('gateway.api.offlineApi', '上线api接口', '/ahcloud-gateway-api/api/onlineApi/**', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.ApiController', 'onlineApi', 'POST', 1, 2, '上线api接口', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM'),

    ('gateway.metadata.selectServiceIdList', '获取服务id选择列表', '/ahcloud-gateway-api/metadata/selectServiceIdList', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.GatewayApiMetaDataController', 'selectServiceIdList', 'GET', 1, 1, '获取服务id选择列表', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM'),
    ('gateway.metadata.selectApiMetadataList', '根据服务id获取api元数据选择列表', '/ahcloud-gateway-api/metadata/selectApiMetadataList', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.GatewayApiMetaDataController', 'selectApiMetadataList', 'GET', 1, 1, '根据服务id获取api元数据选择列表', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM'),

    ('gateway.refresh.refreshApi', '刷新api接口', '/ahcloud-gateway-api/refresh/refreshApi', 'ahcloud-gateway-api', 'com.ahcloud.gateway.api.controller.RefreshController', 'refreshApi', 'POST', 1, 2, '刷新api接口', 1,1,1,1,1,1,0,'SYSTEM', 'SYSTEM');

insert into gateway_route_definition (app_name, route_id, service_id, uri, rpc_type, context_path, predicate_definition_config, filter_definition_config, env, route_type, creator, modifier)
VALUES
    ('ahcloud-gateway-api', 'ahcloud-gateway-api', 'ahcloud-gateway-api', 'lb://ahcloud-gateway-api', 'springCloud', 'ahcloud-gateway-api', '[{"name":"Path","args":{"_genkey_0":"/ahcloud-gateway-api/**"}}]', '[{"name":"StripPrefix","args":{"_genkey_0":"1"}}]', 'dev', 1, 'SYSTEM', 'SYSTEM'),
    ('ahcloud-gateway-api', 'ahcloud-gateway-api', 'ahcloud-gateway-api', 'lb://ahcloud-gateway-api', 'springCloud', 'ahcloud-gateway-api', '[{"name":"Path","args":{"_genkey_0":"/ahcloud-gateway-api/**"}}]', '[{"name":"StripPrefix","args":{"_genkey_0":"1"}}]', 'test', 1, 'SYSTEM', 'SYSTEM'),
    ('ahcloud-gateway-api', 'ahcloud-gateway-api', 'ahcloud-gateway-api', 'lb://ahcloud-gateway-api', 'springCloud', 'ahcloud-gateway-api', '[{"name":"Path","args":{"_genkey_0":"/ahcloud-gateway-api/**"}}]', '[{"name":"StripPrefix","args":{"_genkey_0":"1"}}]', 'sit', 1, 'SYSTEM', 'SYSTEM'),
    ('ahcloud-gateway-api', 'ahcloud-gateway-api', 'ahcloud-gateway-api', 'lb://ahcloud-gateway-api', 'springCloud', 'ahcloud-gateway-api', '[{"name":"Path","args":{"_genkey_0":"/ahcloud-gateway-api/**"}}]', '[{"name":"StripPrefix","args":{"_genkey_0":"1"}}]', 'pre', 1, 'SYSTEM', 'SYSTEM'),
    ('ahcloud-gateway-api', 'ahcloud-gateway-api', 'ahcloud-gateway-api', 'lb://ahcloud-gateway-api', 'springCloud', 'ahcloud-gateway-api', '[{"name":"Path","args":{"_genkey_0":"/ahcloud-gateway-api/**"}}]', '[{"name":"StripPrefix","args":{"_genkey_0":"1"}}]', 'prod', 1, 'SYSTEM', 'SYSTEM');
