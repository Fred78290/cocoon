/*
 * Copyright 1999-2005 The Apache Software Foundation.
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
package org.apache.cocoon.components.validation;

import java.io.IOException;

import org.apache.excalibur.source.Source;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * <p>TODO: ...</p>
 *
 * @author <a href="mailto:pier@betaversion.org">Pier Fumagalli</a>
 */
public interface Validator {

    public static final String ROLE = Validator.class.getName();
    
    public void validate(String xmlUri, String schemaUri)
    throws IOException, SAXException;

    public void validate(String xmlUri, String schemaUri, ErrorHandler errorHandler)
    throws IOException, SAXException;

    public void validate(Source xmlSource, Source schemaSource)
    throws IOException, SAXException;

    public void validate(Source xmlSource, Source schemaSource, ErrorHandler errorHandler)
    throws IOException, SAXException;

    public ValidationHandler getValidationHandler(String schemaUri)
    throws IOException, SAXException;

    public ValidationHandler getValidationHandler(String schemaUri, ErrorHandler errorHandler)
    throws IOException, SAXException;
    
    public ValidationHandler getValidationHandler(Source schemaSource)
    throws IOException, SAXException;

    public ValidationHandler getValidationHandler(Source schemaSource, ErrorHandler errorHandler)
    throws IOException, SAXException;

}
