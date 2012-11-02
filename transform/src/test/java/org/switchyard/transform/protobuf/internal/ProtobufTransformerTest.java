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

import com.google.protobuf.Message;
import org.junit.Test;
import org.switchyard.internal.DefaultMessage;
import org.switchyard.transform.AbstractTransformerTestCase;
import org.switchyard.transform.Transformer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests Google Protocol Buffer Transformers.
 *
 * @author Chris Wash &lt;<a href="mailto:chris.wash@gmail.com">chris.wash@gmail.com</a>&gt;
 */
public class ProtobufTransformerTest extends AbstractTransformerTestCase {

    private Message createNewDefaultPersonInstance() {
        return AddressBookProtos.Person.newBuilder()
                .setId(1234)
                .setName("John Doe")
                .setEmail("jdoe@example.com")
                .addPhone(
                        AddressBookProtos.Person.PhoneNumber.newBuilder()
                                .setNumber("867-5309")
                                .setType(AddressBookProtos.Person.PhoneType.HOME))
                .build();
    }

    private void assertInstanceIsDefaultPerson(Message instance) {

        assertThat(instance, is(instanceOf(AddressBookProtos.Person.class)));
        AddressBookProtos.Person person = (AddressBookProtos.Person) instance;

        assertThat(person.getId(), is(1234));
        assertThat(person.getName(), is("John Doe"));
        assertThat(person.getEmail(), is("jdoe@example.com"));
        assertThat(person.getPhone(0).getNumber(), is("867-5309"));
        assertThat(person.getPhone(0).getType(), is(AddressBookProtos.Person.PhoneType.HOME));

    }

    @Test
    public void shouldTransformToProtobuf() throws Exception {

        Transformer<org.switchyard.Message, org.switchyard.Message> java2ProtobufTransformer = new Java2ProtobufTransformer();

        org.switchyard.Message javaMessage = new DefaultMessage();
        javaMessage.setContent(createNewDefaultPersonInstance());

        org.switchyard.Message protobufMessage = java2ProtobufTransformer.transform(javaMessage);

        byte[] protobufContent = protobufMessage.getContent(byte[].class);
        AddressBookProtos.Person deserializedTransformedInstance = AddressBookProtos.Person.parseFrom(protobufContent);

        assertInstanceIsDefaultPerson(deserializedTransformedInstance);

    }



    @Test
    public void shouldTransformFromProtobufByInstance() throws Exception {

        Transformer<org.switchyard.Message, org.switchyard.Message> protobuf2JavaTransformer = new Protobuf2JavaTransformer<AddressBookProtos.Person>(AddressBookProtos.Person.getDefaultInstance());

        byte[] protobufContent = createNewDefaultPersonInstance().toByteArray();
        DefaultMessage protobufMessage = new DefaultMessage();
        protobufMessage.setContent(protobufContent);

        org.switchyard.Message javaMessage = protobuf2JavaTransformer.transform(protobufMessage);

        AddressBookProtos.Person transformedInstance = (AddressBookProtos.Person) javaMessage.getContent();
        assertInstanceIsDefaultPerson(transformedInstance);

    }

    @Test
    public void shouldTransformFromProtobufByClass() throws Exception {

        Transformer<org.switchyard.Message, org.switchyard.Message> protobuf2JavaTransformer = new Protobuf2JavaTransformer<AddressBookProtos.Person>(AddressBookProtos.Person.class);


        byte[] protobufContent = createNewDefaultPersonInstance().toByteArray();
        DefaultMessage protobufMessage = new DefaultMessage();
        protobufMessage.setContent(protobufContent);

        org.switchyard.Message javaMessage = protobuf2JavaTransformer.transform(protobufMessage);

        AddressBookProtos.Person transformedInstance = (AddressBookProtos.Person) javaMessage.getContent();
        assertInstanceIsDefaultPerson(transformedInstance);

    }

}
