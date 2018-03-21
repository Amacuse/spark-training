package com.griddynamics.dao.codec;

import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.griddynamics.dao.model.Actor;
import com.griddynamics.dao.model.Movie;

import java.nio.ByteBuffer;
import java.time.ZoneId;
import java.util.Date;

import static java.sql.JDBCType.NULL;


public class ActorCodec extends TypeCodec<Actor> {

    private final TypeCodec<UDTValue> innerCodec;
    private final UserType userType;

    public ActorCodec(TypeCodec<UDTValue> innerCodec, Class<Actor> javaClass) {
        super(innerCodec.getCqlType(), javaClass);
        this.innerCodec = innerCodec;
        this.userType = ((UserType) innerCodec.getCqlType());
    }

    @Override
    public ByteBuffer serialize(Actor actor, ProtocolVersion protocolVersion) throws InvalidTypeException {
        return innerCodec.serialize(toUDTValue(actor), protocolVersion);
    }

    @Override
    public Actor deserialize(ByteBuffer byteBuffer, ProtocolVersion protocolVersion) throws InvalidTypeException {
        return toActor(innerCodec.deserialize(byteBuffer, protocolVersion));
    }

    @Override
    public Actor parse(String value) throws InvalidTypeException {
        return value == null || value.isEmpty() || value.equals(NULL.toString()) ? null : toActor(innerCodec.parse(value));
    }

    @Override
    public String format(Actor actor) throws InvalidTypeException {
        return actor == null ? null : innerCodec.format(toUDTValue(actor));
    }

    protected Actor toActor(UDTValue value) {
        return value == null ? null : new Actor.ActorBuilder(
                value.getString("name"),
                value.getString("last_name"))
                .withRate(value.getDouble("rate"))
                .withBirthday(value.getTimestamp("birthday").toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .withFirstMovieDate(value.getTimestamp("first_movie_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .withListOfMovies(value.getList("movies", Movie.class))
                .build();
    }

    protected UDTValue toUDTValue(Actor value) {
        return value == null ? null : userType.newValue()
                .setString("name", value.getName())
                .setString("last_name", value.getLastName())
                .setTimestamp("birthday", Date.from(value.getBirthday().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .setTimestamp("first_movie_date", Date.from(value.getFirstMovieDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .setDouble("rate", value.getRate())
                .setList("movies", value.getMovies(), Movie.class);
    }
}
