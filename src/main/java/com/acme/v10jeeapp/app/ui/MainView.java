package com.acme.v10jeeapp.app.ui;

import com.acme.v10jeeapp.backend.security.boundary.PrincipalMapper;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.apache.shiro.SecurityUtils.getSubject;

@Tag("main-view")
@HtmlImport("src/views/main-view.html")
@Route("")
@Theme(Lumo.class)
@RequiresUser
public class MainView extends PolymerTemplate<MainView.MainModel>
        implements RouterLayout {

    @Inject
    private PrincipalMapper principalMapper;

    @PostConstruct
    private void init() {
        PrincipalCollection principals = getSubject().getPrincipals();
        getModel().setUsername(principalMapper.getUsername(principals));
    }

    @EventHandler
    private void onLogout() {
        getSubject().logout();
        String contextPath = VaadinRequest.getCurrent().getContextPath();
        getUI().ifPresent(ui -> ui.getPage()
                .executeJavaScript("window.location.href='" + contextPath + "';"));
    }

    public interface MainModel extends TemplateModel {
        String getUsername();

        void setUsername(String username);
    }
}
