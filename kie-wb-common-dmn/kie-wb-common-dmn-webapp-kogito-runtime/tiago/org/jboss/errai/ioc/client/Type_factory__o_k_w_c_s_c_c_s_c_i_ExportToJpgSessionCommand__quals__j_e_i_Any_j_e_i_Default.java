package org.jboss.errai.ioc.client;

import javax.enterprise.context.Dependent;
import org.jboss.errai.ioc.client.container.ContextManager;
import org.jboss.errai.ioc.client.container.Factory;
import org.jboss.errai.ioc.client.container.FactoryHandleImpl;
import org.kie.workbench.common.stunner.core.client.canvas.controls.CanvasControl.SessionAware;
import org.kie.workbench.common.stunner.core.client.canvas.util.CanvasFileExport;
import org.kie.workbench.common.stunner.core.client.session.command.AbstractClientSessionCommand;
import org.kie.workbench.common.stunner.core.client.session.command.ClientSessionCommand;
import org.kie.workbench.common.stunner.core.client.session.command.impl.AbstractExportSessionCommand;
import org.kie.workbench.common.stunner.core.client.session.command.impl.ExportToJpgSessionCommand;

public class Type_factory__o_k_w_c_s_c_c_s_c_i_ExportToJpgSessionCommand__quals__j_e_i_Any_j_e_i_Default extends Factory<ExportToJpgSessionCommand> { public Type_factory__o_k_w_c_s_c_c_s_c_i_ExportToJpgSessionCommand__quals__j_e_i_Any_j_e_i_Default() {
    super(new FactoryHandleImpl(ExportToJpgSessionCommand.class, "Type_factory__o_k_w_c_s_c_c_s_c_i_ExportToJpgSessionCommand__quals__j_e_i_Any_j_e_i_Default", Dependent.class, false, null, true));
    handle.setAssignableTypes(new Class[] { ExportToJpgSessionCommand.class, AbstractExportSessionCommand.class, AbstractClientSessionCommand.class, Object.class, ClientSessionCommand.class, SessionAware.class });
  }

  public ExportToJpgSessionCommand createInstance(final ContextManager contextManager) {
    final CanvasFileExport _canvasFileExport_0 = (CanvasFileExport) contextManager.getInstance("Type_factory__o_k_w_c_s_c_c_c_u_CanvasFileExport__quals__j_e_i_Any_j_e_i_Default");
    final ExportToJpgSessionCommand instance = new ExportToJpgSessionCommand(_canvasFileExport_0);
    setIncompleteInstance(instance);
    setIncompleteInstance(null);
    return instance;
  }
}