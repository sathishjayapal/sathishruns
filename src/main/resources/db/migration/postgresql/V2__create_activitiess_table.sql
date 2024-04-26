create sequence activitiess_seq start with 1 increment by 50;

create table activitiess (
    id bigint DEFAULT nextval('activitiess_seq') not null,
    text text not null,
    primary key (id)
);
