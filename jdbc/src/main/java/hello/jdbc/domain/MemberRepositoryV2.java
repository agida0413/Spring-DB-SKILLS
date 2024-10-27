package hello.jdbc.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV2 {

    private final DataSource dataSource;

    public void save(Member member) throws SQLException {
        String sql= "INSERT INTO MEMBER(member_id,money) values(?,?)";


        Connection conn= null;
        PreparedStatement psmt = null;


        try {
            conn = getConnection();

            psmt = conn.prepareStatement(sql);
            psmt.setString(1, member.getMemberId());
            psmt.setInt(2,member.getMoney());

            psmt.executeUpdate();
        } catch (SQLException e) {
            log.error("dberr:{}",e);
            throw e;
        }
        finally {

           close(conn,psmt,null);
        }


    }

    private void close(Connection conn, Statement stmt, ResultSet rs){

        JdbcUtils.closeConnection(conn);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeResultSet(rs);


    }

    public Member findById(String memberId){
        String sql = "select * from member where member_id = ? ";

        Connection conn=null;
        PreparedStatement ps = null;
        ResultSet rs= null;

        try {
            conn=getConnection();
            ps= conn.prepareStatement(sql);
            ps.setString(1,memberId);

            rs= ps.executeQuery();

            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return  member;
            }else{
                throw new NoSuchElementException("member not found");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            close(conn,ps,rs);
        }
    }

    public Member findById(Connection conn,String memberId){
        String sql = "select * from member where member_id = ? ";


        PreparedStatement ps = null;
        ResultSet rs= null;

        try {

            ps= conn.prepareStatement(sql);
            ps.setString(1,memberId);

            rs= ps.executeQuery();

            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return  member;
            }else{
                throw new NoSuchElementException("member not found");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {

            JdbcUtils.closeStatement(ps);
            JdbcUtils.closeResultSet(rs);
        }
    }


    public void update(String memberId, int money) throws SQLException {
        String sql= "update member set money=? where member_id=?";


        Connection conn= null;
        PreparedStatement psmt=null;

        try {
            conn = getConnection();

            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, money);
            psmt.setString(2,memberId);

            psmt.executeUpdate();
        } catch (SQLException e) {
            log.error("dberr:{}",e);
            throw e;
        }
        finally {

            close(conn,psmt,null);
        }

    }


    public void update(Connection conn,String memberId, int money) throws SQLException {
        String sql= "update member set money=? where member_id=?";


        PreparedStatement psmt=null;

        try {


            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, money);
            psmt.setString(2,memberId);

            psmt.executeUpdate();
        } catch (SQLException e) {
            log.error("dberr:{}",e);
            throw e;
        }
        finally {

            JdbcUtils.closeStatement(psmt);

        }

    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private  Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();

        log.info("get conncetion={} , class={}" ,connection,connection.getClass());
        return connection;
    }

}
