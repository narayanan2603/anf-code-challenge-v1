package com.anf.core.constants;


import org.apache.commons.lang3.StringUtils;

/**
 * The AnfConstants will take care of storing all the re-usable constants
 * This will avoid the Duplication in our code base.
 *
 * @author NK
 * @version 1.0
 * @since 02-13-2023
 *
 */
public class AnfConstants {

    //Space to add Listener based Constants
    public static final boolean IS_DEEP = true;
    public static final boolean NO_LOCAL = false;
    public static final String[] UUIDS = null;
    public static final String ANF_SERVICE_USER = "anfserviceuser";
    //End of Listener based constants

    //Space to add Page related constants
    public static final String JCR_CONTENT = "jcr:content";
    public static final String CQ_PAGE = "cq:page";
    public static final String PAGE_CREATED = "pageCreated";
    public static final String[] NODE_TYPES = new String[]{"cq:Page"};
    public static final String ABS_PATH = "/content/anf-code-challenge/us/en";
    public static final String PROPERTY_PATH = "jcr:content/anfCodeChallenge";
    public static final String PROPERTY_BOOLEAN_VALUE = "true";
    public static final String QUERY_ORDER_BY = "@jcr:content/jcr:created";
    public static final String CONTENT_PATH = "/content";
    public static final String CONTENT_EXTENSION = ".html";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String URL = "url";
    public static final String URL_IMAGE = "urlImage";
    public static final String NODE_TYPE = "nt:unstructured";
    public static final String COUNTRY_JSON = "/apps/anf-code-challenge/components/form/country/countries.json";

    //End of Page related constants

    //Space to add the Query related Constants
    public static final String PATH = "path";
    public static final String TYPE = "type";
    public static final String PROPERTY = "property";
    public static final String PROPERTY_VALUE = "property.value";
    public static final String ORDER_BY = "orderby";
    public static final String LIMIT = "p.limit";
    public static final String QUERY_LIMIT = "10";
    //End of Query related Constants..

    //Space for the Server level Constants..
    public static final String LOCAL_HOST = "http://localhost:4502";
    //End of Server level constants..

    //Space for General Constants
    public static final String APPLICATION_TYPE = "application/json";
    public static final String STRING_CONSTANT = StringUtils.EMPTY;
    //End of General Constants

    //Space for Username Constants..
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String AGE = "age";
    public static final String COUNTRY = "country";
    public static final String RESULT = "result";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String VAR_USER_DETAILS = "/var/anf-code-challenge";
    public static final String EXCEPTION_MSG = "Excepting while trying to persist user details {}";
    public static final String USER_DETAIL = "userdetail_";
    public static final String TEXT = "text";
    public static final String VALUE = "value";
    public static final String COUNTRIES = "countries";
    public static final String CODE = "code";
    public static final String NAME = "name";
    //End of Username Constants..
}
