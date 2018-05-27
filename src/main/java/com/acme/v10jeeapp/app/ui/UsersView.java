package com.acme.v10jeeapp.app.ui;

import com.acme.v10jeeapp.app.DataProviderBuilder;
import com.acme.v10jeeapp.app.security.AppPermissions;
import com.acme.v10jeeapp.backend.DataProviderService;
import com.acme.v10jeeapp.backend.security.entity.Role;
import com.acme.v10jeeapp.backend.security.entity.User;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.stream.Collectors;

@Tag("users-view")
@HtmlImport("src/views/users/users-view.html")
@Route(value = "users", layout = MainView.class)
@RequiresPermissions(AppPermissions.USER_ADMIN)
public class UsersView extends PolymerTemplate<TemplateModel> {

    @Inject
    private DataProviderService<User, String> dataProviderService;

    @Id("usersGrid")
    private Grid<User> usersGrid;
    private ConfigurableFilterDataProvider<User, Void, String> dataProvider;

    @PostConstruct
    private void init() {
        dataProvider = new DataProviderBuilder<>(dataProviderService).newDataProvider();
        usersGrid.setDataProvider(dataProvider);
        usersGrid.addColumn(User::getUsername)
                .setWidth("200px")
                .setHeader("Username")
                .setSortable(true)
                .setKey("username")
                .setFlexGrow(1);
        usersGrid.addColumn(user -> user.getRoles().stream()
                .map(Role::getId).collect(Collectors.joining(", ")))
                .setWidth("200px").setHeader("Roles").setFlexGrow(5);
    }

}
