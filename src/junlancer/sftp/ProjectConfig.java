package junlancer.sftp;

public class ProjectConfig {
    /**
     * 项目名称 proj1
     */
    private String pkg;
    /**
     * 服务器,项目的下级目录,代码的根目录,后端没有,前端(dist)  /home/jun/proj1/dist/
     */
    private String sDirPath;
    /**
     * 服务器,后端代码的根目录,前端dist的上级目录 /home/jun/proj1/
     */
    private String sDirPathParent;
    /**
     * 服务器,项目备份文件夹  /home/jun/proj1/backup/
     */
    private String sDirPathBackup;
    /**
     * 本地,后端代码的根目录,前端dist的上级目录  C:\jun\proj1
     */
    private String cDirParent;
    /**
     * 后端没有,前端为dist
     */
    private String rootName;

    /**
     * svn项目地址
     */
    private String SVNurl;

    /**
     * svn账户
     */
    private String SVNusername;

    /**
     * svn密码
     */
    private String SVNpassword;


    public String getSVNurl() {
        return SVNurl;
    }

    public void setSVNurl(String SVNurl) {
        this.SVNurl = SVNurl;
    }

    public String getSVNusername() {
        return SVNusername;
    }

    public void setSVNusername(String SVNusername) {
        this.SVNusername = SVNusername;
    }

    public String getSVNpassword() {
        return SVNpassword;
    }

    public void setSVNpassword(String SVNpassword) {
        this.SVNpassword = SVNpassword;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getsDirPath() {
        return sDirPath;
    }

    public void setsDirPath(String sDirPath) {
        this.sDirPath = sDirPath;
    }

    public String getsDirPathParent() {
        return sDirPathParent;
    }

    public void setsDirPathParent(String sDirPathParent) {
        this.sDirPathParent = sDirPathParent;
    }

    public String getsDirPathBackup() {
        return sDirPathBackup;
    }

    public void setsDirPathBackup(String sDirPathBackup) {
        this.sDirPathBackup = sDirPathBackup;
    }

    public String getcDirParent() {
        return cDirParent;
    }

    public void setcDirParent(String cDirParent) {
        this.cDirParent = cDirParent;
    }

}
