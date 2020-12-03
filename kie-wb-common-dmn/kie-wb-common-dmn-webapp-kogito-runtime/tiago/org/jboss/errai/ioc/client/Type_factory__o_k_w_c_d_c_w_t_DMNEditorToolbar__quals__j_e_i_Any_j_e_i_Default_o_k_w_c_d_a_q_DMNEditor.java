package org.jboss.errai.ioc.client;

import java.lang.annotation.Annotation;
import javax.enterprise.context.Dependent;
import org.jboss.errai.ioc.client.container.ContextManager;
import org.jboss.errai.ioc.client.container.Factory;
import org.jboss.errai.ioc.client.container.FactoryHandleImpl;
import org.kie.workbench.common.dmn.api.qualifiers.DMNEditor;
import org.kie.workbench.common.dmn.client.widgets.toolbar.DMNEditorToolbar;
import org.kie.workbench.common.stunner.client.widgets.toolbar.Toolbar;
import org.kie.workbench.common.stunner.client.widgets.toolbar.impl.EditorToolbar;
import org.kie.workbench.common.stunner.client.widgets.toolbar.impl.ManagedToolbar;
import org.kie.workbench.common.stunner.client.widgets.toolbar.impl.ManagedToolbarDelegate;
import org.kie.workbench.common.stunner.core.client.session.impl.EditorSession;

public class Type_factory__o_k_w_c_d_c_w_t_DMNEditorToolbar__quals__j_e_i_Any_j_e_i_Default_o_k_w_c_d_a_q_DMNEditor extends Factory<DMNEditorToolbar> { public Type_factory__o_k_w_c_d_c_w_t_DMNEditorToolbar__quals__j_e_i_Any_j_e_i_Default_o_k_w_c_d_a_q_DMNEditor() {
    super(new FactoryHandleImpl(DMNEditorToolbar.class, "Type_factory__o_k_w_c_d_c_w_t_DMNEditorToolbar__quals__j_e_i_Any_j_e_i_Default_o_k_w_c_d_a_q_DMNEditor", Dependent.class, false, null, true));
    handle.setAssignableTypes(new Class[] { DMNEditorToolbar.class, ManagedToolbarDelegate.class, Object.class, Toolbar.class, EditorToolbar.class });
    handle.setQualifiers(new Annotation[] { QualifierUtil.ANY_ANNOTATION, QualifierUtil.DEFAULT_ANNOTATION, new DMNEditor() {
        public Class annotationType() {
          return DMNEditor.class;
        }
        public String toString() {
          return "@org.kie.workbench.common.dmn.api.qualifiers.DMNEditor()";
        }
    } });
  }

  public DMNEditorToolbar createInstance(final ContextManager contextManager) {
    final ManagedToolbar<EditorSession> _toolbar_0 = (ManagedToolbar) contextManager.getInstance("Type_factory__o_k_w_c_s_c_w_t_i_ManagedToolbar__quals__j_e_i_Any_j_e_i_Default");
    final DMNEditorToolbar instance = new DMNEditorToolbar(_toolbar_0);
    registerDependentScopedReference(instance, _toolbar_0);
    setIncompleteInstance(instance);
    setIncompleteInstance(null);
    return instance;
  }

  public void invokePostConstructs(final DMNEditorToolbar instance) {
    instance.init();
  }
}