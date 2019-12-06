create table roles
(
    id  bigserial not null,
    name varchar(60),
    primary key (id)
);

create table user_roles
(
    user_id int8 not null ,
    role_id int8 not null,
    primary key (user_id, role_id)
);

create table users
(
    id  bigserial not null,
    email varchar(50),
    name varchar(50),
    password varchar(100),
    username varchar(50),
    primary key (id)
);
