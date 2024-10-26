package hello.jdbc.connection;

import hello.jdbc.domain.Member;
import hello.jdbc.domain.MemberRepositoryV0;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
public class DbConnectionUtilTest {

    MemberRepositoryV0 repositoryV0=new MemberRepositoryV0();

    @Test
    void connection(){
        Connection connection=DBConnectionUtil.getConnection();
        Assertions.assertThat(connection).isNotNull();
    }



    @Test
    void crud() throws SQLException{
        Member member= new Member("memberv1",10000);

        repositoryV0.save(member);

        Member findMember = repositoryV0.findById(member.getMemberId());

        log.info("findmEMBER={}",findMember);


        repositoryV0.update(member.getMemberId(), 20000);

        Member updateMember=repositoryV0.findById(member.getMemberId());


        member.setMoney(20000);
        Assertions.assertThat(updateMember).isEqualTo(member);


        //delete
        repositoryV0.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() ->  repositoryV0.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }


}
