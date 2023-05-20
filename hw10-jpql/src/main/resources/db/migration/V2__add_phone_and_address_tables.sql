create table phone
(
    id   bigserial not null primary key,
    client_id bigint not null references client(id),
    number varchar(50)
);

create table address
(
    id   bigserial not null primary key,
    street varchar(250)
);

alter table client
    add column address_id bigint references address(id);
