package back.backend.entity;

import org.springframework.context.annotation.DependsOn;

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
    @UniqueConstraint(columnNames = {"url"})
})
@DependsOn("User")
public class Form {

    @Id
    @GeneratedValue
    private long id;
    private String url;
    private String title;
    private String style;
    private Integer formIndex;

    @Column(nullable = false, columnDefinition = "int default 3")
    public int forms_order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "form")
    private Question[] questions;

}