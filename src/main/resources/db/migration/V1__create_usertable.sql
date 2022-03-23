create table if not exists users(
    id  serial primary key,
    username varchar(25),
    names varchar(20),
    surname varchar(25),
    email varchar(55)
);