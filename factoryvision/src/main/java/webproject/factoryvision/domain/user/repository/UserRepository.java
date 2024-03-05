package webproject.factoryvision.domain.user.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.redis.CacheNames;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUserId(String userId);
    Optional<User> findById(Long id);
    @Cacheable(cacheNames = CacheNames.USERBYUSERID, key = "'login'+#p0", unless = "#result==null")
    Optional<User> findByUserId(String userId);
}