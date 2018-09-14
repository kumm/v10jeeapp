package com.acme.v10jeeapp.app.ui;

import com.acme.v10jeeapp.app.security.AppPermissions;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.annotation.PostConstruct;

@Tag("user-form")
@HtmlImport("src/views/users/user-form.html")
@Route(value = "", layout = UsersView.class)
@RequiresPermissions(AppPermissions.USER_ADMIN)
public class UserForm extends PolymerTemplate<TemplateModel>
        implements HasUrlParameter<Long> {

    @PostConstruct
    private void init() {
    }

    @EventHandler
    private void onSave() {

    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long userId) {

    }
}
