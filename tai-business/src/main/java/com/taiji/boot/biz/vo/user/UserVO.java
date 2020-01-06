package com.taiji.boot.biz.vo.user;

import com.taiji.boot.biz.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Demo UserVO
 *
 * @author ydy
 * @date 2020/1/6 15:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends BaseVO {

    private static final long serialVersionUID = 7689473993391179743L;

    private Long userId;

    private String name;

    private Integer type;

    private Boolean isMember;

    private Integer age;

    private String email;
}
