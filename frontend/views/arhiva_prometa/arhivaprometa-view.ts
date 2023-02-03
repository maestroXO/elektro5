import '@vaadin/button';
import '@vaadin/checkbox';
import '@vaadin/date-picker';
import '@vaadin/date-time-picker';
import '@vaadin/form-layout';
import '@vaadin/grid';
import '@vaadin/grid/vaadin-grid-column';
import '@vaadin/horizontal-layout';
import '@vaadin/split-layout';
import '@vaadin/text-field';
import { html, LitElement } from 'lit';
import { customElement } from 'lit/decorators.js';

@customElement('arhivaprometa-view')
export class Arhiva_prometaView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`<vaadin-split-layout>
      <div class="grid-wrapper">
        <vaadin-grid id="grid"></vaadin-grid>
      </div>
      <div class="editor-layout">
        <div class="editor">
          <vaadin-form-layout>
            <vaadin-text-field label="Id radnika" id="id_radnika"></vaadin-text-field
            ><vaadin-date-picker label="Datum" id="datum"></vaadin-date-picker
            ><vaadin-text-field label="Dokument" id="dokument"></vaadin-text-field
            ><vaadin-text-field label="Opis" id="opis"></vaadin-text-field
            ><vaadin-text-field label="Zaduzenje" id="zaduzenje"></vaadin-text-field
            ><vaadin-text-field label="Razduzenje" id="razduzenje"></vaadin-text-field
            ><vaadin-date-time-picker label="Date" id="date" step="1"></vaadin-date-time-picker
            ><vaadin-text-field label="Korisnik" id="korisnik"></vaadin-text-field
            ><vaadin-text-field label="Cijena" id="cijena"></vaadin-text-field
            ><vaadin-text-field label="Neto" id="neto"></vaadin-text-field
            ><vaadin-text-field label="Bruto" id="bruto"></vaadin-text-field
            ><vaadin-text-field label="Id transakcije" id="id_transakcije"></vaadin-text-field
            ><vaadin-text-field label="Id opreme" id="id_opreme"></vaadin-text-field>
          </vaadin-form-layout>
        </div>
        <vaadin-horizontal-layout class="button-layout">
          <vaadin-button theme="primary" id="save">Save</vaadin-button>
          <vaadin-button theme="tertiary" slot="" id="cancel">Cancel</vaadin-button>
        </vaadin-horizontal-layout>
      </div>
    </vaadin-split-layout>`;
  }
}
