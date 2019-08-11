package me.qping.utils.codegen.freemarker;

import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;

import java.util.Map;

/**
 * @ClassName DirectiveUtil
 * @Description freemark工具类
 * @Author qping
 * @Date 2019/8/5 20:07
 * @Version 1.0
 **/
public class DirectiveUtil {

    public static String getString(Map map, String propName){
        Object prop = map.get(propName);
        if(prop == null){
            return null;
        }

        SimpleScalar value = (SimpleScalar) prop;
        return value.getAsString();
    }

    public static int getInt(Map map, String propName){
        Object prop = map.get(propName);
        if(prop == null){
            return 0;
        }

        SimpleNumber value = (SimpleNumber) prop;
        return value.getAsNumber().intValue();
    }
}
