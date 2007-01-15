package com.aavu.server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class IOUtils {

	private static final Logger log = Logger.getLogger(IOUtils.class);

	/**
	 * Returns the contents of the input stream as byte array.
	 * @param stream the <code>InputStream</code>
	 * @return the stream content as byte array
	 * @throws IOException 
	 */
	public static byte[] getStreamAsByteArray(InputStream stream) throws IOException
	{
		return getStreamAsByteArray(stream, -1);
	}

	/**
	 * Returns the contents of the input stream as byte array.
	 * @param stream the <code>InputStream</code>
	 * @param length the number of bytes to copy, if length < 0,
	 *        the number is unlimited
	 * @return the stream content as byte array
	 * @throws IOException 
	 */
	public static byte[] getStreamAsByteArray(InputStream stream, int length) throws IOException
	{
		if(length == 0) return new byte[0];
		boolean checkLength = true;
		if(length < 0)
		{
			length = Integer.MAX_VALUE;
			checkLength = false;
		}
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

		int nextValue = stream.read();
		if(checkLength) length--;
		while(-1 != nextValue && length >= 0)
		{
			byteStream.write(nextValue);
			nextValue = stream.read();
			if(checkLength) length--;
		}
		byteStream.flush();

		byte[] rtn = byteStream.toByteArray(); 
		
		try {
			stream.close();	
		} catch (Exception e) {
			log.info("Failed Closing.");
		}
		
		try {
			byteStream.close();	
		} catch (Exception e) {
			log.info("Failed Closing.");
		}
		
		return rtn;
	}
}
