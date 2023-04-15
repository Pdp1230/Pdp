package back.backend.controller.forms;

import back.backend.controller.forms.form.FormRequest;
import back.backend.controller.forms.form.FormResponse;
import back.backend.controller.forms.option.OptionRequest;
import back.backend.controller.forms.option.OptionResponse;
import back.backend.controller.forms.question.QuestionRequest;
import back.backend.controller.forms.question.QuestionResponse;
import back.backend.entity.Form;
import back.backend.entity.Option;
import back.backend.entity.Question;
import back.backend.entity.User;
import back.backend.repository.FormRepository;
import back.backend.repository.OptionRepository;
import back.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public FormsResponse getForms(){
        List<Form> forms = formRepository.findAllByUser(user());
        List<FormResponse> formsResponse = new ArrayList<>();
        for(Form form : forms){
            List<Question> questions = questionRepository.findAllByForm(form);
            List<QuestionResponse> questionsResponse = new ArrayList<>();
            for(Question question : questions){
                List<Option> options = optionRepository.findAllByQuestion(question);
                List<OptionResponse> optionsResponse = new ArrayList<>();
                for(Option option : options){
                    optionsResponse.add(
                        OptionResponse.builder()
                                      .modelQ(option.getModelQ())
                                      .index(option.getOptionIndex())
                                      .build()
                    );
                }
                questionsResponse.add(
                    QuestionResponse.builder()
                                    .modelQ(question.getModelQ())
                                    .index(question.getQuestionIndex())
                                    .type(question.getType())
                                    .cptOptions(options.size())
                                    .options(optionsResponse)
                                    .numberOfOptionsToClassify(question.getNumberOfOptionsToClassify())
                                    .build()
                );
            }
            formsResponse.add(
                FormResponse.builder()
                            .title(form.getTitle())
                            .style(form.getStyle())
                            .id(form.getFetchId())
                            .index(form.getFormIndex())
                            .cptQuestions(questions.size())
                            .questions(questionsResponse)
                            .build()
            );
        }
        return FormsResponse.builder()
                            .forms(formsResponse)
                            .build();
    }

    public FormResponse getFormByFetchId(String fetchId){
        Form form;
        if (!formRepository.existsByFetchId(fetchId))
            return null;
        form = formRepository.findByFetchId(fetchId).get();
        List<Question> questions = questionRepository.findAllByForm(form);
        List<QuestionResponse> questionsResponse = new ArrayList<>();
        for(Question question : questions){
            List<Option> options = optionRepository.findAllByQuestion(question);
            List<OptionResponse> optionsResponse = new ArrayList<>();
            for(Option option : options){
                optionsResponse.add(
                    OptionResponse.builder()
                                    .modelQ(option.getModelQ())
                                    .index(option.getOptionIndex())
                                    .build()
                );
            }
            questionsResponse.add(
                QuestionResponse.builder()
                                .modelQ(question.getModelQ())
                                .index(question.getQuestionIndex())
                                .type(question.getType())
                                .cptOptions(options.size())
                                .options(optionsResponse)
                                .numberOfOptionsToClassify(question.getNumberOfOptionsToClassify())
                                .build()
            );
        }
        return FormResponse.builder()
                            .title(form.getTitle())
                            .style(form.getStyle())
                            .id(form.getFetchId())
                            .cptQuestions(questions.size())
                            .questions(questionsResponse)
                            .build()
        ;
    }

    public ResponseEntity<?> postForm(FormRequest request){
        var form = Form.builder()
                        .user(user())
                        .fetchId(request.getId())
                        .title(request.getTitle())
                        .style(request.getStyle())
                        .formIndex(request.getIndex())
                        .build();
        formRepository.save(form);       
        for (QuestionRequest q : request.getQuestions()) {
            var question = Question.builder()
                            .form(form)
                            .modelQ(q.getModelQ())
                            .questionIndex(q.getIndex())
                            .type(q.getType())
                            .numberOfOptionsToClassify(q.getNumberOfOptionsToClassify())
                            .build();
            questionRepository.save(question);
            for(OptionRequest o : q.getOptions()){
                var option = Option.builder()
                              .question(question)
                              .modelQ(o.getModelQ())
                              .optionIndex(o.getIndex())
                              .build();
                optionRepository.save(option);
            }
        }
        return ResponseEntity.ok().build();
    }

    private User user(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User)authentication.getPrincipal();
    }
}
