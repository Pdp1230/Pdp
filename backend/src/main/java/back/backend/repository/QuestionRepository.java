package back.backend.repository;

import back.backend.entity.Form;
import back.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findById(long id);
    Boolean existsById(long id);
    List<Question> findAllByForm(Form form);
}