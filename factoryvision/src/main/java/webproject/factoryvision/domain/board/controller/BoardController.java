package webproject.factoryvision.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.board.dto.UpdateBoardRequest;
import webproject.factoryvision.domain.board.entity.Board;
import webproject.factoryvision.domain.board.service.BoardService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("factoryvision/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @PostMapping()
    public ResponseEntity<Board> Post(String title, String content, String userId) {
        Board savedPost = boardService.post(title, content, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedPost);
    }

    // 전체 게시글 조회
    @GetMapping()
    public List<Board> findAllPosts() {
        return boardService.findAllPosts();
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public Optional<Board> getBoardDetails(@PathVariable Long id) {
        return boardService.getBoardDetails(id);
    }

    // 게시글 수정
    @PostMapping("/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable("id") long id, @RequestBody UpdateBoardRequest request) {
        Board updatedBoard = boardService.updateBoard(id, request);
        return ResponseEntity.ok()
                .body(updatedBoard);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("id") long id) {
        boardService.DeleteBoard(id);
        return ResponseEntity.ok().build();
    }

}
