package com.anf.core.models;

import com.anf.core.pojo.News;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(AemContextExtension.class)
public class CustomComponentTest {

    private final AemContext aemContext = new AemContext();
    private CustomComponent customComponent = new CustomComponent();
    private static final String TEST_VAR_PATH = "/var/commerce/products/anf-code-challenge";

    private static final String TEST_VAR_DATA_PATH = TEST_VAR_PATH + "/data/newsData";

    private News news;


    @BeforeEach
    public void setUp() throws Exception {
        aemContext.addModelsForClasses(News.class);
        aemContext.load().json("/content/anf-code-challenge/us/en/customcomponent.json", TEST_VAR_PATH);
        customComponent = aemContext.currentResource(TEST_VAR_PATH + "/newsList").adaptTo(CustomComponent.class);
    }

    /**
     * Test method for news feed path
     */
    @Test
    void testGetNewsFeedPath() {
        assertEquals(TEST_VAR_DATA_PATH, "/var/commerce/products/anf-code-challenge/data/newsData");
    }

    /**
     * Test method for news list
     */
    @Test
    void testNewsList() {
        assertNull(news);
        aemContext.currentResource(TEST_VAR_DATA_PATH);
        assertEquals(0, customComponent.getNewsList().size());
    }

    /**
     * Test method for query Path
     */
    @Test
    void testQueryPath() {
        aemContext.currentResource(TEST_VAR_DATA_PATH);
        assertEquals("", customComponent.getFullPath(TEST_VAR_DATA_PATH));
    }
}
