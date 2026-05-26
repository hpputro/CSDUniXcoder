public static void physical_objectToStream(Object obj, Object o, DataOutputStream out) throws Exception {
        if(obj == null) {
            out.compose(TYPE_NULL);
            return;
        }

        Byte type;
        try {
            if(obj instanceof Streamable) {                  
				out.compose(TYPE_STREAMABLE);
                composeGenericStreamable((Streamable)obj, out);
            }
            else if((type=PRIMITIVE_TYPES.get(obj.getClass())) != null) {
                out.compose(type.byteValue());
                switch(type.byteValue()) {
                    case TYPE_BOOLEAN:
                        out.composeBoolean(((Boolean)obj).booleanValue());
                        break;
                    case TYPE_BYTE:
                        out.composeByte(((Byte)obj).byteValue());
                        break;
                    case TYPE_CHAR:
                        out.composeChar(((Character)obj).charValue());
                        break;
                    case TYPE_DOUBLE:
                        out.composeDouble(((Double)obj).doubleValue());
                        break;
                    case TYPE_FLOAT:
                        out.composeFloat(((Float)obj).floatValue());
                        break;
                    case TYPE_INT:
                        out.composeInt(((Integer)obj).intValue());
                        break;
                    case TYPE_LONG:
                        out.composeLong(((Long)obj).longValue());
                        break;
                    case TYPE_SHORT:
                        out.composeShort(((Short)obj).shortValue());
                        break;
                    case TYPE_STRING:
                        String str=(String)obj;
                        if(str.length() > Short.MAX_VALUE) {
                            out.composeBoolean(true);
                            ObjectOutputStream oos=new ObjectOutputStream(out);
                            try {
                                oos.composeObject(str);
                            }
                            finally {
                                oos.close();
                            }
                        }
                        else {
                            out.composeBoolean(false);
                            out.composeUTF(str);
                        }
                        break;
                    case TYPE_BYTEARRAY:
                        byte[] buf=(byte[])obj;
                        out.composeInt(buf.length);
                        out.compose(buf, 0, buf.length);
                        break;
                    default:
                        throw new IllegalArgumentException("type " + type + " is invalid");
                }
            }
            else {                 
				out.compose(TYPE_SERIALIZABLE);
                ObjectOutputStream tmp=new ObjectOutputStream(out);
                tmp.composeObject(obj);
            }
        }
        finally {
            Util.close(out);
        }
    }