package com.acme.v10jeeapp.app.ui;

import com.acme.v10jeeapp.app.HasLogger;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.wcs.vaadin.flow.cdi.NormalRouteScoped;
import com.wcs.vaadin.flow.cdi.RouteScopeOwner;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;

import javax.inject.Inject;
import java.io.Serializable;

@Tag("login-view")
@HtmlImport("src/views/login/login-view.html")
@Route("login")
@Theme(Lumo.class)
@RequiresGuest
public class LoginView extends PolymerTemplate<LoginView.LoginModel>
        implements HasLogger {

    @Inject
    @RouteScopeOwner(LoginView.class)
    private AfterLoginNavigation afterLoginNavigation;

    @NormalRouteScoped
    @RouteScopeOwner(LoginView.class)
    public static class AfterLoginNavigation implements Serializable {
        private Location location = new Location("");

        public void setLocation(Location location) {
            this.location = location;
        }

        public void perform(UI ui) {
            ui.navigate(location.getPath(), location.getQueryParameters());
        }
    }

    @EventHandler
    private void onLogin() {
        UsernamePasswordToken token = new UsernamePasswordToken(
                getModel().getUsername(), getModel().getPassword());
        try {
            SecurityUtils.getSubject().login(token);
        } catch (IncorrectCredentialsException | UnknownAccountException e) {
            getModel().setError("Wrong username or password.");
        } catch (AuthenticationException e) {
            getModel().setError("Authentication error.");
            getLogger().error("Authentication error.", e);
            return;
        }
        getUI().ifPresent(afterLoginNavigation::perform);
    }

    public interface LoginModel extends TemplateModel {
        String getUsername();

        void setUsername(String username);

        String getPassword();

        void setPassword(String password);

        boolean isRememberMe();

        void setRememberMe(boolean rememberMe);

        String getError();

        void setError(String error);
    }
}
