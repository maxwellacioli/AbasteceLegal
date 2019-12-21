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
    principal_id int8,
    primary key (id)
);


create table trips
(
    id  bigserial not null,
    city varchar(255),
    date timestamp,
    description varchar(255),
    fuel_consumption float4 not null,
    fuel_quantity float4 not null,
    fuel_type varchar(255),
    trip_distance float4 not null,
    vehicle_id int8 not null,
    primary key (id)
);

create table vehicles
(
    id  bigserial not null,
    total_distance float4 not null,
    fuel_type varchar(255),
    license_plate varchar(255),
    model varchar(255),
    vehicle_type varchar(255),
    user_id int8 not null,
    primary key (id)
);