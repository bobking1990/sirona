/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.monitoring.reporting.web.plugin;

import org.apache.commons.monitoring.configuration.Configuration;
import org.apache.commons.monitoring.reporting.web.handler.internal.EndpointInfo;
import org.apache.commons.monitoring.reporting.web.handler.internal.Invoker;

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

public final class PluginRepository {
    public static Collection<PluginInfo> PLUGIN_INFO = new CopyOnWriteArrayList<PluginInfo>();

    static {
        for (final Plugin plugin : ServiceLoader.load(Plugin.class, Plugin.class.getClassLoader())) {
            final String name = plugin.name();
            if (name == null) {
                throw new IllegalArgumentException("plugin name can't be null");
            }
            if (!Configuration.is(name + "activated", true)) {
                continue;
            }

            final String mapping = plugin.mapping();
            final Class<?> handler = plugin.endpoints();
            if (mapping != null) {
                PLUGIN_INFO.add(new PluginInfo(mapping, name, EndpointInfo.build(handler, plugin.name(), plugin.mapping())));
            }
        }
    }

    private PluginRepository() {
        // no-op
    }

    public static class PluginInfo {
        private final String url;
        private final String name;
        private final EndpointInfo info;

        public PluginInfo(final String url, String name, final EndpointInfo info) {
            this.url = url;
            this.name = name;
            this.info = info;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        public Map<Pattern, Invoker> getInvokers() {
            return info.getInvokers();
        }
    }
}
