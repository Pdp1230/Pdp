package back.backend.controller.forms;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import back.backend.controller.forms.form.FormRequest;


@RestController
@RequestMapping("/api/form")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;

    @GetMapping("/get")
    public ResponseEntity<FormsResponse> getForms(){
        return ResponseEntity.ok(formService.getForms());
    }

    @PostMapping("/post")
    public ResponseEntity<?> postForm(
        @RequestBody FormRequest request
    ) {
        return ResponseEntity.ok(formService.postForm(request));
    }
    
}
