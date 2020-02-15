import com.mashibing.myinter.MyInterface;
import com.mashibing.myinter.MySubClass;
import com.mashibing.proxy.CalculatorProxy;
import com.mashibing.service.Calculator;
import com.mashibing.service.MyCalculator;
import com.mashibing.service.SecondCalculator;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {

    @Test
    public void test01() throws NoSuchMethodException {

//        MyCalculator myCalculator = new MyCalculator();
//        System.out.println(myCalculator.add(1, 2));
//        System.out.println(myCalculator.div(1, 1));

        Calculator calculator = (Calculator) CalculatorProxy.getProxy(new MyCalculator());
        System.out.println(calculator.add(1, 1));
        calculator.sub(1,1);
        calculator.mul(1,1);
        calculator.div(1,0);
        System.out.println(calculator.getClass());
//        System.out.println("------------------");
//        MyInterface proxy = (MyInterface) CalculatorProxy.getProxy(new MySubClass());
//        proxy.show(100);
    }
    @Test
    public void test02() throws NoSuchMethodException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Calculator calculator = context.getBean( Calculator.class);
        calculator.add(1,1);
    }
}
