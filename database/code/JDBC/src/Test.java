import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Test {
    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1","root","123456");
        PreparedStatement pstmt = conn.prepareStatement("insert into rental(rental_date,inventory_id,customer_id,return_date,staff_id,last_update) values (?,?,?,?,?,?);");
        for (int i = 200000; i < 5000000; i++) {
            pstmt.setDate(1,new Date(new java.util.Date().getTime()));
            pstmt.setInt(2,1);
            pstmt.setInt(3,1);
            pstmt.setDate(4,new Date(new java.util.Date().getTime()));
            pstmt.setInt(5,1);
            pstmt.setDate(6,new Date(new java.util.Date().getTime()));
            pstmt.execute();
        }
        conn.close();
    }
}
