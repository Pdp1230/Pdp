package back.backend.entity.answers;

import org.springframework.context.annotation.DependsOn;

import back.backend.entity.forms.Form;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "formanswers")
@DependsOn("Form")
public class FormAnswer {
    @Id
    @GeneratedValue
    private Long Id;

    private String name;
    private String email;

    @Column(name = "formAnswerIndex", nullable = false, columnDefinition = "int default 1")
    private Integer formAnswerIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private Form form;

    @OneToMany(mappedBy = "formAnswer")
    @OrderColumn(name = "questionAnswerIndex")
    QuestionAnswer[] questionAnswers;

}
