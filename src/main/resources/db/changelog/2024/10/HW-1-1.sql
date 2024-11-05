create table t1_demo.t1$client (
    id bigint generated always as identity primary key,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    middle_name varchar(255)
);


comment on table t1_demo.t1$client is 'Таблица клиентов';

comment on column t1_demo.t1$client.id is 'Числовой уникальный идентификатор';
comment on column t1_demo.t1$client.first_name is 'Имя клиента';
comment on column t1_demo.t1$client.last_name is 'Фамилия клиента';
comment on column t1_demo.t1$client.middle_name is 'Отчество клиента';