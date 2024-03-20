package webproject.factoryvision.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.board.dto.CommentRequest;
import webproject.factoryvision.domain.board.dto.CommentResponse;
import webproject.factoryvision.domain.board.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/factoryvision/board/")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("{postId}/comment")
    @Operation(summary = "댓글 작성", description = "postId 파라미터에 슷자입력, request body에 내용, 닉네임 입력")
    public ResponseEntity<Void> WriteComment(@PathVariable("postId") Long postId, @RequestBody CommentRequest request) {
        commentService.WriteComment(postId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{postId}/comment")
    public ResponseEntity<List<CommentResponse>> getComents(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getCommentsById(postId));
    }

    // 댓글 삭제
    @DeleteMapping("{postId}/comment")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<Void> DeleteComment(@PathVariable("postId") Long postId, Long commentId) {
        commentService.DeleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
