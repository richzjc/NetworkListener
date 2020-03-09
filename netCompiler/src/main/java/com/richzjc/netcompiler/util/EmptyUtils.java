package com.richzjc.netcompiler.util;

import java.util.Collection;
import java.util.Map;

/**
 * 字符串、集合判空工具
 */
public final class EmptyUtils {


    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

}
