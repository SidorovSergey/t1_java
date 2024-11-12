-- create type
create type t1_demo.t1$transaction_status as enum ('ACCEPTED', 'REJECTED', 'BLOCKED', 'CANCELLED', 'REQUESTED');
comment on type t1_demo.t1$transaction_status is 'Статус транзакции';

-- add transaction_id
alter table t1_demo.t1$transaction add column transaction_id uuid;
comment on column t1_demo.t1$transaction.transaction_id is 'Уникальный идентификатор';

update t1_demo.t1$transaction set transaction_id = gen_random_uuid();

alter table t1_demo.t1$transaction alter column transaction_id set not null;

-- add transaction_status
alter table t1_demo.t1$transaction add column transaction_status t1_demo.t1$transaction_status;
comment on column t1_demo.t1$transaction.transaction_status is 'Статус транзакции';

-- add timestamp
alter table t1_demo.t1$transaction add column timestamp timestamp;
comment on column t1_demo.t1$transaction.timestamp is 'Замороженная сумма';