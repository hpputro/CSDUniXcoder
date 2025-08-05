    public static void setDefaultValues(Vector<ProtocolConfiguration> protocol_configs, Vector<Protocol> protocols,
                                        StackType ip_version) throws Exception {
        InetAddress default_ip_address=Util.getNonLoopbackAddress();
        if(default_ip_address == null) {
            log.warn("unable to find an address other than loopback for IP version " + ip_version);
            default_ip_address=Util.getLocalhost(ip_version);
        }

        for(int i=0; i < protocol_configs.size(); i++) {
            ProtocolConfiguration protocol_config=protocol_configs.get(i);
            Protocol protocol=protocols.get(i);
            String protocolName=protocol.getName();

                        Map<String,String> properties=protocol_config.getOriginalProperties();

            Method[] methods=Util.getAllDeclaredMethodsWithAnnotations(protocol.getClass(), Property.class);
            for(int j=0; j < methods.length; j++) {
                if(isSetPropertyMethod(methods[j])) {
                    String propertyName=PropertyHelper.getPropertyName(methods[j]);

                    Object propertyValue=getValueFromProtocol(protocol, propertyName);
                    if(propertyValue == null) {                         Property annotation=methods[j].getAnnotation(Property.class);

                                                String defaultValue=null;
                        if(InetAddressInfo.isInetAddressRelated(methods[j])) {
                            defaultValue=ip_version == StackType.IPv4? annotation.defaultValueIPv4() : annotation.defaultValueIPv6();
                            if(defaultValue != null && defaultValue.length() > 0) {
                                Object converted=null;
                                try {
                                    if(defaultValue.equalsIgnoreCase(Global.NON_LOOPBACK_ADDRESS))
                                        converted=default_ip_address;
                                    else
                                        converted=PropertyHelper.getConvertedValue(protocol, methods[j], properties, defaultValue, true);
                                    methods[j].invoke(protocol, converted);
                                }
                                catch(Exception e) {
                                    throw new Exception("default could not be assigned for method " + propertyName + " in "
                                            + protocolName + " with default " + defaultValue, e);
                                }
                                if(log.isDebugEnabled())
                                    log.debug("set property " + protocolName + "." + propertyName + " to default value " + converted);
                            }
                        }
                    }
                }
            } 

                        Field[] fields=Util.getAllDeclaredFieldsWithAnnotations(protocol.getClass(), Property.class);
            for(int j=0; j < fields.length; j++) {
                String propertyName=PropertyHelper.getPropertyName(fields[j], properties);
                Object propertyValue=getValueFromProtocol(protocol, fields[j]);
                if(propertyValue == null) {
                                        Property annotation=fields[j].getAnnotation(Property.class);

                                        String defaultValue=null;
                    if(InetAddressInfo.isInetAddressRelated(protocol, fields[j])) {
                        defaultValue=ip_version == StackType.IPv4? annotation.defaultValueIPv4() : annotation.defaultValueIPv6();
                        if(defaultValue != null && defaultValue.length() > 0) {
                                                        if(defaultValue != null || !PropertyHelper.usesDefaultConverter(fields[j])) {
                                Object converted=null;
                                try {
                                    if(defaultValue.equalsIgnoreCase(Global.NON_LOOPBACK_ADDRESS))
                                        converted=default_ip_address;
                                    else
                                        converted=PropertyHelper.getConvertedValue(protocol, fields[j], properties, defaultValue, true);
                                    if(converted != null)
                                        setField(fields[j], protocol, converted);
                                }
                                catch(Exception e) {
                                    throw new Exception("default could not be assigned for field " + propertyName + " in "
                                            + protocolName + " with default value " + defaultValue, e);
                                }

                                if(log.isDebugEnabled())
                                    log.debug("set property " + protocolName + "." + propertyName + " to default value " + converted);
                            }
                        }
                    }
                }
            }
        }
    }