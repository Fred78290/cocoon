/*

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================

 Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.

 4. The names "Apache Cocoon" and  "Apache Software Foundation" must  not  be
    used to  endorse or promote  products derived from  this software without
    prior written permission. For written permission, please contact
    apache@apache.org.

 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation and was  originally created by
 Stefano Mazzocchi  <stefano@apache.org>. For more  information on the Apache
 Software Foundation, please see <http://www.apache.org/>.

*/
package org.apache.cocoon.woody.samples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.apache.cocoon.woody.datatype.Enum;

/**
 * Description of Sex.
 * @version CVS $Id: Sex.java,v 1.4 2003/11/08 14:27:03 joerg Exp $
 */
public class Sex implements Enum {

    public static final Sex MALE = new Sex("M");
    public static final Sex FEMALE = new Sex("F");
    private String code;

    private Sex(String code) { this.code = code; }

    public String toString() {
      // Will probably have some i18n support here
      switch(code.charAt(0)) {
          case 'M' : return "male";
          case 'F' : return "female";
          default : return "unknown"; // Should never happen
      }
    }

    public static Sex fromString(String value, Locale locale) {
        if (value.equals("male")) return Sex.MALE;
        if (value.equals("female")) return Sex.FEMALE;
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.woody.datatype.Enum#convertToString(java.lang.Object, java.util.Locale)
     */
    public String convertToString(Locale locale) {
        return toString();
    }
    
    public static Collection listValues() {
        Collection values = new ArrayList(2);
        values.add(Sex.FEMALE);
        values.add(Sex.MALE);
        return values;
    }

}
