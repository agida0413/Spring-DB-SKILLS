package yj.propagation.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yj.propagation.service.BoardService;
import yj.propagation.web.dto.InsertBoardDTO;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //일반 Repository 2개를 하나의 @Transactional 이 붙은 Service에서 묶어서 사용
    @PostMapping(value = "/case1", produces = "application/json", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> insertBoard(@ModelAttribute InsertBoardDTO insertBoardDTO){

    return boardService.boardInsert1(insertBoardDTO);
    }

    //트랜잭션이 적용되지 않은 Service 에서 두개의 독립된 트랜잭션이 적용된 Repository를 호출
    @PostMapping(value = "/case2",produces = "application/json", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> insertBoard2(@ModelAttribute InsertBoardDTO insertBoardDTO){

        return boardService.boardInsert2(insertBoardDTO);
    }

    //트랜잭션이 적용된 Service이고 두개의 독립된 트랜잭션이 적용된 Repositroy(Required 옵션) ====> 성공 시 커밋
    @PostMapping(value = "/case3",produces = "application/json", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> insertBoard3(@ModelAttribute InsertBoardDTO insertBoardDTO){

        return boardService.boardInsert3(insertBoardDTO);
    }

    //트랜잭션이 적용된 Service이고 두개의 독립된 트랜잭션이 적용된 Repositroy(Required 옵션) ====> 실패 시 롤백
    @PostMapping(value = "/case4",produces = "application/json", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> insertBoard4(@ModelAttribute InsertBoardDTO insertBoardDTO){

        return boardService.boardInsert4(insertBoardDTO);
    }

    //Required_NEW 옵션을 통한 컨트롤러
    @PostMapping(value = "/case5",produces = "application/json", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> insertBoard5(@ModelAttribute InsertBoardDTO insertBoardDTO){

        return boardService.boardInsert5(insertBoardDTO);
    }
}
