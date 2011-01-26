//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package com.dyuproject.protostuff.runtime;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import com.dyuproject.protostuff.GraphInput;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Pipe;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.CollectionSchema.MessageFactories;
import com.dyuproject.protostuff.CollectionSchema.MessageFactory;
import com.dyuproject.protostuff.WireFormat.FieldType;
import com.dyuproject.protostuff.runtime.MappedSchema.Field;

/**
 * Static utility for creating runtime {@link Collection} fields.
 *
 * @author David Yu
 * @created Jan 23, 2011
 */
final class RuntimeCollectionFieldFactory
{
    
    private RuntimeCollectionFieldFactory() {}
    
    
    private static <T> Field<T> createCollectionInlineV(int number, String name, 
            final java.lang.reflect.Field f, final MessageFactory messageFactory, 
            final RuntimeFieldFactory<Object> inline)
    {
        return new Field<T>(inline.getFieldType(), number, name, true)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = inline.readFrom(input);
                try
                {
                    final Collection<Object> existing = (Collection<Object>)f.get(message);
                    if(existing == null)
                    {
                        final Collection<Object> collection = messageFactory.newMessage();
                        collection.add(value);
                        f.set(message, collection);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> collection;
                try
                {
                    collection = (Collection<Object>)f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(collection != null && !collection.isEmpty())
                {
                    for(Object o : collection)
                        inline.writeTo(output, number, o, true);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                inline.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createCollectionEnumV(int number, String name, 
            final java.lang.reflect.Field f, final MessageFactory messageFactory,  
            final Class<Object> genericType)
    {
        final EnumIO<?> eio = EnumIO.create(genericType, null);
        return new Field<T>(FieldType.ENUM, number, name, true)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Enum<?> value = eio.readFrom(input);
                try
                {
                    final Collection<Enum<?>> existing = (Collection<Enum<?>>)f.get(message);
                    if(existing == null)
                    {
                        final Collection<Enum<?>> collection = messageFactory.newMessage();
                        collection.add(value);
                        f.set(message, collection);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Enum<?>> collection;
                try
                {
                    collection = (Collection<Enum<?>>)f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(collection != null && !collection.isEmpty())
                {
                    for(Enum<?> en : collection)
                        eio.writeTo(output, number, true, en);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                eio.transfer(pipe, input, output, number, repeated);
            }
        };
    }
    
    private static <T> Field<T> createCollectionPojoV(int number, String name, 
            final java.lang.reflect.Field f, final MessageFactory messageFactory,  
            final Class<Object> genericType)
    {
        return new RuntimeMessageField<T,Object>(
                genericType, RuntimeSchema.getSchemaWrapper(genericType), 
                FieldType.MESSAGE, number, name, true)
        {
            
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(null, getSchema());
                try
                {
                    final Collection<Object> existing = (Collection<Object>)f.get(message);
                    if(existing == null)
                    {
                        final Collection<Object> collection = messageFactory.newMessage();
                        collection.add(value);
                        f.set(message, collection);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> collection;
                try
                {
                    collection = (Collection<Object>)f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(collection != null && !collection.isEmpty())
                {
                    final Schema<Object> schema = getSchema();
                    for(Object o : collection)
                        output.writeObject(number, o, schema, true);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, getPipeSchema(), repeated);
            }
        };
    }
    
    private static <T> Field<T> createCollectionPolymorphicV(int number, String name, 
            final java.lang.reflect.Field f, final MessageFactory messageFactory,  
            final Class<Object> genericType)
    {
        return new PolymorphicRuntimeField<T>(
                genericType, FieldType.MESSAGE, number, name, true)
        {
            {
                f.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            protected void mergeFrom(Input input, T message) throws IOException
            {
                final Object value = input.mergeObject(message, schema);
                if(input instanceof GraphInput && 
                        ((GraphInput)input).isCurrentMessageReference())
                {
                    // a reference from polymorphic+cyclic graph deser
                    try
                    {
                        final Collection<Object> existing = (Collection<Object>)f.get(message);
                        if(existing == null)
                        {
                            final Collection<Object> collection = messageFactory.newMessage();
                            collection.add(value);
                            f.set(message, collection);
                        }
                        else
                            existing.add(value);
                    }
                    catch (IllegalArgumentException e)
                    {
                        throw new RuntimeException(e);
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
            @SuppressWarnings("unchecked")
            protected void writeTo(Output output, T message) throws IOException
            {
                final Collection<Object> existing;
                try
                {
                    existing = (Collection<Object>)f.get(message);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
                
                if(existing != null && !existing.isEmpty())
                {
                    for(Object o : existing)
                        output.writeObject(number, o, schema, true);
                }
            }
            protected void transfer(Pipe pipe, Input input, Output output, 
                    boolean repeated) throws IOException
            {
                output.writeObject(number, pipe, schema.pipeSchema, repeated);
            }
            @SuppressWarnings("unchecked")
            protected void doMergeFrom(Input input, Schema<Object> schema, 
                    Object message) throws IOException
            {
                final Object value = schema.newMessage();
                if(input instanceof GraphInput)
                {
                    // update the actual reference.
                    ((GraphInput)input).updateLast(value, message);
                }
                
                schema.mergeFrom(input, value);
                try
                {
                    final Collection<Object> existing = (Collection<Object>)f.get(message);
                    if(existing == null)
                    {
                        final Collection<Object> collection = messageFactory.newMessage();
                        collection.add(value);
                        f.set(message, collection);
                    }
                    else
                        existing.add(value);
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    
    static final RuntimeFieldFactory<Collection<?>> COLLECTION = new RuntimeFieldFactory<Collection<?>>()
    {
        @SuppressWarnings("unchecked")
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f)
        {
            final Class<Object> genericType;
            try
            {
                genericType = (Class<Object>)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
            }
            catch(Exception e)
            {
                // the value is not a simple parameterized type.
                return null;
            }
            
            final MessageFactory messageFactory = MessageFactories.getFactory(
                    (Class<? extends Collection<?>>)f.getType());
            
            if(messageFactory == null)
            {
                // Not a standard jdk Collection impl.
                return null;
            }
            
            if(genericType.isEnum())
                return createCollectionEnumV(number, name, f, messageFactory, genericType);
            
            final RuntimeFieldFactory<Object> inline = getInline(genericType);
            if(inline != null)
                return createCollectionInlineV(number, name, f, messageFactory, inline);
            
            if(isInvalidChildType(genericType))
                return null;
            
            if(POJO == pojo(genericType) || RuntimeSchema.isRegistered(genericType))
                return createCollectionPojoV(number, name, f, messageFactory, genericType);
            
            return createCollectionPolymorphicV(number, name, f, messageFactory, genericType);
        }
        protected void transfer(Pipe pipe, Input input, Output output, int number, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected Collection<?> readFrom(Input input) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected void writeTo(Output output, int number, Collection<?> value, 
                boolean repeated) throws IOException
        {
            throw new UnsupportedOperationException();
        }
        protected FieldType getFieldType()
        {
            throw new UnsupportedOperationException();
        }
    };

}
