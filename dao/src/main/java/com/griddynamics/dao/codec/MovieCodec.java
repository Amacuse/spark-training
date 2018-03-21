package com.griddynamics.dao.codec;

import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.griddynamics.dao.model.Movie;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static java.sql.JDBCType.NULL;

public class MovieCodec extends TypeCodec<Movie> {

    private final TypeCodec<UDTValue> innerCodec;
    private final UserType userType;

    public MovieCodec(TypeCodec<UDTValue> innerCodec, Class<Movie> javaClass) {
        super(innerCodec.getCqlType(), javaClass);
        this.innerCodec = innerCodec;
        this.userType = ((UserType) innerCodec.getCqlType());
    }

    @Override
    public ByteBuffer serialize(Movie movie, ProtocolVersion protocolVersion) throws InvalidTypeException {
        return innerCodec.serialize(toUDTValue(movie), protocolVersion);
    }

    @Override
    public Movie deserialize(ByteBuffer byteBuffer, ProtocolVersion protocolVersion) throws InvalidTypeException {
        return toMovie(innerCodec.deserialize(byteBuffer, protocolVersion));
    }

    @Override
    public Movie parse(String value) throws InvalidTypeException {
        return value == null || value.isEmpty() || value.equals(NULL.toString()) ? null : toMovie(innerCodec.parse(value));
    }

    @Override
    public String format(Movie movie) throws InvalidTypeException {
        return movie == null ? null : innerCodec.format(toUDTValue(movie));
    }

    protected Movie toMovie(UDTValue value) {
        return value == null ? null : new Movie.MovieBuilder(
                value.getString("title"),
                value.getTimestamp("release_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .withRate(value.getDouble("rate"))
                .withDescription(value.getString("description"))
                .withBoxOfficeRevenue(value.getLong("box_office_revenue"))
                .build();
    }

    protected UDTValue toUDTValue(Movie value) {
        return value == null ? null : userType.newValue()
                .setString("title", value.getTitle())
                .setTimestamp("release_date", Date.from(value.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .setString("description", value.getDescription())
                .setDouble("rate", value.getRate())
                .setLong("box_office_revenue", value.getBoxOfficeRevenue());
    }
}
