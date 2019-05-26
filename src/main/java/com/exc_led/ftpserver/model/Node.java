package com.exc_led.ftpserver.model;

import lombok.Data;

@Data
public class Node {
    private Integer id;

    private String province;

    private String city;

    private String district;

    private String addr;

    private String ip;

    private String ports;

    private String description;

    private String installAddr;

    private String num;

    private Double x;

    private Double y;

    private Double height;

    private Double width;

}