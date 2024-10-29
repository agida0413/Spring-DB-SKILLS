package yj.propagation.domain.board;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardLog {


    private Long boardId;
    private String logText;

    public BoardLog(Long board_id, String logText) {

        this.boardId = board_id;
        this.logText = logText;
    }
}
