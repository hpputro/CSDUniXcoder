public void processOpcode(Node node) {
		NodeList list = node.getChildNodes(), clist;
		int length = list.getLength();
		String nodeName;
		OpcodeInfo info;
		NamedNodeMap attributes;
		int t, counter;
		short[] operands;
		info = new OpcodeInfo();
		for (int i = 0; i < length; i++) {
			node = list.item(i);
			nodeName = node.getNodeName();
			if ("name".equals(nodeName)) {
				info.opname = node.getChildNodes().item(0).getNodeValue();
			} else if ("code".equals(nodeName)) {
				info.opcode = (byte) Integer.parseInt(node.getChildNodes().item(0).getNodeValue());
			} else if ("consumeStack".equals(nodeName)) {
				info.consumeStack = (byte) Integer.parseInt(node.getChildNodes().item(0).getNodeValue());
			} else if ("produceStack".equals(nodeName)) {
				info.produceStack = (byte) Integer.parseInt(node.getChildNodes().item(0).getNodeValue());
			} else if ("operandsInfo".equals(nodeName)) {
				attributes = node.getAttributes();
				t = Integer.parseInt(attributes.item(0).getNodeValue());
				info.operandsCount = (short) t;
				clist = node.getChildNodes();
				operands = new short[t];
				counter = 0;
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if ("length".equals(node.getNodeName())) {
						operands[counter++] = Short.parseShort(node.getFirstChild().getNodeValue());
					}
				}
				info.operandsLength = operands;

			} else if ("operation".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.operation = node.getNodeValue();
					}
				}
			} else if ("format".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.format = node.getNodeValue();
					}
				}
			} else if ("forms".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.forms = node.getNodeValue();
					}
				}
			} else if ("operandStack".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.operandStack = node.getNodeValue();
					}
				}
			} else if ("description".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.description = node.getNodeValue();
					}
				}
			} else if ("runtimeExceptions".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.runtimeExceptions = node.getNodeValue();
					}
				}

			} else if ("linkingExceptions".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.linkingExceptions = node.getNodeValue();
					}
				}
			} else if ("notes".equals(nodeName)) {
				clist = node.getChildNodes();
				for (t = 0; t < clist.getLength(); t++) {
					node = clist.item(t);
					if (Node.CDATA_SECTION_NODE == node.getNodeType()) {
						info.notes = node.getNodeValue();
					}
				}
			}
		}
		infos[info.opcode & 0xFF] = info;
	}