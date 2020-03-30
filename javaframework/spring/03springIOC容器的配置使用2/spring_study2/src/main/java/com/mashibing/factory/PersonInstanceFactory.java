package com.mashibing.factory;

import com.mashibing.bean.Person;

/**
 * 实例工厂
 */
public class PersonInstanceFactory {

    public Person getInstance(String name){
        Person person = new Person();
        person.setId(2);
        person.setName(name);
        person.setAge(22);
        return person;
    }
}
