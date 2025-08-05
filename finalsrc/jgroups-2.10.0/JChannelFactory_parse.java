private void parse(Element root, boolean replace) throws Exception {
        
        String root_name=root.getNodeName();
        if(!PROTOCOL_STACKS.equals(root_name.trim().toLowerCase())) {
            String error="XML protocol stack configuration does not start with a '<config>' element; " +
                    "maybe the XML configuration needs to be converted to the new format ?\n" +
                    "use 'java org.jgroups.conf.XmlConfigurator <old XML file> -new_format' to do so";
            throw new IOException("invalid XML configuration: " + error);
        }

        NodeList tmp_stacks=root.getChildNodes();
        for(int i=0; i < tmp_stacks.getLength(); i++) {
            Node node = tmp_stacks.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE )
                continue;

            Element stack=(Element) node;
            String tmp=stack.getNodeName();
            if(!STACK.equals(tmp.trim().toLowerCase())) {
                throw new IOException("invalid configuration: didn't find a \"" + STACK + "\" element under \"" + PROTOCOL_STACKS + "\"");
            }

            NamedNodeMap attrs = stack.getAttributes();
            Node name=attrs.getNamedItem(NAME);
                        String st_name=name.getNodeValue();
                                    NodeList configs=stack.getChildNodes();
            for(int j=0; j < configs.getLength(); j++) {
                Node tmp_config=configs.item(j);
                if(tmp_config.getNodeType() != Node.ELEMENT_NODE )
                    continue;
                Element cfg = (Element) tmp_config;
                tmp=cfg.getNodeName();
                if(!CONFIG.equals(tmp))
                    throw new IOException("invalid configuration: didn't find a \"" + CONFIG + "\" element under \"" + STACK + "\"");

                XmlConfigurator conf=XmlConfigurator.getInstance(cfg);
                                ConfiguratorFactory.substituteVariables(conf);                 String val=conf.getProtocolStackString();
                if(replace) {
                    stacks.put(st_name, val);
                    if(log.isTraceEnabled())
                        log.trace("added config '" + st_name + "'");
                }
                else {
                    if(!stacks.containsKey(st_name)) {
                        stacks.put(st_name, val);
                        if(log.isTraceEnabled())
                            log.trace("added config '" + st_name + "'");
                    }
                    else {
                        if(log.isTraceEnabled())
                            log.trace("didn't add config '" + st_name + " because one of the same name already existed");
                    }
                }
            }
        }
    }