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
package org.kuali.module.cams.service;

import java.util.List;

import org.kuali.module.cams.bo.Asset;
import org.kuali.core.util.KualiDecimal;
import org.kuali.core.bo.PersistableBusinessObject;
import org.kuali.module.cams.bo.AssetGlobal;
import org.kuali.module.cams.gl.CamsGeneralLedgerPendingEntrySourceBase;


/**
 * The interface defines methods for Asset Document
 */
public interface AssetGlobalService {
	/**
     *
     * To calculate the total payment amounts for each asset.
     * @param assetGlobal
     * @return
     */
    KualiDecimal totalPaymentByAsset(AssetGlobal assetGlobal);
    
    /**
     * 
     * This method checks if member exists in the given group.
     * @param groupName
     * @param memberName
     * @return
     */
    boolean existsInGroup(String groupName, String memberName);
    
    /**
     * 
     * To calculate the total non federal contribution payment amounts for each asset.
     * @param assetGlobal
     * @return 
     */
    KualiDecimal totalNonFederalPaymentByAsset(AssetGlobal assetGlobal);
    
    /**
     * Creates GL Postables
     */
    boolean createGLPostables(AssetGlobal assetGlobal, CamsGeneralLedgerPendingEntrySourceBase assetGlobalGlPoster);
}