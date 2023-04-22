package back.backend.repository.answers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import back.backend.entity.answers.QuestionAnswer;
import back.backend.entity.answers.RankingOrder;

public interface RankingOrderRepository extends JpaRepository<RankingOrder, Long>{
    Optional<RankingOrder> findById(long id);
    Boolean existsById(long id);
    List<RankingOrder> findAllByQuestionAnswer(QuestionAnswer questionAnswer);
    void deleteAllByQuestionAnswer(QuestionAnswer questionAnswer);
}
