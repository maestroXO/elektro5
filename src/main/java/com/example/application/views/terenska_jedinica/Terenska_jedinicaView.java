package com.example.application.views.terenska_jedinica;

import com.example.application.data.entity.Terenska_jedinica;
import com.example.application.data.service.Terenska_jedinicaService;
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

@PageTitle("Terenska_jedinica")
@Route(value = "terenska_jedinica/:terenska_jedinicaID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Tag("terenskajedinica-view")
@JsModule("./views/terenska_jedinica/terenskajedinica-view.ts")
public class Terenska_jedinicaView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String TERENSKA_JEDINICA_ID = "terenska_jedinicaID";
    private final String TERENSKA_JEDINICA_EDIT_ROUTE_TEMPLATE = "terenska_jedinica/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Terenska_jedinica> grid;

    @Id
    private TextField id_terenske_jedinice;
    @Id
    private TextField naziv;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Terenska_jedinica> binder;

    private Terenska_jedinica terenska_jedinica;

    private final Terenska_jedinicaService terenska_jedinicaService;

    public Terenska_jedinicaView(Terenska_jedinicaService terenska_jedinicaService) {
        this.terenska_jedinicaService = terenska_jedinicaService;
        addClassNames("terenskajedinica-view");
        grid.addColumn(Terenska_jedinica::getId_terenske_jedinice).setHeader("Id_terenske_jedinice").setAutoWidth(true);
        grid.addColumn(Terenska_jedinica::getNaziv).setHeader("Naziv").setAutoWidth(true);
        grid.setItems(query -> terenska_jedinicaService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent()
                        .navigate(String.format(TERENSKA_JEDINICA_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(Terenska_jedinicaView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Terenska_jedinica.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_terenske_jedinice).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_terenske_jedinice");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.terenska_jedinica == null) {
                    this.terenska_jedinica = new Terenska_jedinica();
                }
                binder.writeBean(this.terenska_jedinica);
                terenska_jedinicaService.update(this.terenska_jedinica);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(Terenska_jedinicaView.class);
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
        Optional<Long> terenska_jedinicaId = event.getRouteParameters().get(TERENSKA_JEDINICA_ID).map(Long::parseLong);
        if (terenska_jedinicaId.isPresent()) {
            Optional<Terenska_jedinica> terenska_jedinicaFromBackend = terenska_jedinicaService
                    .get(terenska_jedinicaId.get());
            if (terenska_jedinicaFromBackend.isPresent()) {
                populateForm(terenska_jedinicaFromBackend.get());
            } else {
                Notification.show(String.format("The requested terenska_jedinica was not found, ID = %s",
                        terenska_jedinicaId.get()), 3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(Terenska_jedinicaView.class);
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

    private void populateForm(Terenska_jedinica value) {
        this.terenska_jedinica = value;
        binder.readBean(this.terenska_jedinica);

    }
}
