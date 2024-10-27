package hello.jdbc.domain;

import hello.jdbc.connection.DBConnectionUtil;
import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC
 */
@Slf4j
public class MemberRepositoryV0 {

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

       if(stmt!=null){
           try {
               stmt.close();
           } catch (SQLException e) {
              log.info("sql ex");
           }
       }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                log.info("conn close ex");
            }
        }

        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("rs ex");
            }
        }

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

    private  Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

}
