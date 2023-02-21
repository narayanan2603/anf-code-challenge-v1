package com.anf.core.servlets;

import java.io.IOException;
import java.util.Iterator;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.anf.core.constants.AnfConstants;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet to query the data from a particular Page, display the result using SQL2
 *
 * @author NK
 * @version 1.0
 * @since 02-13-2023
 *
 */

@Component(service = {Servlet.class})
@SlingServletPaths(value = "/bin/anf/queryServlet")
@ServiceDescription("Servlet to fetch the first 10 pages Using SQL")
public class QueryServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 2610051404257637265L;
    private static final Logger QUERY_SERVLET_LOG = LoggerFactory.getLogger(QueryServlet.class);
    private static final String CHILD_PAGES = "childPages";


    /**
     * Method to get the request from the URL, set the appropriate Content type and pass that
     * values to the getQueryResult Method..
     *
     * @param request
     * @param response
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        ResourceResolver resourceResolver = request.getResourceResolver();
        response.setContentType(AnfConstants.APPLICATION_TYPE);
        JSONObject jsonObj = new JSONObject();
        response.getWriter().println(getQueryResult(jsonObj, resourceResolver));
    }

    /**
     * SQL2 method to get the page results
     *
     * @param jsonObject
     * @param resourceResolver
     * @return jsonObject
     */
    private String getQueryResult(JSONObject jsonObject, ResourceResolver resourceResolver) {
        String localhost = AnfConstants.LOCAL_HOST;
        try {
            final String sqlQuery =
                    "SELECT * FROM [cq:Page] AS page "
                            + "WHERE ISDESCENDANTNODE(page ,\"/content/anf-code-challenge/us/en\") "
                            + "AND [jcr:content/anfCodeChallenge] = \"true\" "
                            + "ORDER BY page.[jcr:created] ASC";
            Iterator<Resource> pageResources = resourceResolver.findResources(sqlQuery, javax.jcr.query.Query.JCR_SQL2);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < 10 && pageResources.hasNext(); i++) {
                Resource jsonResource = pageResources.next();
                if (jsonResource.getPath().startsWith(AnfConstants.CONTENT_PATH)) {
                    Page page = jsonResource.adaptTo(Page.class);
                    if (null != page) {
                        String pagePath = page.getPath();
                        String fullURL = localhost.concat(pagePath).concat(AnfConstants.CONTENT_EXTENSION);
                        jsonArray.put(fullURL);
                    }
                }
            }
            jsonObject.put(CHILD_PAGES, jsonArray);
        } catch (JSONException jsonException) {
            QUERY_SERVLET_LOG.error("Exception in getSQL2Results method {0}", jsonException);
        }
        return jsonObject.toString();
    }
}