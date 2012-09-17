package org.jlab.elog;

import java.util.ResourceBundle;

/**
 *
 * @author ryans
 */
public class Comment extends LogItem {

    public static final String COMMENT_SCHEMA_URL;    
    
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("org.jlab.elog.elog");
        COMMENT_SCHEMA_URL = bundle.getString("COMMENT_SCHEMA_URL");        
    }
    
    public Comment() throws LogException {
        super("Comment");
    }
    
    @Override
    protected String getSchemaURL() {
        return COMMENT_SCHEMA_URL;
    }
    
}
