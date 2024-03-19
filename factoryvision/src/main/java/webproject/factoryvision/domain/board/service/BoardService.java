package webproject.factoryvision.domain.board.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webproject.factoryvision.domain.board.dto.BoardResponse;
import webproject.factoryvision.domain.board.dto.BoardRequest;
import webproject.factoryvision.domain.board.entity.Board;
import webproject.factoryvision.domain.board.mapper.BoardMapper;
import webproject.factoryvision.domain.board.repository.BoardRepository;
import webproject.factoryvision.domain.user.entity.User;
import webproject.factoryvision.domain.user.repository.UserRepository;
import webproject.factoryvision.domain.user.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Builder
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final UserService userService;

    @Autowired
    public BoardService(BoardRepository boardRepository, BoardMapper boardMapper, UserService userService) {
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
        this.userService = userService;
    }


    @Transactional
    public void post(BoardRequest request) {
        boardRepository.save(boardMapper.toEntity(request));
    }

    public List<BoardResponse> findAllPosts() {
        List<Board> posts = boardRepository.findAll();
//        log.info("게시글 조회 결과 {} ", posts);
//        return posts.stream().map(boardMapper::toDto).collect(Collectors.toList());
        return posts.stream()
                .map(board -> {
                    BoardResponse boardResponse = boardMapper.toDto(board);
                    if (board.getUser() != null) {
                        boardResponse.setName(board.getUser().getName());
                    }
                    boardResponse.setCreatedAt(board.getCreatedAt());
                    return boardResponse;
                }).collect(Collectors.toList());
    }

   public BoardResponse getBoardDetails(Long id) {
       return boardMapper.toDto(boardRepository.findById(id).orElse(null));
   }

   @Transactional
   public void updateBoard(long id, BoardRequest request) {
       Board board = boardRepository.findById(id).orElseThrow();
       board.update(request);
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
