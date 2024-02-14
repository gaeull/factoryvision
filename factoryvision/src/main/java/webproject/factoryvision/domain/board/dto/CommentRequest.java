package webproject.factoryvision.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequest {
    private String content;
    private String nickname;
}
