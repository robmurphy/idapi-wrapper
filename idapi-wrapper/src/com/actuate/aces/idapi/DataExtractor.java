package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;
import org.apache.axis.attachments.AttachmentPart;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;

public class DataExtractor extends BaseController {
	public DataExtractor(BaseController controller) {
		super(controller);
	}

	public DataExtractor(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public DataExtractor(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public DataExtractor(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public ResultSetSchema getMetaData(String sourceFile) {
		ResultSetSchema[] resultSetSchemas = getAllMetaData(sourceFile);

		if (resultSetSchemas == null)
			return null;

		return resultSetSchemas[0];
	}

	public ResultSetSchema getMetaData(String sourceFile, String resultSetName) {

		ResultSetSchema[] resultSetSchemas = getAllMetaData(sourceFile);

		if (resultSetSchemas == null)
			return null;

		for (ResultSetSchema resultSetSchema : resultSetSchemas) {
			if (resultSetSchema.getResultSetName().equals(resultSetName))
				return resultSetSchema;
		}

		return null;
	}

	public ResultSetSchema[] getAllMetaData(String sourceFile) {
		GetMetaData getMetaData = new GetMetaData();

		ObjectIdentifier objectIdentifier = new ObjectIdentifier();
		objectIdentifier.setName(sourceFile);
		objectIdentifier.setType(sourceFile.substring(sourceFile.lastIndexOf(".") + 1));
		getMetaData.setObject(objectIdentifier);

		acxControl.setSOAPHeader("Type", sourceFile.substring(sourceFile.lastIndexOf(".") + 1));
		GetMetaDataResponse response = null;
		try {
			response = acxControl.proxy.getMetaData(getMetaData);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} finally {
			acxControl.removeSOAPHeader("Type");
		}

		if (response.getArrayOfResultSetSchema() == null)
			return null;
		else
			return response.getArrayOfResultSetSchema().getResultSetSchema();
	}

	public void extractToStream(String sourceFile, ResultSetSchema resultSetSchema, OutputStream outputStream) throws IOException, SOAPException {
		String tableName = resultSetSchema.getResultSetName();
		String[] columns = getColumnsFromSchema(resultSetSchema);
		extractToStream(sourceFile, tableName, columns, outputStream);
	}

	public void extractToStream(String sourceFile, String tableName, String columnNames, OutputStream outputStream) throws IOException, SOAPException {
		extractToStream(sourceFile, tableName, columnNames.split(","), outputStream);
	}

	public void extractToStream(String sourceFile, String tableName, String[] columnNames, OutputStream outputStream) throws IOException, SOAPException {

		DataExtraction dataExtraction = new DataExtraction();

		ObjectIdentifier objectIdentifier = new ObjectIdentifier();
		objectIdentifier.setName(sourceFile);
		objectIdentifier.setType(sourceFile.substring(sourceFile.lastIndexOf(".") + 1));
		dataExtraction.setObject(objectIdentifier);

		NameValuePair[] components = new NameValuePair[1];
		components[0] = new NameValuePair("resultSetName", tableName);
		ArrayOfNameValuePair componentValues = new ArrayOfNameValuePair();
		componentValues.setNameValuePair(components);
		dataExtraction.setComponent(componentValues);

		HashMap<String, String> props = new HashMap<String, String>();
		props.put("BIRTDataExtractionEncoding", "utf-8");
		props.put("BIRTDataExtractionLocaleNeutralFormat", "false");
		props.put("Locale", "en_US");
		props.put("BIRTExportDataType", "false");
		props.put("BIRTDataExtractionSep", ",");
		NameValuePair[] properties = getNameValuePairsFromMap(null, props);
		ArrayOfNameValuePair propertyValues = new ArrayOfNameValuePair();
		propertyValues.setNameValuePair(properties);
		dataExtraction.setProperties(propertyValues);

		dataExtraction.setColumns(new ArrayOfString(columnNames));

		acxControl.proxy.dataExtraction(dataExtraction);

		Iterator iter = acxControl.actuateAPI.getCall().getMessageContext().getResponseMessage().getAttachments();
		while (iter.hasNext()) {
			AttachmentPart attachmentPart = (AttachmentPart) iter.next();
			attachmentPart.getDataHandler().writeTo(outputStream);
			attachmentPart.dispose();
		}
	}

	public java.io.File extractToFile(String sourceFile, ResultSetSchema resultSetSchema, String destinationFile) throws IOException, SOAPException {
		String tableName = resultSetSchema.getResultSetName();
		String[] columns = getColumnsFromSchema(resultSetSchema);
		return extractToFile(sourceFile, tableName, columns, destinationFile);
	}

	public java.io.File extractToFile(String sourceFile, String tableName, String columnNames, String destinationFile) throws IOException {
		return extractToFile(sourceFile, tableName, columnNames.split(","), destinationFile);
	}

	public java.io.File extractToFile(String sourceFile, String tableName, String[] columnNames, String destinationFile) throws IOException {
		java.io.File file = new java.io.File(destinationFile);
		FileOutputStream fileOutputStream = new FileOutputStream(file, false);
		try {
			extractToStream(sourceFile, tableName, columnNames, fileOutputStream);
		} catch (SOAPException e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}

	public InputStream getExtractStream(String sourceFile, ResultSetSchema resultSetSchema) {
		String tableName = resultSetSchema.getResultSetName();
		String[] columns = getColumnsFromSchema(resultSetSchema);
		return getExtractStream(sourceFile, tableName, columns);
	}

	public InputStream getExtractStream(String sourceFile, String tableName, String columnNames) {
		return getExtractStream(sourceFile, tableName, columnNames.split(","));
	}

	public InputStream getExtractStream(String sourceFile, String tableName, String[] columnNames) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			extractToStream(sourceFile, tableName, columnNames, baos);
			return new ByteArrayInputStream(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (SOAPException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String[] getColumnsFromSchema(ResultSetSchema resultSetSchema) {
		ColumnSchema[] columnSchemas = resultSetSchema.getArrayOfColumnSchema().getColumnSchema();
		String[] columns = new String[columnSchemas.length];
		for (int i = 0; i < columnSchemas.length; i++) {
			columns[i] = columnSchemas[i].getName();
		}

		return columns;
	}
}
