/*
 *  Copyright 2006-2016 the original author or authors.
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

package com.consol.citrus.validation.text;

import com.consol.citrus.exceptions.ValidationException;
import com.consol.citrus.message.DefaultMessage;
import com.consol.citrus.message.Message;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import com.consol.citrus.validation.context.DefaultValidationContext;
import com.consol.citrus.validation.context.ValidationContext;
import org.apache.commons.codec.binary.Base64;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 */
public class BinaryBase64MessageValidatorTest extends AbstractTestNGUnitTest {

    private BinaryBase64MessageValidator validator = new BinaryBase64MessageValidator();

    @Test
    public void testBinaryBase64Validation() {
        Message receivedMessage = new DefaultMessage("Hello World!".getBytes());
        Message controlMessage = new DefaultMessage(Base64.encodeBase64String("Hello World!".getBytes()));

        ValidationContext validationContext = new DefaultValidationContext();
        validator.validateMessagePayload(receivedMessage, controlMessage, validationContext, context);
    }

    @Test
    public void testBinaryBase64ValidationNoBinaryData() {
        Message receivedMessage = new DefaultMessage("SGVsbG8gV29ybGQh");
        Message controlMessage = new DefaultMessage(Base64.encodeBase64String("Hello World!".getBytes()));

        ValidationContext validationContext = new DefaultValidationContext();
        validator.validateMessagePayload(receivedMessage, controlMessage, validationContext, context);
    }

    @Test
    public void testBinaryBase64ValidationError() {
        Message receivedMessage = new DefaultMessage("Hello World!".getBytes());
        Message controlMessage = new DefaultMessage(Base64.encodeBase64String("Hello Citrus!".getBytes()));

        ValidationContext validationContext = new DefaultValidationContext();
        try {
            validator.validateMessagePayload(receivedMessage, controlMessage, validationContext, context);
        } catch (ValidationException e) {
            Assert.assertTrue(e.getMessage().contains("expected 'SGVsbG8gQ2l0cnVzIQ=='"));
            Assert.assertTrue(e.getMessage().contains("but was 'SGVsbG8gV29ybGQh'"));
            
            return;
        }
        
        Assert.fail("Missing validation exception due to wrong number of JSON entries");
    }
}
