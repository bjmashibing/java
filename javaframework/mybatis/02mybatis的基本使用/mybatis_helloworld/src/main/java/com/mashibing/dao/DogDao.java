package com.mashibing.dao;

import com.mashibing.bean.Dog;
import org.omg.PortableInterceptor.Interceptor;

public interface DogDao {
    public Dog selectDogById(Integer id);
}
