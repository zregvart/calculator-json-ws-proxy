[![Docker Repository on Quay](https://quay.io/repository/zregvart/calculator-json-ws-proxy/status "Docker Repository on Quay")](https://quay.io/repository/zregvart/calculator-json-ws-proxy) [![Docker Repository on Docker Hub](https://img.shields.io/docker/automated/zregvart/calculator-json-ws-proxy.svg "Docker Repository on Docker Hub")](https://hub.docker.com/r/zregvart/calculator-json-ws-proxy) [![CircleCI](https://circleci.com/gh/zregvart/calculator-json-ws-proxy.svg?style=svg)](https://circleci.com/gh/zregvart/calculator-json-ws-proxy)

# Camel Calculator REST/JSON to SOAP-WS proxy

This is an example of using the [Apache Camel](https://camel.apache.org/)
integration framework to create a HTTP proxy using the [Camel Netty](https://camel.apache.org/components/latest/netty-http-component.html)
component.

It's meant to act as a HTTP proxy in front of the [Calculator WebService](http://www.dneonline.com/calculator.asmx).

The request payload received as JSON:

```json
{
  "subtract": {
    "intA":3,
    "intB":1
  }
}
```

will be unmarshalled using [Jackson JSON data format](https://camel.apache.org/components/latest/json-jackson-dataformat.html) marshalled to the SOAP envelope using [SOAP JAXB data format](https://camel.apache.org/components/latest/soapjaxb-dataformat.html) like:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Envelope xmlns="http://www.w3.org/2003/05/soap-envelope" xmlns:ns2="http://tempuri.org/">
    <Body>
        <ns2:Subtract>
            <ns2:intA>3</ns2:intA>
            <ns2:intB>1</ns2:intB>
        </ns2:Subtract>
    </Body>
</Envelope>
```

The address (URL) of the backend is taken from the HTTP (PROXY) request, the response of which is unmarshaled from SOAP and marshalled back to JSON to be returned as the HTTP response from the proxy.

There is no support for HTTP over TLS (`https`) protocol in this example.

## Building and running

To build the Docker image execute:

    $ docker build -t calculator-json-ws-proxy .

To run the Docker image execute:

    $ docker run --rm -p 8080:8080 calculator-json-ws-proxy

To test using `curl` set the `http_proxy` environment variable, for example:

    $ http_proxy=http://localhost:8080 curl -d '{"subtract": {"intA":3, "intB":1}}' http://www.dneonline.com/calculator.asmx
    {"SubtractResult":2}

## Running on OpenShift

To run on openshift, log in via the `oc login` command to the OpenShift cluster
and position yourself in the project of choice using `oc project <project>` and
run:

    $ ./mvnw -Popenshift package
