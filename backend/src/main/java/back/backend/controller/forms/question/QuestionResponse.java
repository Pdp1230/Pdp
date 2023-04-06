package back.backend.controller.forms.question;

import java.util.List;

import back.backend.controller.forms.option.OptionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private String modelQ;
    private String type;
    private Integer index;
    private Integer cptOptions;
    private List<OptionResponse> options;
}
