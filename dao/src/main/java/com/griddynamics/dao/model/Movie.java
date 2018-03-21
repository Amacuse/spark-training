package com.griddynamics.dao.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigInteger;
import java.time.LocalDate;

public class Movie {
    private String title;
    private LocalDate releaseDate;
    private Double rate;
    private String description;
    private Long boxOfficeRevenue;

    private Movie() {
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public double getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }

    public Long getBoxOfficeRevenue() {
        return boxOfficeRevenue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("title", title)
                .append("releaseDate", releaseDate)
                .append("rate", rate)
                .append("description", description)
                .append("boxOfficeRevenue", boxOfficeRevenue)
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
        Movie rhs = (Movie) obj;
        return new EqualsBuilder()
                .append(this.title, rhs.title)
                .append(this.releaseDate, rhs.releaseDate)
                .append(this.rate, rhs.rate)
                .append(this.description, rhs.description)
                .append(this.boxOfficeRevenue, rhs.boxOfficeRevenue)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(title)
                .append(releaseDate)
                .append(rate)
                .append(description)
                .append(boxOfficeRevenue)
                .toHashCode();
    }


    public static class MovieBuilder {
        private String title;
        private LocalDate releaseDate;
        private Double rate;
        private String description;
        private Long boxOfficeRevenue;

        public MovieBuilder(String title, LocalDate releaseDate) {
            this.title = title;
            this.releaseDate = releaseDate;
        }

        public MovieBuilder withRate(Double rate) {
            this.rate = rate;
            return this;
        }

        public MovieBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public MovieBuilder withBoxOfficeRevenue(Long revenue) {
            this.boxOfficeRevenue = revenue;
            return this;
        }

        public Movie build() {
            Movie movie = new Movie();
            movie.title = title;
            movie.releaseDate = releaseDate;
            movie.rate = rate;
            movie.description = description;
            movie.boxOfficeRevenue = boxOfficeRevenue;
            return movie;
        }
    }
}
