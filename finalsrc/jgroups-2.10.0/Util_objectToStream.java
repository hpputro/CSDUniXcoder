public static void objectToStream(Object obj, DataOutputStream out) throws Exception {
        if(obj == null) {
            out.write(TYPE_NULL);
            return;
        }

        Byte type;
        try {
            if(obj instanceof Streamable) {                  out.write(TYPE_STREAMABLE);
                writeGenericStreamable((Streamable)obj, out);
            }
            else if((type=PRIMITIVE_TYPES.get(obj.getClass())) != null) {
                out.write(type.byteValue());
                switch(type.byteValue()) {
                    case TYPE_BOOLEAN:
                        out.writeBoolean(((Boolean)obj).booleanValue());
                        break;
                    case TYPE_BYTE:
                        out.writeByte(((Byte)obj).byteValue());
                        break;
                    case TYPE_CHAR:
                        out.writeChar(((Character)obj).charValue());
                        break;
                    case TYPE_DOUBLE:
                        out.writeDouble(((Double)obj).doubleValue());
                        break;
                    case TYPE_FLOAT:
                        out.writeFloat(((Float)obj).floatValue());
                        break;
                    case TYPE_INT:
                        out.writeInt(((Integer)obj).intValue());
                        break;
                    case TYPE_LONG:
                        out.writeLong(((Long)obj).longValue());
                        break;
                    case TYPE_SHORT:
                        out.writeShort(((Short)obj).shortValue());
                        break;
                    case TYPE_STRING:
                        String str=(String)obj;
                        if(str.length() > Short.MAX_VALUE) {
                            out.writeBoolean(true);
                            ObjectOutputStream oos=new ObjectOutputStream(out);
                            try {
                                oos.writeObject(str);
                            }
                            finally {
                                oos.close();
                            }
                        }
                        else {
                            out.writeBoolean(false);
                            out.writeUTF(str);
                        }
                        break;
                    case TYPE_BYTEARRAY:
                        byte[] buf=(byte[])obj;
                        out.writeInt(buf.length);
                        out.write(buf, 0, buf.length);
                        break;
                    default:
                        throw new IllegalArgumentException("type " + type + " is invalid");
                }
            }
            else {                 out.write(TYPE_SERIALIZABLE);
                ObjectOutputStream tmp=new ObjectOutputStream(out);
                tmp.writeObject(obj);
            }
        }
        finally {
            Util.close(out);
        }
    }