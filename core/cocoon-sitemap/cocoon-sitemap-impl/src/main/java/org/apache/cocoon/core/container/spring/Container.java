/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.cocoon.core.container.spring;

import javax.servlet.ServletContext;

import org.apache.cocoon.spring.impl.ServletContextFactoryBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version $Id$
 */
public abstract class Container {

    /** The name of the request attribute containing the current bean factory. */
    public static final String CONTAINER_REQUEST_ATTRIBUTE = Container.class.getName();

    protected static WebApplicationContext ROOT_CONTAINER;

    public static WebApplicationContext getCurrentWebApplicationContext() {
        final RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return getCurrentWebApplicationContext(attributes);
    }

    /**
     * Return the current web application context.
     * @param attributes     The request attributes.
     * @return The web application context.
     */
    protected static WebApplicationContext getCurrentWebApplicationContext(RequestAttributes attributes) {
        if ( attributes != null ) {
            if (attributes.getAttribute(CONTAINER_REQUEST_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST) != null) {
                return (WebApplicationContext) attributes.getAttribute(CONTAINER_REQUEST_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
            }
        }
        if ( ROOT_CONTAINER == null ) {
            final ServletContext servletContext = ServletContextFactoryBean.getServletContext();
            final WebApplicationContext parentContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            ROOT_CONTAINER = parentContext;
        }
        return ROOT_CONTAINER;
    }

    /**
     * Notify about entering this context.
     * @return A handle which should be passed to {@link #leavingContext(RequestAttributes, Object)}.
     */
    public static Object enteringContext(WebApplicationContext webAppContext) {
        // get request attributes
        final RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        final ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        final WebApplicationContext oldContext = (WebApplicationContext)attributes.getAttribute(CONTAINER_REQUEST_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        final ContextInfo info = new ContextInfo(oldContext, oldClassLoader);
        attributes.setAttribute(CONTAINER_REQUEST_ATTRIBUTE, webAppContext, RequestAttributes.SCOPE_REQUEST);
        Thread.currentThread().setContextClassLoader(webAppContext.getClassLoader());
        return info;
    }

    /**
     * Notify about leaving this context.
     * @param handle     The returned handle from {@link #enteringContext(RequestAttributes)}.
     */
    public static void leavingContext(WebApplicationContext webAppContext, Object handle) {
        if ( !(handle instanceof ContextInfo) ) {
            throw new IllegalArgumentException("Handle must be an instance of ContextInfo and not " + handle);
        }
        final ContextInfo info = (ContextInfo)handle;
        // get request attributes
        final RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        // restore class loader
        Thread.currentThread().setContextClassLoader(info.classLoader);
        // restore previous web application context (or remove attribute)
        if ( info.webAppContext == null ) {
            attributes.removeAttribute(CONTAINER_REQUEST_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        } else {
            attributes.setAttribute(CONTAINER_REQUEST_ATTRIBUTE, info.webAppContext, RequestAttributes.SCOPE_REQUEST);
        }
    }

    public static void shutdown(WebApplicationContext webAppContext) {
        if ( webAppContext instanceof ConfigurableApplicationContext ) {
            ((ConfigurableApplicationContext)webAppContext).close();
        } else if ( webAppContext instanceof ConfigurableBeanFactory ) {
            ((ConfigurableBeanFactory)webAppContext).destroySingletons();
        }
    }

    protected static final class ContextInfo {
        public final ClassLoader classLoader;
        public final WebApplicationContext webAppContext;
        
        public ContextInfo(WebApplicationContext w, ClassLoader c) {
            this.classLoader = c;
            this.webAppContext = w;
        }
    }
}
