package back.backend.controller.forms.question;

import back.backend.controller.forms.option.OptionRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {
    private String modelQ;
    private String type;
    private Integer index;
    private Integer cptOptions;
    private OptionRequest[] options;
}
