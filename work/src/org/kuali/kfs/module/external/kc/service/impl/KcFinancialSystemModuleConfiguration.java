/*
 * Copyright 2008 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kfs.module.external.kc.service.impl;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.kuali.kfs.module.external.kc.service.KcFinancialSystemModuleConfig;
import org.kuali.kfs.sys.FinancialSystemModuleConfiguration;
import org.kuali.rice.kns.bo.ModuleConfiguration;

/**
 * Slim subclass to enforce class hierarchy not enforced by the parent class' contract.
 */
public class KcFinancialSystemModuleConfiguration extends FinancialSystemModuleConfiguration implements KcFinancialSystemModuleConfig {
    
    protected Map<Class,String> externalizableWebBusinessObjectImplementations;

    /**
     * Constructs a FinancialSystemModuleConfiguration.java.
     */
    public KcFinancialSystemModuleConfiguration() {
        super();
    }
    
    
    /**
     * @return the externalizableBusinessObjectImplementations
     */
    public Map<Class,String> getExternalizableWebBusinessObjectImplementations() {
        if (this.externalizableBusinessObjectImplementations == null)
            return null;
        return (Map<Class,String>) Collections.unmodifiableMap(this.externalizableWebBusinessObjectImplementations);
    }

    /**
     * @param externalizableBusinessObjectImplementations the externalizableBusinessObjectImplementations to set
     */
    public void setExternalizableWebBusinessObjectImplementations(
          Map<Class, String> externalizableBusinessObjectImplementations) {
        if (externalizableBusinessObjectImplementations != null) {
           Iterable<Class> impClasses =  externalizableBusinessObjectImplementations.keySet();
              for (Class implClass : impClasses) {
                int implModifiers = implClass.getModifiers();
                if (! Modifier.isInterface(implModifiers)) {
                    throw new RuntimeException("Externalizable Web business object implementation class " +
                            implClass.getName() + " must be a non-interface, non-abstract class");
                }
            }
        }
        this.externalizableWebBusinessObjectImplementations = externalizableBusinessObjectImplementations;
    }
 
}
