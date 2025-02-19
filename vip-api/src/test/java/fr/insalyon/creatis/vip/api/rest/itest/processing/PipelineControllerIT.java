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
package fr.insalyon.creatis.vip.api.rest.itest.processing;

import fr.insalyon.creatis.vip.api.exception.ApiException.ApiError;
import fr.insalyon.creatis.vip.api.rest.config.*;
import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static fr.insalyon.creatis.vip.api.data.AppVersionTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.ApplicationTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.ClassesTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.PipelineTestUtils.*;
import static fr.insalyon.creatis.vip.api.data.UserTestUtils.baseUser1;
import static fr.insalyon.creatis.vip.api.rest.mockconfig.ApplicationsConfigurator.*;
import static fr.insalyon.creatis.vip.application.client.view.ApplicationException.ApplicationError.WRONG_APPLICATION_DESCRIPTOR;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by abonnet on 7/20/16.
 * <p>
 * Test methods on pipeline path
 * <p>
 * Include 2 tests on error handling
 */
@Disabled
public class PipelineControllerIT extends BaseVIPSpringIT {

    @Test
    public void pipelineMethodShouldBeSecured() throws Exception {
        mockMvc.perform(get("/rest/pipelines"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnErrorOnBusinessException() throws Exception {
        when(classBusiness.getUserClasses(
                 eq(baseUser1.getEmail()), anyBoolean()))
            .thenThrow(new BusinessException("test exception"));
        mockMvc.perform(get("/rest/pipelines").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiError.GENERIC_API_ERROR.getCode()));
    }

    @Test
    public void shouldReturnErrorOnUnexpectedException() throws Exception {
        when(classBusiness.getUserClasses(
                 eq(baseUser1.getEmail()), anyBoolean()))
            .thenThrow(new RuntimeException("test exception"));
        mockMvc.perform(get("/rest/pipelines").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiError.GENERIC_API_ERROR.getCode()));
    }

    @Test
    public void shouldReturnPipelines() throws Exception {
        configureApplications(this, baseUser1, Arrays.asList(class1, class2),
                app1, version42, version43,
                app2, version01,
                app3, version01, version42, version43);
        mockMvc.perform(get("/rest/pipelines").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*]", hasSize(5)))
                .andExpect(jsonPath("$[*]", containsInAnyOrder(
                        jsonCorrespondsToPipeline(getPipeline(app1, version42)),
                        jsonCorrespondsToPipeline(getPipeline(app2, version01)),
                        jsonCorrespondsToPipeline(getPipeline(app3, version01)),
                        jsonCorrespondsToPipeline(getPipeline(app3, version42)),
                        jsonCorrespondsToPipeline(getPipeline(app3, version43)))));
    }


    @Test
    public void shouldReturnErrorOnAPIException() throws Exception {
        mockMvc.perform(get("/rest/pipelines/WRONG_APP").with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(ApiError.INVALID_PIPELINE_IDENTIFIER.getCode()));
    }

    @Test
    public void shouldReturnErrorOnConfiguredVipException() throws Exception {
        configureApplications(this, baseUser1, Arrays.asList(class1, class2),
                app2, version42);
        String pipelineId = app2.getName() + "/" + version42.getVersion();
        when(workflowBusiness.getApplicationDescriptor(
                eq(baseUser1), eq(app2.getName()), eq(version42.getVersion())))
                .thenThrow(new BusinessException(WRONG_APPLICATION_DESCRIPTOR, pipelineId));
        mockMvc.perform(get("/rest/pipelines/" + pipelineId).with(baseUser1()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errorCode").value(WRONG_APPLICATION_DESCRIPTOR.getCode()));
    }

    @Test
    public void userGetAPipelineWithPathParameterNonEncoded() throws Exception {
        configureApplications(this, baseUser1, Arrays.asList(class1, class2),
                app2, version42);
        String pipelineId = configureAnApplication(this, baseUser1, app2, version42, 0, 1);
        mockMvc.perform(get("/rest/pipelines/" + pipelineId)
                .with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", jsonCorrespondsToPipeline(getFullPipeline(app2, version42, "desc test", 0, 1))));
    }

    @Test
    public void userGetAPipelineWithQueryParameter() throws Exception {
        configureApplications(this, baseUser1, Arrays.asList(class1, class2),
                app2, version42);
        String pipelineId = configureAnApplication(this, baseUser1, app2, version42, 0, 1);
        mockMvc.perform(get("/rest/pipelines").param("pipelineId", pipelineId)
                .with(baseUser1()))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", jsonCorrespondsToPipeline(getFullPipeline(app2, version42, "desc test", 0, 1))));
    }
}
