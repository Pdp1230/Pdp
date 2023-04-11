package back.backend.repository;

import back.backend.entity.Form;
import back.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FormRepository extends JpaRepository<Form, Long> {
    Optional<Form> findById(long id);
    Optional<Form> findByFetchId(String fetchId);
    Boolean existsById(long id);
    Boolean existsByFetchId(String fetchId);
    List<Form> findAllByUser(User user);
}