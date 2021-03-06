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
package org.kuali.kfs.module.ar.batch;

import java.util.Date;

import org.kuali.kfs.module.ar.batch.service.LockboxService;
import org.kuali.kfs.sys.batch.AbstractStep;
import org.kuali.rice.kew.api.exception.WorkflowException;

public class LockboxStep extends AbstractStep {

    private LockboxService lockboxService;
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LockboxStep.class);

    @Override
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {
        boolean result = true;
        try {
            result = lockboxService.processLockboxes();
        } catch (WorkflowException e) {
            LOG.error("problem during lockboxService.processLockboxes()", e);
            throw new RuntimeException("Could not process lockbox documents", e);
        }
        return result;
    }

    public void setLockboxService(LockboxService ls) {
        lockboxService = ls;
    }

}
