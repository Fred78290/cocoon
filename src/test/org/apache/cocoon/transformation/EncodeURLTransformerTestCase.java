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

package org.apache.cocoon.transformation;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.Processor;
import org.apache.cocoon.SitemapComponentTestCase;
import org.apache.cocoon.environment.internal.EnvironmentHelper;
import org.apache.cocoon.environment.mock.MockEnvironment;
import org.apache.cocoon.environment.mock.MockRequest;
import org.apache.cocoon.environment.mock.MockResponse;
import org.apache.cocoon.environment.mock.MockSession;
import org.w3c.dom.Document;


/**
 * A simple testcase for FilterTransformer.
 *
 * @author <a href="mailto:stephan@apache.org">Stephan Michels </a>
 * @version CVS $Id$
 */
public class EncodeURLTransformerTestCase extends SitemapComponentTestCase {
    
    /** Create new testcase
     * @param name of testase
     */
    public EncodeURLTransformerTestCase(String name) {
        super(name);
    }
    
    /**
     * Run this test suite from commandline
     *
     * @param args commandline arguments (ignored)
     */
    public static void main( String[] args ) {
        TestRunner.run(suite());
    }
    
    /** Create a test suite.
     * This test suite contains all test cases of this class.
     * @return the Test object containing all test cases.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(EncodeURLTransformerTestCase.class);
        return suite;
    }
    
    /** Testcase for encode url transformation
     *
     * @throws Exception iff ComponentManager enterEnvironment fails
     */
    public void testEncodeURL1() throws Exception {
        getLogger().debug("testEncodeURL1");
        
        Parameters parameters = new Parameters();
        
        String input = "resource://org/apache/cocoon/transformation/encodeurl-input-1.xml";
        String result = "resource://org/apache/cocoon/transformation/encodeurl-result-1.xml";
        String src =  null;
        
        // enter & leave environment, as a manager is looked up using
        // the processing context stack
        // enter & leave environment, as a manager is looked up using
        // the processing context stack
        MockEnvironment env = new MockEnvironment();
        Processor processor = (Processor)this.manager.lookup(Processor.ROLE);
        
        EnvironmentHelper.enterProcessor(processor, this.manager, env);
        
        Document inputDocument = load(input);
        Document resultDocument = load(result);
        Document transformDocument = transform("encodeurl", src, parameters, inputDocument);
        
        printDocs( resultDocument, inputDocument, transformDocument );
        
        assertIdentical( resultDocument, transformDocument );
        
        EnvironmentHelper.leaveProcessor();
    }
    
    /** Testcase for encode url transformation
     *
     * @throws Exception iff ComponentManager enterEnvironment fails
     */
    public void testEncodeURL2() throws Exception {
        getLogger().debug("testEncodeURL2");
        
        Parameters parameters = new Parameters();
        
        String input = "resource://org/apache/cocoon/transformation/encodeurl-input-2.xml";
        String result = "resource://org/apache/cocoon/transformation/encodeurl-result-2.xml";
        String src =  null;
        
        // force that sessionId is added to an URL
        MockRequest request = getRequest();
        MockSession session = (MockSession)request.getSession();
        MockResponse response = getResponse();
        
        response.setSession(session);
        getRequest().setIsRequestedSessionIdFromURL( true );

        session.setIsNew(true);
        
        // enter & leave environment, as a manager is looked up using
        // the processing context stack
        // enter & leave environment, as a manager is looked up using
        // the processing context stack
        MockEnvironment env = new MockEnvironment();
        Processor processor = (Processor)this.manager.lookup(Processor.ROLE);
        
        EnvironmentHelper.enterProcessor(processor, this.manager, env);
        
        Document inputDocument = load(input);
        Document resultDocument = load(result);
        Document transformDocument = transform("encodeurl", src, parameters, inputDocument);
        
        printDocs( resultDocument, inputDocument, transformDocument );
        assertIdentical( resultDocument, transformDocument );
        
        EnvironmentHelper.leaveProcessor();
    }
    
    /**
     * print documents to System.out
     *
     * @param resultDocument the expected result document
     * @param inputDocument the input document
     * @param transformDocument  the transformed input document
     */
    protected void printDocs( Document resultDocument, Document inputDocument, Document transformDocument ) {
        System.out.println( "resultDocument" );
        this.print( resultDocument );
        System.out.println( "inputDocument" );
        this.print( inputDocument );
        System.out.println( "transformDocument" );
        this.print( transformDocument );
    }
    
}
