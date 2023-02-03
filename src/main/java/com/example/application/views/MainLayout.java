package com.example.application.views;

import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.data.entity.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.arhiva_prometa.Arhiva_prometaView;
import com.example.application.views.fizička_lokacija.Fizička_lokacijaView;
import com.example.application.views.jm.JMView;
import com.example.application.views.korisnik_aplikacije.Korisnik_aplikacijeView;
import com.example.application.views.oprema.OpremaView;
import com.example.application.views.početna.PočetnaView;
import com.example.application.views.privilegije.PrivilegijeView;
import com.example.application.views.radnici.RadniciView;
import com.example.application.views.sistematizacija.SistematizacijaView;
import com.example.application.views.terenska_jedinica.Terenska_jedinicaView;
import com.example.application.views.transakcije.TransakcijeView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Elektro");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        if (accessChecker.hasAccess(PočetnaView.class)) {
            nav.addItem(new AppNavItem("Početna", PočetnaView.class, "la la-file"));

        }
        if (accessChecker.hasAccess(SistematizacijaView.class)) {
            nav.addItem(new AppNavItem("Sistematizacija", SistematizacijaView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(Terenska_jedinicaView.class)) {
            nav.addItem(new AppNavItem("Terenska_jedinica", Terenska_jedinicaView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(TransakcijeView.class)) {
            nav.addItem(new AppNavItem("Transakcije", TransakcijeView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(Fizička_lokacijaView.class)) {
            nav.addItem(new AppNavItem("Fizička_lokacija", Fizička_lokacijaView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(RadniciView.class)) {
            nav.addItem(new AppNavItem("Radnici", RadniciView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(PrivilegijeView.class)) {
            nav.addItem(new AppNavItem("Privilegije", PrivilegijeView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(JMView.class)) {
            nav.addItem(new AppNavItem("JM", JMView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(OpremaView.class)) {
            nav.addItem(new AppNavItem("Oprema", OpremaView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(Korisnik_aplikacijeView.class)) {
            nav.addItem(new AppNavItem("Korisnik_aplikacije", Korisnik_aplikacijeView.class, "la la-columns"));

        }
        if (accessChecker.hasAccess(Arhiva_prometaView.class)) {
            nav.addItem(new AppNavItem("Arhiva_prometa", Arhiva_prometaView.class, "la la-columns"));

        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(user.getProfilePicture()));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
