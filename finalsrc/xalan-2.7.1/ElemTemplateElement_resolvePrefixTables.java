  public void resolvePrefixTables() throws TransformerException
  {
        setPrefixTable(null);

                    if (null != this.m_declaredPrefixes)
    {
      StylesheetRoot stylesheet = this.getStylesheetRoot();
      
                  int n = m_declaredPrefixes.size();

      for (int i = 0; i < n; i++)
      {
        XMLNSDecl decl = (XMLNSDecl) m_declaredPrefixes.get(i);
        String prefix = decl.getPrefix();
        String uri = decl.getURI();
        if(null == uri)
          uri = "";
        boolean shouldExclude = excludeResultNSDecl(prefix, uri);

                if (null == m_prefixTable)
            setPrefixTable(new ArrayList());

        NamespaceAlias nsAlias = stylesheet.getNamespaceAliasComposed(uri);
        if(null != nsAlias)
        {
                              
                                        decl = new XMLNSDecl(nsAlias.getStylesheetPrefix(), 
                              nsAlias.getResultNamespace(), shouldExclude);
        }
        else
          decl = new XMLNSDecl(prefix, uri, shouldExclude);

        m_prefixTable.add(decl);
        
      }
    }

    ElemTemplateElement parent = this.getParentNodeElem();

    if (null != parent)
    {

            List prefixes = parent.m_prefixTable;

      if (null == m_prefixTable && !needToCheckExclude())
      {

                setPrefixTable(parent.m_prefixTable);
      }
      else
      {

                int n = prefixes.size();
        
        for (int i = 0; i < n; i++)
        {
          XMLNSDecl decl = (XMLNSDecl) prefixes.get(i);
          boolean shouldExclude = excludeResultNSDecl(decl.getPrefix(),
                                                      decl.getURI());

          if (shouldExclude != decl.getIsExcluded())
          {
            decl = new XMLNSDecl(decl.getPrefix(), decl.getURI(),
                                 shouldExclude);
          }
          
                    addOrReplaceDecls(decl);
        }
      }
    }
    else if (null == m_prefixTable)
    {

            setPrefixTable(new ArrayList());
    }
  }