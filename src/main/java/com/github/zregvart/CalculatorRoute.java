/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zregvart;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;

public final class CalculatorRoute extends RouteBuilder {

    @Override
    public void configure() {
        final JacksonDataFormat json = new JacksonDataFormat(Instruction.class);

        final SoapJaxbDataFormat soap = new SoapJaxbDataFormat("org.tempuri");
        soap.setVersion("1.2");

        from("netty-http:proxy://0.0.0.0:8080")
            .unmarshal(json)
            .setHeader("Content-Type", constant("application/soap+xml; charset=\"utf-8\""))
            .setHeader("SOAPAction", simple("${body.soapAction()}"))
            .setBody(simple("${body.operation()}"))
            .marshal(soap)
            .log("${body}")
            .toD("netty-http:"
                + "${header." + Exchange.HTTP_SCHEME + "}://"
                + "${bean:svc?method=hostname}:"
                + "${header." + Exchange.HTTP_PORT + "}"
                + "${header." + Exchange.HTTP_PATH + "}")
            .unmarshal(soap)
            .log("${body}")
            .marshal(json);
    }

}
