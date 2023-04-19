package back.backend.repository.answers;

import java.util.List;
import java.util.Optional;

import back.backend.entity.answers.FormAnswer;
import back.backend.entity.answers.QuestionAnswer;

public interface QuestionAnswerRepository {
    Optional<QuestionAnswer> findById(long id);
    Boolean existsById(long id);
    List<QuestionAnswer> findAllByFormAnswer(FormAnswer formAnswer);
}
