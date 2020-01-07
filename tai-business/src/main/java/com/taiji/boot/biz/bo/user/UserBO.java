package com.taiji.boot.biz.bo.user;

import com.taiji.boot.biz.bo.BaseBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserBO extends BaseBO {

    private static final long serialVersionUID = 8437885617853680372L;

    private String name;

    private Integer type;
}
