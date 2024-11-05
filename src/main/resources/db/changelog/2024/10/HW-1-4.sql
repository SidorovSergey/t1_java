create table t1_demo.t1$account
(
    id           bigint generated always as identity primary key,
    client_id    bigint                  not null references t1_demo.t1$client (id),
    account_type t1_demo.t1$account_type not null,
    balance      numeric(12, 2)          not null
);


comment on table t1_demo.t1$client is 'Таблица счетов';

comment on column t1_demo.t1$account.id is 'Числовой уникальный идентификатор';
comment on column t1_demo.t1$account.client_id is 'Идентификатор клиента';
comment on column t1_demo.t1$account.account_type is 'Тип счета';
comment on column t1_demo.t1$account.balance is 'Баланс счета';