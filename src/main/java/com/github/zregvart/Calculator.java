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

import org.apache.camel.main.Main;

public final class Calculator {

    public static void main(final String[] args) throws Exception {
        try (Main main = new Main(Configuration.class)) {
            main.addRoutesBuilder(new CalculatorRoute());
            main.run(args);
        }
    }

}
