create table simulation_definitions(
    id UUID primary key,
    message text not null,
    create_at timestamp not null,
    update_at timestamp not null,
    end_at timestamp not null
)