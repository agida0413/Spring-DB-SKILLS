package hello.springtx.propagation;

import hello.springtx.order.propagation.LogRepository;
import hello.springtx.order.propagation.MemberRepository;
import hello.springtx.order.propagation.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    @Test
    void outerTxOff_success(){
        //given
        String username = "outerTxoff_success";

        //when
        memberService.joinV1(username);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());


    }


    @Test
    void outerTxOff_fail(){
        //given
        String username = "로그예외_outerTxoff_fail";

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());


    }


    @Test
    void singleTx(){
        //given
        String username = "outerTxoff_success";

        //when
        memberService.joinV1(username);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());


    }


    @Test
    void outerTxOn_success(){
        //given
        String username = "outerTxOn_success";

        //when
        memberService.joinV1(username);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());


    }


    @Test
    void outerTxOn_fail(){
        //given
        String username = "로그예외_outerTxOn_success";

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());


    }


    @Test
    void recoverException_fail(){
        //given
        String username = "로그예외_outerTxOn_success";

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> memberService.joinV2(username)).isInstanceOf(RuntimeException.class);

        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());


    }



    @Test
    void recoverException_success(){
        //given
        String username = "로그예외_outerTxOn_success";

        //when
       memberService.joinV2(username);

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());


    }
}
