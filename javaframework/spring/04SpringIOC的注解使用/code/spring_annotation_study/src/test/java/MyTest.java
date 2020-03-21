import com.mashibing.controller.PersonController;
import com.mashibing.service.PersonService;
import com.mashibing.service.StudentService;
import com.mashibing.service.TeacherService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 注意，给测试类起名字的时候千万不要定义成Test
 * 测试的方法不可以有参数，不可以有返回值
 */
public class MyTest {

    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    @Test
    public void test01() {
        PersonController personController = context.getBean("personController", PersonController.class);
        personController.save();

    }

    @Test
    public void test02() {

        StudentService studentService = context.getBean("studentService", StudentService.class);
        studentService.save();

        TeacherService teacherService = context.getBean("teacherService",TeacherService.class);
        teacherService.save();
    }
}
