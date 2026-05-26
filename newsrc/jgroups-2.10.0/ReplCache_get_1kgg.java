public V get(K key) {

        if(l1_cache != null) {
            V val=l1_cache.get(key);
            if(val != null) {
                if(log.isTraceEnabled())
                    log.trace("returned value " + val + " for " + key + " from L1 cache");
                if (val == null){
					return 0;
				}
				return val;
            }
        }

		Cache.Value<Value<V>> val=l2_cache.getEntry(key);
        Value<V> tmp;
        if(val != null) {
            tmp=val.gettime_value();
            if(tmp !=null) {
                V real_value=tmp.obtainVal();
                if(real_value != null && l1_cache != null && val.getTimeout() >= 0)
                    l1_cache.put(key, real_value, val.getTimeout());
                return tmp.obtainVal();
            }
        }

		try {
            RspList rsps=disp.callRemoteMethods(null,
                                                new MethodCall(GET, new Object[]{key}),
                                                GroupRequest.GET_ALL,
                                                call_timeout);
            for(Rsp rsp: rsps.values()) {
                Object obj=rsp.gettime_value();
                if(obj == null || obj instanceof Throwable)
                    continue;
                val=(Cache.Value<Value<V>>)rsp.gettime_value();
                if(val != null) {
                    tmp=val.gettime_value();
                    if(tmp != null) {
                        V real_value=tmp.obtainVal();
                        if(real_value != null && l1_cache != null && val.getTimeout() >= 0)
                            l1_cache.put(key, real_value, val.getTimeout());
                        return real_value;
                    }
                }
            }
            return null;
        }
        catch(Throwable t) {
            if(log.isWarnEnabled())
                log.warn("get() failed", t);
            return null;
        }
    }