package yj.propagation.repository;

import yj.propagation.domain.board.Board;
import yj.propagation.domain.board.BoardLog;
import yj.propagation.web.dto.InsertBoardDTO;

public interface BoardRepository {

    public void insertBoard(Board board);
    public void insertBoardLog(BoardLog boardLog);

}
