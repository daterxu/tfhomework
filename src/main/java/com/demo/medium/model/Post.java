package com.demo.medium.model;

import lombok.Data;

@Data
public class Post {

    private String mediumUrl;

    public Long clapCount;
}
