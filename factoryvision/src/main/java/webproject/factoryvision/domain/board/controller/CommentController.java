package webproject.factoryvision.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.board.dto.CommentRequest;
import webproject.factoryvision.domain.board.dto.CommentResponse;
import webproject.factoryvision.domain.board.service.CommentService;

import java.util.List;

@Tag(name = "댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/factoryvision/board/")
public class CommentController {

    private final CommentService commentService;
    private final HttpHeaders getHeaders;

    // 댓글 작성
    @PostMapping("{postId}/comment")
    @Operation(summary = "댓글 작성", description = "postId 파라미터에 슷자입력, request body에 내용, 닉네임 입력")
    public ResponseEntity<?> WriteComment(@PathVariable("postId") Long postId, @RequestBody CommentRequest request) {
        try {
            commentService.WriteComment(postId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(getHeaders)
                    .body("댓글 작성 완료");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(getHeaders)
                    .body("게시판 정보가 없습니다.");
        }
    }

    // 전체 댓글 조회
    @GetMapping("{postId}/comment")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId){
        List<CommentResponse> allComments = commentService.getCommentsById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(allComments);
    }

    // 댓글 삭제
    @DeleteMapping("{postId}/comment")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<?> DeleteComment(@PathVariable("postId") Long postId, Long commentId) {
        try {
            commentService.DeleteComment(postId, commentId);
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(getHeaders)
                    .body("댓글 삭제 완료");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(getHeaders)
                    .body("댓글에 해당되는 게시글이 없습니다.");
        }
    }

}
