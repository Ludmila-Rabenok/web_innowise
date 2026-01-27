package by.rabenok.webinnowise.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Procedure {
  private int id;
  private String name;
  private BigDecimal price;
  private Double ratingAverage;
  private int ratingCount;

  public Procedure() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
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

  public Double getRatingAverage() {
    return ratingAverage;
  }

  public void setRatingAverage(Double ratingAverage) {
    this.ratingAverage = ratingAverage;
  }

  public int getRatingCount() {
    return ratingCount;
  }

  public void setRatingCount(int ratingCount) {
    this.ratingCount = ratingCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Procedure procedure = (Procedure) o;
    return id == procedure.id && Double.compare(procedure.ratingAverage, ratingAverage) == 0 && ratingCount == procedure.ratingCount && Objects.equals(name, procedure.name) && Objects.equals(price, procedure.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, ratingAverage, ratingCount);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Procedure{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", price=").append(price);
    sb.append(", ratingAverage=").append(ratingAverage);
    sb.append(", ratingCount=").append(ratingCount);
    sb.append('}');
    return sb.toString();
  }
}