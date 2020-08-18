package me.qping.utils.codegen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

@Controller
@RequestMapping("/file")
public class FileController {

    public static final String basePath = "/Users/qping/test/upload";

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file){

        String fileName = file.getOriginalFilename();
        String filePath =  "/" + fileName;
        File dest = new File(basePath + filePath);

        try {

            dest.getParentFile().mkdirs();

            file.transferTo(dest);

            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败！";
    }

    @RequestMapping("/download")
    public void download(String fileName, HttpServletResponse response){

        FileInputStream fis = null;
        BufferedInputStream bis = null;

        try {
            File file = new File(basePath + "/" + fileName);

            if (!file.exists()) {
                return;
            }

            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" +  URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());

            byte[] buffer = new byte[1024];

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream outputStream = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
