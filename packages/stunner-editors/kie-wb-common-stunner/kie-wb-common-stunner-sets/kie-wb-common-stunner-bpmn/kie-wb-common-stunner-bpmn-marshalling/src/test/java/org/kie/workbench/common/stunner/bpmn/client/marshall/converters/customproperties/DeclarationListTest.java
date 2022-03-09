/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.client.marshall.converters.customproperties;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DeclarationListTest {

    @Test
    public void testNullSafeBuilderMethod() {
        DeclarationList result = DeclarationList.fromString("");
        assertNotNull(result);
        assertTrue(result.getDeclarations().isEmpty());
        result = DeclarationList.fromString(null);
        assertNotNull(result);
        assertTrue(result.getDeclarations().isEmpty());
    }
}
