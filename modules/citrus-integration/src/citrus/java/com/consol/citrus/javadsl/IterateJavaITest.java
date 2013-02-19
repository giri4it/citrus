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

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.consol.citrus.dsl.TestNGCitrusTestBuilder;

/**
 * @author Christoph Deppisch
 */
public class IterateJavaITest extends TestNGCitrusTestBuilder {
    
    @Override
    protected void configure() {
        variable("max", "3");
        
        iterate(echo("index is: ${i}")).condition("i lt= citrus:randomNumber(1)").index("i");
        
        iterate(echo("index is: ${i}")).condition("i lt 20").index("i");
        
        iterate(echo("index is: ${i}")).condition("(i lt 5) or (i = 5)").index("i");
        
        iterate(echo("index is: ${i}")).condition("(i lt 5) and (i lt 3)").index("i");
        
        iterate(echo("index is: ${i}")).condition("i = 0").index("i");
        
        iterate(echo("index is: ${i}")).condition("${max} gt= i").index("i");
        
        iterate(echo("index is: ${i}")).condition("i lt= 50").index("i")
                                       .startsWith(0)
                                       .step(5);
    }
    
    @Test
    public void iterateITest(ITestContext testContext) {
        executeTest(testContext);
    }
}