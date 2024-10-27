package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.domain.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

@Slf4j

//@Transactional
@RequiredArgsConstructor

public class MemberServiceV3_3 {


    private final MemberRepositoryV3 memberRepository;


    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

            Member fromMember= memberRepository.findById(fromId);
            Member toMember = memberRepository.findById(toId);

            try {
                memberRepository.update(fromId,fromMember.getMoney()-money);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(toMember.getMemberId().equals("ex")){
                throw new IllegalStateException("이체중 예외");
            }

            try {
                memberRepository.update(toId,toMember.getMoney()+money);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


    }
}
