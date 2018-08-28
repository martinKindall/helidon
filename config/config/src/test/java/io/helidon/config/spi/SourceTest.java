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

package io.helidon.config.spi;

import java.util.Optional;

import io.helidon.config.ConfigException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;

/**
 * Tests default methods of {@link Source}.
 */
public class SourceTest {

    @Test
    public void testDescriptionTestSource() {
        Source source = new TestSource();

        assertThat(source.description(), is("Test"));
    }

    @Test
    public void testDescriptionLambda() {
        Source source = Optional::empty;

        assertThat(source.description(), is(source.getClass().getSimpleName()));
    }

    private class TestSource implements Source {
        @Override
        public Optional load() throws ConfigException {
            return Optional.empty();
        }
    }
}
