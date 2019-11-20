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

import org.apache.camel.component.netty.NettyConsumer;
import org.apache.camel.component.netty.ServerInitializerFactory;
import org.apache.camel.component.netty.http.HttpServerInitializerFactory;
import org.apache.camel.component.netty.http.NettyHttpConsumer;

public class LoggingServer extends HttpServerInitializerFactory {

    public LoggingServer() {
    }

    public LoggingServer(final NettyHttpConsumer consumer) {
        super(consumer);
    }

    @Override
    public ServerInitializerFactory createPipelineFactory(final NettyConsumer consumer) {
        assert consumer instanceof NettyHttpConsumer;
        return new LoggingServer((NettyHttpConsumer) consumer);
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(new LoggingHandler(LoggingServer.class));
        super.initChannel(ch);
    }
}
