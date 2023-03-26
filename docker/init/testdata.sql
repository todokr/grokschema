CREATE DATABASE test_db;

\c test_db

create table x (
    id integer primary key
);

create table y (
    id integer primary key,
    x_id integer references x(id) not null
);

create table f (
    id integer primary key
);

create table e (
    id integer primary key
);

create table d (
    id integer primary key,
    f_id integer references f(id) not null
);

create table c (
    id integer primary key,
    e_id integer references e(id) not null
);

create table b (
    id integer primary key,
    d_id integer references d(id) not null,
    x_id integer references x(id) not null
);

create table a (
    id integer primary key,
    c_id integer references c(id) not null,
    y_id integer references y(id) not null
);