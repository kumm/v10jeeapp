package com.acme.v10jeeapp.app.ui;

import com.acme.v10jeeapp.app.HasLogger;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;

import javax.servlet.http.HttpServletResponse;

@Tag("login-view")
@HtmlImport("src/views/login/login-view.html")
// @Theme(Lumo.class) todo: breaks deployment, need to move to a common routerLayout
public class LoginView
        extends PolymerTemplate<LoginView.LoginModel>
        implements HasErrorParameter<UnauthenticatedException>, HasLogger {

    private Location targetLocation;

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<UnauthenticatedException> parameter) {
        targetLocation = event.getLocation();
        return HttpServletResponse.SC_OK;
    }

    @EventHandler
    private void onLogin() {
        LoginModel model = getModel();
        UsernamePasswordToken token = new UsernamePasswordToken(
                model.getUsername(), model.getPassword(), model.isRememberMe());
        try {
            SecurityUtils.getSubject().login(token);
        } catch (IncorrectCredentialsException | UnknownAccountException e) {
            model.setError("Wrong username or password.");
        } catch (AuthenticationException e) {
            model.setError("Authentication error.");
            getLogger().error("Authentication error.", e);
            return;
        }
        getUI().ifPresent(this::navigateToTarget);
    }

    private void navigateToTarget(UI ui) {
        ui.navigate(targetLocation.getPath(), targetLocation.getQueryParameters());
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
