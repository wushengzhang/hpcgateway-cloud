/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/11/23 16:43:18                          */
/*==============================================================*/


drop table if exists HG_APP_VASP_CHGCAR;

drop table if exists HG_APP_VASP_COUNTCAR;

drop table if exists HG_APP_VASP_INCAR;

drop table if exists HG_APP_VASP_JOB;

drop table if exists HG_APP_VASP_KPOINTS;

drop table if exists HG_APP_VASP_OUTCAR;

drop table if exists HG_APP_VASP_POSCAR;

drop table if exists HG_APP_VASP_POTCAR;

drop table if exists HG_APP_VASP_TASK;

/*==============================================================*/
/* Table: HG_APP_VASP_CHGCAR                                    */
/*==============================================================*/
create table HG_APP_VASP_CHGCAR
(
   CONTCAR_ID2          int not null,
   job_id               int,
   ELECTRON_POSITION    date,
   primary key (CONTCAR_ID2)
);

/*==============================================================*/
/* Table: HG_APP_VASP_COUNTCAR                                  */
/*==============================================================*/
create table HG_APP_VASP_COUNTCAR
(
   CONTCAR_ID           int not null,
   job_id               int,
   ELECTRON_POSITION    date,
   primary key (CONTCAR_ID)
);

/*==============================================================*/
/* Table: HG_APP_VASP_incar                                     */
/*==============================================================*/
create table HG_APP_VASP_incar
(
   INCAR_ID             int not null,
   job_id               int,
   NGX                  float,
   NGY                  float,
   NGZ                  float,
   NGXF                 float,
   NGYF                 float,
   NGZF                 float,
   NBANDS               int,
   NBLK                 int,
   SYSTEM               char(256),
   NWRITE               int,
   ISTART               int,
   ISPIN                int,
   MAGMON               date,
   INIWAV               int,
   ENCUT                float(24),
   PREC                 char(256),
   NELM                 int,
   NELMIN               int,
   MELMDL               int,
   EDIFF                float(24),
   EDIFFG               float(24),
   NSW                  int,
   NBLOCK               int,
   KBLOCK               int,
   IBRION               int,
   ISIF                 int,
   IWAVPR               int,
   ISYM                 int,
   SYMPEREC             int,
   LCORR                char(256),
   POTIM                float(24),
   TEBEG                float(24),
   TEEND                float(24),
   SMASS                int,
   NPACO                int,
   APACO                int,
   POMASS               float(24),
   ZVAL                 float(24),
   RWIGS                date,
   NELECT               float(24),
   NUPDOWN              int,
   EMIN                 float(24),
   EMAX                 float(24),
   ISMEAR               int,
   SIGMA                float(24),
   ALGO                 char(256),
   IALGO                int,
   LREAL                char(256),
   ROPT                 date,
   GGA                  char(256),
   VOSKOWN              int,
   DIPOL                date,
   AMIX                 float(24),
   BMIX                 float(24),
   WEIMIN               float(24),
   EBREAK               float(24),
   DEPER                float(24),
   TIME                 float(24),
   LWAVE                char(256),
   LCHARG               char(256),
   LVTOT                char(256),
   LVHAR                char(256),
   LELF                 char(256),
   LORBIT               char(256),
   NPAR                 int,
   LSCALAPACK           char(256),
   LSCALU               char(256),
   LASYNC               char(256),
   primary key (INCAR_ID)
);

/*==============================================================*/
/* Table: HG_APP_VASP_JOB                                       */
/*==============================================================*/
create table HG_APP_VASP_JOB
(
   job_id               int not null,
   OUTCAR_ID            int,
   task_id              int,
   INCAR_ID             int,
   POTCAR_ID            int,
   CONTCAR_ID2          int,
   CONTCAR_ID           int,
   KPOINTS_ID           int,
   POSCAR_ID            int,
   primary key (job_id)
);

/*==============================================================*/
/* Table: HG_APP_VASP_KPOINTS                                   */
/*==============================================================*/
create table HG_APP_VASP_KPOINTS
(
   KPOINTS_ID           int not null,
   job_id               int,
   primary key (KPOINTS_ID)
);

/*==============================================================*/
/* Table: HG_APP_VASP_OUTCAR                                    */
/*==============================================================*/
create table HG_APP_VASP_OUTCAR
(
   OUTCAR_ID            int not null,
   job_id               int,
   TOT_ENG              float(24),
   SFE                  float(24),
   BE                   float(24),
   VOLUME               float(24),
   M_MASS               date,
   ATOM_NUM             date,
   TOT_QUALITY          float(24),
   DENSITY              float(24),
   ENERGY_CUTOFF        float(24),
   ION_STEP             int,
   FORCE                float(24),
   VERSION              char(256),
   TOT_TIME             int,
   MEMORY               int,
   CORE                 int,
   primary key (OUTCAR_ID)
);

/*==============================================================*/
/* Table: HG_APP_VASP_POSCAR                                    */
/*==============================================================*/
create table HG_APP_VASP_POSCAR
(
   POSCAR_ID            int not null,
   job_id               int,
   CRYSTAL_PARAMETER    date,
   primary key (POSCAR_ID)
);

/*==============================================================*/
/* Table: HG_APP_VASP_POTCAR                                    */
/*==============================================================*/
create table HG_APP_VASP_POTCAR
(
   POTCAR_ID            int not null,
   job_id               int,
   primary key (POTCAR_ID)
);

/*==============================================================*/
/* Table: HG_APP_VASP_TASK                                      */
/*==============================================================*/
create table HG_APP_VASP_TASK
(
   task_id              int not null,
   primary key (task_id)
);

alter table HG_APP_VASP_CHGCAR add constraint FK_Relationship_14 foreign key (job_id)
      references HG_APP_VASP_JOB (job_id) on delete restrict on update restrict;

alter table HG_APP_VASP_COUNTCAR add constraint FK_Relationship_12 foreign key (job_id)
      references HG_APP_VASP_JOB (job_id) on delete restrict on update restrict;

alter table HG_APP_VASP_INCAR add constraint FK_Relationship_11 foreign key (job_id)
      references HG_APP_VASP_JOB (job_id) on delete restrict on update restrict;

alter table HG_APP_VASP_JOB add constraint FK_Relationship_10 foreign key (INCAR_ID)
      references HG_APP_VASP_INCAR (INCAR_ID) on delete restrict on update restrict;

alter table HG_APP_VASP_JOB add constraint FK_Relationship_13 foreign key (CONTCAR_ID)
      references HG_APP_VASP_COUNTCAR (CONTCAR_ID) on delete restrict on update restrict;

alter table HG_APP_VASP_JOB add constraint FK_Relationship_15 foreign key (CONTCAR_ID2)
      references HG_APP_VASP_CHGCAR (CONTCAR_ID2) on delete restrict on update restrict;

alter table HG_APP_VASP_JOB add constraint FK_Relationship_2 foreign key (KPOINTS_ID)
      references HG_APP_VASP_KPOINTS (KPOINTS_ID) on delete restrict on update restrict;

alter table HG_APP_VASP_JOB add constraint FK_Relationship_4 foreign key (OUTCAR_ID)
      references HG_APP_VASP_OUTCAR (OUTCAR_ID) on delete restrict on update restrict;

alter table HG_APP_VASP_JOB add constraint FK_Relationship_6 foreign key (POTCAR_ID)
      references HG_APP_VASP_POTCAR (POTCAR_ID) on delete restrict on update restrict;

alter table HG_APP_VASP_JOB add constraint FK_Relationship_8 foreign key (POSCAR_ID)
      references HG_APP_VASP_POSCAR (POSCAR_ID) on delete restrict on update restrict;

alter table HG_APP_VASP_JOB add constraint FK_task_job_r foreign key (task_id)
      references HG_APP_VASP_TASK (task_id) on delete restrict on update restrict;

alter table HG_APP_VASP_KPOINTS add constraint FK_Relationship_3 foreign key (job_id)
      references HG_APP_VASP_JOB (job_id) on delete restrict on update restrict;

alter table HG_APP_VASP_OUTCAR add constraint FK_Relationship_5 foreign key (job_id)
      references HG_APP_VASP_JOB (job_id) on delete restrict on update restrict;

alter table HG_APP_VASP_POSCAR add constraint FK_Relationship_9 foreign key (job_id)
      references HG_APP_VASP_JOB (job_id) on delete restrict on update restrict;

alter table HG_APP_VASP_POTCAR add constraint FK_Relationship_7 foreign key (job_id)
      references HG_APP_VASP_JOB (job_id) on delete restrict on update restrict;

