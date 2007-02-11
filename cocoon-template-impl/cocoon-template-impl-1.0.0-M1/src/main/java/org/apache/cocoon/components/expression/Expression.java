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
package org.apache.cocoon.components.expression;

import java.util.Iterator;

/**
 * @version $Id$
 */
public interface Expression {

    Object evaluate(ExpressionContext context)
            throws ExpressionException;

    Iterator iterate(ExpressionContext context)
            throws ExpressionException;

    void assign(ExpressionContext context, Object value)
            throws ExpressionException;

    String getExpression();

    String getLanguage();

    /*
     * This method is added to handle that JXPath have two access methods
     * getValue and getNode, where getNode gives direct access to the object
     * while getValue might do some conversion of the object. I would prefer to
     * get rid of the getNode method, but have not yet figured out how to get
     * work in JXTG
     */
    Object getNode(ExpressionContext context) throws ExpressionException;

    void setProperty(String property, Object value);
}
