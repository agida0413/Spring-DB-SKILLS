package hello.springtx.apply;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class TxBasicTest {


    @Autowired
    BasicService basicService;

    @TestConfiguration
    static class TxApplyBasicConfig{
        @Bean
        BasicService basicService(){
            return new BasicService();
        }
    }

    @Test
    void proxyCheck(){
        log.info("aop class={}",basicService.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest(){
        basicService.tx();
        basicService.nonTx();
    }

    @Slf4j
    static class BasicService{
        @Transactional
        public void tx(){
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();

            log.info("tx actice={}",txActive);


        }

        public void nonTx(){
            log.info("call nontx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();

            log.info("tx actice={}",txActive);
        }

    }

}
