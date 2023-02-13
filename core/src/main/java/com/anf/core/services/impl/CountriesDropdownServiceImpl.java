package com.anf.core.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.anf.core.constants.AnfConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.anf.core.services.CountriesDropdownService;

/**
 * Service implementation To get a countries map from the Apps Node
 */
@Component(immediate = true, service = CountriesDropdownService.class)
public class CountriesDropdownServiceImpl implements CountriesDropdownService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountriesDropdownServiceImpl.class);

    /**
     * resolverFactory object
     */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /**
     * Method to get a countries list from a anf dam node
     *
     * @return countriesMap
     */
    @Override
    public Map<String, String> getCountriesJSON() {
        Map<String, String> countriesMap = new LinkedHashMap<>();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, AnfConstants.NK_SERVICE_USER);
            ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param);

            Resource countriesJSONResource = resolver.getResource(AnfConstants.COUNTRY_JSON);
            if (countriesJSONResource != null) {
                InputStream content = countriesJSONResource.adaptTo(InputStream.class);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8));

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                JSONObject jsonObj = new JSONObject(stringBuilder.toString());
                JSONArray countriesArr = jsonObj.getJSONArray(AnfConstants.COUNTRIES);
                for (int i = 0; i < countriesArr.length(); i++) {
                    JSONObject countryJSON = (JSONObject) countriesArr.get(i);
                    countriesMap.put(countryJSON.get(AnfConstants.CODE).toString(), countryJSON.get(
                            AnfConstants.NAME).toString());
                }
            }
        } catch (JSONException | LoginException | IOException ioException) {
            LOGGER.error("Exception while fetching countries json {}", ioException.getMessage());
        }
        return countriesMap;
    }

}