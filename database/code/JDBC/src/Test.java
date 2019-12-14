import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Test {
    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1","root","123456");
        PreparedStatement pstmt = conn.prepareStatement("insert into tblA(age,birth) values (?,?);");
        for (int i = 200000; i < 1000000; i++) {
            pstmt.setInt(1,i);
            pstmt.setDate(2,new Date(new java.util.Date().getTime()));
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        conn.close();
    }
}
