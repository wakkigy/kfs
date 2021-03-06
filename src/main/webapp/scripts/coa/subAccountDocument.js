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

function onblur_subAccountTypeCode( subAccountTypeCodeField ) {
	// need to call findElPrefix twice to strip off the a21SubAccount prefix
	var subAccountTypeCodeFieldName = subAccountTypeCodeField.name;
	var fieldPrefix = findElPrefix( findElPrefix(subAccountTypeCodeFieldName) );
	var chartCodeFieldName = fieldPrefix + ".chartOfAccountsCode";
	var accountNumberFieldName = fieldPrefix + ".accountNumber";
	
	//alert("chartCodeFieldName = " + chartCodeFieldName + ", accountNumberFieldName = " + accountNumberFieldName + ", subAccountTypeCodeFieldName = " + subAccountTypeCodeFieldName);
	updateCgIcrAccount(chartCodeFieldName, accountNumberFieldName, subAccountTypeCodeFieldName);
}
 
/* This function is for the primary key account number in SubAccount BO. */
function onblur_accountNumberPK( accountNumberField ) {
	var accountNumberFieldName = accountNumberField.name;
	var fieldPrefix = findElPrefix(accountNumberFieldName);
	var chartCodeFieldName = fieldPrefix + ".chartOfAccountsCode";
	var subAccountTypeCodeFieldName = fieldPrefix + ".a21SubAccount.subAccountTypeCode";		
	//alert("chartCodeFieldName = " + chartCodeFieldName + ", accountNumberFieldName = " + accountNumberFieldName + ", subAccountTypeCodeFieldName = " + subAccountTypeCodeFieldName);
	
	var dwrReply = {
		callback: function (param) {
			if ( typeof param == "boolean" && param == false) {	
				loadChartCode(chartCodeFieldName, accountNumberFieldName, subAccountTypeCodeFieldName, true);
			}
			else {
				updateCgIcrAccount(chartCodeFieldName, accountNumberFieldName, subAccountTypeCodeFieldName);				
			}
		},	
		errorHandler:function( errorMessage ) { 
			window.status = errorMessage;
		}
	};
	AccountService.accountsCanCrossCharts(dwrReply);			
}

function updateCgIcrAccount( chartCodeFieldName, accountNumberFieldName, subAccountTypeCodeFieldName ) {	
	var prefix = "document.newMaintainableObject.a21SubAccount";

	// check if the current user has permissions to the ICR fields
	if ( kualiElements[prefix+".financialIcrSeriesIdentifier"].type.toLowerCase() == "hidden" ) {
		return;
	}
	
	var chartCode = getElementValue( chartCodeFieldName );
	var accountNumber = getElementValue( accountNumberFieldName );
	var subAccountTypeCode = getElementValue( subAccountTypeCodeFieldName );
	//alert("chartCode = " + chartCode + ", accountNumber = " + accountNumber + ", subAccountTypeCode = " + subAccountTypeCode);
	
	if (chartCode == "" || accountNumber == "" || subAccountTypeCode == "") {
		setElementValue( prefix + ".financialIcrSeriesIdentifier", "" );
//		setElementValue( prefix + ".indirectCostRcvyFinCoaCode", "" );
//		setElementValue( prefix + ".indirectCostRecoveryAcctNbr", "" );
		setElementValue( prefix + ".offCampusCode", "" );
		setElementValue( prefix + ".indirectCostRecoveryTypeCode", "" );
	}
	else {
		var dwrReply = {
			callback:updateCgIcrAccount_Callback,
			errorHandler:function( errorMessage ) { 
				window.status = errorMessage;
			}
		};	
		A21SubAccountService.buildCgIcrAccount( chartCode, accountNumber, null, subAccountTypeCode, dwrReply );
	}
}

function updateCgIcrAccount_Callback( data ) {	
	var prefix = "document.newMaintainableObject.a21SubAccount";
	
	if (data != null) {
		setElementValue( prefix + ".financialIcrSeriesIdentifier", data.financialIcrSeriesIdentifier );
//		setElementValue( prefix + ".indirectCostRcvyFinCoaCode", data.indirectCostRcvyFinCoaCode );
//		setElementValue( prefix + ".indirectCostRecoveryAcctNbr", data.indirectCostRecoveryAcctNbr );
		setElementValue( prefix + ".offCampusCode", data.offCampusCode );
		setElementValue( prefix + ".indirectCostRecoveryTypeCode", data.indirectCostRecoveryTypeCode );
	}
	else{
		setElementValue( prefix + ".financialIcrSeriesIdentifier", "" );
//		setElementValue( prefix + ".indirectCostRcvyFinCoaCode", "" );
//		setElementValue( prefix + ".indirectCostRecoveryAcctNbr", "" );
		setElementValue( prefix + ".offCampusCode", "" );
		setElementValue( prefix + ".indirectCostRecoveryTypeCode", "" );
	}
}

/* This function is for the a21SubAccount account numbers in SubAccount BO. */
function onblur_accountNumberA21Sub( accountNumberField, chartCodePropertyName ) {
	// need to call findElPrefix twice to strip off the a21SubAccount prefix
	var accountNumberFieldName = accountNumberField.name;
	var fieldPrefix = findElPrefix( findElPrefix(accountNumberFieldName) );
	var chartCodeFieldName = fieldPrefix + "." + chartCodePropertyName;
	//alert("chartCodeFieldName = " + chartCodeFieldName + ", accountNumberFieldName = " + accountNumberFieldName);

	var dwrReply = {
		callback: function (param) {
			if ( typeof param == "boolean" && param == false) {	
				loadChartCode(chartCodeFieldName, accountNumberFieldName, null, false);
			}
		},	
		errorHandler:function( errorMessage ) { 
			window.status = errorMessage;
		}
	};
	AccountService.accountsCanCrossCharts(dwrReply);	
}

function loadChartCode( chartCodeFieldName, accountNumberFieldName, subAccountTypeCodeFieldName, shouldUpdateCgIcr) {
    var accountNumber = getElementValue( accountNumberFieldName );	

    if (accountNumber == "") {
		clearRecipients(chartCodeFieldName);    		
	}
	else {
		var dwrReply = {
				callback: function (data) {
				//alert("chartCode = " + data.chartOfAccountsCode + ", accountNumber = " + accountNumber);
				if ( data != null && typeof data == "object" ) {   
					setRecipientValue( chartCodeFieldName, data.chartOfAccountsCode );
				}
				else {
					clearRecipients(chartCodeFieldName); 
				}
				// call updateCgIcrAccount here instead of in onblur_accountNumberPK, because DWR calls are asynchronized,
				// so need to make sure to wait till chartCodeField is set before calling updateCgIcrAccount
				if (shouldUpdateCgIcr == true) {
					updateCgIcrAccount( chartCodeFieldName, accountNumberFieldName, subAccountTypeCodeFieldName );
				}
			},
			errorHandler:function( errorMessage ) { 
				clearRecipients(chartCodeFieldName); 
				window.status = errorMessage;
			}
		};
		AccountService.getUniqueAccountForAccountNumber( accountNumber, dwrReply );	    
	}
}

/**
 * Gets the value with the given field name of an element, which can be readOnly, and in which case it won't be one of the
 * input fields and can't be retrieved using getElementValue; instead we need to use the element ID to retrieve its value.
 * This function also filters out white spaces as well as any URL links that might be associated with the field.
 * @param fieldName the given field name.
 * @return the field value
 *
function getElementValuePossiblyReadOnly(fieldName) {
	// retrieve html element using fieldName as id
	var field = document.getElementById(fieldName);
	var fieldValue = null;
	//alert("getElementById field = " + field);
	
	if ( field != null ) {
		// if the element id as fieldName exists, then it's an input field, get its value directly
		fieldValue = field.value;
		//alert("fieldValue = " + fieldValue);
	}
	else {
		// otherwise the field is readOnly and its id is fieldName.div 
		fieldValue = dwr.util.getValue(fieldName + ".div");
		//alert("DWR getValue = " + fieldValue);
		
		// sometimes readOnly field is rendered with a URL link, strip that off
		fieldValue = fieldValue.replace(/(<([^>]+)>)/ig,"");
		//alert("After striping URL, fieldValue = " + fieldValue);
		
		// trim &nbsp's and white spaces i
		fieldValue = fieldValue.replace("&nbsp;", "").replace(/^\s+|\s+$/g,"");
		//alert("After striping spaces, fieldValue = " + fieldValue);		
	}
	
	return fieldValue;
}
*/
