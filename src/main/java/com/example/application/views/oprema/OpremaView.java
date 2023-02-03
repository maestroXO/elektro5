package com.example.application.views.oprema;

import com.example.application.data.entity.Oprema;
import com.example.application.data.service.OpremaService;
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

@PageTitle("Oprema")
@Route(value = "oprema/:opremaID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Tag("oprema-view")
@JsModule("./views/oprema/oprema-view.ts")
public class OpremaView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String OPREMA_ID = "opremaID";
    private final String OPREMA_EDIT_ROUTE_TEMPLATE = "oprema/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Oprema> grid;

    @Id
    private TextField id_opreme;
    @Id
    private TextField naziv_opreme;
    @Id
    private TextField id_jm;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Oprema> binder;

    private Oprema oprema;

    private final OpremaService opremaService;

    public OpremaView(OpremaService opremaService) {
        this.opremaService = opremaService;
        addClassNames("oprema-view");
        grid.addColumn(Oprema::getId_opreme).setHeader("Id_opreme").setAutoWidth(true);
        grid.addColumn(Oprema::getNaziv_opreme).setHeader("Naziv_opreme").setAutoWidth(true);
        grid.addColumn(Oprema::getId_jm).setHeader("Id_jm").setAutoWidth(true);
        grid.setItems(query -> opremaService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(OPREMA_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(OpremaView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Oprema.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_opreme).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_opreme");
        binder.forField(id_jm).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id_jm");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.oprema == null) {
                    this.oprema = new Oprema();
                }
                binder.writeBean(this.oprema);
                opremaService.update(this.oprema);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(OpremaView.class);
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
        Optional<Long> opremaId = event.getRouteParameters().get(OPREMA_ID).map(Long::parseLong);
        if (opremaId.isPresent()) {
            Optional<Oprema> opremaFromBackend = opremaService.get(opremaId.get());
            if (opremaFromBackend.isPresent()) {
                populateForm(opremaFromBackend.get());
            } else {
                Notification.show(String.format("The requested oprema was not found, ID = %s", opremaId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(OpremaView.class);
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

    private void populateForm(Oprema value) {
        this.oprema = value;
        binder.readBean(this.oprema);

    }
}
