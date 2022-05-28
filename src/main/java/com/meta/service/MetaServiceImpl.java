package com.meta.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meta.mapper.MetaMapper;
import com.meta.model.pojo.Meta;
import com.meta.model.request.GetMetaTermsRequest;
import com.meta.model.response.GetMetaResponse;
import com.meta.utils.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MetaServiceImpl {

    @Autowired
    private MetaMapper metaMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询 meta
     * */
    public GetMetaResponse getMeta(GetMetaTermsRequest request){
        GetMetaResponse response = new GetMetaResponse();
        // 查询 meta 信息
        QueryWrapper<Meta> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Meta::getDataIsDeleted, false);
        queryWrapper.last("limit 1");
        Meta meta = metaMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(request.getType())){
            switch (request.getType()){
                case ABOUT:
                    response.setAbout(meta.getAbout());
                    break;
                case TERMS_CONDITIONS:
                    response.setTermsAndConditions(meta.getTermsAndConditions());
                    break;
                case POLICY:
                    response.setPolicy(meta.getPolicy());
                    break;
            }
        }else{
            response.setAbout(meta.getAbout());
            response.setTermsAndConditions(meta.getTermsAndConditions());
            response.setPolicy(meta.getPolicy());
        }
        return response;
    }


}
