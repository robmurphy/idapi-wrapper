package com.actuate.aces.idapi.util;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteArrayDataSource implements DataSource {
	private ByteArrayInputStream inStream;
	private String contentType;
	private String name = "";

	public ByteArrayDataSource(ByteArrayInputStream bais, String contentType) {
		inStream = bais;
		this.contentType = contentType;
	}

	public ByteArrayDataSource(byte[] data, String contentType) {
		inStream = new ByteArrayInputStream(data);
		this.contentType = contentType;
	}

	public InputStream getInputStream() throws IOException {
		inStream.reset();
		return inStream;
	}

	public OutputStream getOutputStream() throws IOException {
		throw new IOException("Invalid Operation");
	}

	public String getContentType() {
		return contentType;
	}

	public void setName(String name) {
		if (name != null)
			this.name = name;
	}

	public String getName() {
		return name;
	}
}
