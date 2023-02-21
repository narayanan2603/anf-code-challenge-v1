package com.anf.core.listeners;

import com.anf.core.constants.AnfConstants;
import org.apache.jackrabbit.api.observation.JackrabbitObservationManager;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * The CustomEventListener will take care of creating a property under jcr:content node if the page is being
 * created by this specific template (/content/anf-code-challenge/us/en)
 *
 * @author NK
 * @version 1.0
 * @since 02-11-2023
 *
 */
@Component(service = EventListener.class, immediate = true)
public class CustomEventListener implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger(CustomEventListener.class);
    @Reference
    ResourceResolverFactory resourceResolverFactory;
    ResourceResolver resourceResolver = null;
    private Session observationSession = null;
    private JackrabbitObservationManager observationManager = null;

    /*
     * Activate method will take care of Initializing the Resolver object, session.
     * */
    @Activate
    @Modified
    public void activate() {
        try {
            Map<String, Object> subService = new HashMap<>();
            subService.put(ResourceResolverFactory.SUBSERVICE, AnfConstants.NK_SERVICE_USER);
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(subService);
            observationSession = resourceResolver.adaptTo(Session.class);
            if (null != observationSession) {
                Workspace workSpace = observationSession.getWorkspace();
                if (null != workSpace) {
                    getWorkspace(workSpace);
                }
            }
        } catch (LoginException activateException) {
            LOG.error("An error occurred while getting session", activateException);
        }
    }

    /**
     * getWorkspace method consume the workspace from the Activate method, and it will make sure
     * Event listeners is triggering based on the page creation.
     *
     * @param workspace
     */
    public void getWorkspace(final Workspace workspace) {
        try {
            if (null != workspace) {
                observationManager = (JackrabbitObservationManager) workspace.getObservationManager();
                observationManager.addEventListener(this, Event.NODE_ADDED, AnfConstants.ABS_PATH,
                        AnfConstants.IS_DEEP, AnfConstants.UUIDS, AnfConstants.NODE_TYPES, AnfConstants.NO_LOCAL);
            }
        } catch (Exception workspaceException) {
            LOG.error("An error occurred while getting workspace", workspaceException);
        }
    }

    /*
     * this is method from EventListener interface, This method will take care
     * of getting the resource from the path and pass it over to the getResource method
     * @param eventIterator.
     */
    @Override
    public void onEvent(final EventIterator eventIterator) {
        while (eventIterator.hasNext()) {
            Event event = eventIterator.nextEvent();
            try {
                String path = event.getPath();
                if (path.endsWith(AnfConstants.JCR_CONTENT)) {
                    Resource resource = resourceResolver.resolve(path);
                    getResource(resource);
                }
            } catch (RepositoryException onEventException) {
                LOG.error("Exception in onEvent Method.", onEventException);
            }
        }
    }

    /*
     * This method will receive an object from the onEvent method and taking care of
     * adapting to node and add the property under the jcr:content node.
     * @param resource.
     * */
    public void getResource(final Resource resource) {
        try {
            Node nodes = resource.adaptTo(Node.class);
            if (null != nodes) {
                nodes.setProperty(AnfConstants.PAGE_CREATED, true);
                observationSession.save();
            }
        } catch (Exception resourceException) {
            LOG.error("Exception in getResource method", resourceException);
        }
    }

    /**
     * This method will take care of deactivate the resolver object and closing the session
     * if we did not close it will end up resulting an un-closed resolver issue
     * and leak in session.
     */
    @Deactivate
    protected void deactivate() {
        try {
            if (null != observationManager) {
                observationManager.removeEventListener(this);
            }
        } catch (RepositoryException deactivateMethodException) {
            LOG.error("An error occurred while removing event listener", deactivateMethodException);
        } finally {
            if (null != observationSession) {
                observationSession.logout();
                resourceResolver.close();
            }
        }
    }
}

