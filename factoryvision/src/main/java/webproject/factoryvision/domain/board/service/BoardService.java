package webproject.factoryvision.domain.board.service;

import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webproject.factoryvision.domain.board.dto.UpdateBoardRequest;
import webproject.factoryvision.domain.board.entity.Board;
import webproject.factoryvision.domain.board.repository.BoardRepository;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Builder
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    public Board post(String title, String content, String userId) {
        User user = userRepository.findByUserId(userId);

        Board board = Board.builder()
                .title(title)
                .content(content)
                .userId(user)
                .build();

        return boardRepository.save(board);
    }

    public List<Board> findAllPosts() {
        return boardRepository.findAll();
    }

   public Optional<Board> getBoardDetails(Long id) {
       return boardRepository.findById(id);
   }

   @Transactional
   public Board updateBoard(long id, UpdateBoardRequest request) {
       Board boardInfo = boardRepository.findById(id)
               .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
       boardInfo.update(request);
       return boardInfo;
   }

   public void DeleteBoard(long id) {
        boardRepository.deleteById(id);
   }
}
