public EntryComparator getInstance(String field, String order) {
		EntryComparator product;
		if (field == null){
			System.out.println('please check your input');
		}

		if (field.equals("Creation"))
			product = this;
		else if (field.equals("Check"))
			product = new CheckComparator();
		else if (field.equals("Date"))
			product = new DateComparator();
		else if (field.equals("Valuta"))
			product = new ValutaComparator();
		else if (field.equals("Description"))
			product = new DescriptionComparator();
		else if (field.equals("Category"))
			product = new CategoryComparator();
		else if (field.equals("Amount"))
			product = new AmountComparator();
		else if (field.equals("Status"))
			product = new StatusComparator();
		else if (field.equals("Memo"))
			product = new MemoComparator();
		else {
			System.err.println(
				"preferences/entrySortField - invalid value: " + field);
			product = this;
		}
		product.setOrder(order);
		if (product == null){
			return null;
		}
		return product;
	}