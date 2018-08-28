/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package io.helidon.microprofile.health.checks;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

/**
 * A health check that verifies whether the server is running out of Java heap space. If heap usage exceeds a
 * specified threshold, then the health check will fail.
 *
 * By default, this health check has a threshold of .98 (98%). If heap usage exceeds this level, then the server
 * is considered to be unhealthy. This default can be modified using the
 * {@code healthCheck.heapMemory.thresholdPercent} property. The threshold should be set to a fraction, such as
 * .50 for 50% or .99 for 99%.
 *
 * This health check is automatically created and registered through CDI.
 *
 * This health check can be referred to in properties as "heapMemory". So for example, to exclude this
 * health check from being exposed, use "helidon.health.exclude: heapMemory".
 */
@Health
@ApplicationScoped
public final class HeapMemoryHealthCheck implements HealthCheck {
    private final Runtime rt;
    private final double thresholdPercent;

    @Inject
    HeapMemoryHealthCheck(
            Runtime runtime,
            @ConfigProperty(name = "helidon.health.heapMemory.thresholdPercent", defaultValue = ".98") double threshold) {
        this.thresholdPercent = threshold;
        this.rt = runtime;
    }

    @Override
    public HealthCheckResponse call() {
        final long freeMemory = rt.freeMemory();
        final long totalMemory = rt.totalMemory();
        final long maxMemory = rt.maxMemory();
        final long usedMemory = totalMemory - freeMemory;
        final long threshold = (long) (thresholdPercent * maxMemory);
        return HealthCheckResponse.named("heapMemory")
                .state(threshold >= usedMemory)
                .withData("percentFree", String.format("%.2f%%", 100 * ((double) (maxMemory - usedMemory) / maxMemory)))
                .withData("free", DiskSpaceHealthCheck.format(freeMemory))
                .withData("freeBytes", freeMemory)
                .withData("max", DiskSpaceHealthCheck.format(maxMemory))
                .withData("maxBytes", maxMemory)
                .withData("total", DiskSpaceHealthCheck.format(totalMemory))
                .withData("totalBytes", totalMemory)
                .build();
    }
}
