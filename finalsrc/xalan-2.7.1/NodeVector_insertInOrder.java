  public void insertInOrder(int value)
  {

    for (int i = 0; i < m_firstFree; i++)
    {
      if (value < m_map[i])
      {
        insertElementAt(value, i);

        return;
      }
    }

    addElement(value);
  }