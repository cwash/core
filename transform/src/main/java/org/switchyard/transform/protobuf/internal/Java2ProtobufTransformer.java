/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.switchyard.transform.protobuf.internal;

import org.switchyard.Message;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.transform.BaseTransformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Java to Protobuf Transformer.
 *
 * @author Chris Wash &lt;<a href="mailto:chris.wash@gmail.com">chris.wash@gmail.com</a>&gt;
 */
public class Java2ProtobufTransformer extends BaseTransformer<Message, Message> {

    /**
     * Default constructor.
     */
    public Java2ProtobufTransformer() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message transform(Message message) {

        if (!(message.getContent() instanceof com.google.protobuf.Message)) {
             throw new SwitchYardException("Cannot marshall for type '" + getFrom() + "' because it does not appear to be a com.google.protobuf.Message.");
        }

        com.google.protobuf.Message protobufMessage = message.getContent(com.google.protobuf.Message.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            protobufMessage.writeTo(outputStream);
        } catch (IOException e) {
            throw new SwitchYardException("Problem marshalling for type '" + getFrom() + "'.", e);
        }

        byte[] protobufBytes = outputStream.toByteArray();
        message.setContent(protobufBytes);

        return message;
    }
}
