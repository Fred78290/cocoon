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
package org.apache.cocoon.woody.datatype;

import org.apache.cocoon.woody.util.DomHelper;
import org.w3c.dom.Element;

/**
 * Description of EnumSelectionListBuilder.
 * @version CVS $Id: EnumSelectionListBuilder.java,v 1.3 2004/03/05 13:02:28 bdelacretaz Exp $
 */
public class EnumSelectionListBuilder implements SelectionListBuilder {

    /* (non-Javadoc)
     * @see org.apache.cocoon.woody.datatype.SelectionListBuilder#build(org.w3c.dom.Element, org.apache.cocoon.woody.datatype.Datatype)
     */
    public SelectionList build(Element selectionListElement, Datatype datatype)
        throws Exception {
        String className = DomHelper.getAttribute(selectionListElement, "class");
        boolean nullable = DomHelper.getAttributeAsBoolean(selectionListElement, "nullable", true);
        return new EnumSelectionList(className, datatype, nullable);
    }

}
