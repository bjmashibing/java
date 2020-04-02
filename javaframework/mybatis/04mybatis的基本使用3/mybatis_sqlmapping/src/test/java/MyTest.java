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
import java.util.Arrays;
import java.util.List;

public class MyTest {

    SqlSessionFactory sqlSessionFactory = null;

    @Before
    public void init() {
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

    @Test
    public void test03() throws IOException {

        //获取与数据库相关的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取对应的映射接口对象
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp emp = mapper.selectEmpByStep(7369);
        System.out.println(emp);
        //关闭会话
        sqlSession.close();

    }

    @Test
    public void test04() throws IOException {

        //获取与数据库相关的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取对应的映射接口对象
        DeptDao mapper = sqlSession.getMapper(DeptDao.class);
        Dept dept = mapper.selectDeptByStemp2(10);
        System.out.println(dept.getDname());
        System.out.println(dept.getEmps());
        //关闭会话
        sqlSession.close();

    }


    @Test
    public void test05() throws IOException {

        //获取与数据库相关的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取对应的映射接口对象
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp emp = new Emp();
        emp.setEmpno(7369);
        emp.setEname("SMITH");
        emp.setSal(1000.0);
        Emp emp2 = mapper.selectEmpByCondition(emp);
        System.out.println(emp2);
        //关闭会话
        sqlSession.close();

    }

    @Test
    public void test06() throws IOException {

        //获取与数据库相关的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取对应的映射接口对象
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        List<Emp> list = mapper.selectEmpByDeptnos(Arrays.asList(10, 20));
        for (Emp emp : list) {
            System.out.println(emp);
        }
        //关闭会话
        sqlSession.close();

    }

    @Test
    public void test07() throws IOException {

        //获取与数据库相关的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //获取对应的映射接口对象
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp emp = mapper.selectEmpByEmpno(7369);
        System.out.println(emp);
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        EmpDao mapper2 = sqlSession2.getMapper(EmpDao.class);
        Emp emp2 = mapper2.selectEmpByEmpno(7369);
        System.out.println(emp2);
        sqlSession.close();
        sqlSession2.close();

    }

    @Test
    public void test08() throws IOException {

        //获取与数据库相关的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        //获取对应的映射接口对象
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        EmpDao mapper2 = sqlSession2.getMapper(EmpDao.class);
        Emp emp = mapper.selectEmpByEmpno(7369);
        System.out.println(emp);
        System.out.println("==================");
        emp.setEname("Teacher");
        Integer update = mapper2.update(emp);
        System.out.println(update);
//        sqlSession.clearCache();
        System.out.println("==================");
        emp = mapper.selectEmpByEmpno(7369);
        System.out.println(emp);
        sqlSession.close();

    }

    @Test
    public void test09(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        EmpDao mapper2 = sqlSession2.getMapper(EmpDao.class);

        Emp emp = mapper.selectEmpByEmpno(7369);
        System.out.println(emp);
        sqlSession.close();
        System.out.println("====================");
        Emp emp1 = mapper2.selectEmpByEmpno(7369);
        System.out.println(emp1);
        sqlSession2.close();
    }

    @Test
    public void test10(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp emp = mapper.selectEmpByEmpno(7369);
        System.out.println(emp);
        sqlSession.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        EmpDao mapper2= sqlSession2.getMapper(EmpDao.class);
        Emp emp2 = mapper2.selectEmpByEmpno(7369);
        System.out.println(emp2);
        Emp emp3 = mapper2.selectEmpByEmpno(7369);
        System.out.println(emp3);

        Emp emp4 = mapper2.selectEmpByEmpno(7499);
        System.out.println(emp4);
        Emp emp5 = mapper2.selectEmpByEmpno(7499);
        System.out.println(emp5);
        sqlSession2.close();

    }
}
