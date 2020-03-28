import com.mashibing.bean.Emp;
import com.mashibing.bean.User;
import com.mashibing.dao.EmpDao;
import com.mashibing.dao.UserDao;
import com.mashibing.dao.UserDaoAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MyTest {

    SqlSessionFactory sqlSessionFactory =  null;

    @Before
    public void init(){
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void test01() throws IOException {

        //获取与数据库相关的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取对应的映射接口对象
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        //执行具体的sql语句
        Emp emp = mapper.selectEmpByEmpno(7369);
        System.out.println(emp);
        //关闭会话
        sqlSession.close();

    }

    @Test
    public void test02(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp emp = new Emp();
        emp.setEmpno(3333);
        emp.setEname("zhangsan");
        Integer save = mapper.save(emp);
        System.out.println(save);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void test03(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp emp = new Emp();
        emp.setEmpno(3333);
        emp.setEname("zhangsan");
        emp.setSal(500.0);
        Integer update = mapper.update(emp);
        System.out.println(update);
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void test04(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Integer delete = mapper.delete(3333);
        System.out.println(delete);
        sqlSession.commit();
        sqlSession.close();
    }


    @Test
    public void test05() throws IOException {

        //获取与数据库相关的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取对应的映射接口对象
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        //执行具体的sql语句
        User user = mapper.selectUserById(1);
        System.out.println(user);
        //关闭会话
        sqlSession.close();

    }
}
