package back.backend.controller.forms;

import back.backend.controller.answers.AnswerService;
import back.backend.controller.forms.form.FormRequest;
import back.backend.controller.forms.form.FormResponse;
import back.backend.controller.forms.option.OptionRequest;
import back.backend.controller.forms.option.OptionResponse;
import back.backend.controller.forms.question.QuestionRequest;
import back.backend.controller.forms.question.QuestionResponse;
import back.backend.entity.answers.FormAnswer;
import back.backend.entity.answers.QuestionAnswer;
import back.backend.entity.forms.Form;
import back.backend.entity.forms.Option;
import back.backend.entity.forms.Question;
import back.backend.entity.user.User;
import back.backend.repository.answers.CheckboxChoiceRepository;
import back.backend.repository.answers.FormAnswerRepository;
import back.backend.repository.answers.QuestionAnswerRepository;
import back.backend.repository.answers.RankingOrderRepository;
import back.backend.repository.forms.FormRepository;
import back.backend.repository.forms.OptionRepository;
import back.backend.repository.forms.QuestionRepository;
import jakarta.transaction.Transactional;
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
    private final FormAnswerRepository formAnswerRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final RankingOrderRepository rankingOrderRepository;
    private final CheckboxChoiceRepository checkboxChoiceRepository;
    private final AnswerService answerService;

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
                            .answers(answerService.getFormAnswers(form.getFetchId()))
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

    @Transactional
    public ResponseEntity<?> editFormByFetchId(String fetchId, FormRequest request) {
        if ( !formRepository.existsByFetchId(fetchId) )
            return ResponseEntity.notFound().build();

        Form form = formRepository.findByFetchId(fetchId).get();
        if( !form.getUser().getEmail().equals( user().getEmail() ) )
            return ResponseEntity.badRequest().build();

        form.setTitle(request.getTitle());
        form.setStyle(request.getStyle());
        formRepository.save(form);

        deleteFormDependencies(form);

        // Create new questions and options
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
    
    @Transactional
    public ResponseEntity<?> deleteFormByFetchId(String fetchId) {
        if ( !formRepository.existsByFetchId(fetchId) )
            return ResponseEntity.notFound().build();

        Form form = formRepository.findByFetchId(fetchId).get();
        if( !form.getUser().getEmail().equals( user().getEmail() ) )
            return ResponseEntity.badRequest().build();

        deleteFormDependencies(form);

        formRepository.delete(form);
        return ResponseEntity.ok().build();
    }
    

    private void deleteFormDependencies(Form form){
        List<FormAnswer> formAnswers = formAnswerRepository.findAllByForm(form);
        for(var formAnswer : formAnswers){
            List<QuestionAnswer> questionAnswers = questionAnswerRepository.findAllByFormAnswer(formAnswer);
            for(var questionAnswer : questionAnswers){
                rankingOrderRepository.deleteAllByQuestionAnswer(questionAnswer);
                checkboxChoiceRepository.deleteAllByQuestionAnswer(questionAnswer);
                questionAnswerRepository.delete(questionAnswer);
            }
            formAnswerRepository.delete(formAnswer);
        }

        List<Question> questions = questionRepository.findAllByForm(form);
        for(Question question : questions){
            optionRepository.deleteAllByQuestion(question);
            questionRepository.delete(question);
        }
    }

    private User user(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User)authentication.getPrincipal();
    }
}
