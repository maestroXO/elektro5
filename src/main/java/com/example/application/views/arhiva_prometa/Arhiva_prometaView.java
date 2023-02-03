package com.example.application.views.arhiva_prometa;

import com.example.application.data.entity.Arhiva_prometa;
import com.example.application.data.service.Arhiva_prometaService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
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

@PageTitle("Arhiva_prometa")
@Route(value = "arhiva_prometa/:arhiva_prometaID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("USER")
@Tag("arhivaprometa-view")
@JsModule("./views/arhiva_prometa/arhivaprometa-view.ts")
public class Arhiva_prometaView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String ARHIVA_PROMETA_ID = "arhiva_prometaID";
    private final String ARHIVA_PROMETA_EDIT_ROUTE_TEMPLATE = "arhiva_prometa/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Arhiva_prometa> grid;

    @Id
    private TextField id_radnika;
    @Id
    private DatePicker datum;
    @Id
    private TextField dokument;
    @Id
    private TextField opis;
    @Id
    private TextField zaduzenje;
    @Id
    private TextField razduzenje;
    @Id
    private DateTimePicker date;
    @Id
    private TextField korisnik;
    @Id
    private TextField cijena;
    @Id
    private TextField neto;
    @Id
    private TextField bruto;
    @Id
    private TextField id_transakcije;
    @Id
    private TextField id_opreme;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Arhiva_prometa> binder;

    private Arhiva_prometa arhiva_prometa;

    private final Arhiva_prometaService arhiva_prometaService;

    public Arhiva_prometaView(Arhiva_prometaService arhiva_prometaService) {
        this.arhiva_prometaService = arhiva_prometaService;
        addClassNames("arhivaprometa-view");
        grid.addColumn(Arhiva_prometa::getId_radnika).setHeader("Id_radnika").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getDatum).setHeader("Datum").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getDokument).setHeader("Dokument").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getOpis).setHeader("Opis").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getZaduzenje).setHeader("Zaduzenje").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getRazduzenje).setHeader("Razduzenje").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getDate).setHeader("Date").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getKorisnik).setHeader("Korisnik").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getCijena).setHeader("Cijena").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getNeto).setHeader("Neto").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getBruto).setHeader("Bruto").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getId_transakcije).setHeader("Id_transakcije").setAutoWidth(true);
        grid.addColumn(Arhiva_prometa::getId_opreme).setHeader("Id_opreme").setAutoWidth(true);
        grid.setItems(query -> arhiva_prometaService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(ARHIVA_PROMETA_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(Arhiva_prometaView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Arhiva_prometa.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(id_radnika).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_radnika");
        binder.forField(dokument).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("dokument");
        binder.forField(zaduzenje).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("zaduzenje");
        binder.forField(razduzenje).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("razduzenje");
        binder.forField(korisnik).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("korisnik");
        binder.forField(cijena).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("cijena");
        binder.forField(neto).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("neto");
        binder.forField(bruto).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("bruto");
        binder.forField(id_transakcije).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_transakcije");
        binder.forField(id_opreme).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("id_opreme");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.arhiva_prometa == null) {
                    this.arhiva_prometa = new Arhiva_prometa();
                }
                binder.writeBean(this.arhiva_prometa);
                arhiva_prometaService.update(this.arhiva_prometa);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(Arhiva_prometaView.class);
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
        Optional<Long> arhiva_prometaId = event.getRouteParameters().get(ARHIVA_PROMETA_ID).map(Long::parseLong);
        if (arhiva_prometaId.isPresent()) {
            Optional<Arhiva_prometa> arhiva_prometaFromBackend = arhiva_prometaService.get(arhiva_prometaId.get());
            if (arhiva_prometaFromBackend.isPresent()) {
                populateForm(arhiva_prometaFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested arhiva_prometa was not found, ID = %s", arhiva_prometaId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(Arhiva_prometaView.class);
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

    private void populateForm(Arhiva_prometa value) {
        this.arhiva_prometa = value;
        binder.readBean(this.arhiva_prometa);

    }
}
