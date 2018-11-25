package com.kaibo.runtime.utils;

import android.os.Bundle;

/**
 * @author kaibo
 * @date 2018/11/25 0:27
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

public class BundleUtils {

    public static <T> T get(Bundle bundle, String key) {
        return (T) bundle.get(key);
    }

    public static <T> T get(Bundle bundle, String key, Object defaultValue) {
        Object object = bundle.get(key);
        if (object == null) {
            object = defaultValue;
        }
        return (T) object;
    }


}
