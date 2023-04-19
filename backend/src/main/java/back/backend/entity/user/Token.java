package back.backend.entity.user;

import org.springframework.context.annotation.DependsOn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="tokens")
@DependsOn("User")
public class Token {

  @Id
  @GeneratedValue
  public Long id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType;

  public boolean revoked;

  public boolean expired;

  @Column(nullable = false, columnDefinition = "int default 0")
  public int tokens_order;

  @ManyToOne
  @JoinColumn(name = "user_id")
  public User user;
}
