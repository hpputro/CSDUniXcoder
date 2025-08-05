public void destroy() {
        if(top_prot != null) {
            for(Protocol prot: getProtocols()) {
                if(prot instanceof TP) {
                    TP transport=(TP)prot;
                    if(transport.isSingleton()) {
                        String singleton_name=transport.getSingletonName();
                        synchronized(singleton_transports) {
                            Tuple<TP, ProtocolStack.RefCounter> val=singleton_transports.get(singleton_name);
                            if(val != null) {
                                ProtocolStack.RefCounter counter=val.getVal2();
                                short num_inits=counter.decrementInitCount();
                                if(num_inits >= 1) {
                                    continue;
                                }
                                else
                                    singleton_transports.remove(singleton_name);
                            }
                        }
                    }
                }
                prot.destroy();
            }

            
                    }
    }