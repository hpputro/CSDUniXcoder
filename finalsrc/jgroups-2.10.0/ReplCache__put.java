public V _put(K key, V val, short repl_count, long timeout, boolean force) {

        if(!force) {

                        boolean accept=repl_count == -1;

            if(!accept) {
                if(view != null && repl_count >= view.size()) {
                    accept=true;
                }
                else {
                    List<Address> selected_hosts=hash_function != null? hash_function.hash(key, repl_count) : null;
                    if(selected_hosts != null) {
                        if(log.isTraceEnabled())
                            log.trace("local=" + local_addr + ", hosts=" + selected_hosts);
                        for(Address addr: selected_hosts) {
                            if(addr.equals(local_addr)) {
                                accept=true;
                                break;
                            }
                        }
                    }
                    if(!accept)
                        return null;
                }
            }
        }

        if(log.isTraceEnabled())
            log.trace("_put(" + key + ", " + val + ", " + repl_count + ", " + timeout + ")");

        Value<V> value=new Value<V>(val, repl_count);
        Value<V> retval=l2_cache.put(key, value, timeout);

        if(l1_cache != null)
            l1_cache.remove(key);

        notifyChangeListeners();

        return retval != null? retval.getVal() : null;
    }