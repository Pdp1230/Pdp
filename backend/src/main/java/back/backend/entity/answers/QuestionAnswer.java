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
@Table(name = "questionanswers")
@DependsOn("FormAnswer")
public class QuestionAnswer {
    @Id
    @GeneratedValue
    private Long Id;

    @Column(name = "questionAnswerIndex", nullable = false, columnDefinition = "int default 1")
    private  Integer questionAnswerIndex;

    private String type;
    private String text;
    @Column(length = 3000)
    private String textarea;
    private Integer radioChoice;
    private Integer selectChoice;
    private Integer[] checkboxChoices;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formAnswer_id")
    private FormAnswer formAnswer;

}
