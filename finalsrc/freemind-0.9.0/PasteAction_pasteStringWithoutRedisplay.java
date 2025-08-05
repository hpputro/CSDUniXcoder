	private MindMapNode pasteStringWithoutRedisplay(Transferable t, MindMapNode parent,
													boolean asSibling, boolean isLeft) 
	throws UnsupportedFlavorException, IOException {

       String textFromClipboard = (String) t.getTransferData(DataFlavor.stringFlavor);
	   Pattern mailPattern = Pattern.compile("([^@ <>\\*']+@[^@ <>\\*']+)");

	   String[] textLines = textFromClipboard.split("\n");

	   if (textLines.length > 1) {
		  pMindMapController.getFrame().setWaitingCursor(true); }

	   MindMapNode realParent = null;
	   if (asSibling) {
		  		  		  		  realParent = parent;
		  parent = new MindMapNodeModel(pMindMapController.getFrame(), pMindMapController.getMap()); }

	   ArrayList parentNodes = new ArrayList();
	   ArrayList parentNodesDepths = new ArrayList();

	   parentNodes.add(parent);
	   parentNodesDepths.add(new Integer(-1));

	   String[] linkPrefixes = { "http:
	   MindMapNode pastedNode = null;

	   for (int i = 0; i < textLines.length; ++i) {
		  String text = textLines[i];
		  text = text.replaceAll("\t","        ");
		  if (text.matches(" *")) {
			 continue; }

		  int depth = 0;
		  while (depth < text.length() && text.charAt(depth) == ' ') {
			 ++depth; }
		  String visibleText = text.trim();

		  		  		  
		  if (visibleText.matches("^http:			 visibleText = visibleText.replaceAll("^http:				replaceAll("(/|\\.[^\\./\\?]*)$","").replaceAll("((\\.[^\\./]*\\?)|\\?)[^/]*$"," ? ...").replaceAll("_|%20"," ");
			 String[] textParts = visibleText.split("/");
			 visibleText = "";
			 for (int textPartIdx = 0; textPartIdx < textParts.length; textPartIdx++) {
				if (textPartIdx > 0 ) {
				   visibleText += " > "; }
				visibleText += textPartIdx == 0 ? textParts[textPartIdx] :
				   Tools.firstLetterCapitalized(textParts[textPartIdx].replaceAll("^~*","")); }}

		  MindMapNode node = pMindMapController.newNode(visibleText, parent.getMap());
		  if (textLines.length == 1) {
			 pastedNode = node; }

		  
		  Matcher mailMatcher = mailPattern.matcher(visibleText);
		  if (mailMatcher.find()) {
			 node.setLink("mailto:"+mailMatcher.group()); }

		  		  		  
		  for (int j = 0; j < linkPrefixes.length; j++) {
			 int linkStart = text.indexOf(linkPrefixes[j]);
			 if (linkStart != -1) {
				int linkEnd = linkStart;
				while (linkEnd < text.length() &&
					   !nonLinkCharacter.matcher(text.substring(linkEnd,linkEnd+1)).matches()) {
				   linkEnd++; }
				node.setLink(text.substring(linkStart,linkEnd)); }}

		  		  
		  for (int j = parentNodes.size()-1; j >= 0; --j) {
			 if (depth > ((Integer)parentNodesDepths.get(j)).intValue()) {
				for (int k = j+1; k < parentNodes.size(); ++k) {
					MindMapNode n = (MindMapNode)parentNodes.get(k);
					if(n.getParentNode() == parent){
						addUndoAction(n);
					}
				   parentNodes.remove(k);
				   parentNodesDepths.remove(k); }
				MindMapNode target = (MindMapNode)parentNodes.get(j);
		        node.setLeft(isLeft);
				insertNodeInto(node, target);
				parentNodes.add(node);
				parentNodesDepths.add(new Integer(depth));
				break; }}}

		for (int k = 0; k < parentNodes.size(); ++k) {
			MindMapNode n = (MindMapNode)parentNodes.get(k);
			if(n.getParentNode() == parent){
				addUndoAction(n);
			}
		}
	   return pastedNode;
	}