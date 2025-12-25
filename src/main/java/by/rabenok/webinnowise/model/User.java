package by.rabenok.webinnowise.model;

import java.util.Objects;

public class User {
  private int id;
  private String name;
  private Role role;

  public User() {
  }

  public User(String name) {
    this.name = name;
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

  public void setName(String name) {
    this.name = name;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id == user.id && Objects.equals(name, user.name) && role == user.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, role);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", role=").append(role);
    sb.append('}');
    return sb.toString();
  }
}
