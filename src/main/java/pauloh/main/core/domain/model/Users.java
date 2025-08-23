package pauloh.main.core.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Users {
  private UUID id;

  private String name;

  private String email;

  private Instant createdAt;

  private Instant updatedAt;

  public Users(UUID id, String name, String email, Instant createdAt, Instant updatedAt) {
    this.id = id;
    setName(name);
    setEmail(email);
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public void setName(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    if (name.length() < 3) {
      throw new IllegalArgumentException("Name must be at least 3 characters long");
    }

    this.name = name;
  }

  public void setEmail(String email) {
    if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
      throw new IllegalArgumentException("Invalid email format");
    }

    this.email = email;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

}
