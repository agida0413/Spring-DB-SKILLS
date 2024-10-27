package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.domain.MemberRepositoryV2;
import hello.jdbc.domain.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
//트랜잭션 매니저
public class MemberServiceV3_1 {

    private final PlatformTransactionManager platformTransactionManager;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId,int money) throws SQLException {

        TransactionStatus transaction = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        try{


            Member fromMember= memberRepository.findById(fromId);
            Member toMember = memberRepository.findById(toId);

            memberRepository.update(fromId,fromMember.getMoney()-money);
            if(toMember.getMemberId().equals("ex")){
                throw new IllegalStateException("이체중 예외");
            }

            memberRepository.update(toId,toMember.getMoney()+money);

            platformTransactionManager.commit(transaction);
        } catch (Exception e){
            platformTransactionManager.rollback(transaction);
            throw new IllegalStateException();

        }



    }
}
