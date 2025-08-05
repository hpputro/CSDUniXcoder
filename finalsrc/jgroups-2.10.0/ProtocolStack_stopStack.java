public void stopStack(String cluster_name) {
        if(stopped) return;
        for(final Protocol prot: getProtocols()) {
            if(prot instanceof TP) {
                TP transport=(TP)prot;
                if(transport.isSingleton()) {
                    String singleton_name=transport.getSingletonName();
                    final Map<String,Protocol> up_prots=transport.getUpProtocols();
                    synchronized(up_prots) {
                        up_prots.remove(cluster_name);
                    }
                    synchronized(singleton_transports) {
                        Tuple<TP, ProtocolStack.RefCounter> val=singleton_transports.get(singleton_name);
                        if(val != null) {
                            ProtocolStack.RefCounter counter=val.getVal2();
                            short num_starts=counter.decrementStartCount();
                            if(num_starts > 0) {
                                continue;                             }
                                                                                    }
                    }
                }
            }
            prot.stop();
        }

        TP transport=getTransport();
        transport.unregisterProbeHandler(props_handler);
        stopped=true;
    }