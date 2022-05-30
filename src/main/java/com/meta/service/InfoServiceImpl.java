package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.InfoMapper;
import com.meta.model.enums.InfoEnabledEnum;
import com.meta.model.pojo.Info;
import com.meta.model.request.GetInfoTermsRequest;
import com.meta.model.response.GetInfoResponse;
import com.meta.utils.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class InfoServiceImpl {

    @Autowired
    private InfoMapper infoMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询 meta
     * */
    public GetInfoResponse getMeta(GetInfoTermsRequest request){
        GetInfoResponse response = new GetInfoResponse();
        // 查询 meta 信息
        QueryWrapper<Info> policyWrapper = new QueryWrapper<>();
        if (ObjectUtils.isNotEmpty(request.getType())){
            policyWrapper.lambda().eq(Info::getType, request.getType());
        }
        policyWrapper.lambda().eq(Info::getEnabled, InfoEnabledEnum.ENABLE);
        policyWrapper.lambda().eq(Info::getDataIsDeleted, false);
        List<Info> InfoList = infoMapper.selectList(policyWrapper);

        if (ObjectUtils.isNotEmpty(InfoList)){
            List<String> about = new ArrayList<>();
            List<String> terms = new ArrayList<>();
            List<String> policy = new ArrayList<>();
            InfoList.stream().forEach(info -> {
                switch (info.getType()){
                    case ABOUT:
                        about.add(info.getContext());
                        break;
                    case TERMS_CONDITIONS:
                        terms.add(info.getContext());
                        break;
                    case POLICY:
                        policy.add(info.getContext());
                        break;
                }
            });
            response.setAbouts(about);
            response.setTermsAndConditions(terms);
            response.setPolicies(policy);
        }
        return response;
    }


}
