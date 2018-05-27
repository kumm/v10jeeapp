package com.acme.v10jeeapp.app.ui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.apache.shiro.authz.UnauthorizedException;

import javax.servlet.http.HttpServletResponse;

@Tag("unauthorized-error-view")
@HtmlImport("src/views/unauthorized-error-view.html")
@ParentLayout(MainView.class)
public class UnauthorizedErrorView
        extends PolymerTemplate<UnauthorizedErrorView.UnauthorizedErrorModel>
        implements HasErrorParameter<UnauthorizedException> {
    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<UnauthorizedException> parameter) {
        String message = parameter.getException().getMessage();
        getModel().setMessage(message);
        return HttpServletResponse.SC_OK;
    }

    public interface UnauthorizedErrorModel extends TemplateModel {

        String getMessage();

        void setMessage(String message);
    }
}
