package webproject.factoryvision.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webproject.factoryvision.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUserId(String userId);
    User findByUserId(String userId);
}