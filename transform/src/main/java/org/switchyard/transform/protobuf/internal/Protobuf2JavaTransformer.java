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

import com.google.protobuf.InvalidProtocolBufferException;
import org.switchyard.Message;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.transform.BaseTransformer;

import java.lang.reflect.Method;

/**
 * Protobuf to Java Transformer.
 *
 * @author Chris Wash <a href="mailto:chris.wash@gmail.com">chris.wash@gmail.com</a>
 *
 * @param <T> type to transform to.
 */
public class Protobuf2JavaTransformer<T extends com.google.protobuf.Message> extends BaseTransformer<Message, Message> {

    private final T _exampleInstance;

    /**
     * Public constructor.
     *
     * @param exampleInstance instance representing desired {@link com.google.protobuf.Message} type for transform,
     *                        derived from {@link com.google.protobuf.Message#getDefaultInstanceForType()}, or your
     *                        type's generated static method {@code getDefaultInstance()}
     */
    public Protobuf2JavaTransformer(T exampleInstance) {
        this._exampleInstance = exampleInstance;
    }


    /**
     * Public constructor.
     *
     * @param exampleClass class representing desired {@link com.google.protobuf.Message} type for transform
     */
    @SuppressWarnings({"unchecked", "NullArgumentToVariableArgMethod"})
    public Protobuf2JavaTransformer(Class<T> exampleClass) {
        try {
            Method defaultInstanceMethod = null;
            defaultInstanceMethod = exampleClass.getMethod("getDefaultInstance");
            this._exampleInstance = (T) defaultInstanceMethod.invoke(null, null);
        } catch (Exception e) {
            throw new SwitchYardException("Cannot setup Transformer for type '" + getFrom() + "' because it does not appear to be a com.google.protobuf.Message.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message transform(Message message) {

        byte[] protobufBytes = (byte[]) message.getContent();
        com.google.protobuf.Message.Builder protobufBuilder = null;

        try {
            protobufBuilder = _exampleInstance.newBuilderForType().mergeFrom(protobufBytes);
        } catch (InvalidProtocolBufferException e) {
            throw new SwitchYardException("Cannot unmarshall for type '" + getFrom() + "'.", e);
        }

        if (!protobufBuilder.isInitialized()) {
            throw new SwitchYardException("Cannot unmarshall for type '" + getFrom() + "'.");
        }
        com.google.protobuf.Message protobufMessage = protobufBuilder.build();
        message.setContent(protobufMessage);

        return message;

    }
}
