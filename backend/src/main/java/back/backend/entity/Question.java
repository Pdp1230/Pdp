package back.backend.entity;

import org.springframework.context.annotation.DependsOn;

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
@Table(name = "questions")
@DependsOn("Form")
public class Question {

    @Id
    @GeneratedValue
    private long id;
    private String modelQ;
    private String type;
    @Column(name = "questionIndex", nullable = false, columnDefinition = "int default 1")
    private Integer questionIndex;
    private Integer numberOfOptionsToClassify;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private Form form;

    @OneToMany(mappedBy = "question")
    @OrderColumn(name = "optionIndex")
    private Option[] options;
}