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

package io.helidon.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Internal utility methods.
 */
final class Utils {

    private Utils() {
    }

    /**
     * Loads the first service implementation or throw an exception if nothing found.
     *
     * @param service the service class to load
     * @param <T>     service type
     * @return the loaded service
     * @throws IllegalStateException if none implementation found
     */
    static <T> T loadSpi(Class<T> service) {
        ServiceLoader<T> servers = ServiceLoader.load(service);
        Iterator<T> serversIt = servers.iterator();
        if (serversIt.hasNext()) {
            return serversIt.next();
        } else {
            throw new IllegalStateException("No implementation found for SPI: " + service.getName());
        }
    }

    /**
     * Tokenize provide {@code text} by {@code separator} char respecting quoted sub-sequences. Quoted sub-sequences are
     * parts of {@code text} which starts and ends by the same {@code quoteChar}.
     *
     * @param separator          a token separator.
     * @param quoteChars         characters which can be used for quoting. Quoted part must start and ends with the same
     *                           character.
     * @param includeEmptyTokens return also tokens with {@code length == 0}.
     * @param text               a text to be tokenized.
     * @return A list of tokens without separator characters.
     */
    static List<String> tokenize(char separator, String quoteChars, boolean includeEmptyTokens, String text) {
        char[] quotes = quoteChars == null ? new char[0] : quoteChars.toCharArray();
        StringBuilder token = new StringBuilder();
        List<String> result = new ArrayList<>();
        boolean quoted = false;
        char lastQuoteCharacter = ' ';
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (quoted) {
                if (ch == lastQuoteCharacter) {
                    quoted = false;
                }
                token.append(ch);
            } else {
                if (ch == separator) {
                    if (includeEmptyTokens || token.length() > 0) {
                        result.add(token.toString());
                    }
                    token.setLength(0);
                } else {
                    for (char quote : quotes) {
                        if (ch == quote) {
                            quoted = true;
                            lastQuoteCharacter = ch;
                            break;
                        }
                    }
                    token.append(ch);
                }
            }
        }
        if (includeEmptyTokens || token.length() > 0) {
            result.add(token.toString());
        }
        return result;
    }

    /**
     * Appends the content of the given byte buffer into the given output stream.
     *
     * @param out        the stream where to append the byte buffer
     * @param byteBuffer the byte buffer to append to the stream
     * @throws IOException in case of an IO problem
     */
    static void write(ByteBuffer byteBuffer, OutputStream out) throws IOException {
        if (byteBuffer.hasArray()) {
            out.write(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
        } else {
            byte[] buff = new byte[byteBuffer.remaining()];
            byteBuffer.get(buff);
            out.write(buff);
        }
    }
}
