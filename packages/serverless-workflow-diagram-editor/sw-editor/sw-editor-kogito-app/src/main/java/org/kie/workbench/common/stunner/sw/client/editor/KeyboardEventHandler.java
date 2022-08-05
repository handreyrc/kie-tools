/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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
package org.kie.workbench.common.stunner.sw.client.editor;

import javax.enterprise.context.Dependent;

import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Window;
import org.kie.workbench.common.stunner.client.lienzo.canvas.LienzoPanel;
import org.kie.workbench.common.stunner.client.lienzo.canvas.wires.WiresCanvas;
import org.kie.workbench.common.stunner.client.lienzo.canvas.wires.WiresCanvasView;
import org.kie.workbench.common.stunner.client.widgets.editor.StunnerEditor;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvas;
import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.CanvasHandler;
import org.kie.workbench.common.stunner.core.client.canvas.controls.SelectionControl;
import org.kie.workbench.common.stunner.core.client.session.impl.ViewerSession;
import org.kie.workbench.common.stunner.core.client.shape.Shape;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.graph.Element;

//Handrey PoC
@Dependent
public class KeyboardEventHandler {

    private StunnerEditor stunnerEditor;

    private EventListener keyUpEventListener;
    private EventListener keyDownEventListener;
    private EventListener keyPressEventListener;

    static final String ON_KEY_DOWN = "keydown";
    static final String ON_KEY_UP = "keyup";
    static final String ON_KEY_PRESS = "keypress";

    public Diagram getDiagram() {
        return stunnerEditor.getDiagram();
    }

    public CanvasHandler getCanvasHandler() {
        return stunnerEditor.getSession().getCanvasHandler();
    }

    public AbstractCanvas getCanvas() {
        return (AbstractCanvas) getCanvasHandler().getCanvas();
    }

    public HTMLDivElement getPanelElement() {
        final LienzoPanel panel = (LienzoPanel) getCanvas().getView().getPanel();
        return panel.getView().getElement();
    }

    public elemental2.dom.Element getPanelParentElement() {
        final LienzoPanel panel = (LienzoPanel) getCanvas().getView().getPanel();
        final elemental2.dom.Element parentElement = panel.getView().getElement().parentElement
                .parentElement
                .parentElement; //canvasPanel


        DomGlobal.console.log("ID: " + parentElement.id);

        return parentElement;
    }

    public HTMLDivElement getLayerElement() {
        final WiresCanvas wiresCanvas = (WiresCanvas) getCanvas();
        final WiresCanvasView view = wiresCanvas.getView();
        return view.getLayer().getTopLayer().getViewport().getElement();
    }

    public Window getWindowElement() {
        return DomGlobal.window;
    }

    void addKeyDownHandler(final EventListener keyDownEventListener) {
        if (null != keyDownEventListener) {
            this.keyDownEventListener = keyDownEventListener;
            getPanelParentElement().addEventListener(ON_KEY_DOWN, this.keyDownEventListener);
            DomGlobal.console.log("Keydown added");
        }
    }

    void addKeyUpHandler(final EventListener keyUpEventListener) {
        if (null != keyUpEventListener) {
            this.keyUpEventListener = keyUpEventListener;
            getPanelParentElement().addEventListener(ON_KEY_UP, this.keyUpEventListener);
            DomGlobal.console.log("Keyup added");
        }
    }

    void addKeyPressHandler(final EventListener keyPressEventListener) {
        if (null != keyPressEventListener) {
            this.keyPressEventListener = keyPressEventListener;
            getPanelParentElement().addEventListener(ON_KEY_PRESS, this.keyPressEventListener);
            DomGlobal.console.log("Keypress added");
        }
    }

    public void initKeyHandlers(StunnerEditor stunnerEditor) {
        destroy();

        this.stunnerEditor = stunnerEditor;

        addKeyDownHandler(evt -> {
            DomGlobal.console.log("KEYDOWN EVENT");
        });
        addKeyUpHandler(evt -> {
            DomGlobal.console.log("KEYUP EVENT");
        });
        addKeyPressHandler(evt -> {
            DomGlobal.console.log("KEYPRESS EVENT");
        });

        DomGlobal.console.log("Events added!");
    }

    public Shape getSelectedShape() {
        ViewerSession session = (ViewerSession) stunnerEditor.getSession();
        SelectionControl<AbstractCanvasHandler, Element> selectionControl = session.getSelectionControl();

        if (!selectionControl.getSelectedItems().isEmpty()) {
            String uuid = selectionControl.getSelectedItems().iterator().next();
            return getCanvas().getShape(uuid);
        }

        return null;
    }

    public void destroy() {
        if (null != keyUpEventListener) {
            getPanelElement().removeEventListener(ON_KEY_UP, keyUpEventListener);
            keyUpEventListener = null;
        }
        if (null != keyDownEventListener) {
            getPanelElement().removeEventListener(ON_KEY_DOWN, keyDownEventListener);
            keyDownEventListener = null;
        }
        if (null != keyPressEventListener) {
            getPanelElement().removeEventListener(ON_KEY_PRESS, keyPressEventListener);
            keyPressEventListener = null;
        }
    }
}
