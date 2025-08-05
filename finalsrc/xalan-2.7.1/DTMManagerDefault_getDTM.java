  synchronized public DTM getDTM(Source source, boolean unique,
                                 DTMWSFilter whiteSpaceFilter,
                                 boolean incremental, boolean doIndexing)
  {

    if(DEBUG && null != source)
      System.out.println("Starting "+
                         (unique ? "UNIQUE" : "shared")+
                         " source: "+source.getSystemId()
                         );

    XMLStringFactory xstringFactory = m_xsf;
    int dtmPos = getFirstFreeDTMID();
    int documentID = dtmPos << IDENT_DTM_NODE_BITS;

    if ((null != source) && source instanceof DOMSource)
    {
      DOM2DTM dtm = new DOM2DTM(this, (DOMSource) source, documentID,
                                whiteSpaceFilter, xstringFactory, doIndexing);

      addDTM(dtm, dtmPos, 0);

                        
      return dtm;
    }
    else
    {
      boolean isSAXSource = (null != source)
        ? (source instanceof SAXSource) : true;
      boolean isStreamSource = (null != source)
        ? (source instanceof StreamSource) : false;

      if (isSAXSource || isStreamSource) {
        XMLReader reader = null;
        SAX2DTM dtm;

        try {
          InputSource xmlSource;

          if (null == source) {
            xmlSource = null;
          } else {
            reader = getXMLReader(source);
            xmlSource = SAXSource.sourceToInputSource(source);

            String urlOfSource = xmlSource.getSystemId();

            if (null != urlOfSource) {
              try {
                urlOfSource = SystemIDResolver.getAbsoluteURI(urlOfSource);
              } catch (Exception e) {
                                System.err.println("Can not absolutize URL: " + urlOfSource);
              }

              xmlSource.setSystemId(urlOfSource);
            }
          }

          if (source==null && unique && !incremental && !doIndexing) {
                                                                                                dtm = new SAX2RTFDTM(this, source, documentID, whiteSpaceFilter,
                                 xstringFactory, doIndexing);
          }
          
                    else {
            dtm = new SAX2DTM(this, source, documentID, whiteSpaceFilter,
                              xstringFactory, doIndexing);
          }

                                        addDTM(dtm, dtmPos, 0);


          boolean haveXercesParser =
                     (null != reader)
                     && (reader.getClass()
                               .getName()
                               .equals("org.apache.xerces.parsers.SAXParser") );
        
          if (haveXercesParser) {
            incremental = true;            }
        
                              if (m_incremental && incremental
               ) {
            IncrementalSAXSource coParser=null;

            if (haveXercesParser) {
                            try {
                coParser =(IncrementalSAXSource)
                  Class.forName("org.apache.xml.dtm.ref.IncrementalSAXSource_Xerces").newInstance();  
              }  catch( Exception ex ) {
                ex.printStackTrace();
                coParser=null;
              }
            }

            if (coParser==null ) {
                            if (null == reader) {
                coParser = new IncrementalSAXSource_Filter();
              } else {
                IncrementalSAXSource_Filter filter =
                         new IncrementalSAXSource_Filter();
                filter.setXMLReader(reader);
                coParser=filter;
              }
            }

			
            
          
                        dtm.setIncrementalSAXSource(coParser);

            if (null == xmlSource) {

                            return dtm;
            }

            if (null == reader.getErrorHandler()) {
              reader.setErrorHandler(dtm);
            }
            reader.setDTDHandler(dtm);

            try {
                            
              coParser.startParse(xmlSource);
            } catch (RuntimeException re) {

              dtm.clearCoRoutine();

              throw re;
            } catch (Exception e) {

              dtm.clearCoRoutine();

              throw new org.apache.xml.utils.WrappedRuntimeException(e);
            }
          } else {
            if (null == reader) {

                            return dtm;
            }

                        reader.setContentHandler(dtm);
            reader.setDTDHandler(dtm);
            if (null == reader.getErrorHandler()) {
              reader.setErrorHandler(dtm);
            }

            try {
              reader.setProperty(
                               "http:                               dtm);
            } catch (SAXNotRecognizedException e){}
              catch (SAXNotSupportedException e){}

            try {
              reader.parse(xmlSource);
            } catch (RuntimeException re) {
              dtm.clearCoRoutine();

              throw re;
            } catch (Exception e) {
              dtm.clearCoRoutine();

              throw new org.apache.xml.utils.WrappedRuntimeException(e);
            }
          }

          if (DUMPTREE) {
            System.out.println("Dumping SAX2DOM");
            dtm.dumpDTM(System.err);
          }

          return dtm;
        } finally {
                              if (reader != null && !(m_incremental && incremental)) {
            reader.setContentHandler(m_defaultHandler);
            reader.setDTDHandler(m_defaultHandler);
            reader.setErrorHandler(m_defaultHandler);
            
                        try {
              reader.setProperty("http:            }
            catch (Exception e) {}
          }
          releaseXMLReader(reader);
        }
      } else {

                        throw new DTMException(XMLMessages.createXMLMessage(XMLErrorResources.ER_NOT_SUPPORTED, new Object[]{source}));       }
    }
  }