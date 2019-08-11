package me.qping.utils.codegen.util;

/**
 * @ClassName GenUtil
 * @Description 代码生成工具类
 * @Author qping
 * @Date 2019/8/7 09:23
 * @Version 1.0
 **/
public class GenUtil {

    public static String getJavaName(String fileName) {
        if(!fileName.endsWith(".java")){
            throw new RuntimeException("java 文件的文件扩展必须为 .java !");
        }
        return fileName.substring(0, fileName.length() - 5);
    }

    public static String getCamelCase(String name, String[] beginIgnores, boolean capfirst) {

        for(String ignore : beginIgnores){
            if(name.startsWith(ignore)){
                name = name.substring(ignore.length(), name.length());
            }
        }

        String[] names = name.split("_");
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < names.length; i++){
            String n = names[i];
            if("".equals(n)){
                continue;
            }

            if(!capfirst && i == 0){
                sb.append(n.toLowerCase());
            }else{
                sb.append(n.substring(0, 1).toUpperCase());
                sb.append(n.substring(1).toLowerCase());
            }
        }

        return sb.toString();

    }

}
