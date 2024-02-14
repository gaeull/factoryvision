package webproject.factoryvision.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webproject.factoryvision.domain.board.entity.Board;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
//    Optional<Board> findById(Long id);
}



