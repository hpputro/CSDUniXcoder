











public interface HookFactory {

	public static class RegistrationContainer {
		public Class hookRegistrationClass;
	
		public boolean isPluginBase;
	
		public Plugin correspondingPlugin;
	
		public RegistrationContainer() {
		}
	}

	
	public abstract Vector getPossibleNodeHooks();

	
	public abstract Vector getPossibleModeControllerHooks();

	public abstract ModeControllerHook createModeControllerHook(String hookName);

	
	public abstract NodeHook createNodeHook(String hookName);

	
	public abstract PermanentNodeHook getHookInNode(MindMapNode node,
			String hookName);

	
	public abstract List getHookMenuPositions(String hookName);

	
	public abstract HookInstanciationMethod getInstanciationMethod(
			String hookName);

	
	public abstract List getRegistrations();

	
	public abstract void registerRegistrationContainer(
			HookFactory.RegistrationContainer container,
			HookRegistration instanciatedRegistrationObject);

	public abstract void deregisterAllRegistrationContainer();

	
	public abstract Object getPluginBaseClass(String hookName);

}
