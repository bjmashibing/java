import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

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

//    @Test
//    public void test01() throws IOException {
//
//        //获取与数据库相关的会话
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        //获取对应的映射接口对象
//        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
//        Emp emp = mapper.selectByPrimaryKey(7369);
//        System.out.println(emp);
//        //关闭会话
//        sqlSession.close();
//
//    }
}
