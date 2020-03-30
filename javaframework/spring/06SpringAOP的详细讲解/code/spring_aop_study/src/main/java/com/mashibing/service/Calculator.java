package com.mashibing.service;

import org.springframework.stereotype.Service;


public interface Calculator {

    public Integer add(Integer i,Integer j) throws NoSuchMethodException;
    public Integer sub(Integer i,Integer j) throws NoSuchMethodException;
    public Integer mul(Integer i,Integer j) throws NoSuchMethodException;
    public Integer div(Integer i,Integer j) throws NoSuchMethodException;
}
