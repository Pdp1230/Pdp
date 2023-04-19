package back.backend.controller.answers.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormAnswerResponse {
    private String name;
    private String email;
    private List<QuestionAnswerResponse> questionAnswersResponse;
}
