package back.backend.controller.answers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import back.backend.controller.answers.requests.FormAnswerRequest;
import back.backend.controller.answers.responses.FormAnswerResponse;
import back.backend.controller.answers.responses.QuestionAnswerResponse;
import back.backend.controller.forms.email.EmailSenderService;
import back.backend.entity.answers.FormAnswer;
import back.backend.entity.answers.QuestionAnswer;
import back.backend.repository.answers.FormAnswerRepository;
import back.backend.repository.answers.QuestionAnswerRepository;
import back.backend.repository.forms.FormRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final FormRepository formRepository;
    private final FormAnswerRepository formAnswerRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final EmailSenderService emailSenderService;

    public ResponseEntity<?> postAnswer(String fetchId, FormAnswerRequest formAnswerRequest){
        if(!formRepository.existsByFetchId(fetchId) || formAnswerRequest.getEmail() == null 
            || formAnswerRequest.getAnswers() == null)
            return ResponseEntity.badRequest().build();
        
        var form = formRepository.findByFetchId(fetchId).get();

        var formAnswer = formAnswerRepository.save(FormAnswer.builder()
            .form(form)
            .email(formAnswerRequest.getEmail())
            .name(formAnswerRequest.getName())
            .formAnswerIndex(formAnswerRepository.findAllByForm(form).size()+1)
            .build());
        for(var questionAnswerRequest: formAnswerRequest.getAnswers()){
            var questionAnswerBuilder = QuestionAnswer.builder()
                .formAnswer(formAnswer)
                .type(questionAnswerRequest.getType())
                .questionAnswerIndex(questionAnswerRequest.getIndex());
            
            switch(questionAnswerRequest.getType()){
                case "text":
                    questionAnswerBuilder.text(questionAnswerRequest.getText());
                    break;
                case "textarea":
                    questionAnswerBuilder.textarea(questionAnswerRequest.getTextArea());
                    break;
                case "radio":
                    questionAnswerBuilder.radioChoice(questionAnswerRequest.getRadioChoice());
                    break;
                case "select":
                    questionAnswerBuilder.selectChoice(questionAnswerRequest.getSelectChoice());
                    break;
                case "checkbox":
                    questionAnswerBuilder.checkboxChoices(questionAnswerRequest.getCheckboxChoices());
                    break;
                case "ranking":
                    questionAnswerBuilder.rankingOrder(questionAnswerRequest.getRankingOrder());
                    break;
                default:
                    break;
            }
            questionAnswerRepository.save(questionAnswerBuilder.build());
        }
        emailSenderService.sendSimpleEmail(formAnswerRequest.getEmail(), "Réponse au questionnaire " + form.getTitle(), "Bonjour " + 
                                            formAnswerRequest.getName() + "\n\n Merci d'avoir répondu à notre questionnaire," + 
                                            " votre réponse a bien été prise en compte.\n\n Ce message est automatique veiller ne pas répondre."
        );
        emailSenderService.sendSimpleEmail(form.getUser().getEmail(), "Réponse au questionnaire " + form.getTitle(), "Bonjour " + 
                                            form.getUser().getFirstname() + "\n\n"+ formAnswerRequest.getName() + " a répondu à votre questionnaire: " + 
                                            form.getTitle() + ", vous pouvez visualiser ça réponse ou bien exporter les réponses sur votre espace personnel." +
                                            "\n\nCe message est automatique veiller ne pas répondre."
        );
        return ResponseEntity.ok().build();
    }

    public List<FormAnswerResponse> getFormAnswers(String fetchId){
        if(!formRepository.existsByFetchId(fetchId))
            return null;
        var form = formRepository.findByFetchId(fetchId).get();
        var formAnswers= formAnswerRepository.findAllByForm(form);
        List<FormAnswerResponse> formAnswersResponse = new ArrayList<>();

        for(var formAnswer : formAnswers){
            var questionAnswers = questionAnswerRepository.findAllByFormAnswer(formAnswer);
            List<QuestionAnswerResponse> questtionAnswersResponse = new ArrayList<>();

            for(var questionAnswer : questionAnswers){
                questtionAnswersResponse.add(QuestionAnswerResponse.builder()
                    .type(questionAnswer.getType())
                    .index(questionAnswer.getQuestionAnswerIndex())
                    .text(questionAnswer.getText())
                    .textArea(questionAnswer.getTextarea())
                    .radioChoice(questionAnswer.getRadioChoice())
                    .selectChoice(questionAnswer.getSelectChoice())
                    .checkboxChoices(questionAnswer.getCheckboxChoices())
                    .rankingOrder(questionAnswer.getRankingOrder())
                    .build()
                );
            }
            formAnswersResponse.add(FormAnswerResponse.builder()
                .email(formAnswer.getEmail())
                .name(formAnswer.getName())
                .index(formAnswer.getFormAnswerIndex())
                .answers(questtionAnswersResponse)
                .build()
            );
        }
        return formAnswersResponse;
    }
}
