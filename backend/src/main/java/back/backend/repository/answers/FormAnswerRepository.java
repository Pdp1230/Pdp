package back.backend.repository.answers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import back.backend.entity.answers.FormAnswer;
import back.backend.entity.forms.Form;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long>{
    Optional<FormAnswer> findById(long id);
    Boolean existsById(long id);
    List<FormAnswer> findAllByForm(Form form);
}
