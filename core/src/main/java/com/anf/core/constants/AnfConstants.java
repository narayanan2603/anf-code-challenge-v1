package com.anf.core.constants;


import org.apache.commons.lang3.StringUtils;

public class AnfConstants {

    //Space to add Listener based Constants
    public static final boolean IS_DEEP = true;
    public static final boolean NO_LOCAL = false;
    public static final String[] UUIDS = null;
    public static final String NK_SERVICE_USER = "nkserviceuser";
    //End of Listener based constants

    //Space to add Page related constants
    public static final String JCR_CONTENT = "jcr:content";
    public static final String CQ_PAGE = "cq:page";
    public static final String PAGE_CREATED = "pageCreated";
    public static final String[] NODE_TYPES = new String[]{"cq:Page"};
    public static final String ABS_PATH = "/content/anf-code-challenge/us/en";
    public static final String PROPERTY_PATH = "jcr:content/anfCodeChallenge";
    public static final String PROPERTY_BOOLEAN_VALUE ="true";
    public static final String QUERY_ORDER_BY = "@jcr:content/jcr:created";
    public static final String CONTENT_PATH = "/content";
    public static final String CONTENT_EXTENSION = ".html";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String URL = "url";
    public static final String URL_IMAGE = "urlImage";

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
}
