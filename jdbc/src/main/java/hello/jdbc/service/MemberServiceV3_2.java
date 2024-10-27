package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.domain.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

@Slf4j

//트랜잭션 매니저
public class MemberServiceV3_2 {

    //private final PlatformTransactionManager platformTransactionManager;
   private  TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.txTemplate = txTemplate= new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        txTemplate.executeWithoutResult((status) -> {

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
        });




    }
}
