package me.qping.utils.codegen.generator.condition.jpa;

import me.qping.utils.codegen.bean.build.BuildConfig;
import me.qping.utils.codegen.generator.Condition;

/**
 * @ClassName PrimaryKeyCondition
 * @Description 复合主键判断条件
 * @Author qping
 * @Date 2019/8/5 17:11
 * @Version 1.0
 **/
public class PrimaryKeyCondition implements Condition {
    @Override
    public boolean judge(BuildConfig config) {
        try{
            if(config.getTable().getPrimaryKeys().size() > 1){
                return true;
            }
        }catch (Exception ex){ }
        return false;

    }
}
