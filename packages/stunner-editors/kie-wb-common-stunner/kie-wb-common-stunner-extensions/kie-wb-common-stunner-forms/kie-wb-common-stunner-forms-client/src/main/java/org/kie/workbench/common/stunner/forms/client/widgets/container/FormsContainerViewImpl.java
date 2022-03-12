/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.forms.client.widgets.container;

import javax.inject.Inject;

import org.jboss.errai.common.client.dom.DOMUtil;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.stunner.forms.client.widgets.container.displayer.FormDisplayer;

@Templated
public class FormsContainerViewImpl implements FormsContainerView,
                                               IsElement {

    @Inject
    @DataField
    private Div content;

    @Override
    public void addDisplayer(FormDisplayer displayer) {
        content.appendChild(displayer.getElement());
    }

    @Override
    public void clear() {
        DOMUtil.removeAllChildren(content);
    }

    @Override
    public void removeDisplayer(FormDisplayer displayer) {
        DOMUtil.removeFromParent(displayer.getElement());
    }
}
