  public Object[][] getContents()
  {
    return new Object[][] {

  
    {"ER0000" , "{0}" },

    { ER_FUNCTION_NOT_SUPPORTED,
      "\u51fd\u6570\u4e0d\u53d7\u652f\u6301\uff01"},

    { ER_CANNOT_OVERWRITE_CAUSE,
      "\u65e0\u6cd5\u8986\u76d6\u539f\u56e0"},

    { ER_NO_DEFAULT_IMPL,
      "\u627e\u4e0d\u5230\u7f3a\u7701\u5b9e\u73b0"},

    { ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
      "\u5f53\u524d\u4e0d\u652f\u6301 ChunkedIntArray({0})"},

    { ER_OFFSET_BIGGER_THAN_SLOT,
      "\u504f\u79fb\u5927\u4e8e\u69fd"},

    { ER_COROUTINE_NOT_AVAIL,
      "\u534f\u540c\u7a0b\u5e8f\u4e0d\u53ef\u7528\uff0cid={0}"},

    { ER_COROUTINE_CO_EXIT,
      "CoroutineManager \u63a5\u6536\u5230 co_exit() \u8bf7\u6c42"},

    { ER_COJOINROUTINESET_FAILED,
      "co_joinCoroutineSet() \u5931\u8d25"},

    { ER_COROUTINE_PARAM,
      "\u534f\u540c\u7a0b\u5e8f\u53c2\u6570\u9519\u8bef\uff08{0}\uff09"},

    { ER_PARSER_DOTERMINATE_ANSWERS,
      "\n\u610f\u5916\uff1a\u89e3\u6790\u5668 doTerminate \u5e94\u7b54 {0}"},

    { ER_NO_PARSE_CALL_WHILE_PARSING,
      "\u89e3\u6790\u65f6\u53ef\u80fd\u6ca1\u6709\u8c03\u7528 parse"},

    { ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
      "\u9519\u8bef\uff1a\u6ca1\u6709\u5b9e\u73b0\u4e3a\u8f74 {0} \u8f93\u5165\u7684\u8fed\u4ee3\u5668"},

    { ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
      "\u9519\u8bef\uff1a\u6ca1\u6709\u5b9e\u73b0\u8f74 {0} \u7684\u8fed\u4ee3\u5668"},

    { ER_ITERATOR_CLONE_NOT_SUPPORTED,
      "\u4e0d\u652f\u6301\u8fed\u4ee3\u5668\u514b\u9686"},

    { ER_UNKNOWN_AXIS_TYPE,
      "\u672a\u77e5\u7684\u8f74\u904d\u5386\u7c7b\u578b\uff1a{0}"},

    { ER_AXIS_NOT_SUPPORTED,
      "\u4e0d\u652f\u6301\u8f74\u904d\u5386\u7a0b\u5e8f\uff1a{0}"},

    { ER_NO_DTMIDS_AVAIL,
      "\u65e0\u66f4\u591a\u7684 DTM \u6807\u8bc6\u53ef\u7528"},

    { ER_NOT_SUPPORTED,
      "\u4e0d\u652f\u6301\uff1a{0}"},

    { ER_NODE_NON_NULL,
      "\u5bf9\u4e8e getDTMHandleFromNode\uff0c\u8282\u70b9\u5fc5\u987b\u662f\u975e\u7a7a\u7684"},

    { ER_COULD_NOT_RESOLVE_NODE,
      "\u65e0\u6cd5\u5c06\u8282\u70b9\u89e3\u6790\u5230\u53e5\u67c4"},

    { ER_STARTPARSE_WHILE_PARSING,
       "\u89e3\u6790\u65f6\u53ef\u80fd\u6ca1\u6709\u8c03\u7528 startParse"},

    { ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse \u9700\u8981\u975e\u7a7a\u7684 SAXParser"},

    { ER_COULD_NOT_INIT_PARSER,
       "\u65e0\u6cd5\u7528\u4ee5\u4e0b\u5de5\u5177\u521d\u59cb\u5316\u89e3\u6790\u5668"},

    { ER_EXCEPTION_CREATING_POOL,
       "\u4e3a\u6c60\u521b\u5efa\u65b0\u5b9e\u4f8b\u65f6\u53d1\u751f\u5f02\u5e38"},

    { ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "\u8def\u5f84\u5305\u542b\u65e0\u6548\u7684\u8f6c\u4e49\u5e8f\u5217"},

    { ER_SCHEME_REQUIRED,
       "\u6a21\u5f0f\u662f\u5fc5\u9700\u7684\uff01"},

    { ER_NO_SCHEME_IN_URI,
       "URI {0} \u4e2d\u627e\u4e0d\u5230\u4efb\u4f55\u6a21\u5f0f"},

    { ER_NO_SCHEME_INURI,
       "URI \u4e2d\u627e\u4e0d\u5230\u4efb\u4f55\u6a21\u5f0f"},

    { ER_PATH_INVALID_CHAR,
       "\u8def\u5f84\u5305\u542b\u65e0\u6548\u7684\u5b57\u7b26\uff1a{0}"},

    { ER_SCHEME_FROM_NULL_STRING,
       "\u65e0\u6cd5\u4ece\u7a7a\u5b57\u7b26\u4e32\u8bbe\u7f6e\u6a21\u5f0f"},

    { ER_SCHEME_NOT_CONFORMANT,
       "\u6a21\u5f0f\u4e0d\u4e00\u81f4\u3002"},

    { ER_HOST_ADDRESS_NOT_WELLFORMED,
       "\u4e3b\u673a\u4e0d\u662f\u683c\u5f0f\u6b63\u786e\u7684\u5730\u5740"},

    { ER_PORT_WHEN_HOST_NULL,
       "\u4e3b\u673a\u4e3a\u7a7a\u65f6\uff0c\u65e0\u6cd5\u8bbe\u7f6e\u7aef\u53e3"},

    { ER_INVALID_PORT,
       "\u7aef\u53e3\u53f7\u65e0\u6548"},

    { ER_FRAG_FOR_GENERIC_URI,
       "\u53ea\u80fd\u4e3a\u7c7b\u5c5e URI \u8bbe\u7f6e\u7247\u6bb5"},

    { ER_FRAG_WHEN_PATH_NULL,
       "\u8def\u5f84\u4e3a\u7a7a\u65f6\uff0c\u65e0\u6cd5\u8bbe\u7f6e\u7247\u6bb5"},

    { ER_FRAG_INVALID_CHAR,
       "\u7247\u6bb5\u5305\u542b\u65e0\u6548\u7684\u5b57\u7b26"},

    { ER_PARSER_IN_USE,
      "\u89e3\u6790\u5668\u5df2\u5728\u4f7f\u7528"},

    { ER_CANNOT_CHANGE_WHILE_PARSING,
      "\u89e3\u6790\u65f6\u65e0\u6cd5\u66f4\u6539 {0} {1}"},

    { ER_SELF_CAUSATION_NOT_PERMITTED,
      "\u4e0d\u5141\u8bb8\u81ea\u89e6\u53d1"},

    { ER_NO_USERINFO_IF_NO_HOST,
      "\u5982\u679c\u6ca1\u6709\u6307\u5b9a\u4e3b\u673a\uff0c\u5219\u4e0d\u53ef\u4ee5\u6307\u5b9a\u7528\u6237\u4fe1\u606f"},

    { ER_NO_PORT_IF_NO_HOST,
      "\u5982\u679c\u6ca1\u6709\u6307\u5b9a\u4e3b\u673a\uff0c\u5219\u4e0d\u53ef\u4ee5\u6307\u5b9a\u7aef\u53e3"},

    { ER_NO_QUERY_STRING_IN_PATH,
      "\u8def\u5f84\u548c\u67e5\u8be2\u5b57\u7b26\u4e32\u4e2d\u4e0d\u80fd\u6307\u5b9a\u67e5\u8be2\u5b57\u7b26\u4e32"},

    { ER_NO_FRAGMENT_STRING_IN_PATH,
      "\u8def\u5f84\u548c\u7247\u6bb5\u4e2d\u90fd\u4e0d\u80fd\u6307\u5b9a\u7247\u6bb5"},

    { ER_CANNOT_INIT_URI_EMPTY_PARMS,
      "\u4e0d\u80fd\u4ee5\u7a7a\u53c2\u6570\u521d\u59cb\u5316 URI"},

    { ER_METHOD_NOT_SUPPORTED,
      "\u5c1a\u4e0d\u652f\u6301\u65b9\u6cd5"},

    { ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
      "\u5f53\u524d\u4e0d\u53ef\u91cd\u65b0\u542f\u52a8 IncrementalSAXSource_Filter"},

    { ER_XMLRDR_NOT_BEFORE_STARTPARSE,
      "XMLReader \u4e0d\u5728 startParse \u8bf7\u6c42\u4e4b\u524d"},

    { ER_AXIS_TRAVERSER_NOT_SUPPORTED,
      "\u4e0d\u652f\u6301\u8f74\u904d\u5386\u7a0b\u5e8f\uff1a{0}"},

    { ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
      "\u4ee5\u7a7a\u7684 PrintWriter \u521b\u5efa\u4e86 ListingErrorHandler\uff01"},

    { ER_SYSTEMID_UNKNOWN,
      "SystemId \u672a\u77e5"},

    { ER_LOCATION_UNKNOWN,
      "\u9519\u8bef\u4f4d\u7f6e\u672a\u77e5"},

    { ER_PREFIX_MUST_RESOLVE,
      "\u524d\u7f00\u5fc5\u987b\u89e3\u6790\u4e3a\u540d\u79f0\u7a7a\u95f4\uff1a{0}"},

    { ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "XPathContext \u4e2d\u4e0d\u652f\u6301 createDocument()\uff01"},

    { ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "\u5b50\u5c5e\u6027\u6ca1\u6709\u6240\u6709\u8005\u6587\u6863\uff01"},

    { ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "\u5b50\u5c5e\u6027\u6ca1\u6709\u6240\u6709\u8005\u6587\u6863\u5143\u7d20\uff01"},

    { ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "\u8b66\u544a\uff1a\u65e0\u6cd5\u5728\u8bb0\u5f55\u5143\u7d20\u524d\u8f93\u51fa\u6587\u672c\uff01\u5ffd\u7565..."},

    { ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "DOM \u4e0a\u4e0d\u80fd\u6709\u591a\u4e2a\u6839\uff01"},

    { ER_ARG_LOCALNAME_NULL,
       "\u81ea\u53d8\u91cf\u201clocalName\u201d\u4e3a\u7a7a"},

                { ER_ARG_LOCALNAME_INVALID,
       "QNAME \u4e2d\u7684\u672c\u5730\u540d\u5e94\u5f53\u662f\u6709\u6548\u7684 NCName"},

                { ER_ARG_PREFIX_INVALID,
       "QNAME \u4e2d\u7684\u524d\u7f00\u5e94\u5f53\u662f\u6709\u6548\u7684 NCName"},

    { ER_NAME_CANT_START_WITH_COLON,
      "\u540d\u79f0\u4e0d\u80fd\u4ee5\u5192\u53f7\u5f00\u5934"},

    { "BAD_CODE", "createMessage \u7684\u53c2\u6570\u8d85\u51fa\u8303\u56f4"},
    { "FORMAT_FAILED", "\u5728 messageFormat \u8c03\u7528\u8fc7\u7a0b\u4e2d\u629b\u51fa\u4e86\u5f02\u5e38"},
    { "line", "\u884c\u53f7"},
    { "column","\u5217\u53f7"}


  };
  }