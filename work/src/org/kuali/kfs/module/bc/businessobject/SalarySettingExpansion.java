/*
 * Copyright 2006-2007 The Kuali Foundation.
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

package org.kuali.module.budget.bo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.kuali.core.bo.PersistableBusinessObjectBase;
import org.kuali.core.util.KualiDecimal;
import org.kuali.core.util.TypedArrayList;
import org.kuali.kfs.KFSPropertyConstants;
import org.kuali.module.chart.bo.Account;
import org.kuali.module.chart.bo.Chart;
import org.kuali.module.chart.bo.ObjectCode;
import org.kuali.module.chart.bo.ObjectType;
import org.kuali.module.chart.bo.SubAccount;
import org.kuali.module.chart.bo.SubObjCd;
import org.kuali.module.chart.bo.codes.BalanceTyp;
import org.kuali.module.gl.bo.Balance;
import org.kuali.module.labor.bo.LaborObject;
import org.kuali.module.labor.bo.PositionObjectBenefit;
import org.kuali.rice.KNSServiceLocator;

/**
 * TODO is this needed??? probably need to just point OJB repository to PBGL class or
 * this should extend PBGL if something extra is needed
 * 
 */
public class SalarySettingExpansion extends PersistableBusinessObjectBase {

    private String documentNumber;
    private Integer universityFiscalYear;
    private String chartOfAccountsCode;
    private String accountNumber;
    private String subAccountNumber;
    private String financialObjectCode;
    private String financialSubObjectCode;
    private String financialBalanceTypeCode;
    private String financialObjectTypeCode;
    private KualiDecimal accountLineAnnualBalanceAmount;
    private KualiDecimal financialBeginningBalanceLineAmount;
    
    // Total Fields - First Total Line
    private KualiDecimal csfAmountTotal;
    private BigDecimal csfFullTimeEmploymentQuantityTotal;
    private KualiDecimal appointmentRequestedAmountTotal;
    private BigDecimal appointmentRequestedFteQuantityTotal;
    private KualiDecimal percentChangeTotal;
    
    // Total Fields - Second Total Line
    private KualiDecimal objectBaseCsfAmountTotal;
    private KualiDecimal objectBaseAppointmentRequestedAmountTotal;
    private KualiDecimal objectBasePercentChangeTotal;
    

    private BudgetConstructionHeader budgetConstructionHeader;
    private ObjectCode financialObject;
    private Chart chartOfAccounts;
    private Account account;
    private SubAccount subAccount;
    private SubObjCd financialSubObject;
    private BalanceTyp balanceType;
    private ObjectType objectType;

    private List pendingBudgetConstructionAppointmentFunding;
    
    //TODO These are only used by PBGLExpenditureLines so should probably put these in an extension class
    // These are not defined under ojb since not all expenditure line objects have these
    private LaborObject laborObject;
    private List<PositionObjectBenefit> positionObjectBenefit;

    private KualiDecimal percentChange;
    
    /**
     * Default constructor.
     */
    public SalarySettingExpansion() {
        setPendingBudgetConstructionAppointmentFunding(new TypedArrayList(PendingBudgetConstructionAppointmentFunding.class));
        setPercentChange(null);
        zeroTotals();

    }

    /**
     * 
     * Zeros the totals appearing on the Salary Setting Screen
     */
    public void zeroTotals() {

        csfAmountTotal = new KualiDecimal(0);
        csfFullTimeEmploymentQuantityTotal = new BigDecimal(0).setScale(5,BigDecimal.ROUND_HALF_EVEN);
        appointmentRequestedAmountTotal = new KualiDecimal(0.00);
        appointmentRequestedFteQuantityTotal = new BigDecimal(0).setScale(5,BigDecimal.ROUND_HALF_EVEN);
        percentChangeTotal = new KualiDecimal(0.00);
        objectBaseCsfAmountTotal = new KualiDecimal(0);
        objectBaseAppointmentRequestedAmountTotal = new KualiDecimal(0.00);
        objectBasePercentChangeTotal = new KualiDecimal(0);
    }


    
    /**
     * Sets the percentChange attribute value.
     * @param percentChange The percentChange to set.
     * @deprecated
     */
    public void setPercentChange(KualiDecimal percentChange) {
        this.percentChange = percentChange;
    }

    /**
     * Gets the documentNumber attribute.
     * 
     * @return Returns the documentNumber
     * 
     */
    public String getDocumentNumber() { 
        return documentNumber;
    }

    /**
     * Sets the documentNumber attribute.
     * 
     * @param documentNumber The documentNumber to set.
     * 
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }


    /**
     * Gets the universityFiscalYear attribute.
     * 
     * @return Returns the universityFiscalYear
     * 
     */
    public Integer getUniversityFiscalYear() { 
        return universityFiscalYear;
    }

    /**
     * Sets the universityFiscalYear attribute.
     * 
     * @param universityFiscalYear The universityFiscalYear to set.
     * 
     */
    public void setUniversityFiscalYear(Integer universityFiscalYear) {
        this.universityFiscalYear = universityFiscalYear;
    }


    /**
     * Gets the chartOfAccountsCode attribute.
     * 
     * @return Returns the chartOfAccountsCode
     * 
     */
    public String getChartOfAccountsCode() { 
        return chartOfAccountsCode;
    }

    /**
     * Sets the chartOfAccountsCode attribute.
     * 
     * @param chartOfAccountsCode The chartOfAccountsCode to set.
     * 
     */
    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }


    /**
     * Gets the accountNumber attribute.
     * 
     * @return Returns the accountNumber
     * 
     */
    public String getAccountNumber() { 
        return accountNumber;
    }

    /**
     * Sets the accountNumber attribute.
     * 
     * @param accountNumber The accountNumber to set.
     * 
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    /**
     * Gets the subAccountNumber attribute.
     * 
     * @return Returns the subAccountNumber
     * 
     */
    public String getSubAccountNumber() { 
        return subAccountNumber;
    }

    /**
     * Sets the subAccountNumber attribute.
     * 
     * @param subAccountNumber The subAccountNumber to set.
     * 
     */
    public void setSubAccountNumber(String subAccountNumber) {
        this.subAccountNumber = subAccountNumber;
    }


    /**
     * Gets the financialObjectCode attribute.
     * 
     * @return Returns the financialObjectCode
     * 
     */
    public String getFinancialObjectCode() { 
        return financialObjectCode;
    }

    /**
     * Sets the financialObjectCode attribute.
     * 
     * @param financialObjectCode The financialObjectCode to set.
     * 
     */
    public void setFinancialObjectCode(String financialObjectCode) {
        this.financialObjectCode = financialObjectCode;
    }


    /**
     * Gets the financialSubObjectCode attribute.
     * 
     * @return Returns the financialSubObjectCode
     * 
     */
    public String getFinancialSubObjectCode() { 
        return financialSubObjectCode;
    }

    /**
     * Sets the financialSubObjectCode attribute.
     * 
     * @param financialSubObjectCode The financialSubObjectCode to set.
     * 
     */
    public void setFinancialSubObjectCode(String financialSubObjectCode) {
        this.financialSubObjectCode = financialSubObjectCode;
    }


    /**
     * Gets the financialBalanceTypeCode attribute.
     * 
     * @return Returns the financialBalanceTypeCode
     * 
     */
    public String getFinancialBalanceTypeCode() { 
        return financialBalanceTypeCode;
    }

    /**
     * Sets the financialBalanceTypeCode attribute.
     * 
     * @param financialBalanceTypeCode The financialBalanceTypeCode to set.
     * 
     */
    public void setFinancialBalanceTypeCode(String financialBalanceTypeCode) {
        this.financialBalanceTypeCode = financialBalanceTypeCode;
    }


    /**
     * Gets the financialObjectTypeCode attribute.
     * 
     * @return Returns the financialObjectTypeCode
     * 
     */
    public String getFinancialObjectTypeCode() { 
        return financialObjectTypeCode;
    }

    /**
     * Sets the financialObjectTypeCode attribute.
     * 
     * @param financialObjectTypeCode The financialObjectTypeCode to set.
     * 
     */
    public void setFinancialObjectTypeCode(String financialObjectTypeCode) {
        this.financialObjectTypeCode = financialObjectTypeCode;
    }


    /**
     * Gets the accountLineAnnualBalanceAmount attribute.
     * 
     * @return Returns the accountLineAnnualBalanceAmount
     * 
     */
    public KualiDecimal getAccountLineAnnualBalanceAmount() { 
        return accountLineAnnualBalanceAmount;
    }

    /**
     * Sets the accountLineAnnualBalanceAmount attribute.
     * 
     * @param accountLineAnnualBalanceAmount The accountLineAnnualBalanceAmount to set.
     * 
     */
    public void setAccountLineAnnualBalanceAmount(KualiDecimal accountLineAnnualBalanceAmount) {
        this.accountLineAnnualBalanceAmount = accountLineAnnualBalanceAmount;
    }


    /**
     * Gets the financialBeginningBalanceLineAmount attribute.
     * 
     * @return Returns the financialBeginningBalanceLineAmount
     * 
     */
    public KualiDecimal getFinancialBeginningBalanceLineAmount() { 
        return financialBeginningBalanceLineAmount;
    }

    /**
     * Sets the financialBeginningBalanceLineAmount attribute.
     * 
     * @param financialBeginningBalanceLineAmount The financialBeginningBalanceLineAmount to set.
     * 
     */
    public void setFinancialBeginningBalanceLineAmount(KualiDecimal financialBeginningBalanceLineAmount) {
        this.financialBeginningBalanceLineAmount = financialBeginningBalanceLineAmount;
    }


    /**
     * Gets the pendingBudgetConstructionAppointmentFunding attribute. 
     * @return Returns the pendingBudgetConstructionAppointmentFunding.
     */
    public List<PendingBudgetConstructionAppointmentFunding> getPendingBudgetConstructionAppointmentFunding() {
        return pendingBudgetConstructionAppointmentFunding;
    }

    /**
     * Sets the pendingBudgetConstructionAppointmentFunding attribute value.
     * @param pendingBudgetConstructionAppointmentFunding The pendingBudgetConstructionAppointmentFunding to set.
     * @deprecated
     */
    public void setPendingBudgetConstructionAppointmentFunding(List<PendingBudgetConstructionAppointmentFunding> pendingBudgetConstructionAppointmentFunding) {
        this.pendingBudgetConstructionAppointmentFunding = pendingBudgetConstructionAppointmentFunding;
    }

    /**
     * Gets the financialObject attribute.
     * 
     * @return Returns the financialObject
     * 
     */
    public ObjectCode getFinancialObject() { 
        return financialObject;
    }

    /**
     * Sets the financialObject attribute.
     * 
     * @param financialObject The financialObject to set.
     * @deprecated
     */
    public void setFinancialObject(ObjectCode financialObject) {
        this.financialObject = financialObject;
    }

    /**
     * Gets the chartOfAccounts attribute.
     * 
     * @return Returns the chartOfAccounts
     * 
     */
    public Chart getChartOfAccounts() { 
        return chartOfAccounts;
    }

    /**
     * Sets the chartOfAccounts attribute.
     * 
     * @param chartOfAccounts The chartOfAccounts to set.
     * @deprecated
     */
    public void setChartOfAccounts(Chart chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    /**
     * Gets the account attribute.
     * 
     * @return Returns the account
     * 
     */
    public Account getAccount() { 
        return account;
    }

    /**
     * Sets the account attribute.
     * 
     * @param account The account to set.
     * @deprecated
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Gets the financialSubObject attribute. 
     * @return Returns the financialSubObject.
     */
    public SubObjCd getFinancialSubObject() {
        return financialSubObject;
    }

    /**
     * Sets the financialSubObject attribute value.
     * @param financialSubObject The financialSubObject to set.
     * @deprecated
     */
    public void setFinancialSubObject(SubObjCd financialSubObject) {
        this.financialSubObject = financialSubObject;
    }

    /**
     * Gets the subAccount attribute. 
     * @return Returns the subAccount.
     */
    public SubAccount getSubAccount() {
        return subAccount;
    }

    /**
     * Sets the subAccount attribute value.
     * @param subAccount The subAccount to set.
     * @deprecated
     */
    public void setSubAccount(SubAccount subAccount) {
        this.subAccount = subAccount;
    }

    /**
     * Gets the balanceType attribute. 
     * @return Returns the balanceType.
     */
    public BalanceTyp getBalanceType() {
        return balanceType;
    }

    /**
     * Sets the balanceType attribute value.
     * @param balanceType The balanceType to set.
     * @deprecated
     */
    public void setBalanceType(BalanceTyp balanceType) {
        this.balanceType = balanceType;
    }

    /**
     * Gets the objectType attribute. 
     * @return Returns the objectType.
     */
    public ObjectType getObjectType() {
        return objectType;
    }

    /**
     * Sets the objectType attribute value.
     * @param objectType The objectType to set.
     * @deprecated
     */
    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    
    /**
     * Gets the appointmentRequestedAmountTotal attribute. 
     * @return Returns the appointmentRequestedAmountTotal.
     */
    public KualiDecimal getAppointmentRequestedAmountTotal() {
        return appointmentRequestedAmountTotal;
    }

    /**
     * Sets the appointmentRequestedAmountTotal attribute value.
     * @param appointmentRequestedAmountTotal The appointmentRequestedAmountTotal to set.
     */
    public void setAppointmentRequestedAmountTotal(KualiDecimal appointmentRequestedAmountTotal) {
        this.appointmentRequestedAmountTotal = appointmentRequestedAmountTotal;
    }

    /**
     * Gets the appointmentRequestedFteQuantityTotal attribute. 
     * @return Returns the appointmentRequestedFteQuantityTotal.
     */
    public BigDecimal getAppointmentRequestedFteQuantityTotal() {
        return appointmentRequestedFteQuantityTotal;
    }

    /**
     * Sets the appointmentRequestedFteQuantityTotal attribute value.
     * @param appointmentRequestedFteQuantityTotal The appointmentRequestedFteQuantityTotal to set.
     */
    public void setAppointmentRequestedFteQuantityTotal(BigDecimal appointmentRequestedFteQuantityTotal) {
        this.appointmentRequestedFteQuantityTotal = appointmentRequestedFteQuantityTotal;
    }

    /**
     * Gets the csfAmountTotal attribute. 
     * @return Returns the csfAmountTotal.
     */
    public KualiDecimal getCsfAmountTotal() {
        return csfAmountTotal;
    }

    /**
     * Sets the csfAmountTotal attribute value.
     * @param csfAmountTotal The csfAmountTotal to set.
     */
    public void setCsfAmountTotal(KualiDecimal csfAmountTotal) {
        this.csfAmountTotal = csfAmountTotal;
    }

    /**
     * Gets the csfFullTimeEmploymentQuantityTotal attribute. 
     * @return Returns the csfFullTimeEmploymentQuantityTotal.
     */
    public BigDecimal getCsfFullTimeEmploymentQuantityTotal() {
        return csfFullTimeEmploymentQuantityTotal;
    }

    /**
     * Sets the csfFullTimeEmploymentQuantityTotal attribute value.
     * @param csfFullTimeEmploymentQuantityTotal The csfFullTimeEmploymentQuantityTotal to set.
     */
    public void setCsfFullTimeEmploymentQuantityTotal(BigDecimal csfFullTimeEmploymentQuantityTotal) {
        this.csfFullTimeEmploymentQuantityTotal = csfFullTimeEmploymentQuantityTotal;
    }

    /**
     * Gets the objectBaseAppointmentRequestedAmountTotal attribute. 
     * @return Returns the objectBaseAppointmentRequestedAmountTotal.
     */
    public KualiDecimal getObjectBaseAppointmentRequestedAmountTotal() {
        return objectBaseAppointmentRequestedAmountTotal;
    }

    /**
     * Sets the objectBaseAppointmentRequestedAmountTotal attribute value.
     * @param objectBaseAppointmentRequestedAmountTotal The objectBaseAppointmentRequestedAmountTotal to set.
     */
    public void setObjectBaseAppointmentRequestedAmountTotal(KualiDecimal objectBaseAppointmentRequestedAmountTotal) {
        this.objectBaseAppointmentRequestedAmountTotal = objectBaseAppointmentRequestedAmountTotal;
    }

    /**
     * Gets the objectBaseCsfAmountTotal attribute. 
     * @return Returns the objectBaseCsfAmountTotal.
     */
    public KualiDecimal getObjectBaseCsfAmountTotal() {
        return objectBaseCsfAmountTotal;
    }

    /**
     * Sets the objectBaseCsfAmountTotal attribute value.
     * @param objectBaseCsfAmountTotal The objectBaseCsfAmountTotal to set.
     */
    public void setObjectBaseCsfAmountTotal(KualiDecimal objectBaseCsfAmountTotal) {
        this.objectBaseCsfAmountTotal = objectBaseCsfAmountTotal;
    }

    /**
     * Gets the objectBasePercentChangeTotal attribute. 
     * @return Returns the objectBasePercentChangeTotal.
     */
    public KualiDecimal getObjectBasePercentChangeTotal() {
        return objectBasePercentChangeTotal;
    }

    /**
     * Sets the objectBasePercentChangeTotal attribute value.
     * @param objectBasePercentChangeTotal The objectBasePercentChangeTotal to set.
     */
    public void setObjectBasePercentChangeTotal(KualiDecimal objectBasePercentChangeTotal) {
        this.objectBasePercentChangeTotal = objectBasePercentChangeTotal;
    }

    /**
     * Gets the percentChangeTotal attribute. 
     * @return Returns the percentChangeTotal.
     */
    public KualiDecimal getPercentChangeTotal() {
        return percentChangeTotal;
    }

    /**
     * Sets the percentChangeTotal attribute value.
     * @param percentChangeTotal The percentChangeTotal to set.
     */
    public void setPercentChangeTotal(KualiDecimal percentChangeTotal) {
        this.percentChangeTotal = percentChangeTotal;
    }

    /**
     * Gets the budgetConstructionHeader attribute. 
     * @return Returns the budgetConstructionHeader.
     */
    public BudgetConstructionHeader getBudgetConstructionHeader() {
        return budgetConstructionHeader;
    }

    /**
     * Sets the budgetConstructionHeader attribute value.
     * @param budgetConstructionHeader The budgetConstructionHeader to set.
     * @deprecated
     */
    public void setBudgetConstructionHeader(BudgetConstructionHeader budgetConstructionHeader) {
        this.budgetConstructionHeader = budgetConstructionHeader;
    }    

    /**
     * Gets the laborObject attribute. 
     * @return Returns the laborObject.
     */
    public LaborObject getLaborObject() {
        if (laborObject == null){
            Map pkeys = new HashMap();
            pkeys.put("universityFiscalYear", getUniversityFiscalYear());
            pkeys.put("chartOfAccountsCode", getChartOfAccountsCode());
            pkeys.put("financialObjectCode", getFinancialObjectCode());
            
            setLaborObject((LaborObject) KNSServiceLocator.getBusinessObjectService().findByPrimaryKey(LaborObject.class,pkeys));
            
        }
        return laborObject;
    }

    /**
     * Sets the laborObject attribute value.
     * @param laborObject The laborObject to set.
     */
    public void setLaborObject(LaborObject laborObject) {
        this.laborObject = laborObject;
    }        
    
    /**
     * Gets the positionObjectBenefit attribute. 
     * @return Returns the positionObjectBenefit.
     */
    public List<PositionObjectBenefit> getPositionObjectBenefit() {
        if (positionObjectBenefit == null){
            Map fieldValues = new HashMap();
            fieldValues.put("universityFiscalYear", getUniversityFiscalYear());
            fieldValues.put("chartOfAccountsCode", getChartOfAccountsCode());
            fieldValues.put("financialObjectCode", getFinancialObjectCode());
            
            setPositionObjectBenefit((List<PositionObjectBenefit>) KNSServiceLocator.getBusinessObjectService().findMatching(PositionObjectBenefit.class,fieldValues));
            
        }
        return positionObjectBenefit;
    }

    /**
     * Sets the positionObjectBenefit attribute value.
     * @param positionObjectBenefit The positionObjectBenefit to set.
     */
    public void setPositionObjectBenefit(List<PositionObjectBenefit> positionObjectBenefit) {
        this.positionObjectBenefit = positionObjectBenefit;
    }

    
    /**
     * @see org.kuali.core.bo.PersistableBusinessObjectBase#buildListOfDeletionAwareLists()
     */
    @Override
    public List buildListOfDeletionAwareLists() {
//        return super.buildListOfDeletionAwareLists();
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(this.getPendingBudgetConstructionAppointmentFunding());
        return managedLists;
    }

    /**
     * @see org.kuali.core.bo.BusinessObjectBase#toStringMapper()
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap m = new LinkedHashMap();      
        m.put(KFSPropertyConstants.DOCUMENT_NUMBER, this.documentNumber);
        if (this.universityFiscalYear != null) {
            m.put("universityFiscalYear", this.universityFiscalYear.toString());
        }
        m.put("chartOfAccountsCode", this.chartOfAccountsCode);
        m.put("accountNumber", this.accountNumber);
        m.put("subAccountNumber", this.subAccountNumber);
        m.put("financialObjectCode", this.financialObjectCode);
        m.put("financialSubObjectCode", this.financialSubObjectCode);
        m.put("financialBalanceTypeCode", this.financialBalanceTypeCode);
        m.put("financialObjectTypeCode", this.financialObjectTypeCode);
        return m;
    }

    /**
     * Returns a map with the primitive field names as the key and the primitive values as the map value.
     * 
     * @return Map
     */
    public Map getValuesMap() {
        Map simpleValues = new HashMap();

        simpleValues.put(KFSPropertyConstants.DOCUMENT_NUMBER, getDocumentNumber());
        simpleValues.put("universityFiscalYear", getUniversityFiscalYear());
        simpleValues.put("chartOfAccountsCode", getChartOfAccountsCode());
        simpleValues.put("accountNumber", getAccountNumber());
        simpleValues.put("subAccountNumber", getSubAccountNumber());
        simpleValues.put("financialObjectCode", getFinancialObjectCode());
        simpleValues.put("financialSubObjectCode", getFinancialSubObjectCode());
        simpleValues.put("financialBalanceTypeCode", getFinancialBalanceTypeCode());
        simpleValues.put("financialObjectTypeCode", getFinancialObjectTypeCode());

        return simpleValues;
    }
  
}
