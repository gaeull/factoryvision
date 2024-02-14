package webproject.factoryvision.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webproject.factoryvision.domain.board.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
