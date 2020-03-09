import com.mashibing.bean.Address;
import com.mashibing.bean.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ioc.xml");
        /*根据bean标签的id来获取对象*/
//        Person person = context.getBean("person", Person.class);
//        Person person2 = context.getBean("person",Person.class);
//        System.out.println(person == person2);

        /*根据bean的类型来获取对象
        * 注意：当通过类型进行获取的时候，如果存在两个相同类型对象，将无法完成获取工作
        * */
//        Person bean = context.getBean(Person.class);
//        System.out.println(bean);

        /**
         * 当需要从容器中获取对象的时候，最好要保留无参构造方法，因为底层的实现是反射
         *
         *  Class clazz = Person.class;
         *  Object obj = clazz.newInstance();默认调用无参的构造方法，此方法已经被弃用
         *  Object obj = class.getDeclaredConstructor().newInstance()
         *
         */

//        Person person2 = context.getBean("person", Person.class);
//        System.out.println(person2);
//
//        Person person5 = context.getBean("person5", Person.class);
//        System.out.println(person5);

//        Person person6 = context.getBean("person6", Person.class);
//        System.out.println(person6);
//        Address address2 = context.getBean("address2", Address.class);
//        System.out.println(address2);

//        Person son = context.getBean("son", Person.class);
//        System.out.println(son);
//        Person parent = context.getBean("parent", Person.class);
//        System.out.println(parent);
    }
}
