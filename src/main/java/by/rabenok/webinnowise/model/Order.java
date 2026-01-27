package by.rabenok.webinnowise.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
  private int id;
  private User user;
  private List<Procedure> procedures;
  private LocalDateTime leadTime;
  private Status status;
  private BigDecimal bill;

  public Order() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Procedure> getProcedures() {
    return procedures;
  }

  public void setProcedures(List<Procedure> procedures) {
    this.procedures = procedures;
  }

  public LocalDateTime getLeadTime() {
    return leadTime;
  }

  public void setLeadTime(LocalDateTime leadTime) {
    this.leadTime = leadTime;
  }

  public BigDecimal getBill() {
    return bill;
  }

  public void setBill(BigDecimal bill) {
    this.bill = bill;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return id == order.id && Objects.equals(user, order.user) && Objects.equals(procedures, order.procedures) && Objects.equals(leadTime, order.leadTime) && status == order.status && Objects.equals(bill, order.bill);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user, procedures, leadTime, status, bill);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Order{");
    sb.append("id=").append(id);
    sb.append(", user=").append(user);
    sb.append(", procedures=").append(procedures);
    sb.append(", leadTime=").append(leadTime);
    sb.append(", status=").append(status);
    sb.append(", bill=").append(bill);
    sb.append('}');
    return sb.toString();
  }
}