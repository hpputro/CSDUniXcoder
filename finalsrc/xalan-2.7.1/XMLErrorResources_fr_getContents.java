  public Object[][] getContents()
  {
    return new Object[][] {

  
    {"ER0000" , "{0}" },

    { ER_FUNCTION_NOT_SUPPORTED,
      "Fonction non prise en charge !"},

    { ER_CANNOT_OVERWRITE_CAUSE,
      "Impossible de remplacer la cause"},

    { ER_NO_DEFAULT_IMPL,
      "Impossible de trouver une impl\u00e9mentation par d\u00e9faut "},

    { ER_CHUNKEDINTARRAY_NOT_SUPPORTED,
      "ChunkedIntArray({0}) n''est pas pris en charge"},

    { ER_OFFSET_BIGGER_THAN_SLOT,
      "D\u00e9calage plus important que l'emplacement"},

    { ER_COROUTINE_NOT_AVAIL,
      "Coroutine non disponible, id={0}"},

    { ER_COROUTINE_CO_EXIT,
      "CoroutineManager a re\u00e7u une demande de co_exit()"},

    { ER_COJOINROUTINESET_FAILED,
      "Echec de co_joinCoroutineSet()"},

    { ER_COROUTINE_PARAM,
      "Erreur de param\u00e8tre de Coroutine ({0})"},

    { ER_PARSER_DOTERMINATE_ANSWERS,
      "\nRESULTAT INATTENDU : L''analyseur doTerminate r\u00e9pond {0}"},

    { ER_NO_PARSE_CALL_WHILE_PARSING,
      "parse ne peut \u00eatre appel\u00e9 lors de l'analyse"},

    { ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
      "Erreur : it\u00e9rateur typ\u00e9 de l''axe {0} non impl\u00e9ment\u00e9"},

    { ER_ITERATOR_AXIS_NOT_IMPLEMENTED,
      "Erreur : it\u00e9rateur de l''axe {0} non impl\u00e9ment\u00e9 "},

    { ER_ITERATOR_CLONE_NOT_SUPPORTED,
      "Clone de l'it\u00e9rateur non pris en charge"},

    { ER_UNKNOWN_AXIS_TYPE,
      "Type transversal d''axe inconnu : {0}"},

    { ER_AXIS_NOT_SUPPORTED,
      "Traverseur d''axe non pris en charge : {0}"},

    { ER_NO_DTMIDS_AVAIL,
      "Aucun autre ID de DTM disponible"},

    { ER_NOT_SUPPORTED,
      "Non pris en charge : {0}"},

    { ER_NODE_NON_NULL,
      "Le noeud ne doit pas \u00eatre vide pour getDTMHandleFromNode"},

    { ER_COULD_NOT_RESOLVE_NODE,
      "Impossible de convertir le noeud en pointeur"},

    { ER_STARTPARSE_WHILE_PARSING,
       "startParse ne peut \u00eatre appel\u00e9 pendant l'analyse"},

    { ER_STARTPARSE_NEEDS_SAXPARSER,
       "startParse requiert un SAXParser non vide"},

    { ER_COULD_NOT_INIT_PARSER,
       "impossible d'initialiser l'analyseur"},

    { ER_EXCEPTION_CREATING_POOL,
       "exception durant la cr\u00e9ation d'une instance du pool"},

    { ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE,
       "Le chemin d'acc\u00e8s contient une s\u00e9quence d'\u00e9chappement non valide"},

    { ER_SCHEME_REQUIRED,
       "Processus requis !"},

    { ER_NO_SCHEME_IN_URI,
       "Processus introuvable dans l''URI : {0}"},

    { ER_NO_SCHEME_INURI,
       "Processus introuvable dans l'URI"},

    { ER_PATH_INVALID_CHAR,
       "Le chemin contient un caract\u00e8re non valide : {0}"},

    { ER_SCHEME_FROM_NULL_STRING,
       "Impossible de d\u00e9finir le processus \u00e0 partir de la cha\u00eene vide"},

    { ER_SCHEME_NOT_CONFORMANT,
       "Le processus n'est pas conforme."},

    { ER_HOST_ADDRESS_NOT_WELLFORMED,
       "L'h\u00f4te n'est pas une adresse bien form\u00e9e"},

    { ER_PORT_WHEN_HOST_NULL,
       "Le port ne peut \u00eatre d\u00e9fini quand l'h\u00f4te est vide"},

    { ER_INVALID_PORT,
       "Num\u00e9ro de port non valide"},

    { ER_FRAG_FOR_GENERIC_URI,
       "Le fragment ne peut \u00eatre d\u00e9fini que pour un URI g\u00e9n\u00e9rique"},

    { ER_FRAG_WHEN_PATH_NULL,
       "Le fragment ne peut \u00eatre d\u00e9fini quand le chemin d'acc\u00e8s est vide"},

    { ER_FRAG_INVALID_CHAR,
       "Le fragment contient un caract\u00e8re non valide"},

    { ER_PARSER_IN_USE,
      "L'analyseur est d\u00e9j\u00e0 utilis\u00e9"},

    { ER_CANNOT_CHANGE_WHILE_PARSING,
      "Impossible de modifier {0} {1} durant l''analyse"},

    { ER_SELF_CAUSATION_NOT_PERMITTED,
      "Auto-causalit\u00e9 interdite"},

    { ER_NO_USERINFO_IF_NO_HOST,
      "Userinfo ne peut \u00eatre sp\u00e9cifi\u00e9 si l'h\u00f4te ne l'est pas"},

    { ER_NO_PORT_IF_NO_HOST,
      "Le port peut ne pas \u00eatre sp\u00e9cifi\u00e9 si l'h\u00f4te n'est pas sp\u00e9cifi\u00e9"},

    { ER_NO_QUERY_STRING_IN_PATH,
      "La cha\u00eene de requ\u00eate ne doit pas figurer dans un chemin et une cha\u00eene de requ\u00eate"},

    { ER_NO_FRAGMENT_STRING_IN_PATH,
      "Le fragment ne doit pas \u00eatre indiqu\u00e9 \u00e0 la fois dans le chemin et dans le fragment"},

    { ER_CANNOT_INIT_URI_EMPTY_PARMS,
      "Impossible d'initialiser l'URI avec des param\u00e8tres vides"},

    { ER_METHOD_NOT_SUPPORTED,
      "Cette m\u00e9thode n'est pas encore prise en charge "},

    { ER_INCRSAXSRCFILTER_NOT_RESTARTABLE,
      "IncrementalSAXSource_Filter ne peut red\u00e9marrer"},

    { ER_XMLRDR_NOT_BEFORE_STARTPARSE,
      "XMLReader ne figure pas avant la demande startParse"},

    { ER_AXIS_TRAVERSER_NOT_SUPPORTED,
      "Traverseur d''axe non pris en charge : {0}"},

    { ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
      "ListingErrorHandler cr\u00e9\u00e9 avec PrintWriter vide !"},

    { ER_SYSTEMID_UNKNOWN,
      "ID syst\u00e8me inconnu"},

    { ER_LOCATION_UNKNOWN,
      "Emplacement inconnu de l'erreur"},

    { ER_PREFIX_MUST_RESOLVE,
      "Le pr\u00e9fixe doit se convertir en espace de noms : {0}"},

    { ER_CREATEDOCUMENT_NOT_SUPPORTED,
      "createDocument() non pris en charge dans XPathContext !"},

    { ER_CHILD_HAS_NO_OWNER_DOCUMENT,
      "L'enfant de l'attribut ne poss\u00e8de pas de document propri\u00e9taire !"},

    { ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
      "Le contexte ne poss\u00e8de pas d'\u00e9l\u00e9ment de document propri\u00e9taire !"},

    { ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
      "Avertissement : impossible d'afficher du texte avant l'\u00e9l\u00e9ment de document !  Traitement ignor\u00e9..."},

    { ER_CANT_HAVE_MORE_THAN_ONE_ROOT,
      "Un DOM ne peut poss\u00e9der plusieurs racines !"},

    { ER_ARG_LOCALNAME_NULL,
       "L'argument 'localName' est vide"},

                { ER_ARG_LOCALNAME_INVALID,
       "Dans QNAME, le nom local doit \u00eatre un nom NCName valide"},

                { ER_ARG_PREFIX_INVALID,
       "Dans QNAME, le pr\u00e9fixe doit \u00eatre un nom NCName valide"},

    { ER_NAME_CANT_START_WITH_COLON,
      "Un nom ne peut commencer par le signe deux-points"},

    { "BAD_CODE", "Le param\u00e8tre de createMessage se trouve hors limites"},
    { "FORMAT_FAILED", "Exception soulev\u00e9e lors de l'appel de messageFormat"},
    { "line", "Ligne #"},
    { "column","Colonne #"}


  };
  }