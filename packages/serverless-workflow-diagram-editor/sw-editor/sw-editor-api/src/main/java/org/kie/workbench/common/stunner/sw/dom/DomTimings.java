/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.sw.dom;

public class DomTimings {

    public static final String SWF_VIEWER_E2E = "SWF_VIEWER_E2E";
    public static final String TRANSFORM = "TRANSFORM";
    public static final String OPEN = "OPEN";
    public static final String PARSE = "PARSE";
    public static final String UNMARSHALL = "UNMARSHALL";
    public static final String AUTOLAYOUT = "AUTOLAYOUT";

    public static void time(String label) {
        DomConsole.getInstance().time(label);
    }

    public static void timeEnd(String label) {
        DomConsole.getInstance().timeEnd(label);
    }

}