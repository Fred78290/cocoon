/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.cocoon.components.flow.javascript.fom;

import org.apache.cocoon.components.flow.FlowHelper;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

import org.mozilla.javascript.Scriptable;

import java.util.Map;

/**
 * Provides the interface between the JavaScript flow controller layer and the
 * view layer. A view can obtain the JavaScript "live connect" objects (that
 * allow access to Java constructors) through this interface, as well as
 * the FOM objects.
 *
 * @version CVS $Id: FOM_JavaScriptFlowHelper.java,v 1.4 2004/03/05 13:02:46 bdelacretaz Exp $
 */
public class FOM_JavaScriptFlowHelper extends FlowHelper {

    public static final String PACKAGES_OBJECT =
        "cocoon.flow.js.packages";
    public static final String JAVA_PACKAGE_OBJECT =
        "cocoon.flow.js.packages.java";
    public static final String FOM_REQUEST =
        "cocoon.flow.js.fom.FOM_Request";
    public static final String FOM_RESPONSE =
        "cocoon.flow.js.fom.FOM_Response";
    public static final String FOM_SESSION =
        "cocoon.flow.js.fom.FOM_Session";
    public static final String FOM_CONTEXT =
        "cocoon.flow.js.fom.FOM_Context";
    public static final String FOM_WEB_CONTINUATION =
        "cocoon.flow.js.fom.FOM_WebContinuation";
    /**
     * The parent scope to be used by nested scripts (e.g. Woody event handlers)
     */
    public static final String FOM_SCOPE =
        "cocoon.flow.js.fom.FOM_Scope";

    /**
     * Return the JS "Packages" property (that gives access to Java
     * packages) for use by the view layer
     * @param objectModel The Cocoon Environment's object model
     * @return The Packages property
     */
    public static Scriptable getPackages(Map objectModel) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        return (Scriptable)request.getAttribute(PACKAGES_OBJECT);
    }

    /**
     * Set the JS "Packages" property in the current request
     * @param objectModel The Cocoon Environment's object model
     */
    public static void setPackages(Map objectModel, Scriptable pkgs) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        request.setAttribute(PACKAGES_OBJECT, pkgs);
    }

    /**
     * Return the JS "java" property (that gives access to the "java"
     * package) for use by the view layer
     * @param objectModel The Cocoon Environment's object model
     * @return The java package property
     */
    public static Scriptable getJavaPackage(Map objectModel) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        return (Scriptable)request.getAttribute(JAVA_PACKAGE_OBJECT);
    }

    /**
     * Set the JS "java" property in the current request
     * @param objectModel The Cocoon Environment's object model
     */
    public static void setJavaPackage(Map objectModel, Scriptable javaPkg) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        request.setAttribute(JAVA_PACKAGE_OBJECT, javaPkg);
    }

    public static Scriptable getFOM_Request(Map objectModel) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        return (Scriptable)request.getAttribute(FOM_REQUEST);
    }

    public static void setFOM_Request(Map objectModel, Scriptable fom_request) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        request.setAttribute(FOM_REQUEST, fom_request);
    }

    public static Scriptable getFOM_Response(Map objectModel) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        return (Scriptable)request.getAttribute(FOM_RESPONSE);
    }

    public static void setFOM_Response(Map objectModel, Scriptable fom_response) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        request.setAttribute(FOM_RESPONSE, fom_response);
    }

    public static Scriptable getFOM_Session(Map objectModel) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        return (Scriptable)request.getAttribute(FOM_SESSION);
    }

    public static void setFOM_Session(Map objectModel, Scriptable fom_session) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        request.setAttribute(FOM_SESSION, fom_session);
    }

    public static Scriptable getFOM_Context(Map objectModel) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        return (Scriptable)request.getAttribute(FOM_CONTEXT);
    }

    public static void setFOM_Context(Map objectModel, Scriptable fom_context) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        request.setAttribute(FOM_CONTEXT, fom_context);
    }

    public static Scriptable getFOM_WebContinuation(Map objectModel) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        return (Scriptable)request.getAttribute(FOM_WEB_CONTINUATION);
    }

    public static void setFOM_WebContinuation(Map objectModel,
                                              Scriptable fom_webContinuation) {
        Request request = ObjectModelHelper.getRequest(objectModel);
        request.setAttribute(FOM_WEB_CONTINUATION, fom_webContinuation);
    }
}
