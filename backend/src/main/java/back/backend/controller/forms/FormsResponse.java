package back.backend.controller.forms;

import java.util.List;

import back.backend.controller.forms.form.FormResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormsResponse {
    private List<FormResponse> forms;
}
