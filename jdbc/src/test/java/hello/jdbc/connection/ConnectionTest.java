package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConnectionTest {


    @Test
    void driverManager() throws SQLException {
        Connection connection1 = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
        Connection connection2 = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);

        log.info("connection={} ,class={}",connection1,connection1.getClass());
        log.info("connection={} ,class={}",connection2,connection2.getClass());

    }

    @Test
    void datasourceDriverManager() throws SQLException {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);

        useDatasource(driverManagerDataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        //커넥션 풀링
        //별도의 스레드
        HikariDataSource datasource = new HikariDataSource();
        datasource.setJdbcUrl(ConnectionConst.URL);
        datasource.setUsername(ConnectionConst.USERNAME);
        datasource.setPassword(ConnectionConst.PASSWORD);
        datasource.setMaximumPoolSize(10);
        datasource.setPoolName("MyPool");

        useDatasource(datasource);
        Thread.sleep(1000);

    }
        @Test
        public void useDatasource(DataSource dataSource)throws SQLException{
            Connection connection1 = dataSource.getConnection();
            Connection connection2 = dataSource.getConnection();
            Connection connection3 = dataSource.getConnection();
            Connection connection4 = dataSource.getConnection();
            Connection connection5 = dataSource.getConnection();
            Connection connection6 = dataSource.getConnection();
            Connection connection7 = dataSource.getConnection();
            Connection connection8 = dataSource.getConnection();
            Connection connection9 = dataSource.getConnection();
            Connection connection10 = dataSource.getConnection();



            log.info("connection={} ,class={}",connection1,connection1.getClass());
            log.info("connection={} ,class={}",connection2,connection2.getClass());

        }
}
