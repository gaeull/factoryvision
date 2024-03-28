package webproject.factoryvision.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.board.dto.BoardRequest;
import webproject.factoryvision.domain.board.dto.BoardResponse;
import webproject.factoryvision.domain.board.service.BoardService;
import webproject.factoryvision.exception.UnauthorizedException;

import java.util.List;

@Tag(name = "게시판 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/factoryvision/board")
@CrossOrigin
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final HttpHeaders getHeaders;

    // 게시글 작성
    @PostMapping()
    @Operation(summary = "게시글 작성", description = "사용자id(로그인에 사용되는 userid아님), 제목, 내용 입력")
    public ResponseEntity<?> Post(@RequestBody BoardRequest request) {
        boardService.post(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(getHeaders)
                .body("게시글 작성 완료");
    }

    // 전체 게시글 조회
    @GetMapping()
    @Operation(summary = "전체 게시글 조회", description = "게시글id, 게시글제목, 게시글내용, 사용자id, 사용자이름, 게시글작성일 전체 출력")
    public ResponseEntity<List<BoardResponse>> findAllPosts() {
        List<BoardResponse> allPosts = boardService.findAllPosts();
        return ResponseEntity.status(HttpStatus.OK).body(allPosts);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    @Operation(summary = "게시글 상세 조회(id로)", description = "파라미터 id에 열람하고싶은 게시글id 입력 --> 게시글id, 제목, 내용, 사용자id, 사용자이름, 게시글작성일 출력")
    public ResponseEntity<?> getBoardDetails(@PathVariable Long id) {
        BoardResponse board = boardService.getBoardDetails(id);
        return board != null ? ResponseEntity.status(HttpStatus.OK).body(board) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당되는 게시물이 없습니다.");
    }

    // 게시글 수정
    @Operation(summary = "특정 게시글 수정(id로)", description = "파라미터 id에 수정하고싶은 게시글Id 입력 --> 사용자id(로그인에사용되는 userid아님), 제목, 내용 출력")
    @PostMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable("id") long id, @RequestBody BoardRequest request) {
        try {
            boardService.updateBoard(id, request);
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(getHeaders)
                    .body("게시글 수정 완료");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .headers(getHeaders)
                    .body("해당 사용자는 이 게시판을 수정할 권한이 없습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(getHeaders)
                    .body("게시판 정보가 없습니다.");
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "특정 게시글 삭제(id로)")
    public ResponseEntity<?> deleteBoard(@PathVariable("id") long id) {
        boardService.DeleteBoard(id);
        return ResponseEntity.status(HttpStatus.OK)
                .headers(getHeaders)
                .body("게시글 삭제 완료");
    }

    // 제목 키워드로 검색 기능
    @GetMapping("/search/keyword/{keyword}")
    @Operation(summary = "제목 키워드(str)로 검색", description = "keyword 파라미터에 키워드 입력, pageable에 page설정, size갯수설정, sort는 id로")
    public Page<BoardResponse> searchByKeyword(@PathVariable String keyword, @PageableDefault(sort = "id", direction = Sort.Direction.ASC)Pageable pageable) {
        return boardService.searchByKeyword(keyword, pageable);
    }

//    // user_id로 검색 기능
//    @GetMapping("/search/userId/{userId}")
//    public Page<BoardResponse> searchByUserId(@PathVariable String userId, @PageableDefault(sort = "id", direction = Sort.Direction.ASC)Pageable pageable) {
//        return boardService.searchByUserId(userId, pageable);
//    }

}
