/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.kfs.module.tem.document.validation.impl;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.kuali.kfs.module.tem.TemKeyConstants;
import org.kuali.kfs.module.tem.TemPropertyConstants;
import org.kuali.kfs.module.tem.businessobject.ActualExpense;
import org.kuali.kfs.module.tem.businessobject.PerDiemExpense;
import org.kuali.kfs.module.tem.businessobject.TemTravelExpenseTypeCode;
import org.kuali.kfs.module.tem.document.TravelDocument;
import org.kuali.kfs.module.tem.document.validation.event.AddActualExpenseLineEvent;
import org.kuali.kfs.sys.KFSKeyConstants;
import org.kuali.kfs.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.kns.util.DateUtils;
import org.kuali.rice.kns.util.GlobalVariables;
import org.kuali.rice.kns.util.KualiDecimal;
import org.kuali.rice.kns.util.ObjectUtils;

public class TravelDocumentActualExpenseLineValidation extends TEMDocumentExpenseLineValidation {
    
    /**
     * 
     */
    public TravelDocumentActualExpenseLineValidation() {
        super();
    }

    /**
     * @see org.kuali.kfs.sys.document.validation.Validation#validate(org.kuali.kfs.sys.document.validation.event.AttributedDocumentEvent)
     */
    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean success = true;

        final ActualExpense actualExpense = (ActualExpense) ((AddActualExpenseLineEvent<ActualExpense>) event).getExpenseLine();
        TravelDocument document = (TravelDocument) ((AddActualExpenseLineEvent<ActualExpense>) event).getDocument();

        success = getDictionaryValidationService().isBusinessObjectValid(actualExpense);
        if (success) {
            final boolean isWarning = true;
            success = validateExpenses(actualExpense, document, isWarning);
        }
        return success;
    }
    
    /**
     * Validate the expense
     * 
     * @param expense
     * @param document
     * @return
     */
    public boolean validateExpenses(ActualExpense expense, TravelDocument document, boolean isWarning) {

         boolean success = (validateGeneralRules(expense, document) &&
                validateExpenseDetail(expense, true) &&
                validateAirfareRules(expense, document) &&        
                validateRentalCarRules(expense, document) && 
                validateLodgingRules(expense, document) &&
                validateLodgingAllowanceRules(expense, document) &&
                validatePerDiemRules(expense, document) &&
                validateMaximumAmountRules(expense, document));

        return success;
    }

    /**
     * This method validates following rules 
     * 
     * 1.Validates notes field if indicator set to true 
     * 2.Validates expense amount if expense type is not mileage 
     * 3.Validates currency rate 
     * 4.Validates duplicate entries
     * 
     * @param actualExpense
     * @param document
     * @return boolean
     */
    public boolean validateGeneralRules(ActualExpense actualExpense, TravelDocument document) {
        boolean success = true;
        TemTravelExpenseTypeCode expenseTypeCode = actualExpense.getTravelExpenseTypeCode();

        //validate note required
        if (ObjectUtils.isNotNull(expenseTypeCode) && ObjectUtils.isNotNull(expenseTypeCode.getNoteRequired()) 
                && expenseTypeCode.getNoteRequired() && StringUtils.isEmpty(actualExpense.getDescription())) {
            success = false;
            GlobalVariables.getMessageMap().putError(TemPropertyConstants.TEM_ACTUAL_EXPENSE_NOTCE, KFSKeyConstants.ERROR_REQUIRED, "Notes for expense type " + actualExpense.getTravelExpenseTypeCodeCode());
        }
        
        //validate expense amount greater than 0
        if (!actualExpense.isMileage()) {
            if (actualExpense.getExpenseAmount().isNegative()) {
                GlobalVariables.getMessageMap().putError(TemPropertyConstants.EXPENSE_AMOUNT, KFSKeyConstants.ERROR_ZERO_OR_NEGATIVE_AMOUNT, "Expense Amount");
                success = false;
            }
        }
          
        //validate currency rate is greater than 0
        if (actualExpense.getCurrencyRate().isNegative()) {
            GlobalVariables.getMessageMap().putError(TemPropertyConstants.CURRENCY_RATE, KFSKeyConstants.ERROR_ZERO_OR_NEGATIVE_AMOUNT, "Currency Rate");
            success = false;
        }
        
        //validate duplicate entry
        if (isDuplicateEntry(actualExpense, document)) {
            success = false;
            if (expenseTypeCode != null && expenseTypeCode.isPerDaily()) {
                GlobalVariables.getMessageMap().putError(TemPropertyConstants.EXPENSE_AMOUNT, TemKeyConstants.ERROR_ACTUAL_EXPENSE_DUPLICATE_ENTRY_DAILY, actualExpense.getTravelExpenseTypeCodeCode());
            }
            else {
                GlobalVariables.getMessageMap().putError(TemPropertyConstants.EXPENSE_AMOUNT, TemKeyConstants.ERROR_ACTUAL_EXPENSE_DUPLICATE_ENTRY, actualExpense.getTravelExpenseTypeCodeCode(), actualExpense.getExpenseDate().toString());
            }
        }

        return success;
    }
    

    /**
     * Validate if the expense is required to have detail to be entered.  Detail requirement is defined in the travel expense type code
     * table
     * 
     * @param actualExpense
     * @param isWarning
     * @return
     */
    public boolean validateExpenseDetail(ActualExpense actualExpense, boolean isWarning) {
        boolean success = true;
        actualExpense.refreshReferenceObject("travelExpenseTypeCode");
        TemTravelExpenseTypeCode expenseType = actualExpense.getTravelExpenseTypeCode();

        if (ObjectUtils.isNotNull(expenseType)){
            if (expenseType.getExpenseDetailRequired() && actualExpense.getExpenseDetails().isEmpty()){
                //detail is required when adding the expense
                if (isWarning){
                    GlobalVariables.getMessageMap().putWarning(TemPropertyConstants.TEM_ACTUAL_EXPENSE_DETAIL, TemKeyConstants.ERROR_ACTUAL_EXPENSE_DETAIL_REQUIRED, expenseType.getName());
                }else{
                    GlobalVariables.getMessageMap().putError(TemPropertyConstants.TEM_ACTUAL_EXPENSE_DETAIL, TemKeyConstants.ERROR_ACTUAL_EXPENSE_DETAIL_REQUIRED, expenseType.getName());
                    success = false;                    
                }
            }
        }
        return success;
    }

    /**
     * This method validates following rules 1.Validates user entered amount with max amount & max amount per configured in
     * database(daily & occurrence).
     * 
     * @param actualExpense
     * @param document
     * @return boolean
     */
    public boolean validateMaximumAmountRules(ActualExpense actualExpense, TravelDocument document) {
        boolean success = true;
        TemTravelExpenseTypeCode expenseTypeCode = actualExpense.getTravelExpenseTypeCode();

        KualiDecimal maxAmount = getMaximumAmount(actualExpense, document);
        if (maxAmount.isNonZero() && maxAmount.subtract(getTotalExpenseAmount(actualExpense, document)).isNegative()) {
            if (expenseTypeCode.isPerDaily()) {
                success = false;
                GlobalVariables.getMessageMap().putError(TemPropertyConstants.EXPENSE_AMOUNT, TemKeyConstants.ERROR_ACTUAL_EXPENSE_MAX_AMT_PER_DAILY, expenseTypeCode.getMaximumAmount().toString());
            }
            else if (expenseTypeCode.isPerOccurrence()) {
                success = false;
                GlobalVariables.getMessageMap().putError(TemPropertyConstants.EXPENSE_AMOUNT, TemKeyConstants.ERROR_ACTUAL_EXPENSE_MAX_AMT_PER_OCCU, expenseTypeCode.getMaximumAmount().toString());
            }
        }
        return success;
    }

    /**
     * This method validates following rules 
     * <ol>
     *  <li>Validates if lodging allowance is entered for the same day</li>
     *  <li>Per Diem lodging allowance can also not be entered on the same day</li>
     * </ol>
     * 
     * Warn if there is any lodging allowance in the Per Diem section
     * 
     * @param actualExpense
     * @param document
     * @return boolean
     */
    public boolean validateLodgingRules(ActualExpense actualExpense, TravelDocument document) {
        boolean success = true;

        if (actualExpense.isLodging()) {
            if (isLodgingAllowanceEntered(actualExpense, document)) {
                success = false;
                GlobalVariables.getMessageMap().putError(TemPropertyConstants.CURRENCY_RATE, TemKeyConstants.ERROR_ACTUAL_EXPENSE_LODGING_ENTERED);
            }
        }

        return success;
    }

    /**
     * This method validated following rules 
     * 
     * 1. Validates if lodging allowance is entered for the same day
     * 2. Per Diem lodging allowance can also not be entered on the same day
     * 
     * Warn if there is any lodging allowance in the Per Diem section
     * 
     * @param actualExpense
     * @param document
     * @return boolean
     */
    public boolean validateLodgingAllowanceRules(ActualExpense actualExpense, TravelDocument document) {
        boolean success = true;
        
        if (actualExpense.isLodgingAllowance()) {
            TemTravelExpenseTypeCode expenseTypeCode = actualExpense.getTravelExpenseTypeCode();

            KualiDecimal maxAmount = ObjectUtils.isNotNull(expenseTypeCode) && ObjectUtils.isNotNull(expenseTypeCode.getMaximumAmount()) ? expenseTypeCode.getMaximumAmount() : KualiDecimal.ZERO;
            GlobalVariables.getMessageMap().putInfo(TemPropertyConstants.TEM_ACTUAL_EXPENSE_NOTCE, TemKeyConstants.INFO_TEM_ACTUAL_EXPENSE_LODGING_ALLOWANCE, maxAmount.toString());

            if (isLodgingEntered(actualExpense, document)) {
                success = false;
                GlobalVariables.getMessageMap().putError(TemPropertyConstants.CURRENCY_RATE, TemKeyConstants.ERROR_ACTUAL_EXPENSE_LODGING_ENTERED);
            }
            
            if (isPerDiemLodgingEntered(document.getPerDiemExpenses())) {
                GlobalVariables.getMessageMap().putWarning(TemPropertyConstants.TEM_ACTUAL_EXPENSE_NOTCE, TemKeyConstants.WARNING_PERDIEM_EXPENSE_LODGING_ENTERED);
            }
        }

        return success;
    }

    /**
     * 
     * @param ote
     * @param document
     * @return
     */
    private Boolean isLodgingAllowanceEntered(ActualExpense ote, TravelDocument document) {
        for (ActualExpense actualExpense : document.getActualExpenses()) {
            if (actualExpense.isLodgingAllowance() 
                    && (!ote.equals(actualExpense))
                    && (ote.getExpenseDate() != null 
                            && ote.getExpenseDate().equals(actualExpense.getExpenseDate()))) {
                return true;
            }
        }

        for (PerDiemExpense perDiemExpense : document.getPerDiemExpenses()) {
            if (KualiDecimal.ZERO.isLessThan(perDiemExpense.getLodging())
                    && (ote.getExpenseDate() != null 
                            && ote.getExpenseDate().equals(perDiemExpense.getMileageDate()))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @param ote
     * @param document
     * @return
     */
    private Boolean isLodgingEntered(ActualExpense ote, TravelDocument document) {
        for (ActualExpense actualExpense : document.getActualExpenses()) {
            if (actualExpense.isLodging()
                    && (!ote.equals(actualExpense))
                    && (ote.getExpenseDate() != null  
                            && ote.getExpenseDate().equals(actualExpense.getExpenseDate()))) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * Checking duplicate on expense (non-details) if they are duplicates
     * 
     * @param ote
     * @param document
     * @return
     */
    private Boolean isDuplicateEntry(ActualExpense expense, TravelDocument document) {
        TemTravelExpenseTypeCode expenseTypeCode = expense.getTravelExpenseTypeCode();

        //do a check if its coming out of the document expense list - this will happen during route validation
        if (!document.getActualExpenses().contains(expense)){
            for (ActualExpense actualExpense : document.getActualExpenses()) {
    
                if (expenseTypeCode != null && expenseTypeCode.isPerDaily()) {
                    if (actualExpense.getTravelExpenseTypeCodeCode().equals(expense.getTravelExpenseTypeCodeCode())) {
                        return Boolean.TRUE;
                    }
                }
                else {
                    if (expense.getExpenseDate() != null 
                            && expense.getExpenseDate().equals(actualExpense.getExpenseDate()) 
                            && actualExpense.getTravelExpenseTypeCodeCode().equals(expense.getTravelExpenseTypeCodeCode())) {
                        return Boolean.TRUE;
                    }
                }
            }
        }
        
        return Boolean.FALSE;
    }

    /**
     * Get maximum amount 
     * 
     * @param actualExpense
     * @param document
     * @return
     */
    private KualiDecimal getMaximumAmount(ActualExpense actualExpense, TravelDocument document) {
        KualiDecimal maxAmount = KualiDecimal.ZERO;
        TemTravelExpenseTypeCode expenseTypeCode = actualExpense.getTravelExpenseTypeCode();
        
        if (expenseTypeCode != null && expenseTypeCode.getMaximumAmount() != null) {
            if (expenseTypeCode.isPerDaily()) {
                if (document.getTripBegin() != null && document.getTripEnd() != null) {
                    double noOfDays = DateUtils.getDifferenceInDays(new Timestamp(document.getTripBegin().getTime()), new Timestamp(document.getTripEnd().getTime()));
                    maxAmount = expenseTypeCode.getMaximumAmount().multiply(new KualiDecimal(noOfDays));
                }
            }
            else if (expenseTypeCode.isPerOccurrence()) {
                maxAmount = expenseTypeCode.getMaximumAmount();
            }
            else {
                maxAmount = expenseTypeCode.getMaximumAmount();
            }
        }
        //add the group travler list + self (1)
        KualiDecimal groupTravelMultipier = new KualiDecimal(document.getGroupTravelers().size() + 1);
        return maxAmount.multiply(groupTravelMultipier);
    }

    /**
     * @param ote
     * @param document
     * @return
     */
    private KualiDecimal getTotalExpenseAmount(ActualExpense ote, TravelDocument document) {
        KualiDecimal totalExpenseAmount = KualiDecimal.ZERO;

        for (ActualExpense actualExpense : document.getActualExpenses()) {
            if ((!ote.equals(actualExpense)) && ote.getTravelExpenseTypeCodeCode().equals(actualExpense.getTravelExpenseTypeCodeCode())) {
                totalExpenseAmount = totalExpenseAmount.add(actualExpense.getExpenseAmount());
            }
        }
        return totalExpenseAmount.add(ote.getExpenseAmount());
    }
}
