private void actualizePlugins() {
		if (importWizard == null) {
			
			
			
			pluginInfo = new HashMap();
			allPlugins = new Vector();
			allRegistrations = new HashSet();
				 IUnmarshallingContext unmarshaller = XmlBindingTools.getInstance()
				.createUnmarshaller();
				for (Iterator i =  i.hasNext();) {
				String xmlPluginFile = (String) i.next();
				if (xmlPluginFile.matches(pluginPrefixRegEx)) {
										
					xmlPluginFile = xmlPluginFile.replace('\\', '/')
							+ 
										URL pluginURL = frame.getFreeMindClassLoader().getResource(xmlPluginFile);
										Plugin plugin = null;
					try {
						logger.finest("Reading: " + xmlPluginFile + " from "
								+ pluginURL);
						InputStream in = pluginURL.openStream();
						plugin = (Plugin) unmarshaller.unmarshalDocument(in,
								null);
					} catch (Exception e) {
												freemind.main.Resources.getInstance().logException(e);
						continue;
					}
										for (Iterator j = plugin.getListChoiceList().iterator(); j
							.hasNext();) {
						Object obj = j.next();
						if (obj instanceof PluginAction) {
							PluginAction action = (PluginAction) obj;
							pluginInfo.put(action.getLabel(),
									new HookDescriptorPluginAction(frame, xmlPluginFile, plugin, action));
							allPlugins.add(action.getLabel());

						} else if (obj instanceof PluginRegistration) {
							PluginRegistration registration = (PluginRegistration) obj;
							allRegistrations.add(new HookDescriptorRegistration(frame, xmlPluginFile, plugin, registration));
						}
					}
				}
			}		
		}
	}