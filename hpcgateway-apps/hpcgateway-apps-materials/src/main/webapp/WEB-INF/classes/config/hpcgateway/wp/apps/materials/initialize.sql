/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/9/20 16:29:49                           */
/*==============================================================*/


drop table if exists CMD_EXECUTE;

drop table if exists CMD_REQUEST;

drop table if exists COL_BATCH;

drop table if exists COL_INFO;

drop table if exists COL_NET;

drop table if exists HPC_ACL;

drop table if exists HPC_APP;

drop table if exists HPC_AUTHEN;

drop table if exists HPC_CLOUD_USER;

drop table if exists HPC_CLUSTER;

drop table if exists HPC_CLUSTER_TYPE;

drop table if exists HPC_CLUSTER_USER;

drop table if exists HPC_CONNECT;

drop table if exists HPC_CONNECT_TYPE;

drop table if exists HPC_GROUP;

drop table if exists HPC_GROUP_ALLOC;

drop table if exists HPC_INTERFACE;

drop table if exists HPC_JOB;

drop table if exists HPC_JOB_PRICE;

drop table if exists HPC_NODE;

drop table if exists HPC_NODE_ROLE;

drop table if exists HPC_NODE_ROLE_ALLOC;

drop table if exists HPC_PARAMETER;

drop table if exists HPC_PARAM_ASSIGN;

drop table if exists HPC_PAYMENT;

drop table if exists HPC_PROPERTY;

drop table if exists HPC_PROPERTY_ASSIGN;

drop table if exists HPC_RESOURCE;

drop table if exists HPC_RES_LTD;

drop table if exists HPC_RM;

drop table if exists HPC_ROLE;

drop table if exists HPC_ROLE_ALLOC;

/*==============================================================*/
/* Table: CMD_EXECUTE                                           */
/*==============================================================*/
create table CMD_EXECUTE
(
   CMD_EXECUTE_ID      bigint not null auto_increment,
   CMD_REQUEST_ID       bigint,
   CMD_EXECUTE_HOST     varchar(16) not null,
   CMD_EXECUTE_TIME     timestamp not null,
   CMD_EXECUTE_CODE     varchar(8) not null,
   CMD_EXECUTE_MSG      varchar(1024) not null,
   CMD_EXECUTE_EXIT_STATUS int not null,
   CMD_EXECUTE_STDERR   varchar(1048) not null,
   CMD_EXECUTE_STDOUT   varchar(8192) not null,
   CMD_EXECUTE_STATE    int not null,
   primary key (CMD_EXECUTE_ID)
);

/*==============================================================*/
/* Table: CMD_REQUEST                                           */
/*==============================================================*/
create table CMD_REQUEST
(
   CMD_REQUEST_ID      bigint not null auto_increment,
   HPC_CLUSTER_ID       bigint,
   CMD_REQUEST_TIME     timestamp not null,
   CMD_REQUEST_USERNAME varchar(32) not null,
   CMD_REQUEST_CMDLINE  varchar(1024) not null,
   CMD_REQUEST_STDIN    varchar(1024) not null,
   CMD_REQUEST_HOST_TYPE varchar(16) not null,
   CMD_REQUEST_HOST_EXPR varchar(200),
   CMD_REQUEST_STATE    int not null,
   primary key (CMD_REQUEST_ID)
);

/*==============================================================*/
/* Table: COL_BATCH                                             */
/*==============================================================*/
create table COL_BATCH
(
   COL_BATCH_ID        bigint not null auto_increment,
   HPC_CLUSTER_ID       bigint,
   COL_BATCH_NAME       varchar(48) not null,
   COL_BATCH_TIME       datetime not null,
   COL_BATCH_OPERATOR   varchar(32) not null,
   COL_BATCH_TOTAL      int not null,
   COL_BATCH_IDLE       int not null,
   COL_BATCH_COLLECTING int not null,
   COL_BATCH_SUCCESS    int not null,
   COL_BATCH_ERROR      int not null,
   COL_BATCH_STATE      varchar(16) not null,
   COL_BATCH_MESSAGE    varchar(255) not null,
   primary key (COL_BATCH_ID)
);

/*==============================================================*/
/* Table: COL_INFO                                              */
/*==============================================================*/
create table COL_INFO
(
   COL_INFO_ID         bigint not null auto_increment,
   COL_BATCH_ID         bigint,
   COL_HOSTNAME         varchar(32) not null,
   COL_INFO_TIMESTAMP   timestamp not null,
   COL_INFO_IP          varchar(32) not null,
   COL_INFO_POWER_TYPE  varchar(16),
   COL_INFO_POWER_COMMAND varchar(255),
   COL_INFO_POWER_USER  varchar(16),
   COL_INFO_POWER_PASS  varchar(16),
   COL_INFO_POWER_STATUS char(1) not null,
   COL_INFO_POWER_IDENTIFIER varchar(32),
   COL_INFO_POWER_IP    varchar(20),
   COL_INFO_POWER_NETMASK varchar(20),
   COL_INFO_POWER_GATEWAY varchar(20),
   COL_INFO_POWER_HOSTNAMEALIAS varchar(128),
   COL_INFO_STATE       varchar(16) not null,
   COL_INFO_STEP        varchar(16) not null,
   COL_INFO_MESSAGE     varchar(255),
   COL_INFO_CLEANBUSY   char(1) not null,
   COL_BIOS_VENDER      varchar(128),
   COL_BIOS_VERSION     varchar(32),
   COL_BIOS_RELEASE_DATE varchar(20),
   COL_SYS_MANUFACTURER varchar(128),
   COL_SYS_PRODUCT_NAME varchar(128),
   COL_SYS_VERSION      varchar(32),
   COL_SYS_SERIAL_NUMBER varchar(32),
   COL_SYS_UUID         varchar(48),
   COL_BASEBOARD_MANUFACTURER varchar(128),
   COL_BASEBOARD_PRODUCT_NAME varchar(128),
   COL_BASEBOARD_VERSION varchar(32),
   COL_BASEBOARD_SERIAL_NUMBER varchar(32),
   COL_CHASSIS_MANUFACTURER varchar(128),
   COL_CHASSIS_TYPE     varchar(32),
   COL_CHASSIS_LOCK     varchar(32),
   COL_CHASSIS_VERSION  varchar(32),
   COL_CHASSIS_SERIAL_NUMBER varchar(32),
   primary key (COL_INFO_ID)
);

/*==============================================================*/
/* Table: COL_NET                                               */
/*==============================================================*/
create table COL_NET
(
   COL_NET_ID          bigint not null auto_increment,
   COL_INFO_ID          bigint,
   COL_NET_NAME         varchar(24) not null,
   COL_NET_IDENTIFIER   varchar(96) not null,
   COL_NET_IP           varchar(20),
   COL_NET_BOOTPROTO    varchar(16),
   COL_NET_ONBOOT       varchar(16),
   COL_NET_NETMASK      varchar(20),
   COL_NET_MTU          varchar(8),
   COL_NET_BROADCAST    varchar(20),
   COL_NET_GATEWAY      varchar(20),
   COL_NET_HOSTNAMEALIAS varchar(128),
   primary key (COL_NET_ID)
);

/*==============================================================*/
/* Table: HPC_ACL                                               */
/*==============================================================*/
create table HPC_ACL
(
   HPC_ACL_ID          bigint not null auto_increment,
   HPC_ACL_NAME         varchar(128) not null,
   HPC_ACL_CODE         varchar(64) not null,
   HPC_ACL_DESCRIPTION  varchar(1024),
   primary key (HPC_ACL_ID)
);

/*==============================================================*/
/* Table: HPC_APP                                               */
/*==============================================================*/
create table HPC_APP
(
   HPC_APP_ID          bigint not null auto_increment,
   HPC_APP_NAME         varchar(128) not null,
   HPC_APP_CODE         varchar(128),
   HPC_APP_DESC         varchar(1024),
   primary key (HPC_APP_ID)
);

/*==============================================================*/
/* Table: HPC_AUTHEN                                            */
/*==============================================================*/
create table HPC_AUTHEN
(
   HPC_AUTHEN_ID       bigint not null auto_increment,
   HPC_AUTHEN_NAME      varchar(128) not null,
   HPC_AUTHEN_TYPE      varchar(64) not null,
   HPC_AUTHEN_COMMAND   varchar(256) not null,
   HPC_AUTHEN_PORT      int not null,
   primary key (HPC_AUTHEN_ID)
);

/*==============================================================*/
/* Table: HPC_CLOUD_USER                                        */
/*==============================================================*/
create table HPC_CLOUD_USER
(
   HPC_USER_ID         bigint not null auto_increment,
   HPC_USER_NAME        varchar(512) not null,
   HPC_USER_PASSWD      varchar(512),
   HPC_USER_ASKTO       bigint,
   HPC_USER_BALANCE     decimal(12,2),
   HPC_USER_CHARGED     decimal(12,2),
   HPC_USER_CONSUMED    decimal(12,2),
   HPC_USER_JOB_FEE     decimal(12,2),
   HPC_USER_JOB_NUM     bigint,
   HPC_USER_JOB_HOSTS   bigint,
   HPC_USER_JOB_CORES   bigint,
   HPC_USER_JOB_TIME    datetime,
   HPC_USER_STORAGE_FEE decimal(12,2),
   HPC_USER_STORAGE_CAP decimal(12,2),
   HPC_USER_STORAGE_UNIT varchar(8),
   HPC_USER_STORAGE_TIME datetime,
   primary key (HPC_USER_ID)
);

/*==============================================================*/
/* Table: HPC_CLUSTER                                           */
/*==============================================================*/
create table HPC_CLUSTER
(
   HPC_CLUSTER_ID      bigint not null auto_increment,
   HPC_CLUSTER_TYPE_ID  bigint,
   HPC_AUTHEN_ID        bigint,
   HPC_RM_ID            bigint,
   HPC_ACL_ID           bigint,
   HPC_CLUSTER_NAME     varchar(256) not null,
   HPC_CLUSTER_DESC     varchar(1024),
   HPC_CLUSTER_DEFRM    bigint,
   HPC_CLUSTER_USERNAME varchar(64) not null,
   HPC_CLUSTER_PASSWORD varchar(64) not null,
   HPC_CLUSTER_IDENTIFIER varchar(200) not null,
   HPC_CLUSTER_REALNAME varchar(64),
   HPC_CLUSTER_EMAIL    varchar(128),
   HPC_CLUSTER_PHONE    varchar(128),
   HPC_CLUSTER_WECHAT   varchar(128),
   primary key (HPC_CLUSTER_ID)
);

/*==============================================================*/
/* Table: HPC_CLUSTER_TYPE                                      */
/*==============================================================*/
create table HPC_CLUSTER_TYPE
(
   HPC_CLUSTER_TYPE_ID bigint not null auto_increment,
   HPC_CLUSTER_TYPE_NAME varchar(32) not null,
   HPC_CLUSTER_TYPE_CODE int not null,
   HPC_CLUSTER_TYPE_DESC varchar(200),
   primary key (HPC_CLUSTER_TYPE_ID)
);

/*==============================================================*/
/* Table: HPC_CLUSTER_USER                                      */
/*==============================================================*/
create table HPC_CLUSTER_USER
(
   HPC_CLSTR_USR_ID    bigint not null auto_increment,
   HPC_CLUSTER_ID       bigint,
   HPC_USER_ID          bigint,
   HPC_CLSTR_USR_NAME   varchar(256),
   HPC_CLSTR_USR_PASSWD varchar(256),
   HPC_CLSTR_USR_CRYPT  varchar(16),
   HPC_CLSTR_USR_AUTO   varchar(1),
   HPC_CLSTR_USR_IDRSA  varchar(1024),
   HPC_CLSTR_USR_BALANCE decimal(12,2),
   HPC_CLSTR_USR_CHARGED decimal(12,2),
   HPC_CLSTR_USR_CONSUMED decimal(12,2),
   HPC_CLSTR_USR_JOB_FEE decimal(12,2),
   HPC_CLSTR_USR_JOB_NUM bigint,
   HPC_CLSTR_USR_JOB_HOSTS bigint,
   HPC_CLSTR_USR_JOB_CORES bigint,
   HPC_CLSTR_USR_JOB_TIME datetime,
   HPC_CLSTR_USR_STRG_FEE decimal(12,2),
   HPC_CLSTR_USR_STRG_CAP decimal(12,2),
   HPC_CLSTR_USR_STRG_UNIT varchar(8),
   HPC_CLSTR_USR_STRG_TIME datetime,
   primary key (HPC_CLSTR_USR_ID)
);

/*==============================================================*/
/* Table: HPC_CONNECT                                           */
/*==============================================================*/
create table HPC_CONNECT
(
   HPC_CONN_ID         bigint not null auto_increment,
   HPC_CONN_TYPE_ID     bigint,
   HPC_NODE_ID          bigint,
   HPC_CONN_URL         varchar(1024),
   HPC_CONN_PORT        int,
   HPC_CONN_USER        varchar(512),
   HPC_CONN_PASS        varchar(512),
   HPC_CONN_CRYPT       varchar(126),
   primary key (HPC_CONN_ID)
);

/*==============================================================*/
/* Table: HPC_CONNECT_TYPE                                      */
/*==============================================================*/
create table HPC_CONNECT_TYPE
(
   HPC_CONN_TYPE_ID    bigint not null auto_increment,
   HPC_CONN_TYPE_NAME   varchar(32) not null,
   HPC_CONN_TYPE_CODE   int not null,
   HPC_CONN_TYPE_DESC   varchar(1024),
   primary key (HPC_CONN_TYPE_ID)
);

/*==============================================================*/
/* Table: HPC_GROUP                                             */
/*==============================================================*/
create table HPC_GROUP
(
   HPC_GROUP_ID        bigint not null auto_increment,
   HPC_GROUP_NAME       varchar(128) not null,
   HPC_GROUP_CODE       varchar(16),
   HPC_GROUP_DESC       varchar(1024),
   HPC_GROUP_PARENT     bigint,
   primary key (HPC_GROUP_ID)
);

/*==============================================================*/
/* Table: HPC_GROUP_ALLOC                                       */
/*==============================================================*/
create table HPC_GROUP_ALLOC
(
   HPC_GROUP_ALLOC_ID  bigint not null auto_increment,
   HPC_USER_ID          bigint,
   HPC_GROUP_ID         bigint,
   primary key (HPC_GROUP_ALLOC_ID)
);

/*==============================================================*/
/* Table: HPC_INTERFACE                                         */
/*==============================================================*/
create table HPC_INTERFACE
(
   HPC_IFC_ID          bigint not null auto_increment,
   HPC_NODE_ID          bigint,
   HPC_IFC_NAME         varchar(256) not null,
   HPC_IFC_ADDR         varchar(128) not null,
   HPC_IFC_ALIAS        varchar(1024) not null,
   primary key (HPC_IFC_ID)
);

/*==============================================================*/
/* Table: HPC_JOB                                               */
/*==============================================================*/
create table HPC_JOB
(
   HPC_JOB_ID          bigint not null auto_increment,
   HPC_APP_ID           bigint,
   HPC_CLUSTER_ID       bigint,
   HPC_CLSTR_USR_ID     bigint,
   HPC_JOB_PRICE_ID     bigint,
   HPC_JOB_NAME         varchar(1024) not null,
   HPC_JOB_END          datetime,
   HPC_JOB_START        datetime,
   HPC_JOB_SUBMIT_USER  bigint not null,
   HPC_HOB_PAID_USER    bigint not null,
   HPC_JOB_CORES        int,
   HPC_JOB_HOSTS        int,
   HPC_JOB_NUMBER       bigint,
   HPC_JOB_PRICE        decimal(8,2),
   HPC_JOB_UNIT         varchar(24),
   HPC_JOB_AMOUNT       decimal(12,2) not null,
   HPC_JOB_STATE        varchar(32) not null,
   primary key (HPC_JOB_ID)
);

/*==============================================================*/
/* Table: HPC_JOB_PRICE                                         */
/*==============================================================*/
create table HPC_JOB_PRICE
(
   HPC_JOB_PRICE_ID    bigint not null auto_increment,
   HPC_CLUSTER_ID       bigint,
   HPC_GROUP_ID         bigint,
   HPC_USER_ID          bigint,
   HPC_APP_ID           bigint,
   HPC_JOB_PRICE_VALUE  decimal(12,2) not null,
   primary key (HPC_JOB_PRICE_ID)
);

/*==============================================================*/
/* Table: HPC_NODE                                              */
/*==============================================================*/
create table HPC_NODE
(
   HPC_NODE_ID         bigint not null auto_increment,
   HPC_CLUSTER_ID       bigint,
   HPC_NODE_NAME        varchar(256) not null,
   HPC_NODE_ALIASNAMES  varchar(1024) not null,
   primary key (HPC_NODE_ID)
);

/*==============================================================*/
/* Table: HPC_NODE_ROLE                                         */
/*==============================================================*/
create table HPC_NODE_ROLE
(
   HPC_NODE_ROLE_ID    bigint not null auto_increment,
   HPC_NODE_ROLE_NAME   varchar(128) not null,
   HPC_NODE_ROLE_CODE   int not null,
   HPC_NODE_ROLE_DESC   varchar(1024),
   primary key (HPC_NODE_ROLE_ID)
);

/*==============================================================*/
/* Table: HPC_NODE_ROLE_ALLOC                                   */
/*==============================================================*/
create table HPC_NODE_ROLE_ALLOC
(
   HPC_NODE_ROLE_ALLOC_ID bigint not null auto_increment,
   HPC_NODE_ID          bigint,
   HPC_NODE_ROLE_ID     bigint,
   HPC_NODE_ROLE_ALLOC_NAME varchar(128) not null,
   HPC_NODE_ROLE_ALLOC_PRIORITY int not null,
   HPC_NODE_ROLE_ALLOC_DESCRIPTION varchar(1024),
   HPC_NODE_ROLE_ALLOC_TIME datetime not null,
   primary key (HPC_NODE_ROLE_ALLOC_ID)
);

/*==============================================================*/
/* Table: HPC_PARAMETER                                         */
/*==============================================================*/
create table HPC_PARAMETER
(
   HPC_PARAM_ID        bigint not null auto_increment,
   HPC_PARAM_NAME       varchar(512) not null,
   HPC_PARAM_DESC       varchar(512),
   primary key (HPC_PARAM_ID)
);

/*==============================================================*/
/* Table: HPC_PARAM_ASSIGN                                      */
/*==============================================================*/
create table HPC_PARAM_ASSIGN
(
   HPC_PARAM_ASSIGN_ID bigint not null auto_increment,
   HPC_PARAM_ID         bigint,
   HPC_NODE_ID          bigint,
   HPC_PARAM_ASSIGN_VALUE varchar(512) not null,
   HPC_PARAM_ASSIGN_TIME datetime not null,
   primary key (HPC_PARAM_ASSIGN_ID)
);

/*==============================================================*/
/* Table: HPC_PAYMENT                                           */
/*==============================================================*/
create table HPC_PAYMENT
(
   HPC_PAYMENT_ID      bigint not null auto_increment,
   HPC_CLSTR_USR_ID     bigint,
   HPC_PAYMENT_TIME     datetime not null,
   HPC_PAYMENT_AMOUNT   decimal(10,2) not null,
   HPC_PAYMENT_BALANCE  decimal(12,2) not null,
   primary key (HPC_PAYMENT_ID)
);

/*==============================================================*/
/* Table: HPC_PROPERTY                                          */
/*==============================================================*/
create table HPC_PROPERTY
(
   HPC_PROP_ID         bigint not null auto_increment,
   HPC_PROP_NAME        varchar(256) not null,
   HPC_PROP_DESC        varchar(512),
   primary key (HPC_PROP_ID)
);

/*==============================================================*/
/* Table: HPC_PROPERTY_ASSIGN                                   */
/*==============================================================*/
create table HPC_PROPERTY_ASSIGN
(
   HPC_PROPERTY_ASSIGN_ID bigint not null auto_increment,
   HPC_PROP_ID          bigint,
   HPC_NODE_ID          bigint,
   HPC_PROPERTY_ASSIGN_VALUE varchar(512) not null,
   HPC_PROPERTY_ASSIGN_TIME datetime not null,
   primary key (HPC_PROPERTY_ASSIGN_ID)
);

/*==============================================================*/
/* Table: HPC_RESOURCE                                          */
/*==============================================================*/
create table HPC_RESOURCE
(
   HPC_RESOURCE_ID     bigint not null auto_increment,
   HPC_CLUSTER_ID       bigint,
   HPC_RESOURCE_TYPE    varchar(256) not null,
   HPC_RESOURCE_UNIT    varchar(16) not null,
   HPC_RESOURCE_LIMIT   varchar(128) not null,
   primary key (HPC_RESOURCE_ID)
);

/*==============================================================*/
/* Table: HPC_RES_LTD                                           */
/*==============================================================*/
create table HPC_RES_LTD
(
   HPC_RES_LTD_ID      bigint not null auto_increment,
   HPC_CLSTR_USR_ID     bigint,
   HPC_RESOURCE_ID      bigint,
   HPC_RES_LTD_NUM      bigint not null,
   HPC_RES_LTD_UNIT     varchar(8) not null,
   primary key (HPC_RES_LTD_ID)
);

/*==============================================================*/
/* Table: HPC_RM                                                */
/*==============================================================*/
create table HPC_RM
(
   HPC_RM_ID           bigint not null auto_increment,
   HPC_CLUSTER_ID       bigint,
   HPC_RM_NAME          varchar(256) not null,
   HPC_RM_VERSION       varchar(32) not null,
   HPC_RM_TOPDIR        varchar(1024) not null,
   HPC_RM_BIN           varchar(1024),
   HPC_RM_SBIN          varchar(1024),
   HPC_RM_LIB           varchar(1024),
   HPC_RM_ETC           varchar(1024),
   HPC_RM_CONTROLL      bigint,
   HPC_RM_SCHEDULE      bigint,
   HPC_RM_ACCOUNT       bigint,
   HPC_RM_STORAGE       bigint,
   HC_RM_INSTALL        bigint,
   primary key (HPC_RM_ID)
);

/*==============================================================*/
/* Table: HPC_ROLE                                              */
/*==============================================================*/
create table HPC_ROLE
(
   HPC_ROLE_ID         bigint not null auto_increment,
   HPC_ROLE_NAME        varchar(256) not null,
   HPC_ROLE_CODE        int not null,
   HPC_ROLE_DESCRIPTION varchar(256),
   HPC_ROLE_RIGHTS      varchar(1024),
   HPC_ROLE_TEMPLATE    varchar(4096),
   primary key (HPC_ROLE_ID)
);

/*==============================================================*/
/* Table: HPC_ROLE_ALLOC                                        */
/*==============================================================*/
create table HPC_ROLE_ALLOC
(
   HPC_ROLE_ALLOC_ID   bigint not  null  auto_increment,
   HPC_USER_ID          bigint,
   HPC_ROLE_ID          bigint,
   HPC_ROLE_ALLOC_TIME  datetime not null,
   HPC_ROLE_ALLOC_START datetime,
   HPC_ROLE_ALLOC_EXPIRE datetime,
   primary key (HPC_ROLE_ALLOC_ID)
);

alter table CMD_EXECUTE add constraint FK_R17 foreign key (CMD_REQUEST_ID)
      references CMD_REQUEST (CMD_REQUEST_ID) on delete restrict on update restrict;

alter table CMD_REQUEST add constraint FK_R27 foreign key (HPC_CLUSTER_ID)
      references HPC_CLUSTER (HPC_CLUSTER_ID) on delete restrict on update restrict;

alter table COL_BATCH add constraint FK_R25 foreign key (HPC_CLUSTER_ID)
      references HPC_CLUSTER (HPC_CLUSTER_ID) on delete restrict on update restrict;

alter table COL_INFO add constraint FK_R16 foreign key (COL_BATCH_ID)
      references COL_BATCH (COL_BATCH_ID) on delete restrict on update restrict;

alter table COL_NET add constraint FK_R24 foreign key (COL_INFO_ID)
      references COL_INFO (COL_INFO_ID) on delete restrict on update restrict;

alter table HPC_CLOUD_USER add constraint FK_SUPPORT foreign key (HPC_USER_ASKTO)
      references HPC_CLOUD_USER (HPC_USER_ID) on delete restrict on update restrict;

alter table HPC_CLUSTER add constraint FK_R26 foreign key (HPC_AUTHEN_ID)
      references HPC_AUTHEN (HPC_AUTHEN_ID) on delete restrict on update restrict;

alter table HPC_CLUSTER add constraint FK_R28 foreign key (HPC_ACL_ID)
      references HPC_ACL (HPC_ACL_ID) on delete restrict on update restrict;

alter table HPC_CLUSTER add constraint FK_R35 foreign key (HPC_RM_ID)
      references HPC_RM (HPC_RM_ID) on delete restrict on update restrict;

alter table HPC_CLUSTER add constraint FK_R4 foreign key (HPC_CLUSTER_TYPE_ID)
      references HPC_CLUSTER_TYPE (HPC_CLUSTER_TYPE_ID) on delete restrict on update restrict;

alter table HPC_CLUSTER_USER add constraint FK_R20 foreign key (HPC_USER_ID)
      references HPC_CLOUD_USER (HPC_USER_ID) on delete restrict on update restrict;

alter table HPC_CLUSTER_USER add constraint FK_R21 foreign key (HPC_CLUSTER_ID)
      references HPC_CLUSTER (HPC_CLUSTER_ID) on delete restrict on update restrict;

alter table HPC_CONNECT add constraint FK_R3 foreign key (HPC_NODE_ID)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_CONNECT add constraint FK_R30 foreign key (HPC_CONN_TYPE_ID)
      references HPC_CONNECT_TYPE (HPC_CONN_TYPE_ID) on delete restrict on update restrict;

alter table HPC_GROUP add constraint FK_PARENT foreign key (HPC_GROUP_PARENT)
      references HPC_GROUP (HPC_GROUP_ID) on delete restrict on update restrict;

alter table HPC_GROUP_ALLOC add constraint FK_R5 foreign key (HPC_USER_ID)
      references HPC_CLOUD_USER (HPC_USER_ID) on delete restrict on update restrict;

alter table HPC_GROUP_ALLOC add constraint FK_R6 foreign key (HPC_GROUP_ID)
      references HPC_GROUP (HPC_GROUP_ID) on delete restrict on update restrict;

alter table HPC_INTERFACE add constraint FK_R11 foreign key (HPC_NODE_ID)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_JOB add constraint FK_PAYMENT foreign key (HPC_HOB_PAID_USER)
      references HPC_CLOUD_USER (HPC_USER_ID) on delete restrict on update restrict;

alter table HPC_JOB add constraint FK_R15 foreign key (HPC_APP_ID)
      references HPC_APP (HPC_APP_ID) on delete restrict on update restrict;

alter table HPC_JOB add constraint FK_R34 foreign key (HPC_JOB_PRICE_ID)
      references HPC_JOB_PRICE (HPC_JOB_PRICE_ID) on delete restrict on update restrict;

alter table HPC_JOB add constraint FK_R38 foreign key (HPC_CLSTR_USR_ID)
      references HPC_CLUSTER_USER (HPC_CLSTR_USR_ID) on delete restrict on update restrict;

alter table HPC_JOB add constraint FK_RELATIONSHIP_46 foreign key (HPC_CLUSTER_ID)
      references HPC_CLUSTER (HPC_CLUSTER_ID) on delete restrict on update restrict;

alter table HPC_JOB add constraint FK_SUBMIT foreign key (HPC_JOB_SUBMIT_USER)
      references HPC_CLOUD_USER (HPC_USER_ID) on delete restrict on update restrict;

alter table HPC_JOB_PRICE add constraint FK_R22 foreign key (HPC_USER_ID)
      references HPC_CLOUD_USER (HPC_USER_ID) on delete restrict on update restrict;

alter table HPC_JOB_PRICE add constraint FK_R23 foreign key (HPC_GROUP_ID)
      references HPC_GROUP (HPC_GROUP_ID) on delete restrict on update restrict;

alter table HPC_JOB_PRICE add constraint FK_R32 foreign key (HPC_APP_ID)
      references HPC_APP (HPC_APP_ID) on delete restrict on update restrict;

alter table HPC_JOB_PRICE add constraint FK_R33 foreign key (HPC_CLUSTER_ID)
      references HPC_CLUSTER (HPC_CLUSTER_ID) on delete restrict on update restrict;

alter table HPC_NODE add constraint FK_R1 foreign key (HPC_CLUSTER_ID)
      references HPC_CLUSTER (HPC_CLUSTER_ID) on delete restrict on update restrict;

alter table HPC_NODE_ROLE_ALLOC add constraint FK_R0 foreign key (HPC_NODE_ID)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_NODE_ROLE_ALLOC add constraint FK_R2 foreign key (HPC_NODE_ROLE_ID)
      references HPC_NODE_ROLE (HPC_NODE_ROLE_ID) on delete restrict on update restrict;

alter table HPC_PARAM_ASSIGN add constraint FK_R31 foreign key (HPC_PARAM_ID)
      references HPC_PARAMETER (HPC_PARAM_ID) on delete restrict on update restrict;

alter table HPC_PARAM_ASSIGN add constraint FK_R9 foreign key (HPC_NODE_ID)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_PAYMENT add constraint FK_R13 foreign key (HPC_CLSTR_USR_ID)
      references HPC_CLUSTER_USER (HPC_CLSTR_USR_ID) on delete restrict on update restrict;

alter table HPC_PROPERTY_ASSIGN add constraint FK_R29 foreign key (HPC_PROP_ID)
      references HPC_PROPERTY (HPC_PROP_ID) on delete restrict on update restrict;

alter table HPC_PROPERTY_ASSIGN add constraint FK_R8 foreign key (HPC_NODE_ID)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_RESOURCE add constraint FK_R10 foreign key (HPC_CLUSTER_ID)
      references HPC_CLUSTER (HPC_CLUSTER_ID) on delete restrict on update restrict;

alter table HPC_RES_LTD add constraint FK_R18 foreign key (HPC_RESOURCE_ID)
      references HPC_RESOURCE (HPC_RESOURCE_ID) on delete restrict on update restrict;

alter table HPC_RES_LTD add constraint FK_R19 foreign key (HPC_CLSTR_USR_ID)
      references HPC_CLUSTER_USER (HPC_CLSTR_USR_ID) on delete restrict on update restrict;

alter table HPC_RM add constraint FK_ACCOUNT foreign key (HPC_RM_ACCOUNT)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_RM add constraint FK_CONTROL foreign key (HPC_RM_CONTROLL)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_RM add constraint FK_INSTALL foreign key (HC_RM_INSTALL)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_RM add constraint FK_R36 foreign key (HPC_CLUSTER_ID)
      references HPC_CLUSTER (HPC_CLUSTER_ID) on delete restrict on update restrict;

alter table HPC_RM add constraint FK_SCHEDULE foreign key (HPC_RM_SCHEDULE)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_RM add constraint FK_STORAGE foreign key (HPC_RM_STORAGE)
      references HPC_NODE (HPC_NODE_ID) on delete restrict on update restrict;

alter table HPC_ROLE_ALLOC add constraint FK_R12 foreign key (HPC_USER_ID)
      references HPC_CLOUD_USER (HPC_USER_ID) on delete restrict on update restrict;

alter table HPC_ROLE_ALLOC add constraint FK_R14 foreign key (HPC_ROLE_ID)
      references HPC_ROLE (HPC_ROLE_ID) on delete restrict on update restrict;


insert into hpc_cloud_user (hpc_user_id,hpc_user_name,hpc_user_passwd) values (1,'root','91tUpCLvziXuOh6vAfkdMDaD9KYuOXrdgtr82Q==');


insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (1, 10001,'LoginServer','用户登录服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (2, 10002,'WebServer','WEB服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (3, 10003,'Bootstraper','系统引导服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (4, 10004,'ImageTemplateServer','系统镜像模板服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (5, 10005,'ImageServer','系统镜像服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (6, 10006,'TemplateServer','系统模板服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (7, 10007,'UploadServer','上传服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (8, 10008,'DownloadServer','下载服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (9, 10009,'AppsStoreServer','应用商店服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (10,20001,'FtpServer','FTP服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (11,20002,'VirsualizationServer','虚拟化服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (12,20003,'LdapServer','LDAP服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (13,20004,'NisServer','NIS服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (14,20005,'ActiveDirectories','Windows活动目录服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (15,20006,'NfsServer','NFSv3服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (16,20007,'MdsServer','Lustre的MDS服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (17,20008,'OssServer','Lustre的OSS服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (18,30001,'SlurmController','Slurm控制服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (19,30002,'SlurmBackupController','Slurm备份控制服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (20,30003,'SlurmDbServer','Slurm数据库服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (21,30004,'SlurmDbdServer','Slurm数据库访问服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (22,30005,'SlurmSubmitServer','Slurm作业提交服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (23,30006,'PbsServer','PBS服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (24,30007,'PbsScheduleServer','PBS调度服务器');
insert into hpc_node_role (hpc_node_role_id,hpc_node_role_code,hpc_node_role_name,hpc_node_role_desc) values (25,30008,'PbsSubmitServer','PbS作业提交服务器');

