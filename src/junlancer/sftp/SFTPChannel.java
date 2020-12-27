package junlancer.sftp;

import com.jcraft.jsch.*;
import junlancer.utils.LoadConfig;
import junlancer.utils.StringUtil;

import java.util.logging.Logger;
import java.util.Properties;

/**
 * SFTPChannel
 *
 * @author junlancer
 */
public class SFTPChannel {
    private Session session = null;
    private ChannelSftp channel = null;

    private static final Logger LOG = Logger.getLogger(SFTPChannel.class.getName());


    public ChannelSftp createChannel() throws JSchException {
        String ftpHost = LoadConfig.SFTP_REQ_HOST;
        int ftpPort = LoadConfig.SFTP_REQ_PORT;
        String ftpUserName = LoadConfig.SFTP_REQ_USERNAME;
        String ftpPassword = LoadConfig.SFTP_REQ_PASSWORD;
        // 创建JSch对象
        JSch jsch = new JSch();
        // 根据用户名，主机ip，端口获取一个Session对象
        session = jsch.getSession(ftpUserName, ftpHost, ftpPort);
        session.setPassword(ftpPassword);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        // 为Session对象设置properties
        session.setConfig(config);
        if (LoadConfig.TIME_OUT != 0) {
            session.setTimeout(LoadConfig.TIME_OUT);
        }
        //设置代理
        if (!StringUtil.checkNull(LoadConfig.PROXY_HOST) && LoadConfig.PROXY_PORT != 0) {
            session.setProxy(new ProxyHTTP(LoadConfig.PROXY_HOST, LoadConfig.PROXY_PORT));
        }

        // 通过Session建立链接
        session.connect();
        // 打开SFTP通道
        channel = (ChannelSftp) session.openChannel("sftp");
        // 建立SFTP通道的连接
        channel.connect();
        LOG.info("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName
                + ", returning: " + channel);
        return channel;
    }

    public void closeChannel() {
        if (channel != null) {
            channel.quit();
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}
