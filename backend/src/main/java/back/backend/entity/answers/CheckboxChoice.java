package back.backend.entity.answers;

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
@Table(name = "checkboxchoices")
@DependsOn("QuestionAnswer")
public class CheckboxChoice {
    @Id
    @GeneratedValue
    private Long Id;

    @Column(name = "checkboxChoiceIndex", nullable = false, columnDefinition = "int default 1")
    private  Integer checkboxChoiceIndex;

    private Integer checkboxChoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionAnswer_id")
    QuestionAnswer questionAnswer;
}
