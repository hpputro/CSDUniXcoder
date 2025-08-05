public Object up(Event evt) {
        if(evt.getType() == Event.MSG) {
            Message msg=(Message)evt.getArg();
            CompressHeader hdr=(CompressHeader)msg.getHeader(this.id);
            if(hdr != null) {
                byte[] compressed_payload=msg.getRawBuffer();
                if(compressed_payload != null && compressed_payload.length > 0) {
                    int original_size=hdr.original_size;
                    byte[] uncompressed_payload=new byte[original_size];
                    Inflater inflater=null;
                    try {
                        inflater=inflater_pool.take();
                        inflater.reset();
                        inflater.setInput(compressed_payload, msg.getOffset(), msg.getLength());
                        try {
                            inflater.inflate(uncompressed_payload);
                            if(log.isTraceEnabled())
                                log.trace("uncompressed " + compressed_payload.length + " bytes to " + original_size +
                                        " bytes");
                                                        Message copy=msg.copy(false);
                            copy.setBuffer(uncompressed_payload);
                            return up_prot.up(new Event(Event.MSG, copy));
                                                    }
                        catch(DataFormatException e) {
                            if(log.isErrorEnabled()) log.error("exception on uncompression", e);
                        }
                    }
                    catch(InterruptedException e) {
                        Thread.currentThread().interrupt();                     }
                    finally {
                        if(inflater != null)
                            inflater_pool.offer(inflater);
                    }

                }
            }
        }
        return up_prot.up(evt);
    }