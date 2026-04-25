package com.atguigu.daijia.driver.service.impl;

import com.atguigu.daijia.driver.mapper.DriverLoginLogMapper;
import com.atguigu.daijia.driver.service.DriverLoginLogService;
import com.atguigu.daijia.model.entity.driver.DriverLoginLog;
import com.atguigu.daijia.model.form.driver.LoginLogQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverLoginLogServiceImpl extends ServiceImpl<DriverLoginLogMapper, DriverLoginLog> implements DriverLoginLogService {

    @Autowired
    private DriverLoginLogMapper driverLoginLogMapper;

    @Override
    public PageVo<DriverLoginLog> findLoginLogPage(Page<DriverLoginLog> pageParam, LoginLogQueryForm queryForm) {
        LambdaQueryWrapper<DriverLoginLog> queryWrapper = new LambdaQueryWrapper<>();

        if (queryForm != null) {
            if (queryForm.getDriverId() != null) {
                queryWrapper.eq(DriverLoginLog::getDriverId, queryForm.getDriverId());
            }
            if (StringUtils.hasText(queryForm.getIpaddr())) {
                queryWrapper.eq(DriverLoginLog::getIpaddr, queryForm.getIpaddr());
            }
        }

        // 按创建时间降序排列
        queryWrapper.orderByDesc(DriverLoginLog::getCreateTime);

        Page<DriverLoginLog> pageInfo = page(pageParam, queryWrapper);

        PageVo<DriverLoginLog> pageVo = new PageVo<>();
        pageVo.setPage(pageParam.getCurrent());
        pageVo.setLimit(pageParam.getSize());
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setPages(pageInfo.getPages());
        pageVo.setRecords(pageInfo.getRecords());

        return pageVo;
    }
}
