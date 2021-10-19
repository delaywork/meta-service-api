package com.meta.service;

import com.meta.mapper.ShareMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ShareServiceImpl {

    @Autowired
    private ShareMapper shareMapper;



}
