/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package io.helidon.config.tests.parsers1;

import io.helidon.config.Config;
import io.helidon.config.tests.module.parsers1.Parsers1Priority100ConfigParser;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;

/**
 * Parser tests with custom parser registered.
 */
public class CustomParserPrecedenceTest extends AbstractParserServicesTest {

    @Override
    protected Config.Builder configBuilder() {
        return super.configBuilder()
                .addParser(new Parsers1Priority100ConfigParser());
    }

    @Test
    public void testCustomParserOverride() {
        Config config = configBuilder().build();

        assertThat(config.get(Parsers1Priority100ConfigParser.KEY_PREFIX + KEY).asString(), is(VALUE));
    }

}
