package junlancer.svn;

import junlancer.sftp.ProjectConfig;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.*;

import java.io.File;

/** SVN
 * @author junlancer
 */
public class SVNManager {
    private final ProjectConfig projectConfig;

    private SVNURL svnUrl;

    private SVNUpdateClient updateClient;

    public SVNManager(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
        init();
    }


    public void init() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
        try {
            svnUrl = SVNURL.parseURIEncoded(projectConfig.getSVNurl());
        } catch (SVNException e) {
            e.printStackTrace();
        }
        DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
        SVNClientManager svnClientManager = SVNClientManager.newInstance(
                options, projectConfig.getSVNusername(), projectConfig.getSVNpassword());
        updateClient = svnClientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);
    }

    public long checkOutOrUpdate(String url) {
        File file = new File(url);
        return file.exists() ? update(file) : checkout(file);
    }

    private long update(File file) {

        try {
            return updateClient.doUpdate(file, SVNRevision.HEAD, SVNDepth.INFINITY, true, false);
        } catch (SVNException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private long checkout(File file) {
        file.mkdir();
        try {
            return updateClient.doCheckout(svnUrl, file, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, true);
        } catch (SVNException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
