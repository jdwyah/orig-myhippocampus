package com.aavu.server.s3;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.aavu.server.s3.com.amazon.s3.AWSAuthConnection;
import com.aavu.server.s3.com.amazon.s3.GetResponse;
import com.aavu.server.s3.com.amazon.s3.ListAllMyBucketsResponse;
import com.aavu.server.s3.com.amazon.s3.ListBucketResponse;
import com.aavu.server.s3.com.amazon.s3.ListEntry;
import com.aavu.server.s3.com.amazon.s3.QueryStringAuthGenerator;
import com.aavu.server.s3.com.amazon.s3.Response;
import com.aavu.server.s3.com.amazon.s3.S3Object;

public class S3Test extends TestCase {
    private static final String awsAccessKeyId = "16HF7GQG686H29K1Y702";
    private static final String awsSecretAccessKey = "SS+xlCahxAnpYUIRaci9pssyuN/vjc7iT3pBePde";

    static final String bucketName = awsAccessKeyId + "-test-bucket";
    static int assertionCount = 0;

    static final int UnspecifiedMaxKeys = -1;

    public void testHippoStuff() throws MalformedURLException, IOException{
        String BUCKET = "myhippocampus";
        AWSAuthConnection conn = new AWSAuthConnection(awsAccessKeyId, awsSecretAccessKey);
        														  
        ListBucketResponse listBucketResponse1 = conn.listBucket(BUCKET, null, null, null, null);
        System.out.println("list "+listBucketResponse1.entries.size());
        for (Iterator iter = listBucketResponse1.entries.iterator(); iter.hasNext();) {
        	ListEntry element = (ListEntry) iter.next();
        	System.out.println("element "+element.key);
        }
        System.out.println("response: "+listBucketResponse1.connection.getResponseCode());
        
        listBucketResponse1 = conn.listBucket(BUCKET, "test/218/",null,null,null);
        System.out.println("list "+listBucketResponse1.entries.size());
        for (Iterator iter = listBucketResponse1.entries.iterator(); iter.hasNext();) {
        	ListEntry element = (ListEntry) iter.next();
        	System.out.println("element "+element.key);
        	System.out.println("ETAG "+element.eTag);
        	System.out.println("Storage "+element.storageClass);
        	
        	GetResponse res = conn.get(BUCKET, element.key,null);
        	Map meta = res.object.metadata;
        	for (Iterator iterator = meta.keySet().iterator(); iterator.hasNext();) {
				String mkey = (String) iterator.next();
				System.out.println("KEY "+mkey);
				List values = (List) meta.get(mkey);
				for (Iterator iterator2 = values.iterator(); iterator2.hasNext();) {
					String val = (String) iterator2.next();
					System.out.println("val: "+val);
				}
				
			}
        	System.out.println("Obj: "+res.object);
        }
        
    }
    public void testDriver() throws MalformedURLException, IOException{
        if (awsAccessKeyId.startsWith("<INSERT")) {
            System.err.println("Please examine S3Test.java and update it with your credentials");
            System.exit(-1);
        }


        AWSAuthConnection conn = new AWSAuthConnection(awsAccessKeyId, awsSecretAccessKey);
      
        
        Response response = conn.createBucket(bucketName, null);
        assertEquals(
                "couldn't create bucket",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        ListBucketResponse listBucketResponse = conn.listBucket(bucketName, null, null, null, null);
        assertEquals(
                "couldn't get list",
                HttpURLConnection.HTTP_OK,
                listBucketResponse.connection.getResponseCode());
        assertEquals("list wasn't empty", 0, listBucketResponse.entries.size());
        verifyBucketResponseParameters(listBucketResponse, bucketName, "", "", UnspecifiedMaxKeys, null, false, null);

        // start delimiter tests

        final String text = "this is a test";
        final String key = "example.txt";
        final String innerKey = "test/inner.txt";
        final String lastKey = "z-last-key.txt";

        response = conn.put(bucketName, key, new S3Object(text.getBytes(), null), null);
        assertEquals(
                "couldn't put simple object",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        response = conn.put(bucketName, innerKey, new S3Object(text.getBytes(), null), null);
        assertEquals(
                "couldn't put simple object",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        response = conn.put(bucketName, lastKey, new S3Object(text.getBytes(), null), null);
        assertEquals(
                "couldn't put simple object",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        // plain list
        listBucketResponse = conn.listBucket(bucketName, null, null, null, null);
        assertEquals(
                "couldn't get list",
                listBucketResponse.connection.getResponseCode(),
                HttpURLConnection.HTTP_OK);
        assertEquals("Unexpected list size", 3, listBucketResponse.entries.size());
        assertEquals("Unexpected common prefix size", 0, listBucketResponse.commonPrefixEntries.size());
        verifyBucketResponseParameters(listBucketResponse, bucketName, "", "", UnspecifiedMaxKeys, null, false, null);

        // root "directory"
        listBucketResponse = conn.listBucket(bucketName, null, null, null, "/", null);
        assertEquals(
                "couldn't get list",
                HttpURLConnection.HTTP_OK,
                listBucketResponse.connection.getResponseCode());
        assertEquals("Unexpected list size", 2, listBucketResponse.entries.size());
        assertEquals("Unexpected common prefix size", 1, listBucketResponse.commonPrefixEntries.size());
        verifyBucketResponseParameters(listBucketResponse, bucketName, "", "", UnspecifiedMaxKeys, "/", false, null);

        // root "directory" with a max-keys of "1"
        listBucketResponse = conn.listBucket(bucketName, null, null, new Integer( 1 ), "/", null);
        assertEquals(
                "couldn't get list",
                HttpURLConnection.HTTP_OK,
                listBucketResponse.connection.getResponseCode());
        assertEquals("Unexpected list size", 1, listBucketResponse.entries.size());
        assertEquals("Unexpected common prefix size", 0, listBucketResponse.commonPrefixEntries.size());
        verifyBucketResponseParameters(listBucketResponse, bucketName, "", "", 1, "/", true, "example.txt");

        // root "directory" with a max-keys of "2"
        listBucketResponse = conn.listBucket(bucketName, null, null, new Integer( 2 ), "/", null);
        assertEquals(
                "couldn't get list",
                HttpURLConnection.HTTP_OK,
                listBucketResponse.connection.getResponseCode());
        assertEquals("Unexpected list size", 1, listBucketResponse.entries.size());
        assertEquals("Unexpected common prefix size", 1, listBucketResponse.commonPrefixEntries.size());
        verifyBucketResponseParameters(listBucketResponse, bucketName, "", "", 2, "/", true, "test/");
        String marker = listBucketResponse.nextMarker;
        listBucketResponse = conn.listBucket(bucketName, null, marker, new Integer( 2 ), "/", null);
        assertEquals(
                "couldn't get list",
                HttpURLConnection.HTTP_OK,
                listBucketResponse.connection.getResponseCode());
        assertEquals("Unexpected list size", 1, listBucketResponse.entries.size());
        assertEquals("Unexpected common prefix size", 0, listBucketResponse.commonPrefixEntries.size());
        verifyBucketResponseParameters(listBucketResponse, bucketName, "", marker, 2, "/", false, null);

        // test "directory"
        listBucketResponse = conn.listBucket(bucketName, "test/", null, null, "/", null);
        assertEquals(
                "couldn't get list",
                HttpURLConnection.HTTP_OK,
                listBucketResponse.connection.getResponseCode());
        assertEquals("Unexpected list size", 1, listBucketResponse.entries.size());
        assertEquals("Unexpected common prefix size", 0, listBucketResponse.commonPrefixEntries.size());
        verifyBucketResponseParameters(listBucketResponse, bucketName, "test/", "", UnspecifiedMaxKeys, "/", false, null);

        // remove innerkey
        response = conn.delete(bucketName, innerKey, null);
        assertEquals(
                "couldn't delete entry",
                HttpURLConnection.HTTP_NO_CONTENT,
                response.connection.getResponseCode());

        // remove last key
        response = conn.delete(bucketName, lastKey, null);
        assertEquals(
                "couldn't delete entry",
                HttpURLConnection.HTTP_NO_CONTENT,
                response.connection.getResponseCode());


        // end delimiter tests

        response = conn.put(bucketName, key, new S3Object(text.getBytes(), null), null);
        assertEquals(
                "couldn't reput simple object",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        Map metadata = new HashMap();
        metadata.put("title", Arrays.asList(new String[] { "title" }));
        response = conn.put(bucketName, key, new S3Object(text.getBytes(), metadata), null);
        assertEquals(
                "couldn't put complex object",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        GetResponse getResponse = conn.get(bucketName, key, null);
        assertEquals(
                "couldn't get object",
                getResponse.connection.getResponseCode(),
                HttpURLConnection.HTTP_OK);
        assertEquals("didn't get the right data back", text.getBytes(), getResponse.object.data);
        assertEquals("didn't get the right metadata back", 1, getResponse.object.metadata.size());
        assertEquals(
                "didn't get the right metadata back",
                1,
                ((List)getResponse.object.metadata.get("title")).size());
        assertEquals(
                "didn't get the right metadata back",
                "title",
                ((List)getResponse.object.metadata.get("title")).get(0));
        assertEquals(
                "didn't get the right content-length",
                ""+text.length(),
                getResponse.connection.getHeaderField("Content-Length"));


        Map metadataWithSpaces = new HashMap();
        String titleWithSpaces = " \t  title with leading and trailing spaces    ";
        metadataWithSpaces.put("title", Arrays.asList(new String[] { titleWithSpaces }));
        response = conn.put(bucketName, key, new S3Object(text.getBytes(), metadataWithSpaces), null);
        assertEquals(
                "couldn't put metadata with leading and trailing spaces",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        getResponse = conn.get(bucketName, key, null);
        assertEquals(
                "couldn't get object",
                HttpURLConnection.HTTP_OK,
                getResponse.connection.getResponseCode());
        assertEquals("didn't get the right metadata back", getResponse.object.metadata.size(), 1);
        assertEquals(
                "didn't get the right metadata back",
                1,
                ((List)getResponse.object.metadata.get("title")).size());
        assertEquals(
                "didn't get the right metadata back",
                titleWithSpaces.trim(),
                ((List)getResponse.object.metadata.get("title")).get(0));

        String weirdKey = "&=//%# ++++";
        response = conn.put(bucketName, weirdKey, new S3Object(text.getBytes(), null), null);
        assertEquals(
                "couldn't put weird key",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        getResponse = conn.get(bucketName, weirdKey, null);
        assertEquals(
                "couldn't get weird key",
                HttpURLConnection.HTTP_OK,
                getResponse.connection.getResponseCode());

        // start acl test

        getResponse = conn.getACL(bucketName, key, null);
        assertEquals(
                "couldn't get acl",
                HttpURLConnection.HTTP_OK,
                getResponse.connection.getResponseCode());

        byte[] acl = getResponse.object.data;

        response = conn.putACL(bucketName, key, new String(acl), null);
        assertEquals(
                "couldn't put acl",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        getResponse = conn.getBucketACL(bucketName, null);
        assertEquals(
                "couldn't get bucket acl",
                HttpURLConnection.HTTP_OK,
                getResponse.connection.getResponseCode());

        byte[] bucketACL = getResponse.object.data;

        response = conn.putBucketACL(bucketName, new String(bucketACL), null);
        assertEquals(
                "couldn't put bucket acl",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        // end acl test

        // bucket logging tests
        getResponse = conn.getBucketLogging(bucketName, null);
        assertEquals(
                "couldn't get bucket logging config",
                HttpURLConnection.HTTP_OK, 
                getResponse.connection.getResponseCode());

        byte[] bucketLogging = getResponse.object.data;

        response = conn.putBucketLogging(bucketName, new String(bucketLogging), null);
        assertEquals(
                "couldn't put bucket logging config",
                HttpURLConnection.HTTP_OK,
                response.connection.getResponseCode());

        // end bucket logging tests

        listBucketResponse = conn.listBucket(bucketName, null, null, null, null);
        assertEquals(
                "couldn't list bucket",
                HttpURLConnection.HTTP_OK,
                listBucketResponse.connection.getResponseCode());
        List entries = listBucketResponse.entries;
        assertEquals("didn't get back the right number of entries", 2, entries.size());
        // depends on weirdKey < $key
        assertEquals("first key isn't right", weirdKey, ((ListEntry)entries.get(0)).key);
        assertEquals("second key isn't right", key, ((ListEntry)entries.get(1)).key);
        verifyBucketResponseParameters(listBucketResponse, bucketName, "", "", UnspecifiedMaxKeys, null, false, null);

        listBucketResponse = conn.listBucket(bucketName, null, null, new Integer(1), null);
        assertEquals(
                "couldn't list bucket",
                HttpURLConnection.HTTP_OK,
                listBucketResponse.connection.getResponseCode());
        assertEquals(
                "didn't get back the right number of entries",
                1,
                listBucketResponse.entries.size());
        verifyBucketResponseParameters(listBucketResponse, bucketName, "", "", 1, null, true, null);

        for (Iterator it = entries.iterator(); it.hasNext(); ) {
            ListEntry entry = (ListEntry)it.next();
            response = conn.delete(bucketName, entry.key, null);
            assertEquals(
                    "couldn't delete entry",
                    HttpURLConnection.HTTP_NO_CONTENT,
                    response.connection.getResponseCode());
        }

        ListAllMyBucketsResponse listAllMyBucketsResponse = conn.listAllMyBuckets(null);
        assertEquals(
                "couldn't list all my buckets",
                HttpURLConnection.HTTP_OK,
                listAllMyBucketsResponse.connection.getResponseCode());
        List buckets = listAllMyBucketsResponse.entries;

        response = conn.deleteBucket(bucketName, null);
        assertEquals(
                "couldn't delete bucket",
                HttpURLConnection.HTTP_NO_CONTENT,
                response.connection.getResponseCode());

        listAllMyBucketsResponse = conn.listAllMyBuckets(null);
        assertEquals(
                "couldn't list all my buckets",
                HttpURLConnection.HTTP_OK,
                listAllMyBucketsResponse.connection.getResponseCode());
        assertEquals(
                "bucket count is incorrect",
                buckets.size() - 1,
                listAllMyBucketsResponse.entries.size());

        QueryStringAuthGenerator generator =
            new QueryStringAuthGenerator(awsAccessKeyId, awsSecretAccessKey);

        checkURL(
                generator.createBucket(bucketName, null),
                "PUT",
                HttpURLConnection.HTTP_OK,
                "couldn't create bucket");
        checkURL(
                generator.put(bucketName, key, new S3Object("test data".getBytes(), null), null),
                "PUT",
                HttpURLConnection.HTTP_OK,
                "put object",
                "test data");
        checkURL(
                generator.get(bucketName, key, null),
                "GET",
                HttpURLConnection.HTTP_OK,
                "get object");
        checkURL(
                generator.listBucket(bucketName, null, null, null, null),
                "GET",
                HttpURLConnection.HTTP_OK,
                "list bucket");
        checkURL(
                generator.listAllMyBuckets(null),
                "GET",
                HttpURLConnection.HTTP_OK,
                "list all my buckets");
        checkURL(
                generator.getACL(bucketName, key, null),
                "GET",
                HttpURLConnection.HTTP_OK,
                "get acl");
        checkURL(
                generator.putACL(bucketName, key, new String(acl), null),
                "PUT",
                HttpURLConnection.HTTP_OK,
                "put acl",
                new String(acl));
        checkURL(
                generator.getBucketACL(bucketName, null),
                "GET",
                HttpURLConnection.HTTP_OK,
                "get bucket acl");
        checkURL(
                generator.putBucketACL(bucketName, new String(bucketACL), null),
                "PUT",
                HttpURLConnection.HTTP_OK,
                "put bucket acl",
                new String(bucketACL));
        checkURL(
                generator.getBucketLogging(bucketName, null),
                "GET",
                HttpURLConnection.HTTP_OK,
                "get bucket logging");
        checkURL(
                generator.putBucketLogging(bucketName, new String(bucketLogging), null),
                "PUT",
                HttpURLConnection.HTTP_OK,
                "put bucket logging",
                new String(bucketLogging));
        checkURL(
                generator.delete(bucketName, key, null),
                "DELETE",
                HttpURLConnection.HTTP_NO_CONTENT,
                "delete object");
        checkURL(
                generator.deleteBucket(bucketName, null),
                "DELETE",
                HttpURLConnection.HTTP_NO_CONTENT,
                "delete bucket");

        System.out.println("OK (" + assertionCount + " tests passed)");
    }


    private static void verifyBucketResponseParameters( ListBucketResponse listBucketResponse,
                                                           String bucketName, String prefix, String marker,
                                                           int maxKeys, String delimiter, boolean isTruncated,
                                                           String nextMarker ) {
        assertEquals("Bucket name should match.", bucketName, listBucketResponse.name);
        assertEquals("Bucket prefix should match.", prefix, listBucketResponse.prefix);
        assertEquals("Bucket marker should match.", marker, listBucketResponse.marker);
        assertEquals("Bucket delimiter should match.", delimiter, listBucketResponse.delimiter);
        if ( UnspecifiedMaxKeys != maxKeys ) {
            assertEquals("Bucket max-keys should match.", maxKeys, listBucketResponse.maxKeys);
        }
        assertEquals("Bucket should not be truncated.", isTruncated, listBucketResponse.isTruncated);
        assertEquals("Bucket nextMarker should match.", nextMarker, listBucketResponse.nextMarker);
    }


    public static void assertEquals(String message, int expected, int actual) {
        assertionCount++;
        if (expected != actual) {
            throw new RuntimeException(message + ": expected " + expected + " but got " + actual);
        }
    }

    private static void assertEquals(String message, byte[] expected, byte[] actual) {
        assertionCount++;
        if (! Arrays.equals(expected, actual)) {
            throw new RuntimeException(
                    message +
                    ": expected " +
                    new String(expected) +
                    " but got " +
                    new String(actual));
        }
    }

    public static void assertEquals(String message, Object expected, Object actual) {
        assertionCount++;
        if (expected != actual && (actual == null || ! actual.equals(expected))) {
            throw new RuntimeException(message + ": expected " + expected + " but got " + actual);
        }
    }

    public static void assertEquals(String message, boolean expected, boolean actual) {
        assertionCount++;
        if (expected != actual) {
            throw new RuntimeException(message + ": expected " + expected + " but got " + actual);
        }
    }

    private static void checkURL(String url, String method, int code, String message)
        throws MalformedURLException, IOException
    {
        checkURL(url, method, code, message, null);
    }

    private static void checkURL(String url, String method, int code, String message, String data)
        throws MalformedURLException, IOException
    {
        if (data == null) data = "";

        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        connection.setRequestMethod(method);
        if ("PUT".equals(method)) {
            connection.setRequestProperty("Content-Length", ""+data.getBytes().length);
            connection.setDoOutput(true);
            connection.getOutputStream().write(data.getBytes());
        } else {
            // HttpURLConnection auto populates Content-Type, which we don't want here
            connection.setRequestProperty("Content-Type", "");
        }

        assertEquals(message, code, connection.getResponseCode());
    }
}
