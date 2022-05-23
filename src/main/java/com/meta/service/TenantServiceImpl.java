package com.meta.service;

import com.meta.mapper.TenantMapper;
import com.meta.model.pojo.Tenant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TenantServiceImpl {

    @Autowired
    private TenantMapper tenantMapper;

    /**
     * 新建
     * */
    public Long addTenant(){
        Tenant tenant = Tenant.builder().build();
        tenantMapper.insert(Tenant.builder().build());
        return tenant.getId();
    }


}
