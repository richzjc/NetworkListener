package com.richzjc.netannotation;

import com.richzjc.netannotation.nettype.NetType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface NetChange {
    /**
     * 描述 netType 属性的作用
     *
     * @return 返回网络类型枚举值（如 4G/5G/WiFi）
     */
    NetType netType();
}
