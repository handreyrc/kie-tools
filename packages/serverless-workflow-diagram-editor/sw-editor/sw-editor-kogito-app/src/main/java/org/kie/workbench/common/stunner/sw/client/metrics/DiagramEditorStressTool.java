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

package org.kie.workbench.common.stunner.sw.client.metrics;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import elemental2.dom.DomGlobal;
import elemental2.promise.Promise;
import org.kie.workbench.common.stunner.sw.client.editor.DiagramEditor;
import org.uberfire.client.promise.Promises;

@ApplicationScoped
public class DiagramEditorStressTool {

    private final Promises promises;
    private final DiagramEditor editor;
    private String raw;
    private int operation;

    private static int updateCounter;
    private static final double DELAY = 10000D;
    private static final int NUMBER_OF_NODES = 600;

    @Inject
    public DiagramEditorStressTool(Promises promises, DiagramEditor editor) {
        this.promises = promises;
        this.editor = editor;
        this.raw = null;
        this.operation = 0;
        updateCounter = 0;
    }

    public void run() {
        // Enter number of node and number of refreshes
        run(NUMBER_OF_NODES);
    }

    //Step1
    private void run(int numberOfNodes) {

        if (0 == operation) { // Open
            DomGlobal.console.log("Opening with " + numberOfNodes + " nodes...");
            setContentByStatesCount(numberOfNodes);
            operation = 1; //move to next
        } else if (1 == operation) { // 1 update
            DomGlobal.console.log("Update 1x...");
            setContentByStatesCount(numberOfNodes);
            operation = 2; //move to next
        } else if (2 == operation) { // 50 updates
            DomGlobal.console.log("Update 50x...");
            for (int i = 0; i < 49 + 1; i++) {
                DomGlobal.setTimeout(x -> setContentByStatesCount(numberOfNodes), (i + 1) * DELAY);
            }
            operation = 3; //move to next
        } else if (3 == operation) { //100 updates
            DomGlobal.console.log("Update 100x...");
            for (int i = 0; i < 50 + 1; i++) {
                DomGlobal.setTimeout(x -> setContentByStatesCount(numberOfNodes), (i + 1) * DELAY);
            }
            operation = 4; //move to next
        } else if (4 == operation) { //300 updates
            DomGlobal.console.log("Update 300x...");
            for (int i = 0; i < 200 + 1; i++) {
                DomGlobal.setTimeout(x -> setContentByStatesCount(numberOfNodes), (i + 1) * DELAY);
            }
            operation = 5; //move to next
        } else {
            DomGlobal.console.log("Test finished reload the page to start it over");
        }
    }

    //check for closing and opening a new diagram
    private Promise<Void> setContentByStatesCount(int numberOfNodes) {
        return promises.create((success, failure) -> {
            //DomGlobal.console.log("SetContent [numberOfNodes=" + numberOfNodes + "]");
            if (null == raw) {
                raw = workflow(numberOfNodes);
            }

            editor
                    .setContent("workflow.sw", raw)
                    .then(o -> {
                        //DomGlobal.console.log("SetContent COMPLETED [numberOfNodes=" + numberOfNodes + "]");
                        //editor.onClose(); // do not close in order to test the refresh

                        if (0 == updateCounter) {
                            DomGlobal.console.log("Open done!");
                        } else if (1 == updateCounter) {
                            DomGlobal.console.log("1x update done!");
                        } else if (50 == updateCounter) {
                            DomGlobal.console.log("50x update done!");
                        } else if (100 == updateCounter) {
                            DomGlobal.console.log("100x update done!");
                        } else if (300 == updateCounter) {
                            DomGlobal.console.log("300x update done!");
                        }

                        updateCounter++;
                        success.onInvoke((Void) null);
                        return null;
                    }, o -> {
                        DomGlobal.console.error("SOME ERROR during SetContent: " + o);
                        failure.onInvoke(o);
                        return null;
                    });
        });
    }

    public static final String WORKFLOW_START = "{\n" +
            " \"id\": \"workflow\",\n" +
            " \"version\": \"1.0\",\n" +
            " \"specVersion\": \"0.8\",\n" +
            " \"name\": \"Workflow\",\n" +
            " \"description\": \"Workflow\",\n" +
            " \"states\": [";

    public static final String WORKFLOW_END = " ]\n" +
            "}";

    public static String workflow(int count) {
        String raw = WORKFLOW_START + injectStates("state", count) + WORKFLOW_END;
        return raw;
    }

    public static String injectStates(String name, int count) {
        String result = "";
        for (int i = 0; i < count; i++) {
            String sname = name + "_" + i;
            boolean isLast = i == (count - 1);
            String transition = !isLast ? name + "_" + (i + 1) : "";
            String state = injectState(sname, transition, isLast);
            if (!isLast) {
                state += ",\n";
            }
            result += state;
        }
        return result;
    }

    public static String injectState(String name,
                                     String transition,
                                     boolean end) {
        return "{\n" +
                "   \"name\": \"" + name + "\",\n" +
                "   \"type\": \"inject\",\n" +
                "   \"transition\": \"" + (null != transition ? transition : "") + "\",\n" +
                "   \"end\": " + end + "\n" +
                "  }";
    }
}
