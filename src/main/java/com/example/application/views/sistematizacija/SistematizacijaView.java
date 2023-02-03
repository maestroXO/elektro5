package com.example.application.views.sistematizacija;

import com.example.application.data.entity.Sistematizacija;
import com.example.application.data.service.SistematizacijaService;
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

@PageTitle("Sistematizacija")
@Route(value = "sistematizacija/:sistematizacijaID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Tag("sistematizacija-view")
@JsModule("./views/sistematizacija/sistematizacija-view.ts")
public class SistematizacijaView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String SISTEMATIZACIJA_ID = "sistematizacijaID";
    private final String SISTEMATIZACIJA_EDIT_ROUTE_TEMPLATE = "sistematizacija/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Sistematizacija> grid;

    @Id
    private TextField naziv_sektora;
    @Id
    private TextField naziv_sluzbe;
    @Id
    private TextField id_fizicke_lokacije;
    @Id
    private TextField naziv_odjeljenja;
    @Id
    private TextField ostalo;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Sistematizacija> binder;

    private Sistematizacija sistematizacija;

    private final SistematizacijaService sistematizacijaService;

    public SistematizacijaView(SistematizacijaService sistematizacijaService) {
        this.sistematizacijaService = sistematizacijaService;
        addClassNames("sistematizacija-view");
        grid.addColumn(Sistematizacija::getNaziv_sektora).setHeader("Naziv_sektora").setAutoWidth(true);
        grid.addColumn(Sistematizacija::getNaziv_sluzbe).setHeader("Naziv_sluzbe").setAutoWidth(true);
        grid.addColumn(Sistematizacija::getId_fizicke_lokacije).setHeader("Id_fizicke_lokacije").setAutoWidth(true);
        grid.addColumn(Sistematizacija::getNaziv_odjeljenja).setHeader("Naziv_odjeljenja").setAutoWidth(true);
        grid.addColumn(Sistematizacija::getOstalo).setHeader("Ostalo").setAutoWidth(true);
        grid.setItems(query -> sistematizacijaService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SISTEMATIZACIJA_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(SistematizacijaView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Sistematizacija.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_fizicke_lokacije).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_fizicke_lokacije");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.sistematizacija == null) {
                    this.sistematizacija = new Sistematizacija();
                }
                binder.writeBean(this.sistematizacija);
                sistematizacijaService.update(this.sistematizacija);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(SistematizacijaView.class);
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
        Optional<Long> sistematizacijaId = event.getRouteParameters().get(SISTEMATIZACIJA_ID).map(Long::parseLong);
        if (sistematizacijaId.isPresent()) {
            Optional<Sistematizacija> sistematizacijaFromBackend = sistematizacijaService.get(sistematizacijaId.get());
            if (sistematizacijaFromBackend.isPresent()) {
                populateForm(sistematizacijaFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested sistematizacija was not found, ID = %s", sistematizacijaId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(SistematizacijaView.class);
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

    private void populateForm(Sistematizacija value) {
        this.sistematizacija = value;
        binder.readBean(this.sistematizacija);

    }
}
