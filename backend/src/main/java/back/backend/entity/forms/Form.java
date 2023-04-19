package back.backend.entity.forms;

import org.springframework.context.annotation.DependsOn;

import back.backend.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "forms", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"fetchId"})
})
@DependsOn("User")
public class Form {

    @Id
    @GeneratedValue
    private long id;
    private String fetchId;
    private String title;
    @Column(length = 2040)
    private String style;
    @Column(name = "formIndex", nullable = false, columnDefinition = "int default 1")
    private Integer formIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "form")
    @OrderColumn(name = "questionIndex")
    private Question[] questions;

}