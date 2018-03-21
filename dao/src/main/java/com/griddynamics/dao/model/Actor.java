package com.griddynamics.dao.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.List;

public class Actor {
    private String name;
    private String lastName;
    private LocalDate birthday;
    private Double rate;
    private LocalDate firstMovieDate;
    private List<Movie> movies;

    private Actor() {
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Double getRate() {
        return rate;
    }

    public LocalDate getFirstMovieDate() {
        return firstMovieDate;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("name", name)
                .append("lastName", lastName)
                .append("birthday", birthday)
                .append("rate", rate)
                .append("firstMovieDate", firstMovieDate)
                .append("movies", movies)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Actor rhs = (Actor) obj;
        return new EqualsBuilder()
                .append(this.name, rhs.name)
                .append(this.lastName, rhs.lastName)
                .append(this.birthday, rhs.birthday)
                .append(this.rate, rhs.rate)
                .append(this.firstMovieDate, rhs.firstMovieDate)
                .append(this.movies, rhs.movies)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(lastName)
                .append(birthday)
                .append(rate)
                .append(firstMovieDate)
                .append(movies)
                .toHashCode();
    }


    public static class ActorBuilder {
        private String name;
        private String lastName;
        private LocalDate birthday;
        private Double rate;
        private LocalDate firstMovieDate;
        private List<Movie> movies = Lists.newArrayList();

        public ActorBuilder(String name, String lastName) {
            this.name = name;
            this.lastName = lastName;
        }

        public ActorBuilder withRate(Double rate) {
            this.rate = rate;
            return this;
        }

        public ActorBuilder withBirthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        public ActorBuilder withFirstMovieDate(LocalDate firstMovieDate) {
            this.firstMovieDate = firstMovieDate;
            return this;
        }

        public ActorBuilder withListOfMovies(List<Movie> listOfMovies) {
            this.movies = listOfMovies;
            return this;
        }

        public Actor build() {
            Actor actor = new Actor();
            actor.name = name;
            actor.lastName = lastName;
            actor.birthday = birthday;
            actor.rate = rate;
            actor.firstMovieDate = firstMovieDate;
            actor.movies = movies;
            return actor;
        }
    }
}
