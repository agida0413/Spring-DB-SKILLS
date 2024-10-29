package yj.propagation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yj.propagation.domain.board.Board;
import yj.propagation.domain.board.BoardLog;
import yj.propagation.repository.BoardRepository;
import yj.propagation.web.dto.InsertBoardDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    //일반 Repository 2개를 하나의 @Transactional 이 붙은 Service에서 묶어서 사용
    @Transactional
    public ResponseEntity<?> boardInsert1(InsertBoardDTO insertBoardDTO){
        Board board = insertBoardDTO.toDomain();

        //게시물 삽입 일반 리포지토리 트랜잭션 xxx
        boardRepository.insertBoard(board);

        long boardId= board.getBoardId();


        BoardLog boardLog = new BoardLog(boardId,"현재날짜");

        //게시물  로그  삽입 일반 리포지토리 트랜잭션 xxx
        boardRepository.insertBoardLog(boardLog);


        //결과 : 두 개의 리파지토리는 하나의 물리적 트랜잭션으로 묶인다. --> 아무것도 커밋되지않음.

        return ResponseEntity.ok(board);
    }


    //트랜잭션이 적용되지 않은 Service 에서 두개의 독립된 트랜잭션이 적용된 Repository를 호출
    public ResponseEntity<?> boardInsert2(InsertBoardDTO insertBoardDTO){
        Board board = insertBoardDTO.toDomain();

        //게시물 삽입 트랜잭션
        boardRepository.insertBoard(board);

        long boardId= board.getBoardId();


        BoardLog boardLog = new BoardLog(boardId,"현재날짜");

        //게시물 로그 삽입 트랜잭션
        boardRepository.insertBoardLog(boardLog);

        //결과 : 두 개의 리파지토리는 각각의 물리적 트랜잭션으로 동작 --> 게시물 삽입은 커밋됌 , 로그는 롤백됌
        return ResponseEntity.ok(board);
    }


    //트랜잭션이 적용된 Service이고 두개의 독립된 트랜잭션이 적용된 Repositroy(Required 옵션) ====> 성공 시 커밋
    @Transactional
    public ResponseEntity<?> boardInsert3(InsertBoardDTO insertBoardDTO){
        Board board = insertBoardDTO.toDomain();

        //게시물 삽입 트랜잭션
        boardRepository.insertBoard(board);

        long boardId= board.getBoardId();


        BoardLog boardLog = new BoardLog(boardId,"현재날짜");

        //게시물 로그 삽입 트랜잭션
        boardRepository.insertBoardLog(boardLog);

        //결과 : 정상적으로 두개의 데이터 모드 커밋
        // 두 개의 리파지토리는 각각의 내부 트랜잭션 , Service는 외부트랜잭션으로 동작  = > 내부트랜잭션은 commit x
        // 최종 외부 트랜잭션 커밋시점에서 모두 커밋 (inNewTransacion= true)
        return ResponseEntity.ok(board);
    }


    //트랜잭션이 적용된 Service이고 두개의 독립된 트랜잭션이 적용된 Repositroy(Required 옵션) ====> 실패 시 롤백 ==> 정상흐름으로 전환시도
    @Transactional
    public ResponseEntity<?> boardInsert4(InsertBoardDTO insertBoardDTO){
        Board board = insertBoardDTO.toDomain();


        //게시물 삽입 트랜잭션
        boardRepository.insertBoard(board);

        long boardId= board.getBoardId();


        BoardLog boardLog = new BoardLog(boardId,"현재날짜");

        //게시물 로그 삽입 트랜잭션 , 정상흐름으로 전환 시도

        //만약 잡지 않을 시 전파된 예외 때문에 (트랜잭션 떄문 아님) 외부 트랜잭션이 롤백

        // 예외 잡기 시도
        try {
            boardRepository.insertBoardLog(boardLog);
        } catch (RuntimeException e) {
            log.info("error={}",e);
            log.info("정상 전환");
        }




        //결과 : 전파된 예외 때문에 롤백되는 걸 막으려 예외를 잡아도 UnexpectedRollbackException 발생 , 전체 롤백
        //이유 : 예외를 잡아도 rollbackOnly=  true 표시가 있기때문에 최종 외부트랜잭션 커밋시점에 전체 롤백을 해버림



        return ResponseEntity.ok(board);
    }

    //Required_NEW 옵션을 통한 컨트롤러 = > insertBoardLog Repository에 Required_NEW 옵션
    @Transactional
    public ResponseEntity<?> boardInsert5(InsertBoardDTO insertBoardDTO){
        Board board = insertBoardDTO.toDomain();


        boardRepository.insertBoard(board);

        long boardId= board.getBoardId();


        BoardLog boardLog = new BoardLog(boardId,"현재날짜");


        //만약 잡지 않을 시 전파된 예외 때문에 (트랜잭션 떄문 아님) 외부 트랜잭션이 롤백


        // 예외 잡기 시도
        try {
            boardRepository.insertBoardLog(boardLog);
        } catch (RuntimeException e) {
            log.info("error={}",e);
            log.info("정상 전환");
        }

        //결과 : Board 는 정상 커밋 , BoardLog는 롤백
        //첫번째 포인트 : 트랜잭션과 상관없이 리파지토리에서 올라오는 RuntimeException 때문에 외부 트랜잭션이 롤백되버림
        // -> catch로 예외 정상 흐름으로 전환
        //두번째 포인트 : 예외를 잡더라도 rollbackOnly = true 표시때문에 롤백되버리는 현상을 Required_NEW 옵션으로 독립된 트랜잭션 으로 분리함
        // - > insertBoardLog가 롤백되더라도 InsertBoard 및 외부 트랜잭션에 영향을 주지않음

        //정상적인 응답을 함

        //But
        // 그만큼 하나의 로직에 여러 커넥션을 사용하는 것이기 때문에 성능에 좋지않다.
        return ResponseEntity.ok(board);
    }
}
