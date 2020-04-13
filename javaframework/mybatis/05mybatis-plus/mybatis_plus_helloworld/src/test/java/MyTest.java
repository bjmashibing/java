import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mashibing.bean.Emp;
import com.mashibing.dao.EmpDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;
import java.util.*;

public class MyTest {

    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
    @Test
    public void test01() throws SQLException {
        DruidDataSource dataSource = context.getBean("dataSource", DruidDataSource.class);
        System.out.println(dataSource.getConnection());
    }

    /*
    * 在mybatis-plus中，插入数据的sql语句会伴随你插入的对象的属性值进行更改，比较灵活
    * */
    @Test
    public void test02(){
        EmpDao empDao = context.getBean("empDao", EmpDao.class);
        Emp emp = new Emp();
        emp.seteName("zhangsan");
        emp.setJob("Teacher");
        emp.setMgr(100);
        emp.setSal(1000.0);
        emp.setComm(500.0);
        emp.setHiredate(new Date());
        emp.setDeptno(10);
        int insert = empDao.insert(emp);
        System.out.println();
        System.out.println(insert);
    }

    @Test
    public void test03(){
        EmpDao empDao = context.getBean("empDao", EmpDao.class);
        Emp emp = new Emp();
        emp.setEmpno(6);
        emp.seteName("lisi");
        emp.setJob("Teacher");
        emp.setMgr(100);
        int insert = empDao.updateById(emp);
        System.out.println();
        System.out.println(insert);
    }

    /*delete操作在使用的时候要使用queryWrapper*/
    @Test
    public void test04(){
        EmpDao empDao = context.getBean("empDao", EmpDao.class);
        //根据id删除数据
//        int i = empDao.deleteById(6);
//        System.out.println(i);
        //根据id集合批量删除
//        int i = empDao.deleteBatchIds(Arrays.asList(1, 2, 3));

        //根据map类型的数据进行删除，但是要注意，key为列的名称。value是具体的值
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("empno",4);
//        int i = empDao.deleteByMap(map);
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.eq("empno",7);
//        int delete = empDao.delete(wrapper);
//        System.out.println(delete);
    }

    @Test
    public void test05(){
        EmpDao empdao = context.getBean(EmpDao.class);
        /*查询单条语句，需要添加对应的查询条件，封装在QueryWrapper中
        *
        * 注意使用selectOne的时候有且仅能返回一条语句，如果是多条结果的话，会报错
        * */
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.eq("empno","8");
//        wrapper.eq("e_name","zhangsan");
//        Emp emp = empdao.selectOne(wrapper);
//        System.out.println(emp);

        //查询某一个结果集的数据
//        List<Emp> list = empdao.selectList(null);
//        System.out.println(list);

        //根据id的集合返回数据
//        List<Emp> list = empdao.selectBatchIds(Arrays.asList(8, 9));
//        System.out.println(list);

        //根据id进行查询
//        Emp emp = empdao.selectById(8);

        //查询结果集合封装成一个list里面的对象是map
        //@MapKey 对应的结果是 Map<Object,emp>

//        List<Map<String, Object>> maps = empdao.selectMaps(null);
//        System.out.println();
//        System.out.println(maps);

        //返回满足查询条件的所有行总数
//        Integer integer = empdao.selectCount(null);
//        System.out.println(integer);

        //在使用分页的时候，必须要添加一个插件
        /**
         *   <plugins>
         *         <plugin interceptor="com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor"></plugin>
         *     </plugins>
         *
         */
//        Page<Emp> empPage = empdao.selectPage(new Page<Emp>(2, 2),null);
//        System.out.println("-------------------");
//        System.out.println(empPage.getRecords());


//        empdao.selectMapsPage()
        List<Emp> emp = empdao.selectEmpByCondition();
        System.out.println(emp);
    }
}
