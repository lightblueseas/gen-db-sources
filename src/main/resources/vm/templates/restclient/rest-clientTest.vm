/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *  *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *  *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.alpharogroup.resource.system.rest.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.file.delete.DeleteFileExtensions;
import de.alpharogroup.file.write.WriteFileExtensions;
import de.alpharogroup.resource.system.rest.api.ResourcesResource;

/**
 * The class {@link ResourcesRestClientTest}.
 */
public class ResourcesRestClientTest
{

	/** The rest client. */
	private ResourcesRestClient restClient;

	/** The resources resource. */
	private ResourcesResource resourcesResource;

	/**
	 * Sets the up method.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	public void setUpMethod() throws Exception
	{
		if (restClient == null)
		{
			restClient = new ResourcesRestClient();
			resourcesResource = restClient.getResourcesResource();
			AssertJUnit.assertNotNull(resourcesResource);
		}
	}

	/**
	 * Tear down method.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@AfterMethod
	public void tearDownMethod() throws Exception
	{
	}

	@Test(enabled = false)
	public void testDownloadByName() throws FileNotFoundException, IOException
	{
		final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		final HttpClient client = httpClientBuilder.build();
		final String url = "http://localhost:8080/resource/download/file/by/name/man_placeholder.jpg";
		final HttpGet get = new HttpGet(url);
		final HttpResponse response = client.execute(get);
		final InputStream in = response.getEntity().getContent();
		final byte[] byteArray = IOUtils.toByteArray(in);
		final File manPlaceholder = new File(".", "man_placeholder.jpg");
		WriteFileExtensions.writeByteArrayToFile(manPlaceholder, byteArray);
		DeleteFileExtensions.delete(manPlaceholder);
	}

	@SuppressWarnings("deprecation")
	@Test(enabled = false)
	public void testUploadFile() throws FileNotFoundException, IOException
	{
		// local variables
		HttpPost httpPost = null;
		CloseableHttpClient closeableHttpClient = null;
		HttpResponse httpResponse = null;
		MultipartEntityBuilder multipartEntityBuilder = null;
		HttpEntity httpEntity = null;
		FileBody fileBody = null;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = null;
		final File file = new File(".", "test-text.txt");
		final String stringWrite = "foo bar";
		WriteFileExtensions.writeStringToFile(file, stringWrite, null);
		try
		{
			// http post request header
			httpPost = new HttpPost("http://localhost:8080/resource/upload/file");

			// constructs file to be uploaded
			fileBody = new FileBody(file);
			multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntityBuilder.addPart("uploadfile", fileBody);
			httpEntity = multipartEntityBuilder.build();
			httpPost.setEntity(httpEntity);

			// actual execution of http post request
			final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			closeableHttpClient = httpClientBuilder.build();
			httpResponse = closeableHttpClient.execute(httpPost);

			System.out.println("Response code/message: " + httpResponse.getStatusLine());
			httpEntity = httpResponse.getEntity();

			// get the response content
			inputStream = httpEntity.getContent();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			stringBuilder = new StringBuilder();
			String strReadLine = bufferedReader.readLine();

			// iterate to get the data and append in StringBuilder
			while (strReadLine != null)
			{
				stringBuilder.append(strReadLine);
				strReadLine = bufferedReader.readLine();
				if (strReadLine != null)
				{
					stringBuilder.append("\n");
				}
			}
		}
		catch (final UnsupportedEncodingException usee)
		{
			usee.printStackTrace();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			// shuts down, when work done
			closeableHttpClient.getConnectionManager().closeExpiredConnections();
		}
		final String actual = stringBuilder.toString();
		final String expected = "upload success";
		AssertJUnit.assertEquals(expected, actual.trim());

		DeleteFileExtensions.delete(file);
	}

}
