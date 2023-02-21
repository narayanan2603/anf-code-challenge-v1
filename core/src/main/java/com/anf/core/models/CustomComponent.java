package com.anf.core.models;

import com.anf.core.constants.AnfConstants;
import com.anf.core.pojo.News;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

/**
 * The CustomComponent will take care displaying the results in the Page
 * Either for News feed or the Query builder API of displaying first 10 results
 * Logic defined based on the path.
 * If the path starts with /content it will do the logic for displaying the query build
 * if not it will take care of displaying the news results.
 *
 * @author NK
 * @version 1.0
 * @since 02-11-2023
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CustomComponent {

    private static final Logger CUSTOM_COMPONENT_LOG = LoggerFactory.getLogger(CustomComponent.class);
    @Inject
    public String pagePath;
    @SlingObject
    private ResourceResolver resourceResolver;

    @Inject
    private List<String> list;

    List<String> arrayList = new ArrayList<>();

    private List<News> newsList = new ArrayList<>();

    News news;

    /*
     * Init method will take care of Initializing the Resolver object.
     * It will send Page Object to the queryMap method.
     * */
    @PostConstruct
    private void init() {
        Resource resource = resourceResolver.getResource(getPagePath());
        try {
            if (null != getPagePath() && getPagePath().startsWith(AnfConstants.CONTENT_PATH)) {
                if (null != resource) {
                    Page page = resource.adaptTo(Page.class);
                    if (null != page) {
                        queryMap(page);
                    }
                }
            } else {
                if (resource != null) {
                    Iterator<Resource> newsItems = resource.listChildren();
                    while (newsItems.hasNext()) {
                        Resource newsFeedItemResource = newsItems.next();
                        getNewsItem(newsFeedItemResource);
                    }
                }
            }
        } catch (Exception initException) {
            CUSTOM_COMPONENT_LOG.debug("Exception in Custom Component Init Method", initException);
        }
    }

    /*
     * queryMap method will take care of Constructing Map objects for the query.
     * It will send Map Object to the generateQuery method.
     * */
    public void queryMap(final Page page) {
        try {
            Map<String, String> apiMap = new HashMap<>();
            apiMap.put(AnfConstants.PATH, page.getPath());
            apiMap.put(AnfConstants.TYPE, AnfConstants.CQ_PAGE);
            apiMap.put(AnfConstants.PROPERTY, AnfConstants.PROPERTY_PATH);
            apiMap.put(AnfConstants.PROPERTY_VALUE, AnfConstants.PROPERTY_BOOLEAN_VALUE);
            apiMap.put(AnfConstants.ORDER_BY, AnfConstants.QUERY_ORDER_BY);
            apiMap.put(AnfConstants.LIMIT, AnfConstants.QUERY_LIMIT);
            generateQuery(apiMap);
        } catch (Exception queryMapException) {
            CUSTOM_COMPONENT_LOG.debug("Exception in Custom Component Query Map method", queryMapException);
        }
    }

    /*
     * generateQuery method will take care of Constructing the query.
     * It will send Hit Object to the getHits method.
     * */
    public void generateQuery(final Map<String, String> map) {
        try {
            if (resourceResolver != null) {
                QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
                if (null != queryBuilder) {
                    Query query = queryBuilder.createQuery(
                            PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
                    SearchResult searchResult = query.getResult();
                    for (Hit hit : searchResult.getHits()) {
                        getHits(hit);
                    }
                }
            }
        } catch (Exception generateQueryException) {
            CUSTOM_COMPONENT_LOG.debug("Exception in Custom Component Generate Query Method", generateQueryException);
        }
    }

    /*
     * getHits method will take care of Getting the current Page from the Hits.
     * It will send the String to getList method.
     * @throws RepositoryException
     * */
    public void getHits(final Hit hit) {
        try {
            Page currentPage = hit.getResource().adaptTo(Page.class);
            if (null != currentPage) {
                getList(currentPage.getPath());
            }
        } catch (RepositoryException repositoryException) {
            CUSTOM_COMPONENT_LOG.debug("Exception in Custom Component getHits Method",
                    repositoryException);
        }
    }

    /*
     * getList method will take of adding the pages into Arraylist and send
     * it to the Sightly for rendering purpose.
      @return arrayList;
     * */
    public List<String> getList(final String pages) {
        try {
            arrayList.add(getFullPath(pages));
        } catch (Exception listException) {
            CUSTOM_COMPONENT_LOG.debug("Exception in Custom Component getList Method",
                    listException);
        }
        return arrayList;
    }

    /**
     * getFullPath method will check the current page based on the URL it will
     * append the Host and extension to that URL.
     *
     * @return fullURL;
     */
    public String getFullPath(final String currentPage) {
        String fullURL = StringUtils.EMPTY;
        String localhost = AnfConstants.LOCAL_HOST;
        try {
            if (currentPage.startsWith(AnfConstants.CONTENT_PATH)) {
                fullURL = localhost.concat(currentPage).concat(AnfConstants.CONTENT_EXTENSION);
            }
        } catch (Exception fullPathException) {
            CUSTOM_COMPONENT_LOG.debug("Exception in Custom Component getFullPath method",
                    fullPathException);
        }
        return fullURL;
    }

    /**
     * getNewsItem will iterate the list of children from the /var/commerce/products/anf-code-challenge/newsData
     * Add it on the ValueMap and the News Pojo to return those values in the Sightly.
     */
    public News getNewsItem(Resource newsFeedItemResource) {
        ValueMap valueMap = newsFeedItemResource.getValueMap();
        news = new News();
        news.setAuthor(valueMap.get(AnfConstants.AUTHOR, AnfConstants.STRING_CONSTANT));
        news.setContent(valueMap.get(AnfConstants.CONTENT, AnfConstants.STRING_CONSTANT));
        news.setDescription(valueMap.get(AnfConstants.DESCRIPTION, AnfConstants.STRING_CONSTANT));
        news.setTitle(valueMap.get(AnfConstants.TITLE, AnfConstants.STRING_CONSTANT));
        news.setUrl(valueMap.get(AnfConstants.URL, AnfConstants.STRING_CONSTANT));
        news.setUrlImage(valueMap.get(AnfConstants.URL_IMAGE, AnfConstants.STRING_CONSTANT));
        newsList.add(news);
        return news;
    }

    public List<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(final List<String> arrayList) {
        this.arrayList = arrayList;
    }

    public String getPagePath() {
        return pagePath;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

}
