/*
 * Copyright 2008 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kfs.module.ar.document.web.struts;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.kuali.core.exceptions.InfrastructureException;
import org.kuali.core.web.format.CurrencyFormatter;
import org.kuali.core.web.ui.KeyLabelPair;
import org.kuali.kfs.module.ar.ArConstants;
import org.kuali.kfs.module.ar.businessobject.CustomerInvoiceDetail;
import org.kuali.kfs.module.ar.businessobject.CustomerInvoiceItemCode;
import org.kuali.kfs.module.ar.document.CustomerInvoiceDocument;
import org.kuali.kfs.module.ar.document.service.CustomerInvoiceDetailService;
import org.kuali.kfs.module.ar.document.service.CustomerInvoiceDocumentService;
import org.kuali.kfs.sys.KFSPropertyConstants;
import org.kuali.kfs.sys.businessobject.SourceAccountingLine;
import org.kuali.kfs.sys.businessobject.UnitOfMeasure;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.document.AccountingDocument;
import org.kuali.kfs.sys.web.struts.KualiAccountingDocumentFormBase;

public class CustomerInvoiceDocumentForm extends KualiAccountingDocumentFormBase {

    private CustomerInvoiceDetail newCustomerInvoiceDetail;

    /**
     * Constructs a CustomerInvoiceDocumentForm.java. Also sets new customer invoice document detail to a newly constructed customer
     * invoice detail.
     */
    public CustomerInvoiceDocumentForm() {
        super();
        setDocument(new CustomerInvoiceDocument());
    }

    public CustomerInvoiceDocument getCustomerInvoiceDocument() {
        return (CustomerInvoiceDocument) getDocument();
    }

    /**
     * @see org.kuali.kfs.sys.web.struts.KualiAccountingDocumentFormBase#populate(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void populate(HttpServletRequest request) {
        super.populate(request);
        SpringContext.getBean(CustomerInvoiceDocumentService.class).loadCustomerAddressesForCustomerInvoiceDocument(getCustomerInvoiceDocument());
    }

    /**
     * Reused to create new source accounting line (i.e customer invoice detail line) with defaulted values.
     * 
     * @see org.kuali.kfs.sys.web.struts.KualiAccountingDocumentFormBase#createNewSourceAccountingLine(org.kuali.kfs.sys.document.AccountingDocument)
     */
    @Override
    protected SourceAccountingLine createNewSourceAccountingLine(AccountingDocument financialDocument) {
        if (financialDocument == null) {
            throw new IllegalArgumentException("invalid (null) document");
        }
        try {
            return SpringContext.getBean(CustomerInvoiceDetailService.class).getCustomerInvoiceDetailFromOrganizationAccountingDefaultForCurrentYear();
        }
        catch (Exception e) {
            throw new InfrastructureException("Unable to create a new customer invoice document accounting line", e);
        }
    }

    /**
     * By overriding this method, we can add the invoice total amount to the document header
     * 
     * @see org.kuali.core.web.struts.form.KualiForm#getAdditionalDocInfo1()
     */
    @Override
    public KeyLabelPair getAdditionalDocInfo2() {
        return new KeyLabelPair("DataDictionary.CustomerInvoiceDocument.attributes.sourceTotal", (String) new CurrencyFormatter().format(getCustomerInvoiceDocument().getSourceTotal()));
    }


    /**
     * Configure lookup for Invoice Item Code source accounting line
     * 
     * @see org.kuali.kfs.sys.web.struts.KualiAccountingDocumentFormBase#getForcedLookupOptionalFields()
     */
    @Override
    public Map getForcedLookupOptionalFields() {
        Map forcedLookupOptionalFields = super.getForcedLookupOptionalFields();

        forcedLookupOptionalFields.put(ArConstants.CUSTOMER_INVOICE_DOCUMENT_INVOICE_ITEM_CODE_PROPERTY, ArConstants.CUSTOMER_INVOICE_DOCUMENT_INVOICE_ITEM_CODE_PROPERTY + ";" + CustomerInvoiceItemCode.class.getName());
        forcedLookupOptionalFields.put(ArConstants.CUSTOMER_INVOICE_DOCUMENT_UNIT_OF_MEASURE_PROPERTY, ArConstants.UNIT_OF_MEASURE_PROPERTY + ";" + UnitOfMeasure.class.getName());

        return forcedLookupOptionalFields;
    }

    /**
     * Make amount and sales tax read only
     * 
     * @see org.kuali.core.web.struts.form.KualiTransactionalDocumentFormBase#getForcedReadOnlyFields()
     */
    @Override
    public Map getForcedReadOnlyFields() {
        Map map = super.getForcedReadOnlyFields();
        map.put(KFSPropertyConstants.AMOUNT, Boolean.TRUE);
        map.put("invoiceItemTaxAmount", Boolean.TRUE);
        return map;
    }
}
