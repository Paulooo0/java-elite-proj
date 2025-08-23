package pauloh.main.adapter.output.repository.entity;

import java.time.Instant;
import java.util.UUID;

public class UserEntity {
  private UUID id;

  private String name;

  private String email;

  private Instant createdAt = Instant.now();

  private Instant updatedAt;

  public UserEntity(String name, String email) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.email = email;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
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