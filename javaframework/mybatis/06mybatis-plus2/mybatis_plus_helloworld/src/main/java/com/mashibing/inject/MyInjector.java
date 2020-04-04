package com.mashibing.inject;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyInjector extends  AbstractSqlInjector{

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        return Stream.of(new DeleteAll()).collect(Collectors.toList());
    }
}