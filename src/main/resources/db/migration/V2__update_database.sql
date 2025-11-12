create table
   notifications (
      read boolean not null,
      create_at timestamp(6),
      update_at timestamp(6),
      id uuid not null,
      user_id uuid,
      message varchar(255),
      title varchar(255),
      primary key (id)
   );

alter table if exists notifications add constraint FKj3cxfyal6fn73tgnpfb4dbice foreign key (user_id) references _user;