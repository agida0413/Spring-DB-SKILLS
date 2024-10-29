package yj.propagation.mapper;

import org.apache.ibatis.annotations.Mapper;
import yj.propagation.domain.board.Board;
import yj.propagation.domain.board.BoardLog;
import yj.propagation.web.dto.InsertBoardDTO;
@Mapper
public interface BoardMapper {

    public void insertBoard(Board board);
    public void insertBoardLog(BoardLog boardLog);

}
