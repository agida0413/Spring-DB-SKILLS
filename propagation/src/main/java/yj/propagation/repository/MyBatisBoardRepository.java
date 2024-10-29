package yj.propagation.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import yj.propagation.domain.board.Board;
import yj.propagation.domain.board.BoardLog;
import yj.propagation.mapper.BoardMapper;
import yj.propagation.web.dto.InsertBoardDTO;
@Repository
@RequiredArgsConstructor
public class MyBatisBoardRepository implements BoardRepository{

    private final BoardMapper boardMapper;

    @Override
    @Transactional
    public void insertBoard(Board board) {
        boardMapper.insertBoard(board);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertBoardLog(BoardLog boardLog) {

        boardMapper.insertBoardLog(boardLog);
        throw new RuntimeException();
    }
}
