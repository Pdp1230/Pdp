package back.backend.repository;

import back.backend.entity.Option;
import back.backend.entity.Question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findById(long id);
    Boolean existsById(long id);
    List<Option> findAllByQuestion(Question question);
    void deleteAllByQuestion(Question question);
}