package com.zjy.api.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class HelloReq implements Serializable {
    private String name;
}
