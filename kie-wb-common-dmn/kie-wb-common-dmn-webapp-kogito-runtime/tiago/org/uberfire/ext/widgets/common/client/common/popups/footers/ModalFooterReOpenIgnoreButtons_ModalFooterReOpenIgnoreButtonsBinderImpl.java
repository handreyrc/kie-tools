// .ui.xml template last modified: 1607021623860
package org.uberfire.ext.widgets.common.client.common.popups.footers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiBinderUtil;
import com.google.gwt.user.client.ui.Widget;

public class ModalFooterReOpenIgnoreButtons_ModalFooterReOpenIgnoreButtonsBinderImpl implements UiBinder<com.google.gwt.user.client.ui.Widget, org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons>, org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons.ModalFooterReOpenIgnoreButtonsBinder {


  public com.google.gwt.user.client.ui.Widget createAndBindUi(final org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons owner) {


    return new Widgets(owner).get_f_FlowPanel1();
  }

  /**
   * Encapsulates the access to all inner widgets
   */
  class Widgets {
    private final org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons owner;


    final com.google.gwt.event.dom.client.ClickHandler handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames1 = new com.google.gwt.event.dom.client.ClickHandler() {
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        owner.onActionButtonClick((com.google.gwt.event.dom.client.ClickEvent) event);
      }
    };

    final com.google.gwt.event.dom.client.ClickHandler handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames2 = new com.google.gwt.event.dom.client.ClickHandler() {
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        owner.onIgnoreButtonClick((com.google.gwt.event.dom.client.ClickEvent) event);
      }
    };

    public Widgets(final org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons owner) {
      this.owner = owner;
    }


    /**
     * Getter for clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay called 0 times. Type: GENERATED_BUNDLE. Build precedence: 1.
     */
    private org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons_ModalFooterReOpenIgnoreButtonsBinderImpl_GenBundle get_clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay() {
      return build_clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay();
    }
    private org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons_ModalFooterReOpenIgnoreButtonsBinderImpl_GenBundle build_clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay() {
      // Creation section.
      final org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons_ModalFooterReOpenIgnoreButtonsBinderImpl_GenBundle clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay = (org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons_ModalFooterReOpenIgnoreButtonsBinderImpl_GenBundle) GWT.create(org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterReOpenIgnoreButtons_ModalFooterReOpenIgnoreButtonsBinderImpl_GenBundle.class);
      // Setup section.

      return clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay;
    }

    /**
     * Getter for i18nCommon called 1 times. Type: IMPORTED. Build precedence: 1.
     */
    private org.uberfire.ext.widgets.common.client.resources.i18n.CommonConstants get_i18nCommon() {
      return build_i18nCommon();
    }
    private org.uberfire.ext.widgets.common.client.resources.i18n.CommonConstants build_i18nCommon() {
      // Creation section.
      final org.uberfire.ext.widgets.common.client.resources.i18n.CommonConstants i18nCommon = (org.uberfire.ext.widgets.common.client.resources.i18n.CommonConstants) GWT.create(org.uberfire.ext.widgets.common.client.resources.i18n.CommonConstants.class);
      // Setup section.

      return i18nCommon;
    }

    /**
     * Getter for f_FlowPanel1 called 1 times. Type: DEFAULT. Build precedence: 1.
     */
    private com.google.gwt.user.client.ui.FlowPanel get_f_FlowPanel1() {
      return build_f_FlowPanel1();
    }
    private com.google.gwt.user.client.ui.FlowPanel build_f_FlowPanel1() {
      // Creation section.
      final com.google.gwt.user.client.ui.FlowPanel f_FlowPanel1 = (com.google.gwt.user.client.ui.FlowPanel) GWT.create(com.google.gwt.user.client.ui.FlowPanel.class);
      // Setup section.
      f_FlowPanel1.add(get_actionButton());
      f_FlowPanel1.add(get_ignoreButton());

      return f_FlowPanel1;
    }

    /**
     * Getter for actionButton called 1 times. Type: DEFAULT. Build precedence: 2.
     */
    private org.gwtbootstrap3.client.ui.Button get_actionButton() {
      return build_actionButton();
    }
    private org.gwtbootstrap3.client.ui.Button build_actionButton() {
      // Creation section.
      final org.gwtbootstrap3.client.ui.Button actionButton = (org.gwtbootstrap3.client.ui.Button) GWT.create(org.gwtbootstrap3.client.ui.Button.class);
      // Setup section.
      actionButton.setType(org.gwtbootstrap3.client.ui.constants.ButtonType.PRIMARY);
      actionButton.addClickHandler(handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames1);

      this.owner.actionButton = actionButton;

      return actionButton;
    }

    /**
     * Getter for ignoreButton called 1 times. Type: DEFAULT. Build precedence: 2.
     */
    private org.gwtbootstrap3.client.ui.Button get_ignoreButton() {
      return build_ignoreButton();
    }
    private org.gwtbootstrap3.client.ui.Button build_ignoreButton() {
      // Creation section.
      final org.gwtbootstrap3.client.ui.Button ignoreButton = (org.gwtbootstrap3.client.ui.Button) GWT.create(org.gwtbootstrap3.client.ui.Button.class);
      // Setup section.
      ignoreButton.setText("" + get_i18nCommon().Ignore() + "");
      ignoreButton.setType(org.gwtbootstrap3.client.ui.constants.ButtonType.DEFAULT);
      ignoreButton.addClickHandler(handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames2);

      this.owner.ignoreButton = ignoreButton;

      return ignoreButton;
    }
  }
}
