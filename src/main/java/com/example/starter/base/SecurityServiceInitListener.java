package com.example.starter.base;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.auth.NavigationAccessControl;
import org.jboss.logging.Logger;

public class SecurityServiceInitListener implements VaadinServiceInitListener {
  private static final Logger LOG = Logger.getLogger(SecurityServiceInitListener.class);
  @Override
  public void serviceInit(ServiceInitEvent event) {
/*
    LOG.info("Initializing Vaadin Security");
    NavigationAccessControl accessControl = new NavigationAccessControl();
    accessControl.setLoginView(LoginView.class);
    event.getSource().addUIInitListener(uiEvent ->
        uiEvent.getUI().addBeforeEnterListener(accessControl)
    );
*/
  }
}
