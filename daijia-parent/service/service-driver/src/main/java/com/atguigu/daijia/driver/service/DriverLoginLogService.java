package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.entity.driver.DriverLoginLog;
import com.atguigu.daijia.model.form.driver.LoginLogQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DriverLoginLogService extends IService<DriverLoginLog> {

    /**
     * 分页查询司机登录日志
     */
    PageVo<DriverLoginLog> findLoginLogPage(Page<DriverLoginLog> pageParam, LoginLogQueryForm queryForm);
}
