package junlancer.sftp;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.HTTPProxyData;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;
import junlancer.utils.LoadConfig;
import junlancer.utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * ssh客户端
 *
 * @author junlancer
 */
public class SSH {
    private Session session;
    private Connection conn;

    private void load() {
        conn = new Connection(LoadConfig.SFTP_REQ_HOST, LoadConfig.SFTP_REQ_PORT);
        try {
            if (!StringUtil.checkNull(LoadConfig.PROXY_HOST) && LoadConfig.PROXY_PORT != 0) {
                conn.setProxyData(new HTTPProxyData(LoadConfig.PROXY_HOST, LoadConfig.PROXY_PORT));
            }
            conn.connect();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            conn.authenticateWithPassword(LoadConfig.SFTP_REQ_USERNAME, LoadConfig.SFTP_REQ_PASSWORD);
            session = conn.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行命令
     *
     * @param cmd
     * @return
     * @throws IOException
     */
    public String execCommands(String cmd) throws IOException {
        load();
        session.execCommand(cmd);
        InputStream is = new StreamGobbler(session.getStdout());
        BufferedReader brs = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = brs.readLine();
            if (line == null) {
                break;
            }
            sb.append(line);
            sb.append("\n");
        }
        session.close();
        conn.close();
        return sb.toString();
    }
}
