package com.example.application.views.jm;

import com.example.application.data.entity.Jm;
import com.example.application.data.service.JmService;
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

@PageTitle("JM")
@Route(value = "jm/:jmID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Tag("j-m-view")
@JsModule("./views/jm/j-m-view.ts")
public class JMView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String JM_ID = "jmID";
    private final String JM_EDIT_ROUTE_TEMPLATE = "jm/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Jm> grid;

    @Id
    private TextField id_jm;
    @Id
    private TextField naziv;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Jm> binder;

    private Jm jm;

    private final JmService jmService;

    public JMView(JmService jmService) {
        this.jmService = jmService;
        addClassNames("j-m-view");
        grid.addColumn(Jm::getId_jm).setHeader("Id_jm").setAutoWidth(true);
        grid.addColumn(Jm::getNaziv).setHeader("Naziv").setAutoWidth(true);
        grid.setItems(query -> jmService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(JM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(JMView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Jm.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_jm).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("id_jm");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.jm == null) {
                    this.jm = new Jm();
                }
                binder.writeBean(this.jm);
                jmService.update(this.jm);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(JMView.class);
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
        Optional<Long> jmId = event.getRouteParameters().get(JM_ID).map(Long::parseLong);
        if (jmId.isPresent()) {
            Optional<Jm> jmFromBackend = jmService.get(jmId.get());
            if (jmFromBackend.isPresent()) {
                populateForm(jmFromBackend.get());
            } else {
                Notification.show(String.format("The requested jm was not found, ID = %s", jmId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(JMView.class);
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

    private void populateForm(Jm value) {
        this.jm = value;
        binder.readBean(this.jm);

    }
}
