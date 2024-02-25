package webproject.factoryvision.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webproject.factoryvision.domain.board.dto.CommentRequest;
import webproject.factoryvision.domain.board.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/factoryvision/board/")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("{postId}/comment")
    public ResponseEntity<Void> WriteComment(@PathVariable("postId") Long postId, CommentRequest request) {
        commentService.WriteComment(postId, request);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("{postId}/comment")
    public ResponseEntity<Void> DeleteComment(@PathVariable("postId") Long postId, Long commentId) {
        commentService.DeleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
