package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.InfoMapper;
import com.meta.model.pojo.Info;
import com.meta.model.request.GetInfoTermsRequest;
import com.meta.model.response.GetInfoResponse;
import com.meta.utils.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        QueryWrapper<Info> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Info::getDataIsDeleted, false);
        queryWrapper.last("limit 1");
        Info info = infoMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(request.getType())){
            switch (request.getType()){
                case ABOUT:
                    response.setAbout(info.getAbout());
                    break;
                case TERMS_CONDITIONS:
                    response.setTermsAndConditions(info.getTermsAndConditions());
                    break;
                case POLICY:
                    response.setPolicy(info.getPolicy());
                    break;
            }
        }else{
            response.setAbout(info.getAbout());
            response.setTermsAndConditions(info.getTermsAndConditions());
            response.setPolicy(info.getPolicy());
        }
        return response;
    }


}
