package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ExceptionTEST {

    @Autowired
    RollbackService service;

    @Test
    void runtimeeX(){

        Assertions.assertThatThrownBy(()->service.runtimeException()).isInstanceOf(RuntimeException.class);

    }

    @Test
    void checkedEx(){

        Assertions.assertThatThrownBy(()->service.checkedException()).isInstanceOf(RollbackService.MyException.class);

    }

    @Test
    void rollBackFor(){

        Assertions.assertThatThrownBy(()->service.rollbackFor()).isInstanceOf(RollbackService.MyException.class);

    }

    @TestConfiguration
    static class RollbackTestConfig{
        @Bean
        RollbackService rollbackService(){
            return  new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService{

        @Transactional
        public void runtimeException(){
            log.info("call RuntimeException");
            throw new RuntimeException();
        }

        @Transactional
        public void checkedException() throws MyException {
            log.info("call ChekcedException");
            throw new MyException();
        }

        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call rollbackFor");
            throw new MyException();
        }

        static class MyException extends Exception{

        }
    }

}
