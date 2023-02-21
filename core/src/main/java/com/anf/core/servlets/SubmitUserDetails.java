package com.anf.core.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Servlet;
import com.anf.core.constants.AnfConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.commons.jcr.JcrConstants;

/**
 * The SubmitUserDetails will take care of sending the user enter values to the /var folder
 *
 * @author NK
 * @version 1.0
 * @since 02-15-2023
 *
 */
@Component(service = {Servlet.class})
@SlingServletResourceTypes(
        resourceTypes = "sling/servlet/default",
        methods = HttpConstants.METHOD_POST,
        selectors = "submitUserDetails",
        extensions = "json")
@ServiceDescription("Persist the user details on /var/anf-code-challenge node")
public class SubmitUserDetails extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubmitUserDetails.class);


    /**
     * doPost method will take care of getting the request from the URL, convert the
     * input to Json and pass it over to the backend..
     * */
    @Override
    protected void doPost(final SlingHttpServletRequest slingHttpServletRequest,
                          final SlingHttpServletResponse slingHttpServletResponse) throws IOException {
        try {
            slingHttpServletResponse.setContentType(AnfConstants.APPLICATION_TYPE);
            JSONObject jsonObject = new JSONObject();
            if (persistUserDetails(slingHttpServletRequest)) {
                jsonObject.put(AnfConstants.RESULT, AnfConstants.SUCCESS);
                slingHttpServletResponse.getWriter().println(jsonObject);
                slingHttpServletResponse.setStatus(200);
            } else {
                jsonObject.put(AnfConstants.RESULT, AnfConstants.FAILURE);
                slingHttpServletResponse.getWriter().println(jsonObject);
            }
        } catch (JSONException jsonException) {
            LOGGER.error(AnfConstants.EXCEPTION_MSG, jsonException.getMessage());
        }
    }

    /**
     * persistUserDetails method will take care of storing the user entered
     * value into var/anfuserdetails node
     * @return boolean
     */
    private boolean persistUserDetails(SlingHttpServletRequest slingHttpServletRequest) {
        ResourceResolver resourceResolver = null;
        try {
            if (null != slingHttpServletRequest) {
                String firstName = StringUtils.isNotBlank(slingHttpServletRequest.getParameter(AnfConstants.FIRST_NAME))
                        ? slingHttpServletRequest.getParameter(AnfConstants.FIRST_NAME) : StringUtils.EMPTY;
                String lastName = StringUtils.isNotBlank(slingHttpServletRequest.getParameter(AnfConstants.LAST_NAME))
                        ? slingHttpServletRequest.getParameter(AnfConstants.LAST_NAME) : StringUtils.EMPTY;
                String age = StringUtils.isNotBlank(slingHttpServletRequest.getParameter(AnfConstants.AGE))
                        ? slingHttpServletRequest.getParameter(AnfConstants.AGE) : StringUtils.EMPTY;
                String countryCode = StringUtils.isNotBlank(slingHttpServletRequest.getParameter(AnfConstants.COUNTRY))
                        ? slingHttpServletRequest.getParameter(AnfConstants.COUNTRY) : StringUtils.EMPTY;
                resourceResolver = slingHttpServletRequest.getResourceResolver();
                Resource userDetailsVarRootNode = ResourceUtil.getOrCreateResource(resourceResolver,
                        AnfConstants.VAR_USER_DETAILS, JcrConstants.NT_UNSTRUCTURED, null, Boolean.TRUE);

                Map<String, Object> userInputValues = new HashMap<>();
                userInputValues.put(AnfConstants.FIRST_NAME, firstName);
                userInputValues.put(AnfConstants.LAST_NAME, lastName);
                userInputValues.put(AnfConstants.AGE, age);
                userInputValues.put(AnfConstants.COUNTRY, countryCode);
                userInputValues.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);

                resourceResolver.create(userDetailsVarRootNode, AnfConstants.USER_DETAIL + new Date().getTime(), userInputValues);
                resourceResolver.commit();

                return Boolean.TRUE;
            }
        } catch (PersistenceException persistenceException) {
            LOGGER.error(AnfConstants.EXCEPTION_MSG, persistenceException.getMessage());
        } finally {
            resourceResolver.close();
        }
        return Boolean.FALSE;
    }

}
