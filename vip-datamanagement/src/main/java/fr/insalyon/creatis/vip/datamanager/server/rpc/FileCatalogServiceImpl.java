/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.datamanager.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClient;
import fr.insalyon.creatis.agent.vlet.client.VletAgentClientException;
import fr.insalyon.creatis.agent.vlet.common.bean.GridData;
import fr.insalyon.creatis.devtools.FileUtils;
import fr.insalyon.creatis.vip.common.server.ServerConfiguration;
import fr.insalyon.creatis.vip.datamanager.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanager.client.bean.Data;
import fr.insalyon.creatis.vip.datamanager.client.rpc.FileCatalogService;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerException;
import fr.insalyon.creatis.vip.datamanager.server.DataManagerUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rafael Silva
 */
public class FileCatalogServiceImpl extends RemoteServiceServlet implements FileCatalogService {

    private static Logger logger = Logger.getLogger(FileCatalogServiceImpl.class);

    public List<Data> listDir(String user, String proxyFileName, String baseDir, boolean refresh) {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            List<GridData> list = client.getFolderData(
                    DataManagerUtil.parseBaseDir(user, baseDir), refresh);

            List<Data> dataList = new ArrayList<Data>();
            for (GridData data : list) {
                if (data.getType() == GridData.Type.Folder) {
                    dataList.add(new Data(data.getName(),
                            data.getType().name(), data.getPermissions()));

                } else {
                    String size = FileUtils.parseFileSize(data.getLength());
                    dataList.add(new Data(data.getName(), data.getType().name(),
                            size, data.getModificationDate(), data.getReplicas(),
                            data.getPermissions()));
                }
            }
            return dataList;

        } catch (DataManagerException ex) {
            logger.error(ex);
        } catch (VletAgentClientException ex) {
            logger.error(ex);
        }
        return null;
    }

    public void delete(String user, String proxyFileName, String path) {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            client.delete(DataManagerUtil.parseBaseDir(user, path));

        } catch (DataManagerException ex) {
            logger.error(ex);
        } catch (VletAgentClientException ex) {
            logger.error(ex);
        }
    }

    public void deleteFiles(String user, String proxyFileName, List<String> paths) {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            List<String> parsedPaths = new ArrayList<String>();
            for (String path : paths) {
                try {
                    parsedPaths.add(DataManagerUtil.parseBaseDir(user, path));
                } catch (DataManagerException ex) {
                    logger.error(ex);
                }
            }
            client.deleteFiles(parsedPaths);

        } catch (VletAgentClientException ex) {
            logger.error(ex);
        }
    }

    public void createDir(String user, String proxyFileName, String baseDir, String name) {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            client.createDirectory(DataManagerUtil.parseBaseDir(user, baseDir), name);

        } catch (DataManagerException ex) {
            logger.error(ex);
        } catch (VletAgentClientException ex) {
            logger.error(ex);
        }
    }

    public void rename(String user, String proxyFileName, String oldPath, String newPath) {
        try {
            VletAgentClient client = new VletAgentClient(
                    ServerConfiguration.getInstance().getVletagentHost(),
                    ServerConfiguration.getInstance().getVletagentPort(),
                    proxyFileName);

            client.rename(DataManagerUtil.parseBaseDir(user, oldPath),
                    DataManagerUtil.parseBaseDir(user, newPath));

        } catch (DataManagerException ex) {
            logger.error(ex);
        } catch (VletAgentClientException ex) {
            logger.error(ex);
        }
    }

    public void renameFiles(String user, String proxyFileName, Map<String, String> paths) {

        for (String oldPath : paths.keySet()) {
            String newPath = paths.get(oldPath);
            rename(user, proxyFileName, oldPath, newPath);
        }
    }

    public void configureDataManager(String user, String proxyFileName) {

        VletAgentClient client = new VletAgentClient(
                ServerConfiguration.getInstance().getVletagentHost(),
                ServerConfiguration.getInstance().getVletagentPort(),
                proxyFileName);

        try {
            client.createDirectory(ServerConfiguration.getInstance().getDataManagerUsersHome(),
                    user.replaceAll(" ", "_").toLowerCase());
        } catch (VletAgentClientException ex) {
            if (!ex.getMessage().contains("ERROR: File/Directory exists or Directory is not empty")) {
                logger.error(ex);
            }
        }
        try {
            client.createDirectory(ServerConfiguration.getInstance().getDataManagerUsersHome(),
                    user.replace(" ", "_").toLowerCase()
                    + "_" + DataManagerConstants.TRASH_HOME);
        } catch (VletAgentClientException ex) {
            if (!ex.getMessage().contains("ERROR: File/Directory exists or Directory is not empty")) {
                logger.error(ex);
            }
        }
    }
}
