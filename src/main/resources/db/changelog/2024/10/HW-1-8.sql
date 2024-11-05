create table t1_demo.t1$error_log
(
    id          bigint generated always as identity primary key,
    message     varchar(500) not null,
    signature   varchar(200) not null,
    stack_trace text not null
);

comment on table t1_demo.t1$error_log is 'Таблица журнала ошибок';

comment on column t1_demo.t1$error_log.id is 'Числовой уникальный идентификатор';
comment on column t1_demo.t1$error_log.message is 'Текст сообщения';
comment on column t1_demo.t1$error_log.signature is 'Сигнатура метода';
comment on column t1_demo.t1$error_log.stack_trace is 'Текст стектрейса исключения';