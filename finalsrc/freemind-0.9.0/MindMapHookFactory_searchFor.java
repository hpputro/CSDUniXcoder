private Vector searchFor(Class baseClass, Class mode) {
		actualizePlugins();
		Vector returnValue = new Vector();
		String modeName = mode.getPackage().getName();
		for (Iterator i = allPlugins.iterator(); i.hasNext();) {
			String label = (String) i.next();
			HookDescriptorPluginAction descriptor = getHookDescriptor(label);
						try {
				logger.finest("Loading: " + label);
				if (baseClass.isAssignableFrom(Class.forName(descriptor
						.getBaseClass()))) {
															for (Iterator j = descriptor.getModes().iterator(); j
							.hasNext();) {
						String pmode = (String) j.next();
						if (pmode.equals(modeName)) {
														returnValue.add(label);
						}

					}
				}
			} catch (ClassNotFoundException e) {
				logger.severe("Class not found.");
freemind.main.Resources.getInstance().logException(				e);
			}
		}
		return returnValue;
	}