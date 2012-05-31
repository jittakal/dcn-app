# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                        bigint not null,
  customer_id               bigint not null,
  price_id                  bigint not null,
  constraint pk_account primary key (id))
;

create table area (
  id                        bigint not null,
  name                      varchar(255) not null,
  constraint pk_area primary key (id))
;

create table customer (
  id                        bigint not null,
  name                      varchar(255) not null,
  sub_area_id               bigint not null,
  address                   varchar(255) not null,
  mobile_number             integer,
  home_number               integer,
  email_address             varchar(255),
  joining_date              timestamp not null,
  terminate_date            timestamp,
  constraint pk_customer primary key (id))
;

create table employee (
  id                        bigint not null,
  name                      varchar(255) not null,
  address                   varchar(255) not null,
  mobile_number             integer,
  joining_date              timestamp not null,
  terminate_date            timestamp,
  constraint pk_employee primary key (id))
;

create table price (
  id                        bigint not null,
  amount                    integer not null,
  start_date                timestamp,
  end_date                  timestamp,
  constraint pk_price primary key (id))
;

create table sub_area (
  id                        bigint not null,
  name                      varchar(255) not null,
  area_id                   bigint not null,
  employee_id               bigint not null,
  constraint pk_sub_area primary key (id))
;

create table user (
  id                        bigint not null,
  username                  varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;

create sequence account_seq;

create sequence area_seq;

create sequence customer_seq;

create sequence employee_seq;

create sequence price_seq;

create sequence sub_area_seq;

create sequence user_seq;

alter table account add constraint fk_account_customer_1 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_account_customer_1 on account (customer_id);
alter table account add constraint fk_account_price_2 foreign key (price_id) references price (id) on delete restrict on update restrict;
create index ix_account_price_2 on account (price_id);
alter table customer add constraint fk_customer_subArea_3 foreign key (sub_area_id) references sub_area (id) on delete restrict on update restrict;
create index ix_customer_subArea_3 on customer (sub_area_id);
alter table sub_area add constraint fk_sub_area_area_4 foreign key (area_id) references area (id) on delete restrict on update restrict;
create index ix_sub_area_area_4 on sub_area (area_id);
alter table sub_area add constraint fk_sub_area_employee_5 foreign key (employee_id) references employee (id) on delete restrict on update restrict;
create index ix_sub_area_employee_5 on sub_area (employee_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists account;

drop table if exists area;

drop table if exists customer;

drop table if exists employee;

drop table if exists price;

drop table if exists sub_area;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists account_seq;

drop sequence if exists area_seq;

drop sequence if exists customer_seq;

drop sequence if exists employee_seq;

drop sequence if exists price_seq;

drop sequence if exists sub_area_seq;

drop sequence if exists user_seq;

