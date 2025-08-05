	protected INameEnvironment getNameEnvironment(final JRCompilationUnit[] units)
	{
		final INameEnvironment env = new INameEnvironment() 
		{
			public NameEnvironmentAnswer findType(char[][] compoundTypeName) 
			{
				StringBuffer result = new StringBuffer();
				String sep = "";
				for (int i = 0; i < compoundTypeName.length; i++) {
					result.append(sep);
					result.append(compoundTypeName[i]);
					sep = ".";
				}
				return findType(result.toString());
			}

			public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) 
			{
				StringBuffer result = new StringBuffer();
				String sep = "";
				for (int i = 0; i <  i++) {
					result.append(sep);
					result.append(
					sep = ".";
				}
				result.append(sep);
				result.append(typeName);
				return findType(result.toString());
			}

			private int getClassIndex(String className)
			{
				int classIdx;
				for (classIdx = 0; classIdx < units.length; ++classIdx)
				{
					if (className.equals(units[classIdx].getName()))
					{
						break;
					}
				}
				
				if (classIdx >= units.length)
				{
					classIdx = -1;
				}

				return classIdx;
			}
			
			private NameEnvironmentAnswer findType(String className) 
			{
				try 
				{
					int classIdx = getClassIndex(className);
					
					if (classIdx >= 0)
					{
						ICompilationUnit compilationUnit = 
							new CompilationUnit(
								units[classIdx].getSourceCode(), className);
						if (is2ArgsConstr)
						{
							return (NameEnvironmentAnswer) constrNameEnvAnsCompUnit2Args.newInstance(new Object[] { compilationUnit, null });
						}

						return (NameEnvironmentAnswer) constrNameEnvAnsCompUnit.newInstance(new Object[] { compilationUnit });
					}
					
					String resourceName = className.replace('.', '/') + ".class";
					InputStream is = getResource(resourceName);
					if (is != null) 
					{
						try
						{
							byte[] classBytes = JRLoader.loadBytes(is);
							char[] fileName = className.toCharArray();
							ClassFileReader classFileReader = 
								new ClassFileReader(classBytes, fileName, true);
							
							if (is2ArgsConstr)
							{
								return (NameEnvironmentAnswer) constrNameEnvAnsBin2Args.newInstance(new Object[] { classFileReader, null });
							}

							return (NameEnvironmentAnswer) constrNameEnvAnsBin.newInstance(new Object[] { classFileReader });
						}
						finally
						{
							try
							{
								is.close();
							}
							catch (IOException e)
							{
															}
						}
					}
				}
				catch (JRException e)
				{
					log.error("Compilation error", e);
				}
				catch (org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException exc) 
				{
					log.error("Compilation error", exc);
				}
				catch (InvocationTargetException e)
				{
					throw new JRRuntimeException("Not able to create NameEnvironmentAnswer", e);
				}
				catch (IllegalArgumentException e)
				{
					throw new JRRuntimeException("Not able to create NameEnvironmentAnswer", e);
				}
				catch (InstantiationException e)
				{
					throw new JRRuntimeException("Not able to create NameEnvironmentAnswer", e);
				}
				catch (IllegalAccessException e)
				{
					throw new JRRuntimeException("Not able to create NameEnvironmentAnswer", e);
				}
				return null;
			}

			private boolean isPackage(String result) 
			{
				int classIdx = getClassIndex(result);
				if (classIdx >= 0) 
				{
					return false;
				}
				
				String resourceName = result.replace('.', '/') + ".class";

				boolean isPackage = true;

				InputStream is = getResource(resourceName);
				
				if (is != null)				{              					try        					{
						isPackage = (is.read() > 0);
					}
					catch(IOException e)
					{
											}
					finally
					{
						try
						{
							is.close();
						}
						catch(IOException e)
						{
													}
					}
				}
				
				return isPackage;
			}

			public boolean isPackage(char[][] parentPackageName, char[] packageName) 
			{
				StringBuffer result = new StringBuffer();
				String sep = "";
				if (parentPackageName != null) 
				{
					for (int i = 0; i < parentPackageName.length; i++) 
					{
						result.append(sep);
						result.append(parentPackageName[i]);
						sep = ".";
					}
				}
				if (Character.isUpperCase(packageName[0])) 
				{
					if (!isPackage(result.toString())) 
					{
						return false;
					}
				}
				result.append(sep);
				result.append(
				return isPackage(result.toString());
			}

			public void cleanup() 
			{
			}

		};

		return env;
	}