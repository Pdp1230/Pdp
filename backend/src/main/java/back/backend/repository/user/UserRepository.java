package back.backend.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import back.backend.entity.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}