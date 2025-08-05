public String getPath() {
        String my_path=super.getPath();
        if(my_path != null && my_path.endsWith(File.separator)) {
            int index=my_path.lastIndexOf(File.separator);
            if(index != -1)
                my_path=my_path.substring(0, index);
        }
        return my_path;
    }