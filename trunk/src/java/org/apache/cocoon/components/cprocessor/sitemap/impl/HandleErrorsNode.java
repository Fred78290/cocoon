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
package org.apache.cocoon.components.cprocessor.sitemap.impl;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.cprocessor.InvokeContext;
import org.apache.cocoon.components.cprocessor.ProcessingNode;
import org.apache.cocoon.components.cprocessor.SimpleParentProcessingNode;
import org.apache.cocoon.environment.Environment;

/**
 * Handles &lt;map:handle-errors&gt;
 *
 * @author <a href="mailto:sylvain@apache.org">Sylvain Wallez</a>
 * @version CVS $Id: HandleErrorsNode.java,v 1.2 2004/03/08 13:57:38 cziegeler Exp $
 * 
 * @avalon.component
 * @avalon.service type=ProcessingNode
 * @x-avalon.lifestyle type=singleton
 * @x-avalon.info name=handle-errors-node
 */
public final class HandleErrorsNode extends SimpleParentProcessingNode 
implements ProcessingNode {

    private int statusCode;

    public HandleErrorsNode() {
    }

    public void configure(Configuration config) throws ConfigurationException {
        super.configure(config);
        this.statusCode = config.getAttributeAsInteger("type", -1);
    }
    
    public int getStatusCode() {
        return this.statusCode;
    }

    public final boolean invoke(Environment env, InvokeContext context) throws Exception {

        if (getLogger().isInfoEnabled()) {
            getLogger().info("Processing handle-errors at " + getLocation());
        }

		if (statusCode == -1) {
            // No 'type' attribute : new Cocoon 2.1 behaviour, no implicit generator
            try {
                return invokeNodes(getChildNodes(), env, context);
                
            } catch(ProcessingException pe) {
                // Handle the various cases related to the transition from implicit generators in handle-errors to
                // explicit ones, in order to provide meaningful messages that will ease the migration
                if (statusCode == - 1 &&
                    pe.getMessage().indexOf("must set a generator first before you can use a transformer") != -1) {

                    throw new ProcessingException(
                        "Incomplete pipeline : 'handle-error' without a 'type' must include a generator, at " +
                        this.getLocation() + System.getProperty("line.separator") +
                        "Either add a generator (preferred) or a type='500' attribute (deprecated) on 'handle-errors'");
                        
                } else if (statusCode != -1 &&
                    pe.getMessage().indexOf("Generator already set") != -1){

                    throw new ProcessingException(
                        "Error : 'handle-error' with a 'type' attribute has an implicit generator, at " +
                        this.getLocation() + System.getProperty("line.separator") +
                        "Please remove the 'type' attribute on 'handle-error'");

                } else {
                    // Rethrow the exception
                    throw pe;
                }
            }
		} else {
		    // A 'type' attribute is present : add the implicit generator
            context.getProcessingPipeline().setGenerator("<notifier>", "", Parameters.EMPTY_PARAMETERS, Parameters.EMPTY_PARAMETERS);
            return invokeNodes(getChildNodes(), env, context);
		}
    }

}
