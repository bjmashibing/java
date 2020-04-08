package com.mashibing.service.impl;

import com.mashibing.bean.Emp;
import com.mashibing.mapper.EmpMapper;
import com.mashibing.service.EmpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lian
 * @since 2020-04-04
 */
@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {

}
