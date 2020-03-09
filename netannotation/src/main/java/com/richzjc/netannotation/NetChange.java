package com.richzjc.netannotation;

import com.richzjc.netannotation.nettype.NetType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface NetChange {
    NetType netType();
}
