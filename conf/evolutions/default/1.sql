# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

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
  mobile_number             varchar(255),
  home_number               varchar(255),
  email_address             varchar(255),
  joining_date              timestamp not null,
  terminate_date            timestamp,
  price_id                  bigint not null,
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
  constraint pk_invoice primary key (id))
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
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  role                      varchar(255) not null,
  constraint uq_user_1 unique (username),
  constraint pk_user primary key (id))
;

create sequence area_seq;

create sequence customer_seq;

create sequence employee_seq;

create sequence invoice_seq;

create sequence payment_seq;

create sequence price_seq;

create sequence sub_area_seq;

create sequence user_seq;

alter table customer add constraint fk_customer_sub_area_1 foreign key (sub_area_id) references sub_area (id) on delete restrict on update restrict;
create index ix_customer_sub_area_1 on customer (sub_area_id);
alter table customer add constraint fk_customer_price_2 foreign key (price_id) references price (id) on delete restrict on update restrict;
create index ix_customer_price_2 on customer (price_id);
alter table invoice add constraint fk_invoice_customer_3 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_invoice_customer_3 on invoice (customer_id);
alter table payment add constraint fk_payment_invoice_4 foreign key (invoice_id) references invoice (id) on delete restrict on update restrict;
create index ix_payment_invoice_4 on payment (invoice_id);
alter table sub_area add constraint fk_sub_area_area_5 foreign key (area_id) references area (id) on delete restrict on update restrict;
create index ix_sub_area_area_5 on sub_area (area_id);
alter table sub_area add constraint fk_sub_area_employee_6 foreign key (employee_id) references employee (id) on delete restrict on update restrict;
create index ix_sub_area_employee_6 on sub_area (employee_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists area;

drop table if exists customer;

drop table if exists employee;

drop table if exists invoice;

drop table if exists payment;

drop table if exists price;

drop table if exists sub_area;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists area_seq;

drop sequence if exists customer_seq;

drop sequence if exists employee_seq;

drop sequence if exists invoice_seq;

drop sequence if exists payment_seq;

drop sequence if exists price_seq;

drop sequence if exists sub_area_seq;

drop sequence if exists user_seq;

