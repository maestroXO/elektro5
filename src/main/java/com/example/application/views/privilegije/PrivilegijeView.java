package com.example.application.views.privilegije;

import com.example.application.data.entity.Privilegije;
import com.example.application.data.service.PrivilegijeService;
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

@PageTitle("Privilegije")
@Route(value = "privilegije/:privilegijeID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Tag("privilegije-view")
@JsModule("./views/privilegije/privilegije-view.ts")
public class PrivilegijeView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String PRIVILEGIJE_ID = "privilegijeID";
    private final String PRIVILEGIJE_EDIT_ROUTE_TEMPLATE = "privilegije/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Privilegije> grid;

    @Id
    private TextField id_privilegije;
    @Id
    private TextField naziv_role;
    @Id
    private TextField insert;
    @Id
    private TextField update;
    @Id
    private TextField read;
    @Id
    private TextField delete;
    @Id
    private TextField all;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Privilegije> binder;

    private Privilegije privilegije;

    private final PrivilegijeService privilegijeService;

    public PrivilegijeView(PrivilegijeService privilegijeService) {
        this.privilegijeService = privilegijeService;
        addClassNames("privilegije-view");
        grid.addColumn(Privilegije::getId_privilegije).setHeader("Id_privilegije").setAutoWidth(true);
        grid.addColumn(Privilegije::getNaziv_role).setHeader("Naziv_role").setAutoWidth(true);
        grid.addColumn(Privilegije::getInsert).setHeader("Insert").setAutoWidth(true);
        grid.addColumn(Privilegije::getUpdate).setHeader("Update").setAutoWidth(true);
        grid.addColumn(Privilegije::getRead).setHeader("Read").setAutoWidth(true);
        grid.addColumn(Privilegije::getDelete).setHeader("Delete").setAutoWidth(true);
        grid.addColumn(Privilegije::getAll).setHeader("All").setAutoWidth(true);
        grid.setItems(query -> privilegijeService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PRIVILEGIJE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PrivilegijeView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Privilegije.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_privilegije).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_privilegije");
        binder.forField(insert).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("insert");
        binder.forField(update).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("update");
        binder.forField(read).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("read");
        binder.forField(delete).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("delete");
        binder.forField(all).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("all");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.privilegije == null) {
                    this.privilegije = new Privilegije();
                }
                binder.writeBean(this.privilegije);
                privilegijeService.update(this.privilegije);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(PrivilegijeView.class);
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
        Optional<Long> privilegijeId = event.getRouteParameters().get(PRIVILEGIJE_ID).map(Long::parseLong);
        if (privilegijeId.isPresent()) {
            Optional<Privilegije> privilegijeFromBackend = privilegijeService.get(privilegijeId.get());
            if (privilegijeFromBackend.isPresent()) {
                populateForm(privilegijeFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested privilegije was not found, ID = %s", privilegijeId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(PrivilegijeView.class);
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

    private void populateForm(Privilegije value) {
        this.privilegije = value;
        binder.readBean(this.privilegije);

    }
}
