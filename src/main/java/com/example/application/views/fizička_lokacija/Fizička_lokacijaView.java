package com.example.application.views.fizička_lokacija;

import com.example.application.data.entity.Fizicka_lokacija;
import com.example.application.data.service.Fizicka_lokacijaService;
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

@PageTitle("Fizička_lokacija")
@Route(value = "fizicka_lokacija/:fizicka_lokacijaID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Tag("fizičkalokacija-view")
@JsModule("./views/fizička_lokacija/fizičkalokacija-view.ts")
public class Fizička_lokacijaView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String FIZICKA_LOKACIJA_ID = "fizicka_lokacijaID";
    private final String FIZICKA_LOKACIJA_EDIT_ROUTE_TEMPLATE = "fizicka_lokacija/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Fizicka_lokacija> grid;

    @Id
    private TextField id_fizicke_lokacije;
    @Id
    private TextField naziv_fizicke_lokacije;
    @Id
    private TextField id_terenske_jedinice;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Fizicka_lokacija> binder;

    private Fizicka_lokacija fizicka_lokacija;

    private final Fizicka_lokacijaService fizicka_lokacijaService;

    public Fizička_lokacijaView(Fizicka_lokacijaService fizicka_lokacijaService) {
        this.fizicka_lokacijaService = fizicka_lokacijaService;
        addClassNames("fizičkalokacija-view");
        grid.addColumn(Fizicka_lokacija::getId_fizicke_lokacije).setHeader("Id_fizicke_lokacije").setAutoWidth(true);
        grid.addColumn(Fizicka_lokacija::getNaziv_fizicke_lokacije).setHeader("Naziv_fizicke_lokacije")
                .setAutoWidth(true);
        grid.addColumn(Fizicka_lokacija::getId_terenske_jedinice).setHeader("Id_terenske_jedinice").setAutoWidth(true);
        grid.setItems(query -> fizicka_lokacijaService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(FIZICKA_LOKACIJA_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(Fizička_lokacijaView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Fizicka_lokacija.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_fizicke_lokacije).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_fizicke_lokacije");
        binder.forField(id_terenske_jedinice).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_terenske_jedinice");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.fizicka_lokacija == null) {
                    this.fizicka_lokacija = new Fizicka_lokacija();
                }
                binder.writeBean(this.fizicka_lokacija);
                fizicka_lokacijaService.update(this.fizicka_lokacija);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(Fizička_lokacijaView.class);
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
        Optional<Long> fizicka_lokacijaId = event.getRouteParameters().get(FIZICKA_LOKACIJA_ID).map(Long::parseLong);
        if (fizicka_lokacijaId.isPresent()) {
            Optional<Fizicka_lokacija> fizicka_lokacijaFromBackend = fizicka_lokacijaService
                    .get(fizicka_lokacijaId.get());
            if (fizicka_lokacijaFromBackend.isPresent()) {
                populateForm(fizicka_lokacijaFromBackend.get());
            } else {
                Notification.show(String.format("The requested fizicka_lokacija was not found, ID = %s",
                        fizicka_lokacijaId.get()), 3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(Fizička_lokacijaView.class);
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

    private void populateForm(Fizicka_lokacija value) {
        this.fizicka_lokacija = value;
        binder.readBean(this.fizicka_lokacija);

    }
}
