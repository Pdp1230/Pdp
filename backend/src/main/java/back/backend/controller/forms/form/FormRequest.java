package back.backend.controller.forms.form;

import back.backend.controller.forms.question.QuestionRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormRequest {
    private String title;
    private String style;
    private String id;
    private Integer index;
    private Integer cptQuestions;
    private QuestionRequest[] questions;
}
