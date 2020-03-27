import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Test2 {
    public static void main(String[] args) throws Exception{
        long starttime = System.currentTimeMillis();
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.85.111:3306/sakila","root","123456");
        PreparedStatement pstmt = conn.prepareStatement("insert into userinfo(name) values(?)");
        for (int i = 0; i < 1000000; i++) {
            pstmt.setString(1,i+"");
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        conn.close();
        System.out.println(System.currentTimeMillis()-starttime);
    }
}
