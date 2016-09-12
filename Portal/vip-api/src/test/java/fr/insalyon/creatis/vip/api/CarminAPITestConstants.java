/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
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
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.api;

import fr.insalyon.creatis.vip.api.bean.Module;
import fr.insalyon.creatis.vip.api.rest.model.SupportedTransferProtocol;

import static fr.insalyon.creatis.vip.api.bean.Module.*;
import static fr.insalyon.creatis.vip.api.rest.model.SupportedTransferProtocol.*;

/**
 * Created by abonnet on 7/21/16.
 */
public interface CarminAPITestConstants {
    String TEST_PLATFORM_NAME = "TestPlatform";
    String TEST_PLATFORM_DESCRIPTION = "Test Platform description";
    String TEST_DEFAULT_LIST_LIMIT = "42";
    String TEST_SUPPORTED_API_VERSION = "Version 4.2";
    String TEST_IS_KILL_SUPPORTED = "false";

    /* WARNING : keep following properties consistant */
    SupportedTransferProtocol[] TEST_SUPPORTED_PROTOCOLS = {HTTP, WEBDAV};
    String TEST_SUPPORTED_TRANSFER_PROTOCOLS_STRING = "HTTP,WEBDAV";
    Module[] TEST_SUPPORTED_MODULES = {DATA, COMMERCIAL};
    String TEST_SUPPORTED_MODULES_STRING = "COMMERCIAL,DATA";
    String[] TEST_UNSUPPORTED_METHOD = {"method1", "method2"};
    String TEST_UNSUPPORTED_METHODS_STRING = "method1,method2";
}
