/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.client.workbench.widgets.menu.megamenu.menuitem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.uberfire.mvp.Command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChildMenuItemPresenterTest {

    @Mock
    private ChildMenuItemPresenter.View view;

    @InjectMocks
    private ChildMenuItemPresenter presenter;

    @Test
    public void initTest() {
        presenter.init();

        verify(view).init(presenter);
    }

    @Test
    public void setupAndSelectTest() {
        final String label = "label";
        final Command command = mock(Command.class);
        presenter.setup(label,
                        command);

        verify(view).setLabel(label);
        verify(view).setCommand(command);

        presenter.select();

        verify(view).select();
    }

    @Test
    public void enableTest() {
        presenter.enable();

        verify(view).enable();
    }

    @Test
    public void disableTest() {
        presenter.disable();

        verify(view).disable();
    }
}
