/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.kafka.common.protocol.types;

import java.nio.ByteBuffer;

import org.apache.kafka.common.utils.Utils;

/**
 * A serializable type
 */
public abstract class Type {

    public abstract void write(ByteBuffer buffer, Object o);

    public abstract Object read(ByteBuffer buffer);

    public abstract int sizeOf(Object o);

    public abstract Object validate(Object o);

    public static final Type INT8 = new Type() {
        
        public void write(ByteBuffer buffer, Object o) {
            buffer.put((Byte) o);
        }

        
        public Object read(ByteBuffer buffer) {
            return buffer.get();
        }

        
        public int sizeOf(Object o) {
            return 1;
        }

        
        public String toString() {
            return "INT8";
        }

        
        public Byte validate(Object item) {
            if (item instanceof Byte)
                return (Byte) item;
            else
                throw new SchemaException(item + " is not a Byte.");
        }
    };

    public static final Type INT16 = new Type() {
        
        public void write(ByteBuffer buffer, Object o) {
            buffer.putShort((Short) o);
        }

        
        public Object read(ByteBuffer buffer) {
            return buffer.getShort();
        }

        
        public int sizeOf(Object o) {
            return 2;
        }

        
        public String toString() {
            return "INT16";
        }

        
        public Short validate(Object item) {
            if (item instanceof Short)
                return (Short) item;
            else
                throw new SchemaException(item + " is not a Short.");
        }
    };

    public static final Type INT32 = new Type() {
        
        public void write(ByteBuffer buffer, Object o) {
            buffer.putInt((Integer) o);
        }

        
        public Object read(ByteBuffer buffer) {
            return buffer.getInt();
        }

        
        public int sizeOf(Object o) {
            return 4;
        }

        
        public String toString() {
            return "INT32";
        }

        
        public Integer validate(Object item) {
            if (item instanceof Integer)
                return (Integer) item;
            else
                throw new SchemaException(item + " is not an Integer.");
        }
    };

    public static final Type INT64 = new Type() {
        
        public void write(ByteBuffer buffer, Object o) {
            buffer.putLong((Long) o);
        }

        
        public Object read(ByteBuffer buffer) {
            return buffer.getLong();
        }

        
        public int sizeOf(Object o) {
            return 8;
        }

        
        public String toString() {
            return "INT64";
        }

        
        public Long validate(Object item) {
            if (item instanceof Long)
                return (Long) item;
            else
                throw new SchemaException(item + " is not a Long.");
        }
    };

    public static final Type STRING = new Type() {
        
        public void write(ByteBuffer buffer, Object o) {
            byte[] bytes = Utils.utf8((String) o);
            if (bytes.length > Short.MAX_VALUE)
                throw new SchemaException("String is longer than the maximum string length.");
            buffer.putShort((short) bytes.length);
            buffer.put(bytes);
        }

        
        public Object read(ByteBuffer buffer) {
            int length = buffer.getShort();
            byte[] bytes = new byte[length];
            buffer.get(bytes);
            return Utils.utf8(bytes);
        }

        
        public int sizeOf(Object o) {
            return 2 + Utils.utf8Length((String) o);
        }

        
        public String toString() {
            return "STRING";
        }

        
        public String validate(Object item) {
            if (item instanceof String)
                return (String) item;
            else
                throw new SchemaException(item + " is not a String.");
        }
    };

    public static final Type BYTES = new Type() {
        
        public void write(ByteBuffer buffer, Object o) {
            ByteBuffer arg = (ByteBuffer) o;
            int pos = arg.position();
            buffer.putInt(arg.remaining());
            buffer.put(arg);
            arg.position(pos);
        }

        
        public Object read(ByteBuffer buffer) {
            int size = buffer.getInt();
            ByteBuffer val = buffer.slice();
            val.limit(size);
            buffer.position(buffer.position() + size);
            return val;
        }

        
        public int sizeOf(Object o) {
            ByteBuffer buffer = (ByteBuffer) o;
            return 4 + buffer.remaining();
        }

        
        public String toString() {
            return "BYTES";
        }

        
        public ByteBuffer validate(Object item) {
            if (item instanceof ByteBuffer)
                return (ByteBuffer) item;
            else
                throw new SchemaException(item + " is not a java.nio.ByteBuffer.");
        }
    };

}
