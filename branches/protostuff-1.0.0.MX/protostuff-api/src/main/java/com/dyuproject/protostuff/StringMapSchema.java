//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff;

import java.io.IOException;
import java.util.Map;

/**
 * A utility schema for a {@link Map} with {@link String} keys and single-type object values.
 * Keys cannot be null otherwise the entry is ignored (not serialized).
 * Values however can be null.
 *
 * @author David Yu
 * @created Jun 25, 2010
 */
public class StringMapSchema<V> extends MapSchema<String,V>
{
    
    /**
     * The schema for Map<String,String>
     */
    public static final StringMapSchema<String> VALUE_STRING = new StringMapSchema<String>(null)
    {
        protected String readValueFrom(Input input) throws IOException
        {
            return input.readString();
        }

        protected void writeValueTo(Output output, int fieldNumber, String value, 
                boolean repeated) throws IOException
        {
            output.writeString(fieldNumber, value, repeated);
        }
    };
    
    protected final Schema<V> vSchema;
    
    public StringMapSchema(Schema<V> vSchema)
    {
        this.vSchema = vSchema;
    }

    protected final String readKeyFrom(Input input) throws IOException
    {
        return input.readString();
    }
    
    protected V readValueFrom(Input input) throws IOException
    {
        return input.mergeObject(vSchema.newMessage(), vSchema);
    }

    protected final void writeKeyTo(Output output, int fieldNumber, String value, 
            boolean repeated) throws IOException
    {
        output.writeString(fieldNumber, value, repeated);
    }
    
    protected void writeValueTo(Output output, int fieldNumber, V value, 
            boolean repeated) throws IOException
    {
        output.writeObject(fieldNumber, value, vSchema, repeated);
    }

}
