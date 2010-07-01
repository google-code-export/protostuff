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

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;


/**
 * A buffer that wraps a byte array and has a reference to the next node for dynamic increase.
 * 
 * @author David Yu
 * @created May 18, 2010
 */
public final class LinkedBuffer
{
    
    /**
     * Wraps the byte array buffer as a read-only buffer.
     */
    public static LinkedBuffer wrap(byte[] array, int offset, int length)
    {
        return new LinkedBuffer(array, offset, offset + length);
    }
    
    /**
     * Returns the amount of bytes written into the {@link OutputStream}.
     */
    public static int writeTo(OutputStream out, LinkedBuffer root) throws IOException
    {
        int written = 0;
        for(LinkedBuffer node = root; node != null; node = node.next)
        {
            final int len = node.offset - node.start;
            if(len > 0)
            {
                out.write(node.buffer, node.start, len);
                written += len;
            }
        }
        return written;
    }
    
    /**
     * Returns the amount of bytes written into the {@link DataOutput}.
     */
    public static int writeTo(DataOutput out, LinkedBuffer root) throws IOException
    {
        int written = 0;
        for(LinkedBuffer node = root; node != null; node = node.next)
        {
            final int len = node.offset - node.start;
            if(len > 0)
            {
                out.write(node.buffer, node.start, len);
                written += len;
            }
        }
        return written;
    }

    final byte[] buffer;
    
    final int start;
    
    int offset;

    LinkedBuffer next;
    
    /**
     * Creates a buffer with the specified {@code size}.
     */
    public LinkedBuffer(int size)
    {
        this(new byte[size], 0, 0);
    }
    
    /**
     * Uses the buffer starting at the specified {@code offset}.
     */
    LinkedBuffer(byte[] buffer, int offset)
    {
        this(buffer, offset, offset);
    }

    LinkedBuffer(byte[] buffer, int start, int offset)
    {
        this.buffer = buffer;
        this.start = start;
        this.offset = offset;
    }
    
    /**
     * Uses the buffer starting at the specified {@code offset} and appends to the 
     * provided buffer {@code appendTarget}.
     */
    LinkedBuffer(byte[] buffer, int offset, LinkedBuffer appendTarget)
    {
        this(buffer, offset, offset);
        appendTarget.next = this;
    }
    
    LinkedBuffer(byte[] buffer, int start, int offset, LinkedBuffer appendTarget)
    {
        this(buffer, start, offset);
        appendTarget.next = this;
    }
    
    /**
     * Creates a view from the buffer {@code viewSource} and appends the view to the 
     * provided buffer {@code appendTarget}.
     */
    LinkedBuffer(LinkedBuffer viewSource, LinkedBuffer appendTarget)
    {
        buffer = viewSource.buffer;
        offset = start = viewSource.offset;
        appendTarget.next = this;
    }
    
    /**
     * The buffer next to this will be dereferenced and the offset will be reset to 
     * its starting position.
     */
    public LinkedBuffer reset()
    {
        next = null;
        offset = start;
        return this;
    }

}