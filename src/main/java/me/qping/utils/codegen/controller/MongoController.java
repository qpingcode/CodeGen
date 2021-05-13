package me.qping.utils.codegen.controller;

import com.jcraft.jsch.Session;
import com.mongodb.*;
import me.qping.common.utils.DateUtil;
import me.qping.utils.NetUtils;
import me.qping.utils.codegen.bean.datagrab.FrontEnd;
import me.qping.utils.codegen.bean.datagrab.KeyVal;
import me.qping.utils.codegen.bean.datagrab.Row;
import me.qping.utils.codegen.bean.datagrab.Statis;
import me.qping.utils.codegen.util.FileUtil;
import me.qping.utils.codegen.util.SSHUtil;
import me.qping.utils.excel.ExcelUtil;
import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @ClassName MongoController
 * @Description TODO
 * @Author qping
 * @Date 2021/5/12 11:31
 * @Version 1.0
 **/
@Controller
@RequestMapping("/mongo")
public class MongoController {

    @RequestMapping(value = "/test", produces ="application/json;charset=UTF-8")
    @ResponseBody
    public Object test() throws ParseException {

        ExcelUtil excelUtil = new ExcelUtil().firstHeader(true).sheetNo(0);
        InputStream inputStream = this.getClass().getResourceAsStream("/医院部署列表.xlsx");
        List<FrontEnd> list = excelUtil.read(FrontEnd.class, inputStream);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = sdf.parse("2021-02-01");
        Date endDate = sdf.parse("2021-05-01");


        int p2Count = 0;
        int psCount = 0;
        int p5Count = 0;
        int p2sIsNull = 0;
        int allIsNull = 0;

        int i = 0;
        for(FrontEnd frontEnd : list){
            String hospital = frontEnd.getHospital();
            String host = frontEnd.getIp().trim();
            String username = frontEnd.getUsername();
            String password = frontEnd.getPassword();
            int port = frontEnd.getPort();
            int orgId = frontEnd.getOrgId();
            boolean success = false;

            try{
                System.out.print(++i + " " + hospital);
                boolean port27017 = testPort(host, 27017);
                boolean portSSH = testPort(host, port);
                boolean port52000 = testPort(host, 27017);
                if(port27017){
                    p2Count ++;
                }
                if(portSSH){
                    psCount ++;
                }
                if(port52000){
                    p5Count ++;
                }

                if(!portSSH && !port27017){
                    p2sIsNull ++;
                }

                if(!portSSH && !port27017 && !port52000){
                    allIsNull ++;
                }

                System.out.println(" 结果： 27017：" + port27017 + " ssh：" + portSSH + " 52000：" + port52000);
                // 27017 端口通，直接查询mongo端口
            }catch (Exception ex){}
        }

        System.out.println(String.format("total: %s 27017 count: %s, ssh count: %s, 52000 count: %s ssh&mongo null: %s all null:%s", i, p2Count, psCount, p5Count, p2sIsNull, allIsNull));

        return true;
    }



    @RequestMapping(value = "/get", produces ="application/json;charset=UTF-8")
    @ResponseBody
    public Object get() throws ParseException {

        ExcelUtil excelUtil = new ExcelUtil().firstHeader(true).sheetNo(0);
        InputStream inputStream = this.getClass().getResourceAsStream("/医院部署列表.xlsx");
        List<FrontEnd> list = excelUtil.read(FrontEnd.class, inputStream);

        FileUtil fileUtil = FileUtil.open("/Users/qping/Desktop/data/trans.txt");
        FileUtil successLog = FileUtil.open("/Users/qping/Desktop/data/success.txt");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = sdf.parse("2021-02-01");
        Date endDate = sdf.parse("2021-05-01");

        int i = 0;
        for(FrontEnd frontEnd : list){
            String hospital = frontEnd.getHospital();
            String host = frontEnd.getIp().trim();
            String username = frontEnd.getUsername();
            String password = frontEnd.getPassword();
            int port = frontEnd.getPort();
            int orgId = frontEnd.getOrgId();
            boolean success = false;


            try{

                System.out.print(++i + " " + hospital);

                boolean port27017 = testPort(host, 27017);
                // 27017 端口通，直接查询mongo端口

                if(port27017) {
                    success = getDataFromMongo(host, 27017, orgId, hospital, beginDate, endDate, fileUtil, successLog);
                }

                if(success){
                    continue;
                }

                boolean port_ssh_is_ok = testPort(host, port);
                if(port_ssh_is_ok){
                    int localPort = 30303;

                    Session session = null;
                    try{
                        session = SSHUtil.sshTunnel(
                                localPort,
                                host,
                                port,
                                username,
                                password,
                                "127.0.0.1",
                                27017
                        );
                        success = getDataFromMongo("127.0.0.1", localPort, orgId, hospital, beginDate, endDate, fileUtil, successLog);
                    }catch (Exception ex){
                        System.out.println("ssh 转发失败：" + ex.getMessage());
                    }finally {
                        if(session != null){
                            session.disconnect();
                        }
                    }

                 }

                System.out.println(" " + (success ? "成功" : "失败"));

            }catch (Exception ex){

                System.out.println(" 异常：" +  ex.getMessage() );
            }


        }

        fileUtil.close();
        successLog.close();

        return true;
    }

    public static boolean in(String tableName){
        if(tableName == null) return false;

        if(tableName.toUpperCase().equals("MEDICALRECORD") ||
            tableName.toUpperCase().equals("EMR_OUTPATIENT_VISIT_RECORD") ||
            tableName.toUpperCase().equals("OUTHOSPITALRECORD")
        ){
            return true;
        }
        return false;
    }

    public static boolean getDataFromMongo(String host, int port, int orgId, String orgName, Date beginDate, Date endDate, FileUtil fileUtil, FileUtil successLog){

//        MongoCredential credential = MongoCredential.createCredential(username, "admin", password.toCharArray());
//        MongoClient mongoClient = new MongoClient(new ServerAddress("host1", 27017), Arrays.asList(credential));

        MongoClient mongoClient = new MongoClient(host, port);
        try{
            DBObject queryCondition = new BasicDBObject("$gte", beginDate).append("$lte", endDate);
            BasicDBObject query = new BasicDBObject("transBeginTime", queryCondition);

            Map<String, String> tableNameMap = new HashMap<>();
            mongoClient.getDatabase("dataTrans").getCollection("trans_overview").find(query).forEach(new Consumer<Document>() {
                @Override
                public void accept(Document document) {
                    String batchNo = document.getString("batchNo");
                    Integer transId = document.getInteger("transId");
                    Integer nodeType = document.getInteger("nodeType");
                    String targetTableName = document.getString("targetTableName");

                    if(nodeType != null && nodeType == 2){
                        tableNameMap.put(batchNo + "|" + transId, targetTableName);
                    }
                }
            });

            BasicDBObject query2 = new BasicDBObject("beginTime", queryCondition);

            mongoClient.getDatabase("dataTrans").getCollection("trans_log").find(query2).forEach(new Consumer<Document>() {
                @Override
                public void accept(Document document) {
                    String batchNo = document.getString("batchNo");
                    Integer transId = document.getInteger("transId");

                    String tableName = tableNameMap.get(batchNo + "|" + transId);
                    if(in(tableName)){
                        Long il = document.getLong("il");
                        Long ol = document.getLong("ol");

                        if(document.get("params") == null){
                            return;
                        }

                        List<Document> params = document.getList("params", Document.class);

                        String begin = null, end = null;
                        for(Document kv: params){
                            if(kv != null && kv.get("key") != null && kv.getString("key").equals("begin")){
                                begin = kv.getString("val");
                            }
                            if(kv != null && kv.get("key") != null && kv.getString("key").equals("end")){
                                end =  kv.getString("val");
                            }
                        }

                        try {
                            fileUtil.writeLine(
                                    String.format("%s %s %s %s %s %s %s %s",
                                            orgId,
                                            orgName,
                                            tableName.toUpperCase(),
                                            begin,
                                            end,
                                            il,
                                            ol,
                                            batchNo
                                    )

                            );
                            fileUtil.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            try {
                successLog.writeLine(orgId + " " + orgName + " " + true);
                successLog.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            if(mongoClient != null) mongoClient.close();
        }
        try {
            successLog.writeLine(orgId + " " + orgName + " " + false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void export(){



    }


    public boolean testPort(String ip, int port){
        boolean port_is_ok = false;
        try {
            port_is_ok = NetUtils.testPort(ip, port);
        } catch (Exception e) {
        }
        return port_is_ok;
    }

    private void test1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = sdf.parse("2021-03-01");
        Date endDate = sdf.parse("2021-04-05");

        FileUtil fileUtil = FileUtil.open("/Users/qping/Desktop/data/trans.txt");
        FileUtil successLog = FileUtil.open("/Users/qping/Desktop/data/success.txt");

        Session session = null;
        try{
            int localPort = 30303;
            session = SSHUtil.sshTunnel(
                    localPort,
                    "192.168.60.18",
                    65122,
                    "root",
                    "WStl\"5ea9",
                    "20.32.32.3",
                    27017
            );
            getDataFromMongo("127.0.0.1", localPort, 15695, "南京市第一医院", beginDate, endDate, fileUtil, successLog);
        }catch (Exception ex){
            System.out.println("ssh 转发失败：" + ex.getMessage());
        }finally {
            if(session != null){
                session.disconnect();
            }
        }

        fileUtil.close();
        successLog.close();
    }


    public static void main(String[] args) throws ParseException, IOException {

        List<Statis> statisList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(new File("/Users/qping/Documents/Cloud/work/ETL/统计目录/trans.txt")));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date beginOk = sdf.parse("2021-03-01");
        Date endOk = sdf.parse("2021-04-01");

        String currentOrgId = null, currentOrgName = null;

        int i = 0;
        String line = null;
        while ((line = br.readLine()) != null){

            System.out.println(++i);
            String[] words = line.split(" ");
            String orgId = words[0];
            String orgName = words[1];
            String tableName  = words[2];
            String begin = words[3];
            String end = words[4];
            String il = words[5];
            String ol = words[6];
            String batchNo = words[7];
            String executeTime = batchNo.substring(0,8);

            // 首次或者切换了机构
            if(currentOrgId != null && !currentOrgId.equals(orgId)){
                Statis statis1 = getStatis(getList("MEDICALRECORD"), Integer.parseInt(currentOrgId), currentOrgName, "MEDICALRECORD");
                Statis statis2= getStatis(getList("EMR_OUTPATIENT_VISIT_RECORD"), Integer.parseInt(currentOrgId), currentOrgName, "EMR_OUTPATIENT_VISIT_RECORD");
                Statis statis3 = getStatis(getList("OUTHOSPITALRECORD"), Integer.parseInt(currentOrgId), currentOrgName, "OUTHOSPITALRECORD");

                if(statis1.getDays() > 0){
                    statisList.add(statis1);
                }

                if(statis2.getDays() > 0){
                    statisList.add(statis2);
                }

                if(statis3.getDays() > 0){
                    statisList.add(statis3);
                }


                clear();

            }

            currentOrgId = orgId;
            currentOrgName = orgName;

            // 过滤出所有合法的数据 03-01～04-01
            Date beginDate , endDate;
            try{
                beginDate = sdf.parse(begin.substring(0,10));
                endDate = sdf.parse(end.substring(0,10));
            }catch (Exception ex){
//                System.out.println("日期不合法" + begin  + " " + end);
                continue;
            }

            if(beginDate.compareTo(endDate) >= 0){
//                System.out.println("开始时间不能大雨结束时间" + begin + end);
                continue;
            }

            if(beginDate.compareTo(beginOk) < 0){
//                System.out.println("开始日期不正确：" + begin );
                continue;
            }

            if(endDate.compareTo(endOk) > 0){
//                System.out.println("结束日期不正确：" + end );
                continue;
            }

            Row row = new Row();
            row.orgId = orgId;
            row.orgName = orgName;
            row.tableName = tableName;
            row.begin = begin;
            row.end = end;
            row.il = il;
            row.ol = ol;
            row.batchNo = batchNo;
            row.executeTime = executeTime;
            row.beginDate = beginDate;
            row.endDate = endDate;


            // 只保留最新的记录 MEDICALRECORD  EMR_OUTPATIENT_VISIT_RECORD  OUTHOSPITALRECORD
            List<Row> list = getList(tableName);
            Iterator<Row> itr = list.iterator();

            boolean ok = true;
            while(itr.hasNext()){
                Row r = itr.next();

                if(beginDate.compareTo(r.beginDate) == 0 && endDate.compareTo(r.endDate)  == 0){
                    if(executeTime.compareTo(r.executeTime) > 0){
                        list.remove(r);
                    }else if(executeTime.compareTo(r.executeTime) == 0){
                        System.out.println("出现两条一摸一样的记录");
                        ok = false;
                    }
                    break;
                }


                if(beginDate.compareTo(r.beginDate) < 0 && endDate.compareTo(r.endDate) > 0 ){
                    if(executeTime.compareTo(r.executeTime) > 0){
                        list.remove(r);
                    }else{
                        ok = false;
                    }

                    break;
                }

                if(beginDate.compareTo(r.beginDate) > 0 && endDate.compareTo(r.endDate) < 0){
                    if(executeTime.compareTo(r.executeTime) > 0){
                        System.out.println("无法处理时间段重叠的记录");
                        ok = false;
                    }
                    break;
                }

                if(beginDate.compareTo(r.endDate) >= 0  || endDate.compareTo(r.beginDate) <= 0){
                    continue;
                }

                ok = false;
                System.out.println("无法处理特殊的时间段");
            }

            if(ok){
                list.add(row);
            }

        }

        ExcelUtil excelUtil = new ExcelUtil();
        excelUtil.write(Statis.class, new FileOutputStream(new File("/Users/qping/Documents/Cloud/work/ETL/统计目录/1.xlsx")) , statisList);

    }

    private static Statis getStatis(List<Row> list,int orgId,String orgName, String tableName) {

        Statis statis = new Statis();

        statis.setTableName(tableName);
        statis.setIl(list.stream().mapToInt(v->Integer.valueOf(v.il)).sum());
        statis.setOl(list.stream().mapToInt(v->Integer.valueOf(v.ol)).sum());
        statis.setOrgId(orgId);
        statis.setOrgName(orgName);
        statis.setDays(countDays(list));

        return statis;
    }

    private static int countDays(List<Row> list) {
        int days = 0;
        for(Row row : list){
            days += ( row.endDate.getTime() - row.beginDate.getTime()) / (3600000 * 24);
        }
        return days;
    }

    private static void clear() {

        list1.clear();
        list2.clear();
        list3.clear();
    }

    static List<Row> list1 = new ArrayList<>();
    static List<Row> list2 = new ArrayList<>();
    static List<Row> list3 = new ArrayList<>();
    public static List<Row> getList(String tableName){
        List<Row> list = null;
        if(tableName.equals("MEDICALRECORD")){
            return list1;
        }else if(tableName.equals("EMR_OUTPATIENT_VISIT_RECORD")){
            return list2;
        }else if(tableName.equals("OUTHOSPITALRECORD")){
            return list3;
        }else{
            throw new RuntimeException("错误的表");
        }
    }


}
