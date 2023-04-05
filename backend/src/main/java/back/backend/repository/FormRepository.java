package back.backend.repository;

import back.backend.entity.Form;
import back.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FormRepository extends JpaRepository<Form, Long> {
    Optional<Form> findById(long id);
    Boolean existsById(long id);
    List<Form> findAllByUser(User user);
}