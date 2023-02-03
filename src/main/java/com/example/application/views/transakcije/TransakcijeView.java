package com.example.application.views.transakcije;

import com.example.application.data.entity.Transakcije;
import com.example.application.data.service.TransakcijeService;
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

@PageTitle("Transakcije")
@Route(value = "transakcije/:transakcijeID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@Tag("transakcije-view")
@JsModule("./views/transakcije/transakcije-view.ts")
public class TransakcijeView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String TRANSAKCIJE_ID = "transakcijeID";
    private final String TRANSAKCIJE_EDIT_ROUTE_TEMPLATE = "transakcije/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Transakcije> grid;

    @Id
    private TextField id_transakcije;
    @Id
    private TextField naziv;
    @Id
    private TextField zaduzenje;
    @Id
    private TextField razduzenje;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Transakcije> binder;

    private Transakcije transakcije;

    private final TransakcijeService transakcijeService;

    public TransakcijeView(TransakcijeService transakcijeService) {
        this.transakcijeService = transakcijeService;
        addClassNames("transakcije-view");
        grid.addColumn(Transakcije::getId_transakcije).setHeader("Id_transakcije").setAutoWidth(true);
        grid.addColumn(Transakcije::getNaziv).setHeader("Naziv").setAutoWidth(true);
        grid.addColumn(Transakcije::getZaduzenje).setHeader("Zaduzenje").setAutoWidth(true);
        grid.addColumn(Transakcije::getRazduzenje).setHeader("Razduzenje").setAutoWidth(true);
        grid.setItems(query -> transakcijeService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(TRANSAKCIJE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TransakcijeView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Transakcije.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_transakcije).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_transakcije");
        binder.forField(zaduzenje).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("zaduzenje");
        binder.forField(razduzenje).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("razduzenje");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.transakcije == null) {
                    this.transakcije = new Transakcije();
                }
                binder.writeBean(this.transakcije);
                transakcijeService.update(this.transakcije);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(TransakcijeView.class);
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
        Optional<Long> transakcijeId = event.getRouteParameters().get(TRANSAKCIJE_ID).map(Long::parseLong);
        if (transakcijeId.isPresent()) {
            Optional<Transakcije> transakcijeFromBackend = transakcijeService.get(transakcijeId.get());
            if (transakcijeFromBackend.isPresent()) {
                populateForm(transakcijeFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested transakcije was not found, ID = %s", transakcijeId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(TransakcijeView.class);
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

    private void populateForm(Transakcije value) {
        this.transakcije = value;
        binder.readBean(this.transakcije);

    }
}
