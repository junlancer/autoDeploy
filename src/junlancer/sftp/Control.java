package junlancer.sftp;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import junlancer.svn.SVNManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import static junlancer.utils.ZIPUtil.toZip;

/**
 * 主流程
 *
 * @author junlancer
 */
public class Control {
    private static final Logger LOG = Logger.getLogger(Control.class.getName());
    private SSH ssh;

    private void backUp(ProjectConfig projectConfig, OutputStream out) throws IOException {
        if (!projectConfig.getsDirPathBackup().contains(projectConfig.getsDirPathParent())) {
            throw new IOException(projectConfig.getPkg() + "备份文件夹地址错误");
        }
        String cmd = "rm -rf " + projectConfig.getsDirPathBackup() + "; " +
                "mkdir " + projectConfig.getsDirPathBackup() + "; " +
                "mv " + projectConfig.getsDirPath() + " " + projectConfig.getsDirPathBackup() + "; " +
                "mkdir " + projectConfig.getsDirPath();
        LOG.info(projectConfig.getPkg() + "正在备份");
        out.write(("\r\n" + projectConfig.getPkg() + " Backing up" + "\r\n").getBytes());
        ssh.execCommands(cmd);
    }

    public void upload(ProjectConfig projectConfig, OutputStream out) throws Exception {
        //svn更新代码
        LOG.info(projectConfig.getPkg() + "正在获取最新代码");
        out.write(("\r\n" + projectConfig.getPkg() + " Getting the latest code" + "\r\n").getBytes());
        SVNManager manager = new SVNManager(projectConfig);
        long checkOutOrUpdate = manager.checkOutOrUpdate(projectConfig.getcDirParent());
        LOG.info(projectConfig.getPkg() + "获取最新代码完成,当前版本 " + checkOutOrUpdate);
        if(checkOutOrUpdate<0) {
            throw new Exception("svn error");
        }
        out.write(("\r\n" + projectConfig.getPkg() + " Get the latest code completion, current version " + checkOutOrUpdate + "\r\n").getBytes());
        String path = projectConfig.getcDirParent();
        File c = new File(path);
        if (c.exists() && c.isDirectory()) {
            if (path.charAt(path.length() - 1) != '\\') {
                path += '\\';
            }
            String dist = path + projectConfig.getRootName();
            File fd = new File(dist);
            if (!fd.exists() || !fd.isDirectory()) {
                throw new IOException(projectConfig.getPkg() + "上传项目文件夹不存在: ");
            }
            File distZIP = new File(path + projectConfig.getRootName() + ".zip");
            if (distZIP.exists()) {
                distZIP.delete();
                distZIP.createNewFile();
            }
            ssh = new SSH();
            SFTPChannel sftpChannel = new SFTPChannel();
            ChannelSftp channelSftp = sftpChannel.createChannel();
            backUp(projectConfig, out);
            LOG.info(projectConfig.getPkg() + "备份完成，正在压缩项目文件");
            out.write(("\r\n" + projectConfig.getPkg() + " The backup is complete and the project file is being compressed" + "\r\n").getBytes());
            toZip(dist, new FileOutputStream(distZIP));
            LOG.info(projectConfig.getPkg() + "压缩完成，正在上传");
            out.write(("\r\n" + projectConfig.getPkg() + " Compression is complete, uploading" + "\r\n").getBytes());
            channelSftp.put(distZIP.getAbsolutePath(), projectConfig.getsDirPathParent());
            LOG.info(projectConfig.getPkg() + "上传完成,正在解压部署");
            out.write(("\r\n" + projectConfig.getPkg() + " Upload is complete, unzip and deploy" + "\r\n").getBytes());
            String cmd = "cd " + projectConfig.getsDirPathParent() + "; " +
                    "unzip " + projectConfig.getRootName() + ".zip";
            ssh.execCommands(cmd);
            LOG.info(projectConfig.getPkg() + "部署完成");
            out.write(("\r\n" + projectConfig.getPkg() + " Deployment complete! \r\n").getBytes());
            sftpChannel.closeChannel();
        } else {
            throw new IOException(projectConfig.getPkg() + "上传文件夹不存在");
        }

    }


}
