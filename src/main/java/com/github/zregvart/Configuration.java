/*
 * Copyright (C) 2016 Red Hat, Inc.
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
package com.github.zregvart;

import org.apache.camel.BindToRegistry;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {

    public static final class Service {
        private static final Logger LOG = LoggerFactory.getLogger(Service.class);

        public String hostname(final Exchange exchange) {
            final String envHostname = System.getenv("SERVICE_HOSTNAME");
            if (envHostname != null) {
                LOG.debug("Using hostname from environment: {}", envHostname);
                return envHostname;
            }

            final Message message = exchange.getMessage();
            final String headerHostname = message.getHeader("Host", String.class);
            if (headerHostname != null) {
                LOG.debug("Using hostname from the Host HTTP header: {}", headerHostname);
                return headerHostname;
            }

            final String proxyHostname = message.getHeader(Exchange.HTTP_HOST, String.class);
            LOG.debug("Using hostname from the proxy request: {}", proxyHostname);
            return proxyHostname;
        }
    }

    @BindToRegistry
    public Service svc() {
        return new Service();
    }
}
