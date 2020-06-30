drop table if exists user;
create table user
(
    user_id  bigint auto_increment
        primary key,
    email    varchar(100) not null,
    password varchar(100) null,
    enabled  bit          null
);

drop table if exists user_role;
create table user_role
(
    role_id bigint auto_increment
        primary key,
    role    varchar(10) null,
    user_id bigint      null
);

drop table if exists coupon;
create table coupon
(
    id            bigint auto_increment
        primary key,
    coupon_num    varchar(50) null,
    status        varchar(10) null,
    user_id       bigint      null,
    expiration_at datetime    null,
    used_at       datetime    null,
    created_at    datetime    null,
    updated_at    datetime    null,
    issued_at     datetime    null,
    issued       bit         null,
    enabled       bit         null
);




