public Object down(Event evt) {
        if(evt.getType() == Event.MSG) {
            Message msg=(Message)evt.getArg();
            int length=msg.getLength();             if(length >= min_size) {
                byte[] payload=msg.getRawBuffer();                 byte[] compressed_payload=new byte[length];
                int compressed_size;
                Deflater deflater=null;
                try {
                    deflater=deflater_pool.take();
                    deflater.reset();
                    deflater.setInput(payload, msg.getOffset(), length);
                    deflater.finish();
                    deflater.deflate(compressed_payload);
                    compressed_size=deflater.getTotalOut();

                    if ( compressed_size < length ) {                         byte[] new_payload=new byte[compressed_size];
                        System.arraycopy(compressed_payload, 0, new_payload, 0, compressed_size);
                        msg.setBuffer(new_payload);
                        msg.putHeader(this.id, new CompressHeader(length));
                        if(log.isTraceEnabled())
                            log.trace("compressed payload from " + length + " bytes to " + compressed_size + " bytes");
                    }
                    else {
                        if(log.isTraceEnabled())
                            log.trace("Skipping compression since the compressed message is larger than the original");
                    }
                }
                catch(InterruptedException e) {
                    Thread.currentThread().interrupt();                     throw new RuntimeException(e);
                }
                finally {
                    if(deflater != null)
                        deflater_pool.offer(deflater);
                }
            }
        }
        return down_prot.down(evt);
    }