package com.acme.v10jeeapp.app.ui;

import com.acme.v10jeeapp.app.QuerySortOrders;
import com.acme.v10jeeapp.app.security.AppPermissions;
import com.acme.v10jeeapp.backend.security.boundary.UserService;
import com.acme.v10jeeapp.backend.security.entity.Role;
import com.acme.v10jeeapp.backend.security.entity.User;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Tag("users-view")
@HtmlImport("src/views/users/users-view.html")
@Route(value = "users", layout = MainView.class)
@RequiresPermissions(AppPermissions.USER_ADMIN)
public class UsersView extends PolymerTemplate<TemplateModel> {

    @Inject
    private UserService userService;

    @Id("usersGrid")
    private Grid<User> usersGrid;

    @PostConstruct
    private void init() {
        usersGrid.setDataProvider(DataProvider.fromCallbacks(this::fetch, this::count));
        usersGrid.addColumn(User::getUsername).setWidth("200px").setHeader("Username").setFlexGrow(1);
        usersGrid.addColumn(user -> user.getRoles().stream()
                .map(Role::getId).collect(Collectors.joining(", ")))
                .setWidth("200px").setHeader("Roles").setFlexGrow(5);
    }

    private Stream<User> fetch(Query<User, Void> query) {
        List<User> users = userService.list(
                query.getOffset(), query.getLimit(),
                QuerySortOrders.toSortOrderList(query.getSortOrders()));
        return users.stream();
    }

    private int count(Query<User, Void> query) {
        return userService.count();
    }

}
