package by.rabenok.webinnowise.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Procedure {
  private int id;
  private final String name;
  private BigDecimal price;
  private List<Rating> ratings;

  public Procedure(String name, BigDecimal price) {
    this.name = name;
    this.price = price;
    ratings = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public List<Rating> getRatings() {
    return ratings;
  }

  public void setRatings(List<Rating> ratings) {
    this.ratings = ratings;
  }

  public void addRating(Rating rating) {
    ratings.add(rating);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Procedure procedure = (Procedure) o;
    return Objects.equals(name, procedure.name) && Objects.equals(price, procedure.price) && Objects.equals(ratings, procedure.ratings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, price, ratings);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Procedure{");
    sb.append("name='").append(name).append('\'');
    sb.append(", price=").append(price);
    sb.append(", ratings=").append(ratings);
    sb.append('}');
    return sb.toString();
  }
}