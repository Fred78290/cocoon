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
package org.apache.cocoon.components.language.markup.xsp;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.CascadingRuntimeException;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;

import org.apache.cocoon.components.modules.input.InputModule;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Helper class that caches references to InputModules for use in
 * XSPs. Works in conjunction with the input.xsl
 * logicsheet. References are obtained the first time a module is
 * accessed and kept until the page is completely displayed.
 *
 * @author <a href="mailto:haul@apache.org">Christian Haul</a>
 * @version CVS $Id: XSPModuleHelper.java,v 1.8 2004/03/08 13:58:31 cziegeler Exp $
 */
public class XSPModuleHelper {

    protected final static String INPUT_MODULE_SELECTOR = InputModule.ROLE+"Selector";

    private static final String PREFIX = "input";
    private static final String URI = "http://apache.org/cocoon/xsp/input/1.0";

    /* Operation codes */
    private final static int OP_GET = 0;
    private final static int OP_VALUES = 1;
    private final static int OP_NAMES = 2;

    private Map inputModules;
    private ServiceManager serviceManager;
    private ServiceSelector serviceInputSelector;
    

    /**
     * Get the input module
     */
    private InputModule getInputModule(String name)
    throws CascadingRuntimeException {
        if ( this.inputModules == null ) {
            throw new RuntimeException("ModuleHelper is not setup correctly.");
        }
        InputModule module = (InputModule) this.inputModules.get(name);
        if ( module == null ) {
            try {
                if (this.serviceInputSelector.isSelectable(name)) {
                    module = (InputModule) this.serviceInputSelector.select(name);
                }                            
            } catch (Exception e) {
                throw new CascadingRuntimeException("Unable to lookup input module " + name, e);
            }
            if ( module == null ) {
                throw new RuntimeException("No such InputModule: "+name);
            }
            this.inputModules.put(name, module);
        }
        return module;
    }
    
    /**
     * Capsules use of an InputModule. Does all the lookups and so
     * on. Returns either an Object, an Object[], or an Iterator,
     * depending on the method called i.e. the op specified. The
     * second module is preferred and has an non null name. If an
     * exception is encountered, a warn message is printed and null is
     * returned.
     * @param op an <code>int</code> value encoding the desired operation
     * @param name a <code>String</code> value holding the name of the
     * InputModule
     * @param attr a <code>String</code> value holding the name of the
     * attribute to return. Is disregarded when attribute names is
     * requested.
     * @param objectModel a <code>Map</code> value holding the current
     * ObjectModel
     * @return an <code>Object</code> value
     * @exception CascadingRuntimeException if an error occurs. The real
     * exception can be obtained with <code>getCause</code>.
     */
    private Object get(int op, String name, String attr, Map objectModel, Configuration conf) throws CascadingRuntimeException {

        Object value = null;
        final InputModule input = this.getInputModule(name);

        try {

            switch (op) {
            case OP_GET:    
                value = input.getAttribute(attr, conf, objectModel);
                break;
            case OP_VALUES:
                value = input.getAttributeValues(attr, conf, objectModel);
                break;
            case OP_NAMES:
                value = input.getAttributeNames(conf, objectModel);
                break;
            }

        } catch (Exception e) {
            throw new CascadingRuntimeException("Error accessing attribute '"+attr+"' from input module '"+name+"'. "+e.getMessage(), e);
        }

        return value;
    }

    private Object get(int op, String name, String attr, Map objectModel) throws RuntimeException {
        return get(op, name, attr, objectModel, null);
    }

    /**
     * Initializes the instance for first use. Stores references to
     * service manager and service selector in instance 
     *
     * @param manager a <code>ServiceManager</code> value
     * @exception RuntimeException if an error occurs
     */
    public void setup(ServiceManager manager) throws RuntimeException {

        this.inputModules = new HashMap();
        this.serviceManager = manager;
        try {
            this.serviceInputSelector = (ServiceSelector) this.serviceManager.lookup(INPUT_MODULE_SELECTOR); 
        } catch (Exception e) {
            throw new CascadingRuntimeException("Could not obtain selector for InputModule.",e);
        }
    }


    /**
     * Get a single attribute value from a module. Uses cached
     * reference if existing.
     *
     * @param objectModel a <code>Map</code> value
     * @param conf a <code>Configuration</code> containing the module dynamic configuration (aka modeConf)
     * @param module a <code>String</code> value holding the module name
     * @param name a <code>String</code> value holding the attribute name
     * @param deflt an <code>Object</code> value holding a default value
     * @return an <code>Object</code> value
     * @exception RuntimeException if an error occurs
     */
    public Object getAttribute(Map objectModel, Configuration conf, String module, String name, Object deflt) throws RuntimeException {

        Object result = this.get(OP_GET, module, name, objectModel, conf);
        if (result == null) result = deflt;
        return result;
    }

    /**
     * Get a single attribute value from a module.  Same as {@link
     * #getAttribute(Map, Configuration, String, String, Object)} with a
     * <code>null</code> configuration.
     */
    public Object getAttribute(Map objectModel, String module, String name, Object deflt) throws RuntimeException {
        return getAttribute(objectModel, null, module, name, deflt);
    }


    /**
     * Get an array of values from a module. Uses cached reference if
     * existing.
     *
     * @param objectModel a <code>Map</code> value
     * @param conf a <code>Configuration</code> containing the module dynamic configuration (aka modeConf)
     * @param module a <code>String</code> value holding the module name
     * @param name a <code>String</code> value holding the attribute name
     * @param deflt an <code>Object[]</code> value holding a default value
     * @return an <code>Object[]</code> value
     * @exception RuntimeException if an error occurs
     */
    public Object[] getAttributeValues(Map objectModel, Configuration conf, String module, String name, Object[] deflt) throws RuntimeException {

        Object[] result = (Object[]) this.get(OP_VALUES, module, name, objectModel, conf);
        if (result == null) result = deflt;
        return result;
    }

    /**  Get an array of values from a module. Same as {@link
     * #getAttributeValues(Map, Configuration, String, String, Object[])} with a
     * <code>null</code> configuration.
     */
    public Object[] getAttributeValues(Map objectModel, String module, String name, Object[] deflt) throws RuntimeException {
        return getAttributeValues(objectModel, null, module, name, deflt);
    }

    /**
     * Output the request attribute values for a given name to the
     * content handler.
     *
     * @param objectModel The Map objectModel
     * @param contentHandler The SAX content handler
     * @param module a <code>String</code> value holding the module name
     * @param name a <code>String</code> value holding the attribute name
     * @exception SAXException If a SAX error occurs
     * @exception RuntimeException if an error occurs
     */
    public void getAttributeValues(Map objectModel, ContentHandler contentHandler, String module, String name )
        throws SAXException, RuntimeException {

        AttributesImpl attr = new AttributesImpl();
        XSPObjectHelper.addAttribute(attr, "name", name);

        XSPObjectHelper.start(URI, PREFIX, contentHandler,
            "attribute-values", attr);

        Object[] values = this.getAttributeValues(objectModel, module, name, null);

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                XSPObjectHelper.elementData(URI, PREFIX, contentHandler,
                    "value", String.valueOf(values[i]));
            }
        }

        XSPObjectHelper.end(URI, PREFIX, contentHandler, "attribute-values");
    }


    /**
     * Get an iterator to a collection of attribute names from a
     * module.
     *
     * @param objectModel a <code>Map</code> value
     * @param module the module's name
     * @return an <code>Iterator</code> value
     * @exception RuntimeException if an error occurs
     */
    public Iterator getAttributeNames(Map objectModel, Configuration conf, String module) throws RuntimeException {

        return (Iterator) this.get(OP_NAMES, module, null, objectModel);
    }

    /**  Get an iterator to a collection of attribute names from a module. Same
     * as {@link #getAttributeNames(Map, Configuration, String)} with a
     * <code>null</code> configuration.
     */
    public Iterator getAttributeNames(Map objectModel, String module) throws RuntimeException {
        return getAttributeNames(objectModel, (Configuration)null, module);
    }

    /**
     * Output attribute names for a given request
     *
     * @param objectModel The Map objectModel
     * @param contentHandler The SAX content handler
     * @param module the module's name
     * @exception SAXException If a SAX error occurs
     * @exception RuntimeException if an error occurs
     */
    public  void getAttributeNames(Map objectModel, ContentHandler contentHandler, String module)
        throws SAXException, RuntimeException {

        XSPObjectHelper.start(URI, PREFIX, contentHandler, "attribute-names");

        Iterator iter = this.getAttributeNames(objectModel, module);
        while (iter.hasNext()) {
            String name = (String) iter.next();
            XSPObjectHelper.elementData(URI, PREFIX, contentHandler, "name", name);
        }

        XSPObjectHelper.end(URI, PREFIX, contentHandler, "attribute-names");
    }



    /**
     * Releases all obtained module references.
     *
     * @exception RuntimeException if an error occurs
     */
    public void releaseAll() throws RuntimeException {

        if ( this.inputModules != null ) {
            if ( this.serviceManager != null ) {
                try {
                    Iterator iter = this.inputModules.keySet().iterator();
                    while (iter.hasNext()) {
                        this.serviceInputSelector.release(this.inputModules.get(iter.next()));
                    }
                    this.inputModules = null;
                    this.serviceManager.release(this.serviceInputSelector);
                    this.serviceManager = null;
                    this.inputModules = null;
                } catch (Exception e) {
                    throw new CascadingRuntimeException("Could not release InputModules.",e);
                }
                
            }
        }
    }
}
