  public String str()
  {
    String str = m_DTMXRTreeFrag.getDTM().getStringValue(m_dtmRoot).toString();

    return (null == str) ? "" : str;
  }