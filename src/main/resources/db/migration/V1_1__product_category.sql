create table tb_payment (
    id bigserial not null,
    client_id varchar(255) not null,
    payment_id bigint not null,
    payment_status varchar(255) not null,
    payment_details varchar(255) not null,
    payment_date timestamp(6) not null,
    payment_amount numeric(19,2) not null,
    qr_code text not null,
    qr_code_base64 text not null,
    orders varchar(255) not null,
    create_by varchar(255) not null,
    created_date timestamp(6) not null,
    last_modified_by varchar(255),
    last_modified_date timestamp(6),
    status varchar(255) not null,
    primary key (id)
);