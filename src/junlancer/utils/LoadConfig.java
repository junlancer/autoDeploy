package junlancer.utils;

import junlancer.sftp.ProjectConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class LoadConfig {
    public static String SFTP_REQ_HOST;
    public static int SFTP_REQ_PORT;
    public static String SFTP_REQ_USERNAME;
    public static String SFTP_REQ_PASSWORD;
    public static int TIME_OUT;
    public static String PROXY_HOST;
    public static int PROXY_PORT;
    public static HashMap<String, ProjectConfig> projectConfigHashMap = new HashMap<>();

    public static void loadConfig() throws Exception {
        //user.dir指定了当前的路径,当前jar文件夹的路径
        String configPath = System.getProperty("user.dir") + "\\config.properties";
        System.out.println("configPath: " + configPath);
        Properties properties = new Properties();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(configPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SFTP_REQ_HOST = properties.getProperty("SFTP_REQ_HOST");
        String srq = properties.getProperty("SFTP_REQ_PORT");
        SFTP_REQ_USERNAME = properties.getProperty("SFTP_REQ_USERNAME");
        SFTP_REQ_PASSWORD = properties.getProperty("SFTP_REQ_PASSWORD");
        if (StringUtil.checkNull(SFTP_REQ_HOST, srq, SFTP_REQ_USERNAME, SFTP_REQ_PASSWORD)) {
            throw new Exception("系统配置参数缺失");
        }
        SFTP_REQ_PORT = Integer.parseInt(srq);
        if (!StringUtil.checkNull(properties.getProperty("TIME_OUT"))) {
            TIME_OUT = Integer.parseInt(properties.getProperty("TIME_OUT"));
        }
        PROXY_HOST = properties.getProperty("PROXY_HOST");
        if (!StringUtil.checkNull(properties.getProperty("PROXY_PORT"))) {
            PROXY_PORT = Integer.parseInt(properties.getProperty("PROXY_PORT"));
        }


        int index = 0;
        while (true) {
            if (StringUtil.checkNull(properties.getProperty("projectConfig.pkg_" + index))) {
                if (index == 0) {
                    throw new Exception("项目配置参数缺失");
                }
                break;
            }
            ProjectConfig projectConfig = new ProjectConfig();
            projectConfig.setPkg(properties.getProperty("projectConfig.pkg_" + index));
            projectConfig.setcDirParent(properties.getProperty("projectConfig.cDirParent_" + index));
            projectConfig.setsDirPath(properties.getProperty("projectConfig.sDirPath_" + index));
            projectConfig.setsDirPathBackup(properties.getProperty("projectConfig.sDirPathBackup_" + index));
            projectConfig.setsDirPathParent(properties.getProperty("projectConfig.sDirPathParent_" + index));
            projectConfig.setRootName(properties.getProperty("projectConfig.rootName_" + index));
            projectConfig.setSVNusername(properties.getProperty("projectConfig.SVNusername_" + index));
            projectConfig.setSVNpassword(properties.getProperty("projectConfig.SVNpassword_" + index));
            projectConfig.setSVNurl(properties.getProperty("projectConfig.SVNurl_" + index));
            if (StringUtil.checkNull(projectConfig.getPkg(), projectConfig.getcDirParent(), projectConfig.getsDirPath(), projectConfig.getsDirPathBackup(), projectConfig.getsDirPathParent(),
                    projectConfig.getRootName(), projectConfig.getSVNusername(), projectConfig.getSVNpassword(), projectConfig.getSVNurl())) {
                throw new Exception("项目配置参数缺失");
            }
            projectConfigHashMap.put(projectConfig.getPkg(), projectConfig);
            index++;
        }

    }
}
