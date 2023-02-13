package com.anf.core.servlets;


import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.anf.core.constants.AnfConstants;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component(
        service = Servlet.class,
        name = "com.anf.core.servlets.CountriesDropdownServlet",
        immediate = true,
        property = {
                Constants.SERVICE_DESCRIPTION + "=CountriesDropdownServlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=/bin/countries"
        })
public class CountriesDropdownServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 2610051404257637267L;

    /**
     * To fetch a country dropdown and assign the valueMap as text value pair for dynamic dropdown
     *
     *
     */
    @Override
    protected void doGet(final SlingHttpServletRequest slingHttpServletRequest,
                         final SlingHttpServletResponse slingHttpServletResponse) throws IOException {
        final ResourceResolver resolver = slingHttpServletRequest.getResourceResolver();
        Resource countriesJSONResource = resolver.getResource(AnfConstants.COUNTRY_JSON);
        if (null != countriesJSONResource) {
            Map<String, String> countriesMap = getCountriesJSON(countriesJSONResource);
            DataSource ds = new SimpleDataSource(
                    new TransformIterator(countriesMap.keySet().iterator(), new Transformer() {
                        public Object transform(Object object) {
                            String value = (String) object;
                            ValueMap valueMap = new ValueMapDecorator(new HashMap<String, Object>());
                            valueMap.put(AnfConstants.VALUE, value);
                            valueMap.put(AnfConstants.TEXT, countriesMap.get(value));
                            return new ValueMapResource(
                                    resolver, new ResourceMetadata(), AnfConstants.NODE_TYPE, valueMap);
                        }
                    }));

            slingHttpServletRequest.setAttribute(DataSource.class.getName(), ds);
        }
    }

    public Map<String, String> getCountriesJSON(final Resource resource) throws IOException {
        Map<String, String> countriesMap = new LinkedHashMap<>();
        if (resource != null) {
            InputStream content = resource.adaptTo(InputStream.class);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            try {
                JSONObject jsonObj = new JSONObject(stringBuilder.toString());
                Iterator<String> keys = jsonObj.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (jsonObj.get(key) != null) {
                        countriesMap.put(key, jsonObj.get(key).toString());
                    }
                }
            } catch (JSONException jsonException) {
                jsonException.toString();
            }
        }
        return countriesMap;
    }
}

