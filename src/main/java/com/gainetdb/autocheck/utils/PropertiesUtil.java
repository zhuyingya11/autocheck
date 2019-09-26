package com.gainetdb.autocheck.utils;

import com.gainetdb.autocheck.AutocheckApplication;
import lombok.extern.slf4j.Slf4j;

import javax.xml.ws.Endpoint;
import java.io.*;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {
    private String properiesName = "";

    public PropertiesUtil() {

    }
    public PropertiesUtil(String fileName) {
        this.properiesName = fileName;
    }
    public String readProperty(String key) {
        String value = "";
        InputStream is = null;
        try {
            is = new FileInputStream(properiesName);
            Properties p = new Properties();
            p.load(new InputStreamReader(is, "UTF-8"));//解决中文乱码问题
            value = p.getProperty(key);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return value;
    }

    public Properties getProperties() {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(properiesName);
            p.load(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return p;
    }

    public void writeProperty(String key, String value) {
        InputStream is = null;
        OutputStream os = null;
        Properties p = new Properties();
        try {
            is = new FileInputStream(properiesName);
            p.load(is);
            os = new FileOutputStream(PropertiesUtil.class.getClassLoader().getResource(properiesName).getFile());

            p.setProperty(key, value);
            p.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
                if (null != os)
                    os.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
//        System.out.println(System.getProperty("user.dir"));
//        String path = AutocheckApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        File file = new File(path);
//        System.out.println("系统部署路径："+path);
//        String parentPath = file.getParent();
//        System.out.println("系统资源文件路径："+parentPath);
//        PropertiesUtil propertiesUtil = new PropertiesUtil(parentPath + "/config/db.properties");
//        String address = propertiesUtil.readProperty("ws_address");
//       // Endpoint.publish(address, new TransDataServiceImpl());
//        System.out.println("-------------数据同步接口启动完毕，正在等待接收数据--------------");正在等待接收数据--------------

        System.out.println(System.getProperty("user.dir"));
        String path=System.getProperty("user.dir");
        PropertiesUtil propertiesUtil = new PropertiesUtil(path + "/account.properties");
       String phone = propertiesUtil.readProperty("phone");
        String username = propertiesUtil.readProperty("username");
        String password = propertiesUtil.readProperty("password");
        String lat = propertiesUtil.readProperty("lat");
        String  lng = propertiesUtil.readProperty("lng");
        String about = propertiesUtil.readProperty("about");
     log.info("读取外部配置文件成功");
         log.info(String.format("读取配置参数为：username->%s,password->%s,phone->%s,lat->%s,lng->%s,about->%s",username,password,phone,lat,lng,about));
    }
}
