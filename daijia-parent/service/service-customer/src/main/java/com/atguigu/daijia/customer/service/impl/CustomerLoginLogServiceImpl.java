package com.atguigu.daijia.customer.service.impl;

import com.atguigu.daijia.customer.mapper.CustomerLoginLogMapper;
import com.atguigu.daijia.customer.service.CustomerLoginLogService;
import com.atguigu.daijia.model.entity.customer.CustomerLoginLog;
import com.atguigu.daijia.model.form.customer.LoginLogQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerLoginLogServiceImpl extends ServiceImpl<CustomerLoginLogMapper, CustomerLoginLog> implements CustomerLoginLogService {

    @Autowired
    private CustomerLoginLogMapper customerLoginLogMapper;

    @Override
    public PageVo<CustomerLoginLog> findLoginLogPage(Page<CustomerLoginLog> pageParam, LoginLogQueryForm queryForm) {
        LambdaQueryWrapper<CustomerLoginLog> queryWrapper = new LambdaQueryWrapper<>();

        if (queryForm != null) {
            if (queryForm.getCustomerId() != null) {
                queryWrapper.eq(CustomerLoginLog::getCustomerId, queryForm.getCustomerId());
            }
            if (StringUtils.hasText(queryForm.getIpaddr())) {
                queryWrapper.eq(CustomerLoginLog::getIpaddr, queryForm.getIpaddr());
            }
        }

        // 按创建时间降序排列
        queryWrapper.orderByDesc(CustomerLoginLog::getCreateTime);

        Page<CustomerLoginLog> pageInfo = page(pageParam, queryWrapper);

        PageVo<CustomerLoginLog> pageVo = new PageVo<>();
        pageVo.setPage(pageParam.getCurrent());
        pageVo.setLimit(pageParam.getSize());
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setPages(pageInfo.getPages());
        pageVo.setRecords(pageInfo.getRecords());

        return pageVo;
    }
}
