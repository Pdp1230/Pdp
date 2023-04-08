package back.backend.controller.forms.form;

import java.util.List;

import back.backend.controller.forms.question.QuestionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormResponse {
    private String title;
    private String url;
    private String style;
    private Integer index;
    private Integer cptQuestions;
    private List<QuestionResponse> questions;
}
