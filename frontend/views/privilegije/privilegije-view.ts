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

@customElement('privilegije-view')
export class PrivilegijeView extends LitElement {
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
            <vaadin-text-field label="Id privilegije" id="id_privilegije"></vaadin-text-field
            ><vaadin-text-field label="Naziv role" id="naziv_role"></vaadin-text-field
            ><vaadin-text-field label="Insert" id="insert"></vaadin-text-field
            ><vaadin-text-field label="Update" id="update"></vaadin-text-field
            ><vaadin-text-field label="Read" id="read"></vaadin-text-field
            ><vaadin-text-field label="Delete" id="delete"></vaadin-text-field
            ><vaadin-text-field label="All" id="all"></vaadin-text-field>
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
