	public String getCountryMapping(String country)
	{
		if (country == null)
			return null;
		if (countryMappings.get(country) == null)
			return country;
		return countryMappings.get(country);
	}