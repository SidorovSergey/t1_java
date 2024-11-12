-- create type
create type t1_demo.t1$account_status as enum ('ARRESTED', 'BLOCKED', 'CLOSED', 'OPEN');
comment on type t1_demo.t1$account_status is 'Статус счета';

-- add account_id
alter table t1_demo.t1$account add column account_id uuid;
comment on column t1_demo.t1$account.account_id is 'Уникальный идентификатор';

update t1_demo.t1$account set account_id = gen_random_uuid();

alter table t1_demo.t1$account alter column account_id set not null;

-- add account_status
alter table t1_demo.t1$account add column account_status t1_demo.t1$account_status;
comment on column t1_demo.t1$account.account_status is 'Статус счета';

update t1_demo.t1$account set account_status = 'OPEN';

alter table t1_demo.t1$account alter column account_status set not null;

-- add frozen_amount
alter table t1_demo.t1$account add column frozen_amount numeric(12, 2);
comment on column t1_demo.t1$account.frozen_amount is 'Замороженная сумма';