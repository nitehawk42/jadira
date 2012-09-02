/*
 *  Copyright 2010 Chris Pheby
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jadira.bindings.core.jdk;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class URLStringBindingTest {

    private static final URLStringBinding BINDING = new URLStringBinding();
    
    @Test
    public void testUnmarshal() throws MalformedURLException{
     
        assertEquals(new URL("http://www.example.com"), BINDING.unmarshal("http://www.example.com"));
    }
    
    @Test
    public void testMarshal() throws MalformedURLException{

        assertEquals("http://www.example.com", BINDING.marshal(new URL("http://www.example.com")));
    }
}
