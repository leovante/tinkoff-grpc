--liquibase formatted sql
--changeset temnikov_do:002-create-table-orders

create table orders
(
    id                  SERIAL                      not null
        constraint orders_pkey primary key,
    figi                varchar(51)                 not null,
    figi_title          varchar(51),
    purchase_price      double precision            not null,
    purchase_commission double precision            not null,
    purchase_date_time  timestamp without time zone not null,
    sell_price          double precision,
    sell_date_time      timestamp without time zone not null,
    sell_profit         double precision,
    sell_commission     double precision,
    strategy            varchar(51)                 not null,
    currency            varchar(51)                 not null,
    lots                int                         not null,
    details             jsonb                       not null,
    version             int                         not null,
    created             timestamp without time zone not null,
    unique (figi, strategy)

);

comment on table orders is '1 текущие ордера стратегий';
