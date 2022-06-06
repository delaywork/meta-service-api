package com.meta.model;


import com.meta.model.enums.OrderEnum;
import lombok.Data;

@Data
public class Order {

    private OrderEnum order;

    private String column;

}
