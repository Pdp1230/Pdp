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
    private String url;
    private String title;
    private String style;
    private List<QuestionResponse> questions;
}
