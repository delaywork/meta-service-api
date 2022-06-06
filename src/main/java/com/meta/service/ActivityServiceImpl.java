package com.meta.service;

import com.meta.mapper.ActivityMapper;
import com.meta.model.pojo.Activity;
import com.meta.utils.ShareThreadPoolUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Log4j2
public class ActivityServiceImpl {

    @Resource
    private ActivityMapper activityMapper;

    /**
     * add activity
     * */
    public void addActivity(Activity activity){
        log.info("add activity, accountId:{}, type:{}", activity.getOperationAccount(), activity.getOperationType());
        ShareThreadPoolUtil.execute("add activity", () -> activityMapper.insert(activity));
    }

}
