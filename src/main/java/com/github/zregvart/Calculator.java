package com.github.zregvart;

import org.apache.camel.main.Main;

public final class Calculator {

    public static void main(final String[] args) throws Exception {
        try (Main main = new Main()) {
            main.addRoutesBuilder(new CalculatorRoute());
            main.run(args);
        }
    }

}
