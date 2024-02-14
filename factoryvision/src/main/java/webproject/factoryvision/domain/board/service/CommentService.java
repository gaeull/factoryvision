package webproject.factoryvision.domain.board.service;

import lombok.Builder;
import org.springframework.stereotype.Service;
import webproject.factoryvision.domain.board.dto.CommentRequest;
import webproject.factoryvision.domain.board.entity.Board;
import webproject.factoryvision.domain.board.entity.Comment;
import webproject.factoryvision.domain.board.repository.BoardRepository;
import webproject.factoryvision.domain.board.repository.CommentRepository;


@Service
@Builder
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public void WriteComment(Long id, CommentRequest request) {
        Board board = boardRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Target post does not exist."));
        Comment result = Comment.builder()
                .content(request.getContent())
                .nickname(request.getNickname())
                .board(board)
                .build();
        commentRepository.save(result);
    }

    public void DeleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
