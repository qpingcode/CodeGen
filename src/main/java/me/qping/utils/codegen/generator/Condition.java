package me.qping.utils.codegen.generator;

import me.qping.utils.codegen.bean.build.BuildConfig;

public interface Condition {
    public boolean judge(BuildConfig config);
}
