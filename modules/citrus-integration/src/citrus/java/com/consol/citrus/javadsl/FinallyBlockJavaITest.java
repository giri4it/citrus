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

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.consol.citrus.dsl.TestNGCitrusTestBuilder;

/**
 * @author Christoph Deppisch
 */
public class FinallyBlockJavaITest extends TestNGCitrusTestBuilder {
    
    @Autowired
    @Qualifier("testDataSource")
    private DataSource dataSource;
    
    @Override
    protected void configure() {
        variable("orderId", "citrus:randomNumber(5)");

        sql(dataSource)
            .statement("INSERT INTO ORDERS (ORDER_ID, REQUEST_TAG, CONVERSATION_ID, CREATION_DATE) VALUES (${orderId},1,1,'citrus:currentDate(dd.MM.yyyy)')");
        
        echo("ORDER creation time: citrus:currentDate('dd.MM.yyyy')");
        
        doFinally(
                sql(dataSource).statement("DELETE FROM ORDERS WHERE ORDER_ID='${orderId}'")
        );
    }
    
    @Test
    public void finallyBlockITest(ITestContext testContext) {
        executeTest(testContext);
    }
}