        public byte[] objectToByteBuffer(Object obj) throws Exception {
            ByteArrayOutputStream out_stream=new ByteArrayOutputStream(35);
            DataOutputStream out=new DataOutputStream(out_stream);
            try {
                if(obj == null) {
                    out_stream.write(NULL);
                    out_stream.flush();
                    return out_stream.toByteArray();
                }

                if(obj instanceof MethodCall) {
                    out.writeByte(METHOD_CALL);
                    MethodCall call=(MethodCall)obj;
                    out.writeShort(call.getId());
                    Object[] args=call.getArgs();
                    if(args == null || args.length == 0) {
                        out.writeShort(0);
                    }
                    else {
                        out.writeShort(args.length);
                        for(int i=0; i < args.length; i++) {
                            Util.objectToStream(args[i], out);
                        }
                    }
                }
                else if(obj instanceof Cache.Value) {
                    Cache.Value value=(Cache.Value)obj;
                    out.writeByte(VALUE);
                    out.writeLong(value.getTimeout());
                    Util.objectToStream(value.getValue(), out);
                }
                else {
                    out.writeByte(OBJ);
                    Util.objectToStream(obj, out);
                }
                out.flush();
                return out_stream.toByteArray();
            }
            finally {
                Util.close(out);
            }
        }