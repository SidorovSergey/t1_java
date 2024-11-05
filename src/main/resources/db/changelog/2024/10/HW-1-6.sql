create table t1_demo.t1$transaction
(
    id         bigint generated always as identity primary key,
    account_id bigint                      not null references t1_demo.t1$account (id),
    amount     numeric(12, 2)              not null,
    created_at timestamp without time zone not null
);


comment on table t1_demo.t1$client is 'Таблица транзакций';

comment on column t1_demo.t1$transaction.id is 'Числовой уникальный идентификатор';
comment on column t1_demo.t1$transaction.account_id is 'Идентификатор счета';
comment on column t1_demo.t1$transaction.amount is 'Сумма транзакции';
comment on column t1_demo.t1$transaction.created_at is 'Время транзакции';