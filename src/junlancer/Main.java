package junlancer;

import junlancer.sftp.Control;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import static junlancer.utils.LoadConfig.loadConfig;
import static junlancer.utils.LoadConfig.projectConfigHashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        //System.setProperty ("jsse.enableSNIExtension", "false");
        loadConfig();
        // write your code here
        ServerSocket socket = new ServerSocket(999);
        int index = 1;
        String[] proList = new String[projectConfigHashMap.size() + 1];
        StringBuilder sb = new StringBuilder("\r\nchose one what you want to do \r\nq : quit! \r\n");
        for (String projectConfigPkg : projectConfigHashMap.keySet()) {
            sb.append(index).append(" : upload ").append(projectConfigPkg).append("\r\n");
            proList[index++] = projectConfigPkg;
        }

        while (true) {
            Socket c = socket.accept();
            try {
                OutputStream out = c.getOutputStream();
                InputStream in = c.getInputStream();
                while (true) {
                    out.write(sb.toString().getBytes());
                    int bt = in.read();
                    if (bt == 'q') {
                        out.write("\r\nbye!".getBytes());
                        c.close();
                        break;
                    }
                    try {
                        int x = bt - '0';
                        if (x > 0 && x < index) {
                            new Control().upload(projectConfigHashMap.get(proList[x]), out);
                        }
                    } catch (Exception e) {
                        out.write(("\r\n" + e.toString()).getBytes());
                    }
                }

            } catch (Exception ignored) {
                System.out.println(c.getInetAddress() + "discollected!");
            }
        }
    }
}
