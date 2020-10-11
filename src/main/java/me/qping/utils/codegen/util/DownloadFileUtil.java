package me.qping.utils.codegen.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;

@Slf4j
public class DownloadFileUtil {

    public static final String separator = File.separator;

    /**
     * 下载样表
     * @param filePath 文件上级目录
     * @param fileName 文件名
     * @param newName  下载的展示文件名
     * @return 响应
     */
    public static ResponseEntity<InputStreamResource> download(String filePath, String fileName, String newName) {
//        String route = "static" + separator + "templates" + separator;
        String path = null;
        ResponseEntity<InputStreamResource> response = null;
        try {

            if(!filePath.endsWith(separator)){
                filePath += separator;
            }

            path = filePath + fileName;
//            ClassPathResource classPathResource = new ClassPathResource(path);
//            InputStream inputStream = classPathResource.getInputStream();
            InputStream inputStream = new FileInputStream(new File(path));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition",
                    "attachment; filename="
                            + new String(newName.getBytes("gbk"), "iso8859-1"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            response = ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(inputStream));
        } catch (FileNotFoundException e1) {
            log.error("找不到指定的文件", e1);
        } catch (IOException e) {
            log.error("获取不到文件流", e);
        }
        return response;
    }
}