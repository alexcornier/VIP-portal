/*
Copyright 2009-2015

CREATIS
CNRS UMR 5220 -- INSERM U1044 -- Université Lyon 1 -- INSA Lyon

Authors

Nouha Boujelben (nouha.boujelben@creatis.insa-lyon.fr)
Frédéric Cervenansky (frederic.cervnansky@creatis.insa-lyon.fr)
Rafael Ferreira da Silva (rafael.silva@creatis.insa-lyon.fr)
Tristan Glatard (tristan.glatard@creatis.insa-lyon.fr)
Ibrahim  Kallel (ibrahim.kallel@creatis.insa-lyon.fr)
Kévin Moulin (kevmoulin@wanadoo.fr)
Sorina Pop (sorina.pop@creatis.insa-lyon.fr)

This software is a web portal for pipeline execution on distributed systems.

This software is governed by the CeCILL-B license under French law and
abiding by the rules of distribution of free software.  You can use,
modify and/ or redistribute the software under the terms of the
CeCILL-B license as circulated by CEA, CNRS and INRIA at the following
URL "http://www.cecill.info".

As a counterpart to the access to the source code and rights to copy,
modify and redistribute granted by the license, users are provided
only with a limited warranty and the software's author, the holder of
the economic rights, and the successive licensors have only limited
liability.

In this respect, the user's attention is drawn to the risks associated
with loading, using, modifying and/or developing or reproducing the
software by the user in light of its specific status of free software,
that may mean that it is complicated to manipulate, and that also
therefore means that it is reserved for developers and experienced
professionals having in-depth computer knowledge. Users are therefore
encouraged to load and test the software's suitability as regards
their requirements in conditions enabling the security of their
systems and/or data to be ensured and, more generally, to use and
operate it in the same conditions as regards security.

The fact that you are presently reading this means that you have had
knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.server.rpc;

import com.google.gwt.logging.shared.RemoteLoggingService;
import fr.insalyon.creatis.vip.core.client.view.CoreException;
import fr.insalyon.creatis.vip.core.server.rpc.AbstractRemoteServiceServlet;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.apache.log4j.Logger;

/**
 *
 * @author pgirard
 */
public class LoggerServlet extends AbstractRemoteServiceServlet implements RemoteLoggingService  {
    
        private static final Logger logger = Logger.getLogger(LoggerServlet.class);

       @Override
        public String logOnServer(LogRecord record) {

           final Level level = record.getLevel();
           final String message = record.getMessage();
      //   final Throwable thrown = record.getThrown();
      //   final Logger logger = LoggerFactory.getLogger(record.getLoggerName());

          logger.info(message);

            try {
                this.trace(logger, "VENANT LOGGER SERVLET **********************");
            } catch (CoreException ex) {
                java.util.logging.Logger.getLogger(LoggerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

      return null;
  }
        
}
