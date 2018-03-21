CREATE TYPE movie(
    title text,
    release_date timestamp ,
    rate double ,
    description text ,
    box_office_revenue bigint );

CREATE TYPE actor(
    name text ,
    last_name text ,
    birthday timestamp ,
    rate double ,
    first_movie_date timestamp ,
    movies list <frozen<movie>>);