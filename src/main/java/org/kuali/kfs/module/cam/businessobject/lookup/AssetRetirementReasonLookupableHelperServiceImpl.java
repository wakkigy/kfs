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
package org.kuali.kfs.module.cam.businessobject.lookup;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.kuali.kfs.module.cam.CamsConstants;
import org.kuali.kfs.module.cam.CamsPropertyConstants;
import org.kuali.kfs.module.cam.businessobject.AssetGlobal;
import org.kuali.kfs.module.cam.businessobject.AssetRetirementGlobal;
import org.kuali.kfs.module.cam.businessobject.AssetRetirementReason;
import org.kuali.kfs.sys.KFSConstants;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.kfs.sys.document.authorization.FinancialSystemMaintenanceDocumentAuthorizerBase;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kns.document.authorization.BusinessObjectRestrictions;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * This class overrides the getReturnUrl, setFieldConversions and getActionUrls for
 * {@link OrganizationRoutingModelName}
 */
public class AssetRetirementReasonLookupableHelperServiceImpl extends KualiLookupableHelperServiceImpl {
    protected boolean initializingAssetRetirement = true;


    /**
     * Overrides the base implementation to add in new parameters to the return url
     * <ul>
     * <li>{@link KFSConstants.DISPATCH_REQUEST_PARAMETER}</li>
     * <li>{@link KFSConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE}</li>
     * <li>{@link KFSConstants.OVERRIDE_KEYS}</li>
     * </ul>
     * {@link KFSConstants.DISPATCH_REQUEST_PARAMETER}
     *
     * @see org.kuali.rice.kns.lookup.AbstractLookupableHelperServiceImpl#getReturnUrl(org.kuali.rice.krad.bo.BusinessObject, java.util.Map,
     *      java.lang.String)
     */
    @Override
    public HtmlData getReturnUrl(BusinessObject businessObject, LookupForm lookupForm, List returnKeys, BusinessObjectRestrictions businessObjectRestrictions) {
        AssetRetirementReason assetRetirementReason = (AssetRetirementReason) businessObject;
        
        ParameterService parameterService = SpringContext.getBean(ParameterService.class);
        String mergeParam = parameterService.getParameterValueAsString(AssetGlobal.class, CamsConstants.Parameters.MERGE_SEPARATE_RETIREMENT_REASONS);
        String razeParam = parameterService.getParameterValueAsString(AssetRetirementGlobal.class, CamsConstants.Parameters.RAZE_RETIREMENT_REASONS);
        
        if (initializingAssetRetirement) {
            FinancialSystemMaintenanceDocumentAuthorizerBase documentAuthorizer = (FinancialSystemMaintenanceDocumentAuthorizerBase) SpringContext.getBean(DocumentDictionaryService.class).getDocumentAuthorizer(CamsConstants.DocumentTypeName.ASSET_RETIREMENT_GLOBAL);
            
            // do not allow user to issue a retirement doc. if not active.
            if (!assetRetirementReason.isActive()) {
                return getEmptyAnchorHtmlData();
            }
            
            if (assetRetirementReason.isRetirementReasonRestrictionIndicator()) {
                boolean isAuthorized = documentAuthorizer.isAuthorized(businessObject, CamsConstants.CAM_MODULE_CODE, CamsConstants.PermissionNames.USE_RESTRICTED_RETIREMENT_REASON, GlobalVariables.getUserSession().getPerson().getPrincipalId());
                
                if (!isAuthorized) {
                    return getEmptyAnchorHtmlData();
                }
            } else if (mergeParam != null && Arrays.asList(mergeParam.split(";")).contains(assetRetirementReason.getRetirementReasonCode())) {
                boolean isAuthorized = documentAuthorizer.isAuthorized(businessObject, CamsConstants.CAM_MODULE_CODE, CamsConstants.PermissionNames.MERGE, GlobalVariables.getUserSession().getPerson().getPrincipalId());
                
                if (!isAuthorized) {
                    return getEmptyAnchorHtmlData();
                }
            } else if (razeParam != null && Arrays.asList(razeParam.split(";")).contains(assetRetirementReason.getRetirementReasonCode())) {
                boolean isAuthorized = documentAuthorizer.isAuthorized(businessObject, CamsConstants.CAM_MODULE_CODE, CamsConstants.PermissionNames.RAZE, GlobalVariables.getUserSession().getPerson().getPrincipalId());
                
                if (!isAuthorized) {
                    return getEmptyAnchorHtmlData();
                }
            }            
        }
        
        Properties parameters = getParameters(businessObject, lookupForm.getFieldConversions(), lookupForm.getLookupableImplServiceName(), returnKeys);
        parameters.put(KFSConstants.DISPATCH_REQUEST_PARAMETER, KFSConstants.MAINTENANCE_NEWWITHEXISTING_ACTION);
        parameters.put(KFSConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, AssetRetirementGlobal.class.getName());
        parameters.put(KFSConstants.OVERRIDE_KEYS, CamsPropertyConstants.AssetRetirementGlobal.RETIREMENT_REASON_CODE);
        parameters.put(KFSConstants.REFRESH_CALLER, CamsPropertyConstants.AssetRetirementGlobal.RETIREMENT_REASON_CODE+"::"+assetRetirementReason.getRetirementReasonCode());
        if(!lookupForm.isHideReturnLink()){
            setBackLocation(KFSConstants.MAINTENANCE_ACTION);        
        }
        return getReturnAnchorHtmlData(businessObject, parameters, lookupForm, returnKeys, businessObjectRestrictions);
    }

    /**
     * Overrides base implementation to determine whether or not we are dealing with looking up the model or editing it
     *
     * @see org.kuali.rice.kns.lookup.AbstractLookupableHelperServiceImpl#setFieldConversions(java.util.Map)
     */
    @Override
    public void setFieldConversions(Map fieldConversions) {
        super.setFieldConversions(fieldConversions);
        if (fieldConversions == null || fieldConversions.size() == 0) {
            initializingAssetRetirement = false;
        }
    }

    /**
     * Overrides base implementation to remove the action urls if we are initializing the asset retirement reason
     *
     * @see org.kuali.rice.kns.lookup.AbstractLookupableHelperServiceImpl#getCustomActionUrls(org.kuali.rice.krad.bo.BusinessObject, List pkNames)
     */
    @Override
    public List<HtmlData> getCustomActionUrls(BusinessObject businessObject, List pkNames) {
        if (!initializingAssetRetirement) {
            return super.getCustomActionUrls(businessObject, pkNames);
        }
        else {
            return super.getEmptyActionUrls();
        }
    }
}
