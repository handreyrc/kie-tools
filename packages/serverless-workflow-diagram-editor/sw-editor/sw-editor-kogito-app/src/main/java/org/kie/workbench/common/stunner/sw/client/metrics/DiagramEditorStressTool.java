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

    @Inject
    public DiagramEditorStressTool(Promises promises, DiagramEditor editor) {
        this.promises = promises;
        this.editor = editor;
    }

    public void run() {
        setContentByStatesCount(new int[]{1, 1, 2, 5, 10, 25, 50, 100, 150, 200, 250, 300, 600});
    }

    private void setContentByStatesCount(int[] iterations) {
        DomGlobal.console.log("STRESSING...");
        setContentByStatesCount(iterations, 0);
    }

    private void setContentByStatesCount(int[] iterations, final int index) {
        if (index < iterations.length) {
            final int count = iterations[index];
            setContentByStatesCount(index, count)
                    .then(unused -> {
                        setContentByStatesCount(iterations, index + 1);
                        return null;
                    }, e -> {
                        DomGlobal.console.error("STRESS ERROR during workflow[" + index + "]: " + e);
                        return null;
                    });
        } else {
            DomGlobal.console.log("STRESS COMPLETED");
        }
    }

    private Promise<Void> setContentByStatesCount(final int i, final int count) {
        return promises.create((success, failure) -> {
            DomGlobal.console.log("SetContent [i=" + i + ", count=" + count + "]");
            String raw = workflow(count);
            editor
                    .setNewContent("workflow" + i + ".sw", raw)
                    .then(o -> {
                        DomGlobal.console.log("SetContent COMPLETED [i=" + i + ", count=" + count + "]");
                        editor.onClose();
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