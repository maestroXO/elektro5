package com.example.application.views.korisnik_aplikacije;

import com.example.application.data.entity.Korisnik_aplikacije;
import com.example.application.data.service.Korisnik_aplikacijeService;
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

@PageTitle("Korisnik_aplikacije")
@Route(value = "korisnik_aplikacije/:korisnik_aplikacijeID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Tag("korisnikaplikacije-view")
@JsModule("./views/korisnik_aplikacije/korisnikaplikacije-view.ts")
public class Korisnik_aplikacijeView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String KORISNIK_APLIKACIJE_ID = "korisnik_aplikacijeID";
    private final String KORISNIK_APLIKACIJE_EDIT_ROUTE_TEMPLATE = "korisnik_aplikacije/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Korisnik_aplikacije> grid;

    @Id
    private TextField id_radnika;
    @Id
    private TextField ime;
    @Id
    private TextField prezime;
    @Id
    private TextField id_privilegije;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Korisnik_aplikacije> binder;

    private Korisnik_aplikacije korisnik_aplikacije;

    private final Korisnik_aplikacijeService korisnik_aplikacijeService;

    public Korisnik_aplikacijeView(Korisnik_aplikacijeService korisnik_aplikacijeService) {
        this.korisnik_aplikacijeService = korisnik_aplikacijeService;
        addClassNames("korisnikaplikacije-view");
        grid.addColumn(Korisnik_aplikacije::getId_radnika).setHeader("Id_radnika").setAutoWidth(true);
        grid.addColumn(Korisnik_aplikacije::getIme).setHeader("Ime").setAutoWidth(true);
        grid.addColumn(Korisnik_aplikacije::getPrezime).setHeader("Prezime").setAutoWidth(true);
        grid.addColumn(Korisnik_aplikacije::getId_privilegije).setHeader("Id_privilegije").setAutoWidth(true);
        grid.setItems(query -> korisnik_aplikacijeService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent()
                        .navigate(String.format(KORISNIK_APLIKACIJE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(Korisnik_aplikacijeView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Korisnik_aplikacije.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_radnika).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_radnika");
        binder.forField(id_privilegije).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_privilegije");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.korisnik_aplikacije == null) {
                    this.korisnik_aplikacije = new Korisnik_aplikacije();
                }
                binder.writeBean(this.korisnik_aplikacije);
                korisnik_aplikacijeService.update(this.korisnik_aplikacije);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(Korisnik_aplikacijeView.class);
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
        Optional<Long> korisnik_aplikacijeId = event.getRouteParameters().get(KORISNIK_APLIKACIJE_ID)
                .map(Long::parseLong);
        if (korisnik_aplikacijeId.isPresent()) {
            Optional<Korisnik_aplikacije> korisnik_aplikacijeFromBackend = korisnik_aplikacijeService
                    .get(korisnik_aplikacijeId.get());
            if (korisnik_aplikacijeFromBackend.isPresent()) {
                populateForm(korisnik_aplikacijeFromBackend.get());
            } else {
                Notification.show(String.format("The requested korisnik_aplikacije was not found, ID = %s",
                        korisnik_aplikacijeId.get()), 3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(Korisnik_aplikacijeView.class);
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

    private void populateForm(Korisnik_aplikacije value) {
        this.korisnik_aplikacije = value;
        binder.readBean(this.korisnik_aplikacije);

    }
}
