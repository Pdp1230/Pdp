package back.backend.repository.answers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import back.backend.entity.answers.CheckboxChoice;
import back.backend.entity.answers.QuestionAnswer;

public interface CheckboxChoiceRepository extends JpaRepository<CheckboxChoice, Long> {
    Optional<CheckboxChoice> findById(long id);
    Boolean existsById(long id);
    List<CheckboxChoice> findAllByQuestionAnswer(QuestionAnswer questionAnswer);
    void deleteAllByQuestionAnswer(QuestionAnswer questionAnswer);
}
