create sequence if not exists account_seq start with 1 increment by 50
create sequence if not exists asset_entity_seq start with 1 increment by 50
create sequence if not exists currency_seq start with 1 increment by 50
create table if not exists account (currency_id integer, id integer not null, display_name varchar(255), label varchar(255), primary key (id))
create table if not exists asset_entity (account_id integer, amount float(53) not null, currency_id integer, id integer not null, rate float(53) not null, local_date_time timestamp(6), primary key (id))
create table if not exists currency (id integer not null, display_name varchar(255) unique, primary key (id))
alter table if exists account add constraint if not exists FK316pn109iutn6yqoxrqp09cpc foreign key (currency_id) references currency
alter table if exists asset_entity add constraint if not exists FKgte3a9qyc28h95dn2jnadv57k foreign key (account_id) references account
alter table if exists asset_entity add constraint if not exists FKfowyf6dc3we1qygwdirln1999 foreign key (currency_id) references currency
