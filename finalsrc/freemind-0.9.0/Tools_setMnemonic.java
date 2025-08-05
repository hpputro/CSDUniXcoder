public void setMnemonic(char charAfterMnemoSign) {
	        int vk = (int) charAfterMnemoSign;
	        if(vk >= 'a' && vk <='z')
	            vk -= ('a' - 'A');
	 		action.putValue(Action.MNEMONIC_KEY, new Integer(vk));
		}