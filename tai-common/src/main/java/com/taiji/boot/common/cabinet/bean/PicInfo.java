package com.taiji.boot.common.cabinet.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Demo PicInfo
 *
 * @author YangDy
 * @date 2020/11/19 09:41
 */
@Data
public final class PicInfo implements Serializable {

    private static final long serialVersionUID = 2839745981342336378L;

    private String width;

    private String height;
}
