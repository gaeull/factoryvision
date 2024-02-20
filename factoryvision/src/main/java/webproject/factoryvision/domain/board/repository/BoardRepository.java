package webproject.factoryvision.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webproject.factoryvision.domain.board.entity.Board;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    //    Optional<Board> findById(Long id);
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);
    Page<Board> findByUser_UserId(String userId, Pageable pageable);
}



