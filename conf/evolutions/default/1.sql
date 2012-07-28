# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table amply (
  id                        bigint not null,
  name                      varchar(255) not null,
  area_id                   bigint not null,
  constraint uq_amply_1 unique (name),
  constraint pk_amply primary key (id))
;

create table area (
  id                        bigint not null,
  name                      varchar(255) not null,
  employee_id               bigint not null,
  constraint uq_area_1 unique (name),
  constraint pk_area primary key (id))
;

create table customer (
  id                        bigint not null,
  name                      varchar(255) not null,
  area_id                   bigint not null,
  sub_area_id               bigint not null,
  node_id                   bigint not null,
  amply_id                  bigint not null,
  address                   varchar(255) not null,
  mobile_number             varchar(255),
  id_number                 varchar(255) not null,
  joining_date              timestamp not null,
  terminate_date            timestamp,
  price_id                  bigint not null,
  deposite                  integer not null,
  balance                   integer not null,
  constraint pk_customer primary key (id))
;

create table employee (
  id                        bigint not null,
  name                      varchar(255) not null,
  address                   varchar(255) not null,
  mobile_number             varchar(255),
  joining_date              timestamp not null,
  terminate_date            timestamp,
  constraint pk_employee primary key (id))
;

create table invoice (
  id                        bigint not null,
  customer_id               bigint not null,
  month                     integer not null,
  year                      integer not null,
  amount                    integer not null,
  paid                      boolean not null,
  invoice_date              timestamp not null,
  constraint pk_invoice primary key (id))
;

create table node (
  id                        bigint not null,
  name                      varchar(255) not null,
  area_id                   bigint not null,
  constraint uq_node_1 unique (name),
  constraint pk_node primary key (id))
;

create table payment (
  id                        bigint not null,
  invoice_id                bigint not null,
  payment_date              timestamp not null,
  amount                    integer not null,
  constraint pk_payment primary key (id))
;

create table price (
  id                        bigint not null,
  amount                    integer not null,
  constraint uq_price_1 unique (amount),
  constraint pk_price primary key (id))
;

create table sub_area (
  id                        bigint not null,
  name                      varchar(255) not null,
  area_id                   bigint not null,
  constraint uq_sub_area_1 unique (name),
  constraint pk_sub_area primary key (id))
;

create table user (
  id                        bigint not null,
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  role                      varchar(255) not null,
  constraint uq_user_1 unique (username),
  constraint pk_user primary key (id))
;

create sequence amply_seq;

create sequence area_seq;

create sequence customer_seq;

create sequence employee_seq;

create sequence invoice_seq;

create sequence node_seq;

create sequence payment_seq;

create sequence price_seq;

create sequence sub_area_seq;

create sequence user_seq;

alter table amply add constraint fk_amply_area_1 foreign key (area_id) references area (id) on delete restrict on update restrict;
create index ix_amply_area_1 on amply (area_id);
alter table area add constraint fk_area_employee_2 foreign key (employee_id) references employee (id) on delete restrict on update restrict;
create index ix_area_employee_2 on area (employee_id);
alter table customer add constraint fk_customer_area_3 foreign key (area_id) references area (id) on delete restrict on update restrict;
create index ix_customer_area_3 on customer (area_id);
alter table customer add constraint fk_customer_sub_area_4 foreign key (sub_area_id) references sub_area (id) on delete restrict on update restrict;
create index ix_customer_sub_area_4 on customer (sub_area_id);
alter table customer add constraint fk_customer_node_5 foreign key (node_id) references node (id) on delete restrict on update restrict;
create index ix_customer_node_5 on customer (node_id);
alter table customer add constraint fk_customer_amply_6 foreign key (amply_id) references amply (id) on delete restrict on update restrict;
create index ix_customer_amply_6 on customer (amply_id);
alter table customer add constraint fk_customer_price_7 foreign key (price_id) references price (id) on delete restrict on update restrict;
create index ix_customer_price_7 on customer (price_id);
alter table invoice add constraint fk_invoice_customer_8 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_invoice_customer_8 on invoice (customer_id);
alter table node add constraint fk_node_area_9 foreign key (area_id) references area (id) on delete restrict on update restrict;
create index ix_node_area_9 on node (area_id);
alter table payment add constraint fk_payment_invoice_10 foreign key (invoice_id) references invoice (id) on delete restrict on update restrict;
create index ix_payment_invoice_10 on payment (invoice_id);
alter table sub_area add constraint fk_sub_area_area_11 foreign key (area_id) references area (id) on delete restrict on update restrict;
create index ix_sub_area_area_11 on sub_area (area_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists amply;

drop table if exists area;

drop table if exists customer;

drop table if exists employee;

drop table if exists invoice;

drop table if exists node;

drop table if exists payment;

drop table if exists price;

drop table if exists sub_area;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists amply_seq;

drop sequence if exists area_seq;

drop sequence if exists customer_seq;

drop sequence if exists employee_seq;

drop sequence if exists invoice_seq;

drop sequence if exists node_seq;

drop sequence if exists payment_seq;

drop sequence if exists price_seq;

drop sequence if exists sub_area_seq;

drop sequence if exists user_seq;

