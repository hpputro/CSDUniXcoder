public void startStack(String cluster_name, Address local_addr) throws Exception {
        if(stopped == false) return;

        Protocol above_prot=null;
        for(final Protocol prot: getProtocols()) {
            if(prot instanceof TP) {
                String singleton_name=((TP)prot).getSingletonName();
                TP transport=(TP)prot;
                if(transport.isSingleton() && cluster_name != null) {
                    final Map<String, Protocol> up_prots=transport.getUpProtocols();

                    synchronized(singleton_transports) {
                        synchronized(up_prots) {
                            Set<String> keys=up_prots.keySet();
                            if(keys.contains(cluster_name))
                                throw new IllegalStateException("cluster '" + cluster_name + "' is already connected to singleton " +
                                        "transport: " + keys);

                            for(Iterator<Map.Entry<String,Protocol>> it=up_prots.entrySet().iterator(); it.hasNext();) {
                                Map.Entry<String,Protocol> entry=it.next();
                                Protocol tmp=entry.getValue();
                                if(tmp == above_prot) {
                                    it.remove();
                                }
                            }

                            if(above_prot != null) {
                                TP.ProtocolAdapter ad=new TP.ProtocolAdapter(cluster_name, local_addr, prot.getId(),
                                                                             above_prot, prot,
                                                                             transport.getThreadNamingPattern());
                                ad.setProtocolStack(above_prot.getProtocolStack());
                                above_prot.setDownProtocol(ad);
                                up_prots.put(cluster_name, ad);
                            }
                        }
                        Tuple<TP, ProtocolStack.RefCounter> val=singleton_transports.get(singleton_name);
                        if(val != null) {
                            ProtocolStack.RefCounter counter=val.getVal2();
                            short num_starts=counter.incrementStartCount();
                            if(num_starts >= 1) {
                                continue;
                            }
                            else {
                                prot.start();
                                above_prot=prot;
                                continue;
                            }
                        }
                    }
                }
            }
            prot.start();
            above_prot=prot;
        }

        TP transport=getTransport();
        transport.registerProbeHandler(props_handler);
        stopped=false;
    }