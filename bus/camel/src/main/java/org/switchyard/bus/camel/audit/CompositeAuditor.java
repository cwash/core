/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.switchyard.bus.camel.audit;

import java.util.List;

import org.apache.camel.Exchange;
import org.switchyard.bus.camel.processors.Processors;

/**
 * Implementation of auditor which calls wrapped auditors.
 */
public class CompositeAuditor implements Auditor {

    private final List<Auditor> _auditors;

    /**
     * Creates new composite.
     * 
     * @param auditors Auditors to be wrapped.
     */
    public CompositeAuditor(List<Auditor> auditors) {
        this._auditors = auditors;
    }

    @Override
    public void beforeCall(Processors processor, Exchange exchange) {
        for (Auditor auditor : _auditors) {
            auditor.beforeCall(processor, exchange);
        }
    }

    @Override
    public void afterCall(Processors processor, Exchange exchange) {
        for (Auditor auditor : _auditors) {
            auditor.afterCall(processor, exchange);
        }
    }

}
