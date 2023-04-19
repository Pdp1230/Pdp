package back.backend.entity.forms;

import org.springframework.context.annotation.DependsOn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "options")
@DependsOn("Question")
public class Option {

    @Id
    @GeneratedValue
    private long id;
    private String modelQ;
    @Column(name = "optionIndex", nullable = false, columnDefinition = "int default 1")
    private Integer optionIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
}