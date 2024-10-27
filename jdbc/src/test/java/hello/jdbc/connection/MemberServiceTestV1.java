package hello.jdbc.connection;

import hello.jdbc.domain.Member;
import hello.jdbc.domain.MemberRepositoryV1;
import hello.jdbc.service.MemberServiceV1;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

class MemberServiceTestV1 {


    public static final String MEMBER_A="memberA";
    public static final String MEMBER_B="memberB";
    public static final String MEMBER_EX="ex";


    private MemberRepositoryV1 memberRepositoryV1;
    private MemberServiceV1 memberServiceV1;

    @BeforeEach
    void before(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);

            memberRepositoryV1  =  new MemberRepositoryV1(driverManagerDataSource);
            memberServiceV1=new MemberServiceV1(memberRepositoryV1);





    }

    @SneakyThrows
    @Test
    @DisplayName("정상이체")
    void accout(){
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
            memberRepositoryV1.save(memberA);
            memberRepositoryV1.save(memberB);
            memberServiceV1.accountTransfer(memberA.getMemberId(),memberB.getMemberId(),2000);

            Member finaMemberA=memberRepositoryV1.findById(memberA.getMemberId());
            Member finaMemberB=memberRepositoryV1.findById(memberB.getMemberId());

        Assertions.assertThat(finaMemberA.getMoney()).isEqualTo(8000);
        Assertions.assertThat(finaMemberB.getMoney()).isEqualTo(12000);
    }


    @SneakyThrows
    @Test
    @DisplayName("이체중 예외 발생")
    void accout2(){
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_EX, 10000);
        memberRepositoryV1.save(memberA);
        memberRepositoryV1.save(memberB);
        memberServiceV1.accountTransfer(memberA.getMemberId(),memberB.getMemberId(),2000);



        Member finaMemberA=memberRepositoryV1.findById(memberA.getMemberId());
        Member finaMemberB=memberRepositoryV1.findById(memberB.getMemberId());

        Assertions.assertThat(finaMemberA.getMoney()).isEqualTo(8000);
        Assertions.assertThat(finaMemberB.getMoney()).isEqualTo(10000);
    }


    @AfterEach
    void after() throws SQLException {
        memberRepositoryV1.delete(MEMBER_A);
        memberRepositoryV1.delete(MEMBER_B);
        memberRepositoryV1.delete(MEMBER_EX);
    }
}
