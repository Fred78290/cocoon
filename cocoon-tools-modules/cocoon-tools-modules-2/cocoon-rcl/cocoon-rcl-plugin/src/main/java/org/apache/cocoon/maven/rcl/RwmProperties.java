/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.cocoon.maven.rcl;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.maven.plugin.MojoExecutionException;

public class RwmProperties {

    private static final String COB_INF_DIR = "/COB-INF";
    private static final String BLOCK_CONTEXT_URL_PARAM = "/blockContextURL";
    private static final String CLASSES_DIR = "%classes-dir";    
    
    private Configuration props;

    public RwmProperties(File propsFile) throws ConfigurationException {
        props = new PropertiesConfiguration(propsFile);
    }
    
    public Set getClassesDirs() {
        return getFilteredPropertiesValuesAsSet(CLASSES_DIR);
    }
    
    public Properties getSpringProperties() throws MojoExecutionException {
        Properties springProps = new Properties();
        for(Iterator rclIt = props.getKeys(); rclIt.hasNext();) {
            String key = (String) rclIt.next();
            if(!key.endsWith(CLASSES_DIR)) {
                springProps.put(key, this.props.getString(key));
            }
            if(key.endsWith(CLASSES_DIR) && !CLASSES_DIR.equals(key)) {
                String newKey = key.substring(0, key.length() - CLASSES_DIR.length()) + BLOCK_CONTEXT_URL_PARAM;
                File blockContext = new File(this.props.getString(key) + COB_INF_DIR);
                try {
                    springProps.put(newKey, blockContext.toURL().toExternalForm());
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException("Can't create URL to  " + blockContext, e);
                }
            }
        } 
        return springProps;
    }    
    
    private Set getFilteredPropertiesValuesAsSet(String filter) {
        Set returnSet = new HashSet();
        for (Iterator rclIt = props.getKeys(); rclIt.hasNext();) {
            String key = (String) rclIt.next();
            if (key.endsWith(filter)) {
                String[] values = this.props.getStringArray(key);
                for (int i = 0; i < values.length; i++) {
                    returnSet.add(values[i]);
                }
            }
        }        
        return returnSet;
    }
    
}

    
