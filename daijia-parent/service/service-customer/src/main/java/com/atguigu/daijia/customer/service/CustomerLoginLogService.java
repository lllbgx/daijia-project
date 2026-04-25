package com.atguigu.daijia.customer.service;

import com.atguigu.daijia.model.entity.customer.CustomerLoginLog;
import com.atguigu.daijia.model.form.customer.LoginLogQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CustomerLoginLogService extends IService<CustomerLoginLog> {

    /**
     * 分页查询用户登录日志
     */
    PageVo<CustomerLoginLog> findLoginLogPage(Page<CustomerLoginLog> pageParam, LoginLogQueryForm queryForm);
}
