package com.anf.core.models;

import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.anf.core.services.CountriesDropdownService;

/**
 * The CountriesDropdownModel will take care of Populating the countries Dropdown
 *
 * @author NK
 * @version 1.0
 * @since 02-13-2023
 */
@Model(adaptables = Resource.class)
public class CountriesDropdownModel {

    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    protected String countries;

    @OSGiService
    protected CountriesDropdownService countriesDropdownService;


    /**
     * To get a country code
     *
     * @return countries
     */
    public String getCountryCode() {
        return countries;
    }

    /**
     * To get a countries json as a key value pair. Key is country code and value is country name.
     *
     * @return map
     */
    public Map<String, String> getCountriesJSON() {
        return countriesDropdownService.getCountriesJSON();
    }

}