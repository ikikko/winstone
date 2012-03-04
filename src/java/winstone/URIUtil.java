package winstone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class URIUtil {
    /**
     * Eliminates "." and ".." in the path.
     * So that this method can be used for any string that looks like an URI,
     * this method preserves the leading and trailing '/'.
     */
    static String canonicalPath(String path) {
        List r = new ArrayList(Arrays.asList(path.split("[/\\\\]+")));
        for (int i=0; i<r.size(); ) {
            String cur = (String)r.get(i);
            if (cur.length()==0 || cur.equals(".")) {
                // empty token occurs for example, "".split("/+") is [""]
                r.remove(i);
            } else
            if (cur.equals("..")) {
                // i==0 means this is a broken URI.
                r.remove(i);
                if (i>0) {
                    r.remove(i-1);
                    i--;
                }
            } else {
                i++;
            }
        }

        StringBuilder buf = new StringBuilder();
        if (path.startsWith("/"))
            buf.append('/');
        boolean first = true;
        for (Object aR : r) {
            String token = (String) aR;
            if (!first) buf.append('/');
            else first = false;
            buf.append(token);
        }
        // translation: if (path.endsWith("/") && !buf.endsWith("/"))
        if (path.endsWith("/") && (buf.length()==0 || buf.charAt(buf.length()-1)!='/'))
            buf.append('/');
        return buf.toString();
    }

    /**
     * Performs necessary escaping to render arbitrary plain text as plain text without any markup.
     */
    public static String htmlEscape(String text) {
        StringBuilder buf = new StringBuilder(text.length()+64);
        for( int i=0; i<text.length(); i++ ) {
            char ch = text.charAt(i);
            if(ch=='<')
                buf.append("&lt;");
            else
            if(ch=='&')
                buf.append("&amp;");
            else
                buf.append(ch);
        }
        return buf.toString();
    }
}
