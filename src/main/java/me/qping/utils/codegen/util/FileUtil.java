package me.qping.utils.codegen.util;

import java.io.*;

/**
 * @ClassName FileUtil
 * @Description 读写一个文件
 * @Author qping
 * @Date 2021/4/28 09:13
 * @Version 1.0
 **/
public class FileUtil {

    File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public static FileUtil open(String file){
        FileUtil fileUtil = new FileUtil();
        fileUtil.setFile(new File(file));
        return fileUtil;
    }

    public String read() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while( (line = br.readLine()) != null){
            sb.append(line);
        }
        return sb.toString();
    }

    BufferedWriter out;

    public void write(String word, boolean append) throws IOException {
        if(out == null){
            out = new BufferedWriter(new FileWriter(file, append));
        }
        out.write(word);
    }

    public void write(String word) throws IOException {
        write(word, true);
    }

    public void writeLine(String word) throws IOException {
        write(word + "\n", true);
    }

    /**
     * 快速输出
     * */
    public void flush() throws IOException {
        out.flush();
    }


    public void close(){
        if(out != null){
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("20210102".compareTo("20200102"));
    }

}
