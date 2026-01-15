package by.rabenok.webinnowise.model;

public enum Rating {
  ONE(1, "Очень плохо"),
  TWO(2, "Плохо"),
  THREE(3, "Удовлетворительно"),
  FOUR(4, "Хорошо"),
  FIVE(5, "Очень хорошо");

  private final int value;
  private final String description;

  Rating(int value, String description) {
    this.value = value;
    this.description = description;
  }

  public int getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }

  public static Rating fromValue(int value) {
    for (Rating rating : values()) {
      if (rating.value == value) {
        return rating;
      }
    }
    throw new IllegalArgumentException("You can give grade from 1 to 5.");
  }
}