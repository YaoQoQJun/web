
-- ----------------------------
-- 增加多租户参数配置
-- ----------------------------
INSERT INTO "BLADEX"."BLADE_PARAM"("ID", "PARAM_NAME", "PARAM_KEY", "PARAM_VALUE", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES ('1238706101399142402', '租户默认管理密码', 'tenant.default.password', 'admin', NULL, 1123598821738675201, 1123598813738675201, TO_DATE('2020-03-14 13:58:43', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-03-14 13:58:43', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);
INSERT INTO "BLADEX"."BLADE_PARAM"("ID", "PARAM_NAME", "PARAM_KEY", "PARAM_VALUE", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES ('1238706160295559170', '租户默认账号额度', 'tenant.default.accountNumber', '100', NULL, 1123598821738675201, 1123598813738675201, TO_DATE('2020-03-14 13:58:57', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-03-14 13:58:57', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);
INSERT INTO "BLADEX"."BLADE_PARAM"("ID", "PARAM_NAME", "PARAM_KEY", "PARAM_VALUE", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES ('1238706330076790786', '租户默认菜单集合', 'tenant.default.menuCode', 'desk,flow,work,monitor,resource,role,user,dept,post,dictbiz,topmenu', NULL, 1123598821738675201, 1123598813738675201, TO_DATE('2020-03-14 13:59:38', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-03-14 13:59:38', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);


-- ----------------------------
-- 增加用户表字段
-- ----------------------------
ALTER TABLE "BLADEX"."BLADE_USER"
    ADD ("CODE" NVARCHAR2(12) )
    ADD ("POST_ID" NVARCHAR2(1000) );
COMMENT ON COLUMN "BLADEX"."BLADE_USER"."CODE" IS '用户编号';
COMMENT ON COLUMN "BLADEX"."BLADE_USER"."POST_ID" IS '岗位id';

-- ----------------------------
-- 增加岗位管理表
-- ----------------------------
CREATE TABLE "BLADEX"."BLADE_POST" (
    "ID" NUMBER(20) NOT NULL ,
    "TENANT_ID" NVARCHAR2(12) ,
    "CATEGORY" NUMBER(11) ,
    "POST_CODE" VARCHAR2(12) ,
    "POST_NAME" VARCHAR2(64) ,
    "SORT" NUMBER(11) ,
    "REMARK" NVARCHAR2(255) ,
    "CREATE_USER" NUMBER(20) ,
    "CREATE_DEPT" NUMBER(20) ,
    "CREATE_TIME" DATE ,
    "UPDATE_USER" NUMBER(20) ,
    "UPDATE_TIME" DATE ,
    "STATUS" NUMBER(11) ,
    "IS_DELETED" NUMBER(11) ,
    PRIMARY KEY ("ID")
);
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."ID" IS '主键';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."TENANT_ID" IS '租户ID';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."CATEGORY" IS '岗位类型';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."POST_CODE" IS '岗位编号';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."POST_NAME" IS '岗位名称';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."SORT" IS '岗位排序';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."REMARK" IS '岗位描述';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."CREATE_USER" IS '创建人';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."CREATE_DEPT" IS '创建部门';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."CREATE_TIME" IS '创建时间';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."UPDATE_USER" IS '修改人';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."UPDATE_TIME" IS '修改时间';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."STATUS" IS '状态';
COMMENT ON COLUMN "BLADEX"."BLADE_POST"."IS_DELETED" IS '是否已删除';
COMMENT ON TABLE "BLADEX"."BLADE_POST" IS '岗位表';

-- ----------------------------
-- 增加岗位管理表数据
-- ----------------------------
INSERT INTO "BLADE_POST"("ID", "TENANT_ID", "CATEGORY", "POST_CODE", "POST_NAME", "SORT", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES (1123598817738675201, '000000', 1, 'ceo', '首席执行官', 1, '总经理', 1123598821738675201, 1123598813738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);
INSERT INTO "BLADE_POST"("ID", "TENANT_ID", "CATEGORY", "POST_CODE", "POST_NAME", "SORT", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES (1123598817738675202, '000000', 1, 'coo', '首席运营官', 2, '常务总经理', 1123598821738675201, 1123598813738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);
INSERT INTO "BLADE_POST"("ID", "TENANT_ID", "CATEGORY", "POST_CODE", "POST_NAME", "SORT", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES (1123598817738675203, '000000', 1, 'cfo', '首席财务官', 3, '财务总经理', 1123598821738675201, 1123598813738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);
INSERT INTO "BLADE_POST"("ID", "TENANT_ID", "CATEGORY", "POST_CODE", "POST_NAME", "SORT", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES (1123598817738675204, '000000', 1, 'cto', '首席技术官', 4, '技术总监', 1123598821738675201, 1123598813738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);
INSERT INTO "BLADE_POST"("ID", "TENANT_ID", "CATEGORY", "POST_CODE", "POST_NAME", "SORT", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES (1123598817738675205, '000000', 1, 'cio', '首席信息官', 5, '信息总监', 1123598821738675201, 1123598813738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);
INSERT INTO "BLADE_POST"("ID", "TENANT_ID", "CATEGORY", "POST_CODE", "POST_NAME", "SORT", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES (1123598817738675206, '000000', 2, 'pm', '技术经理', 6, '研发和产品是永远的朋友', 1123598821738675201, 1123598813738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);
INSERT INTO "BLADE_POST"("ID", "TENANT_ID", "CATEGORY", "POST_CODE", "POST_NAME", "SORT", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES (1123598817738675207, '000000', 2, 'hrm', '人力经理', 7, '人力资源部门工作管理者', 1123598821738675201, 1123598813738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);
INSERT INTO "BLADE_POST"("ID", "TENANT_ID", "CATEGORY", "POST_CODE", "POST_NAME", "SORT", "REMARK", "CREATE_USER", "CREATE_DEPT", "CREATE_TIME", "UPDATE_USER", "UPDATE_TIME", "STATUS", "IS_DELETED")
VALUES (1123598817738675208, '000000', 3, 'staff', '普通员工', 8, '普通员工', 1123598821738675201, 1123598813738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1123598821738675201, TO_DATE('2020-04-01 00:00:00', 'SYYYY-MM-DD HH24:MI:SS'), 1, 0);

-- ----------------------------
-- 增加岗位管理菜单数据
-- ----------------------------
INSERT INTO "BLADEX"."BLADE_MENU"("ID", "PARENT_ID", "CODE", "NAME", "ALIAS", "PATH", "SOURCE", "SORT", "CATEGORY", "ACTION", "IS_OPEN", "REMARK", "IS_DELETED")
VALUES ('1164733389668962251', '1123598815738675203', 'post', '岗位管理', 'menu', '/system/post', 'iconfont iconicon_message', 2, 1, 0, 1, NULL, 0);
INSERT INTO "BLADEX"."BLADE_MENU"("ID", "PARENT_ID", "CODE", "NAME", "ALIAS", "PATH", "SOURCE", "SORT", "CATEGORY", "ACTION", "IS_OPEN", "REMARK", "IS_DELETED")
VALUES ('1164733389668962252', '1164733389668962251', 'post_add', '新增', 'add', '/system/post/add', 'plus', 1, 2, 1, 1, NULL, 0);
INSERT INTO "BLADEX"."BLADE_MENU"("ID", "PARENT_ID", "CODE", "NAME", "ALIAS", "PATH", "SOURCE", "SORT", "CATEGORY", "ACTION", "IS_OPEN", "REMARK", "IS_DELETED")
VALUES ('1164733389668962253', '1164733389668962251', 'post_edit', '修改', 'edit', '/system/post/edit', 'form', 2, 2, 2, 1, NULL, 0);
INSERT INTO "BLADEX"."BLADE_MENU"("ID", "PARENT_ID", "CODE", "NAME", "ALIAS", "PATH", "SOURCE", "SORT", "CATEGORY", "ACTION", "IS_OPEN", "REMARK", "IS_DELETED")
VALUES ('1164733389668962254', '1164733389668962251', 'post_delete', '删除', 'delete', '/api/blade-system/post/remove', 'delete', 3, 2, 3, 1, NULL, 0);
INSERT INTO "BLADEX"."BLADE_MENU"("ID", "PARENT_ID", "CODE", "NAME", "ALIAS", "PATH", "SOURCE", "SORT", "CATEGORY", "ACTION", "IS_OPEN", "REMARK", "IS_DELETED")
VALUES ('1164733389668962255', '1164733389668962251', 'post_view', '查看', 'view', '/system/post/view', 'file-text', 4, 2, 2, 1, NULL, 0);

-- ----------------------------
-- 增加岗位管理菜单权限数据
-- ----------------------------
INSERT INTO "BLADEX"."BLADE_ROLE_MENU"(ID,MENU_ID,ROLE_ID)
VALUES ('1161272893875225001', '1164733389668962251', '1123598816738675201');
INSERT INTO "BLADEX"."BLADE_ROLE_MENU"(ID,MENU_ID,ROLE_ID)
VALUES ('1161272893875225002', '1164733389668962252', '1123598816738675201');
INSERT INTO "BLADEX"."BLADE_ROLE_MENU"(ID,MENU_ID,ROLE_ID)
VALUES ('1161272893875225003', '1164733389668962253', '1123598816738675201');
INSERT INTO "BLADEX"."BLADE_ROLE_MENU"(ID,MENU_ID,ROLE_ID)
VALUES ('1161272893875225004', '1164733389668962254', '1123598816738675201');
INSERT INTO "BLADEX"."BLADE_ROLE_MENU"(ID,MENU_ID,ROLE_ID)
VALUES ('1161272893875225005', '1164733389668962255', '1123598816738675201');
INSERT INTO "BLADEX"."BLADE_ROLE_MENU"(ID,MENU_ID,ROLE_ID)
VALUES ('1161272893875225006', '1164733389668962256', '1123598816738675201');

-- ----------------------------
-- 增加岗位类型字典数据
-- ----------------------------
INSERT INTO "BLADEX"."BLADE_DICT"(ID, PARENT_ID, CODE, DICT_KEY, DICT_VALUE, SORT, REMARK, IS_SEALED, IS_DELETED)
VALUES (1123598814738777220, 0, 'post_category', '-1', '岗位类型', 12, NULL, 0, 0);
INSERT INTO "BLADEX"."BLADE_DICT"(ID, PARENT_ID, CODE, DICT_KEY, DICT_VALUE, SORT, REMARK, IS_SEALED, IS_DELETED)
VALUES (1123598814738777221, 1123598814738777220, 'post_category', '1', '高层', 1, NULL, 0, 0);
INSERT INTO "BLADEX"."BLADE_DICT"(ID, PARENT_ID, CODE, DICT_KEY, DICT_VALUE, SORT, REMARK, IS_SEALED, IS_DELETED)
VALUES (1123598814738777222, 1123598814738777220, 'post_category', '2', '中层', 2, NULL, 0, 0);
INSERT INTO "BLADEX"."BLADE_DICT"(ID, PARENT_ID, CODE, DICT_KEY, DICT_VALUE, SORT, REMARK, IS_SEALED, IS_DELETED)
VALUES (1123598814738777223, 1123598814738777220, 'post_category', '3', '基层', 3, NULL, 0, 0);
INSERT INTO "BLADEX"."BLADE_DICT"(ID, PARENT_ID, CODE, DICT_KEY, DICT_VALUE, SORT, REMARK, IS_SEALED, IS_DELETED)
VALUES (1123598814738777224, 1123598814738777220, 'post_category', '4', '其他', 4, NULL, 0, 0);
