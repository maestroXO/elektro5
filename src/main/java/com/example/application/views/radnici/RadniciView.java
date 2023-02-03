package com.example.application.views.radnici;

import com.example.application.data.entity.Radnici;
import com.example.application.data.service.RadniciService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Radnici")
@Route(value = "radnici/:radniciID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Tag("radnici-view")
@JsModule("./views/radnici/radnici-view.ts")
public class RadniciView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String RADNICI_ID = "radniciID";
    private final String RADNICI_EDIT_ROUTE_TEMPLATE = "radnici/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Radnici> grid;

    @Id
    private TextField id_radnika;
    @Id
    private TextField ime;
    @Id
    private TextField prezime;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Radnici> binder;

    private Radnici radnici;

    private final RadniciService radniciService;

    public RadniciView(RadniciService radniciService) {
        this.radniciService = radniciService;
        addClassNames("radnici-view");
        grid.addColumn(Radnici::getId_radnika).setHeader("Id_radnika").setAutoWidth(true);
        grid.addColumn(Radnici::getIme).setHeader("Ime").setAutoWidth(true);
        grid.addColumn(Radnici::getPrezime).setHeader("Prezime").setAutoWidth(true);
        grid.setItems(query -> radniciService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(RADNICI_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(RadniciView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Radnici.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_radnika).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_radnika");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.radnici == null) {
                    this.radnici = new Radnici();
                }
                binder.writeBean(this.radnici);
                radniciService.update(this.radnici);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(RadniciView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> radniciId = event.getRouteParameters().get(RADNICI_ID).map(Long::parseLong);
        if (radniciId.isPresent()) {
            Optional<Radnici> radniciFromBackend = radniciService.get(radniciId.get());
            if (radniciFromBackend.isPresent()) {
                populateForm(radniciFromBackend.get());
            } else {
                Notification.show(String.format("The requested radnici was not found, ID = %s", radniciId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(RadniciView.class);
            }
        }
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Radnici value) {
        this.radnici = value;
        binder.readBean(this.radnici);

    }
}
