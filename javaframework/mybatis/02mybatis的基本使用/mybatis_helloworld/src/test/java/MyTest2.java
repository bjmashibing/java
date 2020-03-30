import com.mashibing.bean.Dog;
import com.mashibing.bean.Emp;
import com.mashibing.bean.User;
import com.mashibing.dao.DogDao;
import com.mashibing.dao.EmpDao;
import com.mashibing.dao.UserDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest2 {

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
        DogDao mapper = sqlSession.getMapper(DogDao.class);
        //执行具体的sql语句
        Dog dog = mapper.selectDogById(1);
        System.out.println(dog);
        //关闭会话
        sqlSession.close();

    }


}
