package yj.propagation.domain.board;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Board {

    private Long boardId;
    private String text;

    public Board() {
    }

    public Board(Long boardId, String text) {
        this.boardId = boardId;
        this.text = text;
    }
}
