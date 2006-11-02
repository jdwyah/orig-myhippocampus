package com.aavu.server.service.facebook.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//
// +---------------------------------------------------------------------------+
// | Facebook Development Platform (beta) Java client                          |   
// +---------------------------------------------------------------------------+
// | Copyright (c) 2006 Facebook, Inc.                                         | 
// | All rights reserved.                                                      |
// |                                                                           |
// | Redistribution and use in source and binary forms, with or without        |
// | modification, are permitted provided that the following conditions        |
// | are met:                                                                  |
// |                                                                           |
// | 1. Redistributions of source code must retain the above copyright         |
// |    notice, this list of conditions and the following disclaimer.          |
// | 2. Redistributions in binary form must reproduce the above copyright      |
// |    notice, this list of conditions and the following disclaimer in the    |
// |    documentation and/or other materials provided with the distribution.   |
// |                                                                           |
// | THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR      |
// | IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES |
// | OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.   |
// | IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,          |
// | INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT  |
// | NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, |
// | DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY     |
// | THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT       |
// | (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF  |
// | THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.         |
// +---------------------------------------------------------------------------+
// | For help with this library, contact api-help@facebook.com                 |
// +---------------------------------------------------------------------------+
//

/**
 * Interacts with the Facebook API. This class returns raw XML results, which
 * should be parsed according to the XML specification on
 * <a href="http://developers.facebook.com/">the Facebook developers' site</a>.
 * <p>
 * This uses the JDK 1.4 Logger class to log errors and debugging messages.
 * To see verbose output (incoming XML and outgoing requests), use a log
 * level of FINE or higher.
 */
public class FacebookRestClient {
	  private Logger  _log = Logger.getLogger(getClass().getName());
	  private String  _secret;
	  private String  _sessionKey;
	  private String  _apiKey;
	  private URL     _serverUrl;
	  private boolean _debug;

	  public FacebookRestClient(String serverAddr, String sessionKey, String apiKey, String secret) throws MalformedURLException {
	    _sessionKey = sessionKey;
	    _apiKey     = apiKey;
	    _secret     = secret;
	    _serverUrl  = new URL(serverAddr);

	    /*
	     * Figure out the current logging level so we can skip some computation if
	     * the admin doesn't have debug logging enabled.
	     */
	    Level logLevel = figureLogLevel(_log);
	    _debug = logLevel.intValue() <= Level.FINE.intValue();
	  }

	  public static Level figureLogLevel(Logger logger) {
	    Level logLevel = logger.getLevel();
	    if (logLevel == null) {
	      // Java logging API doesn't have a "get the current log level no matter
	      // where it's configured" call, so walk up the log tree.
	      for (Logger l = logger; l != null; l = l.getParent()) {
	        logLevel = l.getLevel();
	        if (logLevel != null) {
	          break;
	        }
	      }
	      if (logLevel == null) {
	        logLevel = Level.INFO;
	      }
	    }
	    return logLevel;
	  }

	  /**
	   * Call the specified method, with the given parameters, and return a DOM tree with the results.
	   *
	   * @param method the name of the method
	   * @param params a list of arguments to the method specified as "name=value"
	   * @throws Exception with a description of any errors given to us by the server.
	   */
	  public Document callMethod(String method, List<String> params) throws FacebookException, IOException {
	    params.add("method=" + method);
	    params.add("session_key=" + _sessionKey);
	    params.add("api_key=" + _apiKey);
	    params.add("call_id=" + System.currentTimeMillis());
	    params.add("sig=" + generateSig(params));

	    try {
	      StringBuffer postData = new StringBuffer();
	      for (String param : params) {
	        if (postData.length() > 0) postData.append("&");
	        postData.append(param);
	      }

	      _log.fine("Sending request to " + _serverUrl + " : " +
	              postData.toString());

	      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	      InputStream is = postRequest(method, postData.toString());

	      /*
	       * We want to be able to log the response for debugging or in case
	       * of an error, so buffer it temporarily.
	       * */
	      BufferedInputStream bis = new BufferedInputStream(is);
	      bis.mark(100000);

	      if (_debug) {
	          BufferedReader br = new BufferedReader(new InputStreamReader(bis));
	          String s;
	          StringBuffer sb = new StringBuffer("Response: ");
	          while ((s = br.readLine()) != null)
	              sb.append(s).append("\n");
	          _log.fine(sb.toString());
	          bis.reset();
	      }

	      Document doc = builder.parse(bis);
	      doc.normalizeDocument();
	      stripEmptyTextNodes(doc);

	      NodeList errors = doc.getElementsByTagName("fb_error");
	      if (errors.getLength() > 0) {
	        int errorCode = Integer.parseInt(errors.item(0).getFirstChild()
	                            .getFirstChild().getTextContent());
	        String errorMessage = errors.item(0).getLastChild().getLastChild()
	                            .getTextContent();
	        _log.info("Got error " + errorCode + " (" + errorMessage +
	                ") from Facebook API server at " + _serverUrl +
	                " for request " + postData.toString());
	        
	        throw new FacebookException(errorCode, errorMessage);
	      }
	      return doc;
	    } catch (javax.xml.parsers.ParserConfigurationException ex) {
	      _log.log(Level.SEVERE, "No XML parser available!", ex);
	    } catch (org.xml.sax.SAXException ex) {
	      throw new IOException("error parsing xml");
	    }
	    return null;
	  }

	  /**
	   * Since DOM reads newlines as textnodes we want to strip out those
	   * nodes to make it easier to use the tree.
	   */
	  private static void stripEmptyTextNodes(Node n) {
	    NodeList children = n.getChildNodes();
	    int length = children.getLength();
	    for (int i=0; i < length; i++) {
	      Node c = children.item(i);
	      if (!c.hasChildNodes() && c.getNodeType() == Node.TEXT_NODE && c.getTextContent().trim().length() == 0) {
	        n.removeChild(c);
	        i--;
	        length--;
	        children = n.getChildNodes();
	      } else {
	        stripEmptyTextNodes(c);
	      }
	    }
	  }

	  private String generateSig(List<String> params) {
	    StringBuffer buffer = new StringBuffer();
	    Collections.sort(params);
	    for (String param : params) {
	      buffer.append(param);
	    }
	    buffer.append(_secret);
	    try {
	      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	      StringBuffer result = new StringBuffer();
	      for (byte b : md.digest(buffer.toString().getBytes())) { 
	        result.append(Integer.toHexString((b & 0xf0) >>> 4));
	        result.append(Integer.toHexString(b & 0x0f));
	      }
	      return result.toString();
	    } catch (java.security.NoSuchAlgorithmException ex) {
	      _log.log(Level.SEVERE, "No MD5 digest available!", ex);
	      return "";
	    }
	  }

	  private InputStream postRequest(String method, String params) throws IOException {
	    HttpURLConnection conn = (HttpURLConnection)_serverUrl.openConnection();
	    try {
	      conn.setRequestMethod("POST");
	    } catch (ProtocolException ex) {
	      _log.log(Level.SEVERE, "Java doesn't recognize POST request type!", ex);
	    }
	    conn.setDoOutput(true);
	    conn.connect();
	    conn.getOutputStream().write(params.getBytes());
	 
	    return conn.getInputStream();
	  }
	}

