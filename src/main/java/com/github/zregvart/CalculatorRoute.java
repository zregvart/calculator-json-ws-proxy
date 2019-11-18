package com.github.zregvart;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
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
            .process(CalculatorRoute::operation)
            .marshal(soap)
            .log("${body}")
            .setHeader("Content-Type", constant("application/soap+xml; charset=\"utf-8\""))
            .toD("netty-http:"
                + "${headers." + Exchange.HTTP_SCHEME + "}://"
                + "${headers." + Exchange.HTTP_HOST + "}:"
                + "${headers." + Exchange.HTTP_PORT + "}"
                + "${headers." + Exchange.HTTP_PATH + "}")
            .unmarshal(soap)
            .log("${body}")
            .marshal(json);
    }

    public static void operation(final Exchange exchange) {
        final Message message = exchange.getMessage();
        final Instruction instruction = message.getBody(Instruction.class);

        final Object operation = instruction.operation();

        message.setBody(operation);
    }
}