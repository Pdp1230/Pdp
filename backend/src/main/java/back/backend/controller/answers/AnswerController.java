package back.backend.controller.answers;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import back.backend.controller.answers.requests.FormAnswerRequest;
import back.backend.controller.answers.responses.FormAnswerResponse;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @GetMapping("/getAll/{fetchId}")
    public ResponseEntity<List<FormAnswerResponse>> getAllFormAnswers(@PathVariable String fetchId){
        var response = answerService.getFormAnswers(fetchId);
        if(response == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit/{fetchId}")
    public ResponseEntity<?> submitFormAnswer(
            @PathVariable String fetchId, @RequestBody FormAnswerRequest formAnswerRequest
        ){
        return answerService.postAnswer(fetchId, formAnswerRequest);
    }
}
