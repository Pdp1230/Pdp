package back.backend.controller.answers.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerResponse {
    private String type;
    private Integer index;
    private String text;
    private String textArea;
    private Integer radioChoice;
    private Integer selectChoice;
    private Integer[] checkboxChoices;
}
