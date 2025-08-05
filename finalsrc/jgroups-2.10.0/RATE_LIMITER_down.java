public Object down(Event evt) {
        if(evt.getType() == Event.MSG) {
            Message msg=(Message)evt.getArg();
            int len=msg.getLength();

            lock.lock();
            try {
                if(len > max_bytes) {
                    log.error("message length (" + len + " bytes) exceeded max_bytes (" + max_bytes + "); " +
                            "adjusting max_bytes to " + len);
                    max_bytes=len;
                }

                while(true) {
                    boolean size_exceeded=num_bytes_sent + len >= max_bytes,
                            time_exceeded=System.currentTimeMillis() > end_of_current_period;
                    if(!size_exceeded && !time_exceeded)
                        break;

                    if(time_exceeded) {
                        reset();
                    }
                    else {                         long block_time=end_of_current_period - System.currentTimeMillis();
                        if(block_time > 0) {
                            try {
                                block.await(block_time, TimeUnit.MILLISECONDS);
                                num_blockings++;
                                total_block_time+=block_time;
                            }
                            catch(InterruptedException e) {
                            }
                        }
                    }
                }
            }
            finally {
                num_bytes_sent+=len;
                lock.unlock();
            }

            return down_prot.down(evt);
        }

        return down_prot.down(evt);
    }