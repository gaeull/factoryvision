package webproject.factoryvision.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.board.dto.BoardRequest;
import webproject.factoryvision.domain.board.dto.BoardResponse;
import webproject.factoryvision.domain.board.service.BoardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/factoryvision/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @PostMapping()
    public ResponseEntity<Void> Post(@RequestBody BoardRequest request) {
        boardService.post(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 전체 게시글 조회
    @GetMapping()
    public List<BoardResponse> findAllPosts() {
        return boardService.findAllPosts();
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoardDetails(@PathVariable Long id) {

        BoardResponse board = boardService.getBoardDetails(id);
        return board != null ? ResponseEntity.ok(board) : ResponseEntity.notFound().build();
    }

    // 게시글 수정
    @PostMapping("/{id}")
    public ResponseEntity<Void> updateBoard(@PathVariable("id") long id, @RequestBody BoardRequest request) {
        boardService.updateBoard(id, request);
        return ResponseEntity.noContent().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("id") long id) {
        boardService.DeleteBoard(id);
        return ResponseEntity.ok().build();
    }

    // 제목 키워드로 검색 기능
    @GetMapping("/search/keyword/{keyword}")
    public Page<BoardResponse> searchByKeyword(@PathVariable String keyword, @PageableDefault(sort = "id", direction = Sort.Direction.ASC)Pageable pageable) {
        return boardService.searchByKeyword(keyword, pageable);
    }

//    // user_id로 검색 기능
//    @GetMapping("/search/userId/{userId}")
//    public Page<BoardResponse> searchByUserId(@PathVariable String userId, @PageableDefault(sort = "id", direction = Sort.Direction.ASC)Pageable pageable) {
//        return boardService.searchByUserId(userId, pageable);
//    }

}
