/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.dmn.client.shape.view.connections;

import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.MultiPathDecorator;
import com.ait.lienzo.client.core.shape.PolyLine;
import com.ait.lienzo.client.core.types.DashArray;
import com.ait.lienzo.shared.core.types.ColorName;

public class KnowledgeRequirementConnection extends Connection {

    static final double DECORATOR_WIDTH = 10;
    static final double DECORATOR_HEIGHT = 10;
    static final double DASHES = 4;
    static final double DASH = 4;

    public KnowledgeRequirementConnection(final double x1,
                                          final double y1,
                                          final double x2,
                                          final double y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    protected MultiPathDecorator createTail() {
        return new MultiPathDecorator(new MultiPath()
                                              .M(0,
                                                 DECORATOR_HEIGHT)
                                              .L(DECORATOR_WIDTH / 2,
                                                 0)
                                              .L(DECORATOR_WIDTH,
                                                 DECORATOR_HEIGHT)
                                              .setFillColor(ColorName.BLACK)
                                              .setFillAlpha(1));
    }

    @Override
    protected void setDashArray(final PolyLine line) {
        line.setDashArray(new DashArray(DASH,
                                        DASHES));
    }
}
