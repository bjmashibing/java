import com.mashibing.service.BookService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileNotFoundException;

public class MyTest2 {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

    @Test
    public void test01() throws FileNotFoundException {
        BookService bean = context.getBean(BookService.class);
        bean.buyBook();
    }
}
