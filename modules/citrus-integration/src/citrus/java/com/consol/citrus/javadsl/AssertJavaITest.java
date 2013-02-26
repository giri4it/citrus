/*
 * Copyright 2006-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.javadsl;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.consol.citrus.dsl.TestNGCitrusTestBuilder;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.exceptions.ValidationException;

/**
 * @author Christoph Deppisch
 */
public class AssertJavaITest extends TestNGCitrusTestBuilder {
    
    @Override
    protected void configure() {
        variable("failMessage", "Something went wrong!");
        
        assertException(fail("Fail once"))
                .exception(CitrusRuntimeException.class);
        
        assertException(fail("Fail again"))
                .exception(CitrusRuntimeException.class)
                .message("Fail again");
        
        assertException(fail("${failMessage}"))
                .exception(CitrusRuntimeException.class)
                .message("${failMessage}");
        
        assertException(fail("${failMessage}"))
                .exception(CitrusRuntimeException.class)
                .message("@contains('wrong')@");
        
        assertException(
                assertException(fail("Fail another time"))
                    .exception(IOException.class))
                .exception(ValidationException.class);
        
        assertException(
                assertException(fail("Fail with nice error message"))
                    .exception(CitrusRuntimeException.class)
                    .message("Fail again"))
                .exception(ValidationException.class);
        
        assertException(
                assertException(echo("Nothing fails here"))
                    .exception(CitrusRuntimeException.class))
                .exception(ValidationException.class);
        
        assertException(
                assertException(echo("Nothing fails here either"))
                    .exception(CitrusRuntimeException.class)
                    .message("Must be failing"))
                .exception(ValidationException.class);
    }
    
    @Test
    public void assertITest(ITestContext testContext) {
        executeTest(testContext);
    }
}