package me.qping.utils.codegen.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @ClassName SSHUtil
 * @Description TODO
 * @Author qping
 * @Date 2021/5/12 11:43
 * @Version 1.0
 **/
public class SSHUtil {

    public static Session sshTunnel(int localPort, String sshHost, int sshPort, String sshUserName, String sshPassWord, String remotoHost, int remotoPort) {
        Session session = null;
        try {
            JSch jsch = new JSch();


            if (sshPort <= 0) {
                //连接服务器，采用默认端口
                session = jsch.getSession(sshUserName, sshHost);
            } else {
                //采用指定的端口连接服务器
                session = jsch.getSession(sshUserName, sshHost, sshPort);
            }

            //如果服务器连接不上，则抛出异常
            if (session == null) {
                throw new Exception("session is null");
            }

            //设置登陆主机的密码
            session.setPassword(sshPassWord);//设置密码
            //设置第一次登陆的时候提示，可选值：(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //设置登陆超时时间
            session.connect(30000);
            session.setPortForwardingL(localPort, remotoHost, remotoPort);
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return session;
        }
    }

}
