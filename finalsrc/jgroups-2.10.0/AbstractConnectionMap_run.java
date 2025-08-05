public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                lock.lock();
                try {
                    for(Iterator<Entry<Address,V>> it=conns.entrySet().iterator();it.hasNext();) {
                        Entry<Address,V> entry=it.next();
                        V c=entry.getValue();
                        if(c.isExpired(System.currentTimeMillis())) {
                            log.info("Connection " + c.toString() + " reaped");
                            Util.close(c);                                            
                            it.remove();                           
                        }
                    }
                }
                finally {
                    lock.unlock();
                }
                Util.sleep(reaperInterval);
            }           
        }