insert into roles
    (name)
values
    ('ROLE_USER');

insert into roles
    (name)
values
    ('ROLE_ADMIN');

insert into users
    (email,
     name,
     password,
     username)
values
    ('admin@admin.com',
     'Admin',
     '$2a$10$FodbKr7YVUX.R7QukdU0keBefDMygBCykFdZ0c99LhFLwOFliUf8S',
     'admin');

insert into user_roles
    (user_id,
     role_id)
values
    (1, 2)