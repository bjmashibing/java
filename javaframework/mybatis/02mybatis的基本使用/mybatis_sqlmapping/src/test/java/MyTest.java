import com.mashibing.bean.Dept;
import com.mashibing.bean.Emp;
import com.mashibing.dao.DeptDao;
import com.mashibing.dao.EmpDao;
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
        Emp emp = mapper.selectEmpByEmpno(7369);
        System.out.println(emp);
        //关闭会话
        sqlSession.close();

    }
    @Test
    public void test02() throws IOException {

        //获取与数据库相关的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取对应的映射接口对象
        DeptDao mapper = sqlSession.getMapper(DeptDao.class);
        Dept dept = mapper.selectDeptByDeptno(10);
        System.out.println(dept);
        //关闭会话
        sqlSession.close();

    }

}
