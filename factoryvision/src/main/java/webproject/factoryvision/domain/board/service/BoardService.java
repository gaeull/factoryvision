package webproject.factoryvision.domain.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webproject.factoryvision.domain.board.dto.BoardResponse;
import webproject.factoryvision.domain.board.dto.BoardRequest;
import webproject.factoryvision.domain.board.entity.Board;
import webproject.factoryvision.domain.board.mapper.BoardMapper;
import webproject.factoryvision.domain.board.repository.BoardRepository;
import webproject.factoryvision.domain.user.repository.UserRepository;
import webproject.factoryvision.domain.user.service.UserService;
import webproject.factoryvision.exception.UnauthorizedException;
import webproject.factoryvision.domain.token.TokenProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Builder
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public void post(BoardRequest request) {
        boardRepository.save(boardMapper.toEntity(request));
    }

    private BoardResponse mapToBoardResponse(Board board) {
        BoardResponse boardResponse = boardMapper.toDto(board);
        if (board.getUser() != null) {
            boardResponse.setName(board.getUser().getName());
        }
        boardResponse.setCreatedAt(board.getCreatedAt());
        return boardResponse;
    }

    public List<BoardResponse> findAllPosts() {
        return boardRepository.findAll().stream()
                .map(this::mapToBoardResponse)
                .collect(Collectors.toList());
    }

    public BoardResponse getBoardDetails(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        if (board.isPresent()) {
            Board boardResponse = board.get();
            return mapToBoardResponse(boardResponse);
        } else {
            return null;
        }
   }

   @Transactional
   public void updateBoard(long id, BoardRequest request) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String userId = authentication.getName();
//       log.info("저장된 곳에서의 인증 userID {}", userId);

       Optional<Board> boardInfo = boardRepository.findById(id);

       if (boardInfo.isPresent()) {
           Board board = boardInfo.get();
           String boardUserId = board.getUser().getUserId();
//           log.info("board에 저장된 userid정보 {} ", boardUserId);
           if (userId.equals(boardUserId)) {
               board.update(request, userRepository);
           } else {
               throw new UnauthorizedException("해당 사용자는 이 게시판을 수정할 권한이 없습니다.");
           }
       } else {
           throw new EntityNotFoundException("게시판 정보가 없습니다.");
       }
   }

   public void DeleteBoard(long id) {
        boardRepository.deleteById(id);
   }

   // 검색 기능
    @Transactional
    public Page<BoardResponse> searchByKeyword(String keyword, Pageable pageable) {
        Page<Board> boardList = boardRepository.findByTitleContaining(keyword, pageable);
        return boardList.map(boardMapper::toDto);
    }

//    @Transactional
//    public Page<BoardResponse> searchByUserId(Long userId, Pageable pageable) {
//        Page<Board> boardList = boardRepository.findByUserId(userId, pageable);
//        return boardList.map(boardMapper::toDto);
//    }


}
