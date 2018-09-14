package com.acme.v10jeeapp.app.ui;

import com.acme.v10jeeapp.app.DataProviderBuilder;
import com.acme.v10jeeapp.backend.DataProviderService;
import com.acme.v10jeeapp.backend.security.entity.Role;
import com.acme.v10jeeapp.backend.security.entity.User;
import com.vaadin.cdi.annotation.RouteScoped;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.templatemodel.TemplateModel;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.stream.Collectors;

@Tag("users-view")
@HtmlImport("src/views/users/users-view.html")
@RoutePrefix("users")
@ParentLayout(MainView.class)
@RouteScoped
public class UsersView extends PolymerTemplate<TemplateModel>
        implements RouterLayout {

    @Inject
    private DataProviderService<User, String> dataProviderService;

    @Id("usersGrid")
    private Grid<User> usersGrid;

    @Id
    private TextField filterField;

    private ConfigurableFilterDataProvider<User, Void, String> dataProvider;

    @PostConstruct
    private void init() {
        dataProvider = new DataProviderBuilder<>(dataProviderService)
                .newDataProvider()
                .withConfigurableFilter();
        dataProvider.setFilter("");
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
        usersGrid.addSelectionListener(this::onUserSelect);
        filterField.setValueChangeMode(ValueChangeMode.EAGER);
        filterField.addValueChangeListener(this::onFilterChange);
    }

    private void onUserSelect(SelectionEvent<Grid<User>, User> gridUserSelectionEvent) {
        Long id = gridUserSelectionEvent.getFirstSelectedItem()
                .orElse(new User()).getId();
        openForm(id);
    }

    private void openForm(Long id) {
        getUI().ifPresent(ui -> ui.navigate(UserForm.class, id));
    }

    private void onFilterChange(ComponentValueChangeEvent<TextField, String> event) {
        String filter = event.getValue().trim();
        dataProvider.setFilter(filter);
        dataProvider.refreshAll();
    }

}
