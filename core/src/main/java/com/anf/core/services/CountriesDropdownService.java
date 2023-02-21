package com.anf.core.services;

import java.util.Map;

/**
 * CountriesDropdownService to get a countries dropdown list
 * @author NK
 * @version 1.0
 * @since 02-15-2023
 *
 *
 */
public interface CountriesDropdownService {

    /**
     * Method to get a countries from a content dam node.
     * It will return as key value pair.
     * Key - country code , value - country name
     * @return map
     */
    Map<String,String> getCountriesJSON();
}