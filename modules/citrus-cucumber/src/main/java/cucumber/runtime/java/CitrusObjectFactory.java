/*
 * Copyright 2006-2016 the original author or authors.
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

package cucumber.runtime.java;

import com.consol.citrus.annotations.CitrusAnnotations;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.annotations.CitrusDslAnnotations;
import com.consol.citrus.dsl.design.DefaultTestDesigner;
import com.consol.citrus.dsl.design.TestDesigner;
import com.consol.citrus.dsl.runner.DefaultTestRunner;
import com.consol.citrus.dsl.runner.TestRunner;
import cucumber.runtime.CucumberException;

/**
 * @author Christoph Deppisch
 * @since 2.6
 */
public class CitrusObjectFactory extends DefaultJavaObjectFactory {

    /** Test designer */
    private TestDesigner designer;

    /** Test runner */
    private TestRunner runner;

    /** Test context */
    private TestContext context;

    /** Static self reference */
    private static CitrusObjectFactory selfReference;

    /** Mode to use for injection */
    private InjectionMode mode = null;

    /**
     * Default constructor with static self reference initialization.
     */
    public CitrusObjectFactory() {
        selfReference = this;
    }

    @Override
    public boolean addClass(Class<?> clazz) {
        InjectionMode requiredMode = InjectionMode.analyseMode(clazz, mode);
        if (mode == null) {
            mode = requiredMode;
        } else if (!mode.equals(requiredMode)) {
            throw new CucumberException("Illegal mix of test designer and runner mode within test run");
        }

        return super.addClass(clazz);
    }

    @Override
    public void start() {
        super.start();
        context = CitrusBackend.getCitrus().createTestContext();

        if (mode == null) {
            mode = InjectionMode.valueOf(System.getProperty("citrus.cucumber.injection.mode", InjectionMode.DESIGNER.name()));
        }

        if (InjectionMode.DESIGNER.equals(mode)) {
            designer = new DefaultTestDesigner(CitrusBackend.getCitrus().getApplicationContext(), context);
        }

        if (InjectionMode.RUNNER.equals(mode)) {
            runner = new DefaultTestRunner(CitrusBackend.getCitrus().getApplicationContext(), context);
        }
    }

    @Override
    public <T> T getInstance(Class<T> type) {
        if (CitrusObjectFactory.class.isAssignableFrom(type)) {
            return (T) this;
        }

        T instance = super.getInstance(type);
        CitrusAnnotations.injectAll(instance, CitrusBackend.getCitrus());

        if (InjectionMode.DESIGNER.equals(mode)) {
            CitrusDslAnnotations.injectTestDesigner(instance, designer);
        }

        if (InjectionMode.RUNNER.equals(mode)) {
            CitrusDslAnnotations.injectTestRunner(instance, runner);
        }

        return instance;
    }

    /**
     * Static access to self reference.
     * @return
     */
    public static CitrusObjectFactory instance() throws IllegalAccessException {
        if (selfReference == null) {
            throw new IllegalAccessException("Illegal access to self reference - not available yet");
        }

        return selfReference;
    }
}
