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
package com.github.zregvart.support;

import io.netty.channel.Channel;
import io.netty.handler.logging.LoggingHandler;

import org.apache.camel.component.netty.ClientInitializerFactory;
import org.apache.camel.component.netty.NettyProducer;
import org.apache.camel.component.netty.http.HttpClientInitializerFactory;
import org.apache.camel.component.netty.http.NettyHttpProducer;

public class LoggingClient extends HttpClientInitializerFactory {

    public LoggingClient() {
    }

    public LoggingClient(final NettyHttpProducer producer) {
        super(producer);
    }

    @Override
    public ClientInitializerFactory createPipelineFactory(final NettyProducer producer) {
        assert producer instanceof NettyHttpProducer;
        return new LoggingClient((NettyHttpProducer) producer);
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new LoggingHandler(LoggingClient.class));
        super.initChannel(ch);
    }
}
