package pauloh.main.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Users {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(nullable = false, updatable = false, columnDefinition = "UUID DEFAULT gen_random_uuid()")
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  public Users(String name, String email) {
    this.id = UUID.randomUUID();
    setName(name);
    setEmail(email);
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
}
