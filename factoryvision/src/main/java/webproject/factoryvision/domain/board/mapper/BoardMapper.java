package webproject.factoryvision.domain.board.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import webproject.factoryvision.domain.board.dto.BoardResponse;
import webproject.factoryvision.domain.board.dto.BoardRequest;
import webproject.factoryvision.domain.board.entity.Board;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
    @Mapping(source = "userId", target = "user.id")
    Board toEntity(BoardRequest request);

    @Mapping(source = "user.id", target = "userId")
    BoardResponse toDto(Board board);

    List<BoardResponse> toBoardDtoList(List<Board> boardList);
}
