package com.meta.utils;


import com.meta.model.Order;
import com.meta.model.enums.OrderEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class ParamsUtil {

    private static String UTF_8 = "UTF-8";

    /**
     * 解析字符串参数
     * */
    public static List<String> getStringParams(String s){
        try{
           s = URLDecoder.decode(s, UTF_8);
        }catch (Exception e){
            log.info("ParamsUtil ---> getStringParams 解码失败, s:{}", s);
        }
        log.info("ParamsUtil ---> getStringParams, s:{}", s);
        if (StringUtils.isBlank(s)){
            return null;
        }
        String[] split = s.split("\\|");
        List<String> strings = Arrays.asList(split);
        return strings;
    }

    /**
     * 解析排序字段
     * */
    public static List<Order> getOrderParams(String s){
        try{
            s = URLDecoder.decode(s, UTF_8);
        }catch (Exception e){
            log.info("ParamsUtil ---> getStringParams 解码失败, s:{}", s);
        }
        log.info("ParamsUtil ---> getOrderParams, s:{}", s);
        if (StringUtils.isBlank(s)){
            return null;
        }
        List<Order> orders = new ArrayList<>();
        String[] split = s.split("\\|");
        List<String> strings = Arrays.asList(split);
        strings.forEach(string -> {
            Order order = new Order();
            if (string.startsWith("-")){
                order.setOrder(OrderEnum.DESC);
                order.setColumn(string.substring(1));
                orders.add(order);
            }else{
                order.setOrder(OrderEnum.ASC);
                order.setColumn(string);
                orders.add(order);
            }
        });
        return orders;
    }

}
