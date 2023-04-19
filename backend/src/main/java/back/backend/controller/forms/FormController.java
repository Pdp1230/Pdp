package back.backend.controller.forms;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import back.backend.controller.forms.form.FormRequest;
import back.backend.controller.forms.form.FormResponse;


@RestController
@RequestMapping("/api/form")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;

    @GetMapping("/get")
    public ResponseEntity<FormsResponse> getForms(){
        return ResponseEntity.ok(formService.getForms());
    }

    @GetMapping("/getform/{fetchId}")
    public ResponseEntity<FormResponse> getForm(@PathVariable String fetchId){
        FormResponse response = formService.getFormByFetchId(fetchId);
        if(response == null)
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/post")
    public ResponseEntity<?> postForm(
        @RequestBody FormRequest request
    ) {
        return ResponseEntity.ok(formService.postForm(request));
    }
    @DeleteMapping("/delete/{fetchId}")
    public ResponseEntity<?> deleteForm(@PathVariable String fetchId) {
    ResponseEntity<?> deleted = formService.deleteFormByFetchId(fetchId);
    if (deleted != null) {
        return ResponseEntity.ok().build();
    } else {
        return ResponseEntity.notFound().build();
    }
}
    
}
