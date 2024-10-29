package yj.propagation.web.dto;

import lombok.Data;
import yj.propagation.domain.board.Board;

@Data
public class InsertBoardDTO {


    private String text;

    public Board toDomain(){
        Board board= new Board();
        board.setText(this.text);
        return  board;
    }
}
