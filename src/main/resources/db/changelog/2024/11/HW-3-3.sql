-- add client_id
alter table t1_demo.t1$client add column client_id uuid, add constraint client_id_key unique (client_id);
comment on column t1_demo.t1$client.client_id is 'Уникальный идентификатор';

update t1_demo.t1$client set client_id = gen_random_uuid();

alter table t1_demo.t1$client alter column client_id set not null;