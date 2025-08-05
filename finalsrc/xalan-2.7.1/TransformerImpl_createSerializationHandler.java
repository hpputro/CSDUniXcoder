    public SerializationHandler createSerializationHandler(
            Result outputTarget, OutputProperties format)
              throws TransformerException
    {

      SerializationHandler xoh;

                  org.w3c.dom.Node outputNode = null;

      if (outputTarget instanceof DOMResult)
      {
        outputNode = ((DOMResult) outputTarget).getNode();
        org.w3c.dom.Node nextSibling = ((DOMResult)outputTarget).getNextSibling();

        org.w3c.dom.Document doc;
        short type;

        if (null != outputNode)
        {
          type = outputNode.getNodeType();
          doc = (org.w3c.dom.Node.DOCUMENT_NODE == type)
                ? (org.w3c.dom.Document) outputNode
                : outputNode.getOwnerDocument();
        }
        else
        {
          boolean isSecureProcessing = m_stylesheetRoot.isSecureProcessing();
          doc = org.apache.xml.utils.DOMHelper.createDocument(isSecureProcessing);
          outputNode = doc;
          type = outputNode.getNodeType();

          ((DOMResult) outputTarget).setNode(outputNode);
        }

        DOMBuilder handler =
          (org.w3c.dom.Node.DOCUMENT_FRAGMENT_NODE == type)
          ? new DOMBuilder(doc, (org.w3c.dom.DocumentFragment) outputNode)
          : new DOMBuilder(doc, outputNode);
        
        if (nextSibling != null)
          handler.setNextSibling(nextSibling);
        
          String encoding = format.getProperty(OutputKeys.ENCODING);          
          xoh = new ToXMLSAXHandler(handler, (LexicalHandler)handler, encoding);
      }
      else if (outputTarget instanceof SAXResult)
      {
        ContentHandler handler = ((SAXResult) outputTarget).getHandler();
        
        if (null == handler)
           throw new IllegalArgumentException(
             "handler can not be null for a SAXResult"); 
             
        LexicalHandler lexHandler;
        if (handler instanceof LexicalHandler)     
            lexHandler = (LexicalHandler)  handler;
        else
            lexHandler = null;
            
        String encoding = format.getProperty(OutputKeys.ENCODING); 
        String method = format.getProperty(OutputKeys.METHOD);

        ToXMLSAXHandler toXMLSAXHandler = new ToXMLSAXHandler(handler, lexHandler, encoding);
        toXMLSAXHandler.setShouldOutputNSAttr(false);
        xoh = toXMLSAXHandler;   


        String publicID = format.getProperty(OutputKeys.DOCTYPE_PUBLIC); 
        String systemID = format.getProperty(OutputKeys.DOCTYPE_SYSTEM); 
        if (systemID != null)
            xoh.setDoctypeSystem(systemID);
        if (publicID != null)
            xoh.setDoctypePublic(publicID);
        
        if (handler instanceof TransformerClient) {
            XalanTransformState state = new XalanTransformState();
            ((TransformerClient)handler).setTransformState(state);
            ((ToSAXHandler)xoh).setTransformState(state);
        }

 
      }

                  else if (outputTarget instanceof StreamResult)
      {
        StreamResult sresult = (StreamResult) outputTarget;

        try
        {
          SerializationHandler serializer =
            (SerializationHandler) SerializerFactory.getSerializer(format.getProperties());

          if (null != sresult.getWriter())
            serializer.setWriter(sresult.getWriter());
          else if (null != sresult.getOutputStream())
            serializer.setOutputStream(sresult.getOutputStream());
          else if (null != sresult.getSystemId())
          {
            String fileURL = sresult.getSystemId();

            if (fileURL.startsWith("file:            {
              if (fileURL.substring(8).indexOf(":") >0)
                fileURL = fileURL.substring(8);
              else
                fileURL = fileURL.substring(7);
            }
            else if (fileURL.startsWith("file:/"))
            {
                if (fileURL.substring(6).indexOf(":") >0)
                    fileURL = fileURL.substring(6);
                  else
                    fileURL = fileURL.substring(5);            	
            }

            m_outputStream = new java.io.FileOutputStream(fileURL);

            serializer.setOutputStream(m_outputStream);
            
            xoh = serializer;
          }
          else
            throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_NO_OUTPUT_SPECIFIED, null)); 
          
        
          xoh = serializer;  
        }
        catch (IOException ioe)
        {
          throw new TransformerException(ioe);
        }
      }
      else
      {
        throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_CANNOT_TRANSFORM_TO_RESULT_TYPE, new Object[]{outputTarget.getClass().getName()}));                                                                                     }
      
                  xoh.setTransformer(this);

      SourceLocator srcLocator = getStylesheet();
      xoh.setSourceLocator(srcLocator);
      
      
      return xoh;
    }