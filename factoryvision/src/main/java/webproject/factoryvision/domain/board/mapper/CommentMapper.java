package webproject.factoryvision.domain.board.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import webproject.factoryvision.domain.board.dto.BoardResponse;
import webproject.factoryvision.domain.board.dto.CommentResponse;
import webproject.factoryvision.domain.board.entity.Board;
import webproject.factoryvision.domain.board.entity.Comment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    CommentResponse toDto(Comment comment);
}
