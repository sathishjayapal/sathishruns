Drop table if exists activitiess;
Drop sequence if exists activities_seq;
Drop table if exists activities;
create sequence activities_seq start with 1 increment by 50;

create table activities (
    id bigint DEFAULT nextval('activities_seq') not null,
    activityID text not null,
    activity_date text not null,
    activity_type text not null,
    activity_name text not null,
    activity_description text not null,
    elapsed_time text,
    distance text not null,
    max_heart_rate text,
    calories text not null,
    primary key (id)
);
