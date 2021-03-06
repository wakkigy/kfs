/*
 * The Kuali Financial System, a comprehensive financial management system for higher education.
 * 
 * Copyright 2005-2014 The Kuali Foundation
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kuali.kfs.pdp;

/**
 * Contains PDP parameter constants
 */
public class PdpParameterConstants {
    public static final String DISBURSEMENT_CANCELLATION_DAYS = "DISBURSEMENT_CANCELLATION_DAYS";
    public static final String PAYMENT_LOAD_FAILURE_EMAIL_SUBJECT_PARAMETER_NAME = "FAILURE_EMAIL_SUBJECT";
    public static final String PAYMENT_LOAD_SUCCESS_EMAIL_SUBJECT_PARAMETER_NAME = "SUCCESS_EMAIL_SUBJECT";
    public static final String PAYMENT_LOAD_THRESHOLD_EMAIL_SUBJECT_PARAMETER_NAME = "THRESHOLD_EMAIL_SUBJECT";
    public static final String PAYMENT_LOAD_TAX_EMAIL_SUBJECT_PARAMETER_NAME = "TAX_EMAIL_SUBJECT";
    public static final String ACH_SUMMARY_EMAIL_SUBJECT_PARAMETER_NAME = "ACH_SUMMARY_EMAIL_SUBJECT";
    public static final String ACH_SUMMARY_TO_EMAIL_ADDRESS_PARMAETER_NAME = "ACH_SUMMARY_TO_EMAIL_ADDRESSES";
    public static final String ACH_SUMMARY_CC_EMAIL_ADDRESSES_PARMAETER_NAME = "ACH_SUMMARY_CC_EMAIL_ADDRESSES";
    public static final String ACH_SUMMARY_BCC_EMAIL_ADDRESSES_PARMAETER_NAME = "ACH_SUMMARY_BCC_EMAIL_ADDRESSES";
    public static final String CORPORATION_OWNERSHIP_TYPE_PARAMETER_NAME  = "CORPORATION_OWNERSHIP_TYPE";
    public static final String TAXABLE_PAYMENT_REASON_CODES_BY_OWNERSHIP_CODES_PARAMETER_NAME = "TAXABLE_PAYMENT_REASON_CODES_BY_OWNERSHIP_CODES";
    public static final String NON_TAXABLE_PAYMENT_REASON_CODES_BY_OWNERSHIP_CODES_PARAMETER_NAME = "NON_TAXABLE_PAYMENT_REASON_CODES_BY_OWNERSHIP_CODES";
    public static final String TAXABLE_PAYMENT_REASON_CODES_FOR_BLANK_CORPORATION_OWNERSHIP_TYPE_CATEGORIES_PARAMETER_NAME = "TAXABLE_PAYMENT_REASON_CODES_FOR_BLANK_CORPORATION_OWNERSHIP_TYPE_CATEGORIES";
    public static final String TAXABLE_PAYMENT_REASON_CODES_BY_CORPORATION_OWNERSHIP_TYPE_CATEGORY_PARAMETER_NAME = "TAXABLE_PAYMENT_REASON_CODES_BY_CORPORATION_OWNERSHIP_TYPE_CATEGORY";
    public static final String NON_TAXABLE_PAYMENT_REASON_CODES_BY_CORPORATION_OWNERSHIP_TYPE_CATEGORY_PARAMETER_NAME = "NON_TAXABLE_PAYMENT_REASON_CODES_BY_CORPORATION_OWNERSHIP_TYPE_CATEGORY";

    public static String FEDERAL_ACH_BANK_FILE_URL = "FEDERAL_ACH_BANK_FILE_URL";
    public static String ACH_BANK_INPUT_FILE = "ACH_BANK_INPUT_FILE";

    public static String HARD_EDIT_CC = "HARD_EDIT_TO_EMAIL_ADDRESSES";
    public static String SOFT_EDIT_CC = "SOFT_EDIT_TO_EMAIL_ADDRESSES";
    public static String SEND_ACH_EMAIL_NOTIFICATION = "SEND_ACH_EMAIL_NOTIFICATION_IND";
    public static String PDP_ERROR_EXCEEDS_NOTE_LIMIT_EMAIL = "ERROR_EXCEEDS_NOTE_LIMIT_EMAIL_ADDRESSES";

    public static String TAX_GROUP_EMAIL_ADDRESS = "TAX_GROUP_TO_EMAIL_ADDRESSES";
    public static String TAX_CANCEL_EMAIL_LIST = "TAX_CANCEL_TO_EMAIL_ADDRESSES";
    public static String TAX_CANCEL_CONTACT = "TAX_CONTACT";

    public static String MAX_NOTE_LINES = "MAX_NOTE_LINES";
    public static String FORMAT_SUMMARY_ROWS = "FORMAT_SUMMARY_REVIEW_RESULTS_PER_PAGE";

    public static class BatchConstants{
        public static final String BATCH_ID_PARAM = "batchId";
    }

    public static class PaymentDetail {
        public static final String DETAIL_ID_PARAM = "detailId";
        public static final String SHOW_PAYMENT_DETAIL = "showPaymentDetail";
    }

    public static class FormatProcess {
        public static final String PROCESS_ID_PARAM = "processId";
    }

    public static final String MESSAGE_PARAM = "message";
    public static final String ERROR_KEY_LIST_PARAM = "errorKeyList";
    public static final String ERROR_KEY_LIST_SEPARATOR = ";";
    public static final String ACTION_SUCCESSFUL_PARAM = "actionSuccesful";
}
