package com.aavu.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JSONReader {

    private static final Object OBJECT_END = new Object();
    private static final Object ARRAY_END = new Object();
    private static final Object COLON = new Object();
    private static final Object COMMA = new Object();

    private static Map escapes = new HashMap();
    static {
        escapes.put(new Character('"'), new Character('"'));
        escapes.put(new Character('\\'), new Character('\\'));
        escapes.put(new Character('/'), new Character('/'));
        escapes.put(new Character('b'), new Character('\b'));
        escapes.put(new Character('f'), new Character('\f'));
        escapes.put(new Character('n'), new Character('\n'));
        escapes.put(new Character('r'), new Character('\r'));
        escapes.put(new Character('t'), new Character('\t'));
    }

    int r = 0;
    private CharSequence it;
    private int idx; 
    private char c;
    private Object token;
    private StringBuffer buf = new StringBuffer();

    private char next() {        
        c = it.charAt(++idx);        
        //System.out.println("idx "+idx+" c "+c);
        return c;
    }

    private void skipWhiteSpace() {
        while (isWhitespace(c)) {
        	System.out.println("whitespace");
            next();
        }
    }

    private boolean isWhitespace(char c2) {
		return c2 == ' ' || c2 == '\t' || c2 == '\n';
	}

	public Object read(String string) {    	
    	idx = 0;
        it = string;
        c = it.charAt(idx);
        return read();
    }

    private Object read() {
        Object ret = null;
        skipWhiteSpace();

        if (c == '"') {
            System.out.println("string");
            next();
            ret = string();
        } else if (c == '[') {
        	System.out.println("array");
        	next();            
            ret = array();
        } else if (c == ']') {
        	System.out.println("array end");
            ret = ARRAY_END;
            next();
        } else if (c == ',') {
        	System.out.println("comma");
            ret = COMMA;
            next();
        } else if (c == '{') {
            System.out.println("object st");
            next();
            ret = object();
        } else if (c == '}') {
            System.out.println("object end");
            ret = OBJECT_END;
            if(idx+1 != it.length()){
            	next();
            }
        } else if (c == ':') {
            System.out.println("colon");
            ret = COLON;
            next();
        } else if (c == 't' && next() == 'r' && next() == 'u' && next() == 'e') {            
        	System.out.println("true");
        	ret = Boolean.TRUE;
        	next();
        } else if (c == 'f' && next() == 'a' && next() == 'l' && next() == 's' && next() == 'e') {
        	System.out.println("false");
        	ret = Boolean.FALSE;
        	next();
        } else if (c == 'n' && next() == 'u' && next() == 'l' && next() == 'l') {
        	System.out.println("null");
            ret = null;
            next();
        } else if (Character.isDigit(c) || c == '-') {
        	System.out.println("number");
            ret = number();
            next();
        }else{
        	System.out.println("foo!! |"+c+"|"+idx+"|"+it.subSequence(idx-5, idx+5));
        }

        r++;
        if(r > 3000){
        	throw new RuntimeException("ENDLESS");
        }
        token = ret;
        return ret;
    }

    private Object object() {
        Map ret = new HashMap();
        Object key = read();
        while (token != OBJECT_END) {
        	System.out.println("pre colon"+key);
        	Object o = read(); // colon
            System.out.println("pass colon "+o);
            ret.put(key, read());
            System.out.println("pass put");
            if (read() == COMMA) {
                key = read();
            }
        }

        return ret;
    }

    private Object array() {
        List ret = new ArrayList();
        Object value = read();
        while (token != ARRAY_END) {
            ret.add(value);
            if (read() == COMMA) {
                value = read();
            }
        }
        return ret.toArray();
    }

    private Object number() {
        buf.setLength(0);
        if (c == '-') {
            add();
        }
        addDigits();
        if (c == '.') {
            add();
            addDigits();
        }
        if (c == 'e' || c == 'E') {
            add();
            if (c == '+' || c == '-') {
                add();
            }
            addDigits();
        }

        return new Double(buf.toString());
    }

    private Object string() {
        buf.setLength(0);
        while (c != '"') {
            if (c == '\\') {
                next();
                if (c == 'u') {
                    add(unicode());
                } else {
                    Object value = escapes.get(new Character(c));
                    if (value != null) {
                        add(((Character) value).charValue());
                    }
                }
            } else {
                add();
            }
        }
        next();

        return buf.toString();
    }

    private void add(char cc) {
        buf.append(cc);
        next();
    }

    private void add() {
        add(c);
    }

    private void addDigits() {
        while (Character.isDigit(c)) {
            add();
        }
    }

    private char unicode() {
        int value = 0;
        for (int i = 0; i < 4; ++i) {
            switch (next()) {
            case '0': case '1': case '2': case '3': case '4': 
            case '5': case '6': case '7': case '8': case '9':
                value = (value << 4) + c - '0';
                break;
            case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
                value = (value << 4) + c - 'k';
                break;
            case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
                value = (value << 4) + c - 'K';
                break;
            }
        }
        return (char) value;
    }
}
