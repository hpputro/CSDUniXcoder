

































public interface ModeController extends TextTranslator  {

    public static final String NODESEPARATOR = "<nodeseparator>";
	
    ModeController load(URL file) throws FileNotFoundException, IOException, XMLParseException, URISyntaxException;
    
    ModeController load(File file) throws FileNotFoundException, IOException;
    
    
    void loadURL(String relative);
    boolean save(File file);
    MindMap newMap();
    
    boolean save();
    boolean saveAs();
    void open();
    boolean close(boolean force, MapModuleManager mapModuleManager);
    
    MindMapNode createNodeTreeFromXml(Reader pReader, HashMap pIDToTarget)
		throws XMLParseException, IOException;
        void startupController();
    void shutdownController();
    
        void doubleClick(MouseEvent e);
    void plainClick(MouseEvent e);
    
    void setVisible(boolean visible);

    boolean isBlocked();
        
    NodeAdapter getNodeFromID(String nodeID);
    
    String getNodeID(MindMapNode selected);

    
    public void select( NodeView node) ;

    MindMapNode getSelected();
    NodeView getSelectedView();
	
	List getSelecteds();
	
    List getSelectedsByDepth();
    
    public void sortNodesByDepth(List inPlaceList) ;
	    
    boolean extendSelection(MouseEvent e);
    
    void nodeChanged(MindMapNode n);
    
    void onSelectHook(NodeView node);
    
    void onDeselectHook(NodeView node);
    

	void onViewCreatedHook(NodeView newView);
	

	void onViewRemovedHook(NodeView newView);
	
    
    public interface NodeSelectionListener {

        
        void onUpdateNodeHook(MindMapNode node);

        
        void onSelectHook(NodeView node);
        
        void onDeselectHook(NodeView node);

		
		void onSaveNode(MindMapNode node);

    }

    void registerNodeSelectionListener(NodeSelectionListener listener);

    void deregisterNodeSelectionListener(NodeSelectionListener listener);
    
    
    void firePreSaveEvent(MindMapNode node);
    
    
    public interface NodeLifetimeListener {

        
        void onCreateNodeHook(MindMapNode node);

        
        void onPreDeleteNode(MindMapNode node);
        
        void onPostDeleteNode(MindMapNode node, MindMapNode parent);

    }

    
    void   registerNodeLifetimeListener(NodeLifetimeListener listener);
    void deregisterNodeLifetimeListener(NodeLifetimeListener listener);
    
    void fireNodePreDeleteEvent(MindMapNode node);
    
     
    void setFolded(MindMapNode node, boolean folded);
	
	void displayNode(MindMapNode node);
	
    void centerNode(MindMapNode node);
	String getLinkShortText(MindMapNode node);

    public JToolBar getModeToolBar();
    
    public Component getLeftToolBar();


	
	public void updateMenus(StructuredMenuHolder holder);
	public void updatePopupMenu(StructuredMenuHolder holder);

    JPopupMenu getPopupMenu();
    void showPopupMenu(MouseEvent e);
    
    JPopupMenu getPopupForModel(java.lang.Object obj);

	FreeMindMain getFrame();
	MapView getView();
	MindMap getMap();
	
	void setModel(MapAdapter model);
	Mode getMode();
	Controller getController();
	HookFactory getHookFactory();
	Color getSelectionColor();
    
    String getText(String textId);
    URL getResource(String path);
    
    public File getLastCurrentDir();
    
    public void setLastCurrentDir(File pLastCurrentDir);

    AttributeController getAttributeController();
    void nodeRefresh(MindMapNode node);

    NodeView getNodeView(MindMapNode node);

    void refreshMap();

    Transferable copy(MindMapNode node, boolean saveInvisible);
    Transferable copy();
    Transferable copySingle();
    public Transferable copy(List selectedNodes, boolean copyInvisible);

	JFileChooser getFileChooser(FileFilter filter);
    
}
