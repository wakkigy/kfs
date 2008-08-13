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
package org.kuali.kfs.module.bc.document.dataaccess;

import java.util.Collection;
import java.util.List;

import org.kuali.core.util.KualiInteger;
import org.kuali.kfs.module.bc.businessobject.BudgetConstructionAccountOrganizationHierarchy;
import org.kuali.kfs.module.bc.businessobject.BudgetConstructionFundingLock;
import org.kuali.kfs.module.bc.businessobject.BudgetConstructionHeader;
import org.kuali.kfs.module.bc.businessobject.BudgetConstructionPosition;
import org.kuali.kfs.module.bc.businessobject.PendingBudgetConstructionAppointmentFunding;
import org.kuali.kfs.module.bc.businessobject.PendingBudgetConstructionGeneralLedger;


/**
 * This interface defines the methods that a BudgetConstructionDao must provide.
 */
public interface BudgetConstructionDao {

    /**
     * This gets a BudgetConstructionHeader using the candidate key chart, account, subaccount, fiscalyear
     * 
     * @param chartOfAccountsCode
     * @param accountNumber
     * @param subAccountNumber
     * @param fiscalYear
     * @return BudgetConstructionHeader
     */
    public BudgetConstructionHeader getByCandidateKey(String chartOfAccountsCode, String accountNumber, String subAccountNumber, Integer fiscalYear);

    /**
     * This saves a BudgetConstructionHeader object to the database
     * 
     * @param bcHeader
     */
    public void saveBudgetConstructionHeader(BudgetConstructionHeader bcHeader);

    /**
     * This gets a BudgetConstructionFundingLock using the primary key chart, account, subaccount, fiscalyear, pUId
     * 
     * @param chartOfAccountsCode
     * @param accountNumber
     * @param subAccountNumber
     * @param fiscalYear
     * @param personUniversalIdentifier
     * @return BudgetConstructionFundingLock
     */
    public BudgetConstructionFundingLock getByPrimaryId(String chartOfAccountsCode, String accountNumber, String subAccountNumber, Integer fiscalYear, String personUniversalIdentifier);

    /**
     * This saves a BudgetConstructionFundingLock to the database
     * 
     * @param budgetConstructionFundingLock
     */
    public void saveBudgetConstructionFundingLock(BudgetConstructionFundingLock budgetConstructionFundingLock);

    /**
     * This deletes a BudgetConstructionFundingLock from the database
     * 
     * @param budgetConstructionFundingLock
     */
    public void deleteBudgetConstructionFundingLock(BudgetConstructionFundingLock budgetConstructionFundingLock);

    /**
     * This gets the set of BudgetConstructionFundingLocks asssociated with a BC EDoc (account). Each BudgetConstructionFundingLock
     * has the positionNumber dummy attribute set to the associated Position that is locked. A positionNumber value of "NotFnd"
     * indicates the BudgetConstructionFundingLock is an orphan.
     * 
     * @param chartOfAccountsCode
     * @param accountNumber
     * @param subAccountNumber
     * @param fiscalYear
     * @return Collection<BudgetConstructionFundingLock>
     */
    public Collection<BudgetConstructionFundingLock> getFlocksForAccount(String chartOfAccountsCode, String accountNumber, String subAccountNumber, Integer fiscalYear);

    /**
     * Returns the position number associated with a funding lock or the not found string if the lock is an orphan.
     * 
     * @param budgetConstructionFundingLock - funding lock to get position for
     * @return position number associated with lock
     */
    public String getPositionAssociatedWithFundingLock(BudgetConstructionFundingLock budgetConstructionFundingLock);

    /**
     * Gets a BudgetConstructionPosition from the database by the primary key positionNumber, fiscalYear
     * 
     * @param positionNumber
     * @param fiscalYear
     * @return BudgetConstructionPosition
     */
    public BudgetConstructionPosition getByPrimaryId(String positionNumber, Integer fiscalYear);

    /**
     * Saves a BudgetConstructionPosition to the database
     * 
     * @param bcPosition
     */
    public void saveBudgetConstructionPosition(BudgetConstructionPosition bcPosition);

    /**
     * This method deletes all BudgetConstructionPullup rows associated with a user.
     * 
     * @param personUserIdentifier
     */
    public void deleteBudgetConstructionPullupByUserId(String personUserIdentifier);

    /**
     * This method returns a list of BudgetConstructionPullup objects (organizations) ownded by the user that have the pullflag set
     * 
     * @param personUserIdentifier
     * @return
     */
    public List getBudgetConstructionPullupFlagSetByUserId(String personUserIdentifier);

    /**
     * This returns a list of BudgetConstructionPullup objects (organizations) that are children to the passed in organization for
     * the user
     * 
     * @param personUniversalIdentifier
     * @param chartOfAccountsCode
     * @param organizationCode
     * @return
     */
    public List getBudgetConstructionPullupChildOrgs(String personUniversalIdentifier, String chartOfAccountsCode, String organizationCode);

    /**
     * Returns the sum of the salary detail request amounts for an accounting line
     * 
     * @param salaryDetailLine
     * @return
     */
    public KualiInteger getPendingBudgetConstructionAppointmentFundingRequestSum(PendingBudgetConstructionGeneralLedger salaryDetailLine);

    /**
     * returns a list of fringe benefit accounting lines for a document
     * 
     * @param documentNumber
     * @param fringeObjects
     * @return
     */
    public List getDocumentPBGLFringeLines(String documentNumber, List fringeObjects);

    /**
     * Determines is a user is an active Budget Construction document delegate approver for the Budget Construction document type or
     * the 'ALL' document type
     * 
     * @param chartCd
     * @param accountNbr
     * @param personUniversalIdentifier
     * @return
     */
    public boolean isDelegate(String chartCd, String accountNbr, String personUniversalIdentifier);

    /**
     * Returns a list of account organization hierarchy levels for an account
     * 
     * @param chartOfAccountsCode
     * @param accountNumber
     * @param universityFiscalYear
     * @return
     */
    public List<BudgetConstructionAccountOrganizationHierarchy> getAccountOrgHierForAccount(String chartOfAccountsCode, String accountNumber, Integer universityFiscalYear);

    /**
     * Returns a list of labor objects that are Salary Setting detail related
     * 
     * @param universityFiscalYear
     * @param chartOfAccountsCode
     * @return
     */
    public List<String> getDetailSalarySettingLaborObjects(Integer universityFiscalYear, String chartOfAccountsCode);

    /**
     * Returns a list of Pending Budget GL rows that are Salary Setting detail related, based on the set of salarySettingObjects
     * passed in
     * 
     * @param documentNumber
     * @param salarySettingObjects
     * @return
     */
    public List getPBGLSalarySettingRows(String documentNumber, List salarySettingObjects);

    /**
     * Retrieves all <code>PendingBudgetConstructionAppointmentFunding</code> records for the given position key.
     * 
     * @param universityFiscalYear budget fiscal year, primary key field for position record
     * @param positionNumber position number, primary key field for position record
     * @return List of PendingBudgetConstructionAppointmentFunding objects
     */
    public List<PendingBudgetConstructionAppointmentFunding> getAllFundingForPosition(Integer universityFiscalYear, String positionNumber);
}
