import java.util.Scanner;

/**
 * @author: 马士兵教育
 * @create: 2019-08-18 16:42
 */
public class Administrator {

    String loginName = "admin";
    String password = "123";

    public void introduce(){
        System.out.println(loginName);
        System.out.println(password);
    }

    public static void main(String[] args) {
        Administrator admin = new Administrator();
        System.out.println(admin.loginName);
        System.out.println(admin.password);
        admin.introduce();

        //从控制台读取数据，需要Scanner对象
        Scanner sc  = new Scanner(System.in);
        System.out.println("修改密码");
        while(true){
            System.out.println("请输入用户名称：");
            String name = sc.nextLine();
            System.out.println("请输入密码：");
            String pwd = sc.nextLine();
            if (name.equals(admin.loginName) && pwd.equals(admin.password)){
                admin.loginName = sc.nextLine();
                admin.password = sc.nextLine();
                System.out.println("修改成功");

                break;
            }else{
                System.out.println("用户名和密码不正确，请重新输入");
            }
        }
        admin.introduce();
    }
}
