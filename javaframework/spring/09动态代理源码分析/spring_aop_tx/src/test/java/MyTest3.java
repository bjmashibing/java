import com.mashibing.service.BookService;
import com.mashibing.service.MultService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileNotFoundException;

public class MyTest3 {

    @Test
    public void test01() {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//        MultService bean = context.getBean(MultService.class);
//        bean.mult();
//        bean.buyBook();
        BookService bean = context.getBean(BookService.class);
        bean.mult();
    }
}
