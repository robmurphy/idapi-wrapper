/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi.actions;

import com.actuate.aces.idapi.BaseController;
import com.actuate.aces.idapi.FileRemover;
import com.actuate.aces.idapi.Uploader;
import com.actuate.aces.idapi.actions.model.Filters;
import com.actuate.aces.idapi.actions.model.VolumeUploadModel;
import com.actuate.aces.idapi.control.ActuateException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class VolumeUpload extends BaseController {

    private ListFilesFilter listFilesFilter;

	public VolumeUpload(BaseController controller) {
		super(controller);
	}

	public VolumeUpload(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public VolumeUpload(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public boolean upload(String sourcePath, String targetPath, String modelFileName) throws IOException, JAXBException {
		return upload(sourcePath, targetPath, modelFileName, false);
	}

	public boolean upload(String sourcePath, String targetPath, String modelFileName, boolean deleteFirst) throws IOException, JAXBException {
		File modelFile = new File(modelFileName);
		return upload(sourcePath, targetPath, modelFile, deleteFirst);
	}

	public boolean upload(String sourcePath, String targetPath, File modelFile) throws IOException, JAXBException {
		return upload(sourcePath, targetPath, modelFile, false);
	}

	public boolean upload(String sourcePath, String targetPath, File modelFile, boolean deleteFirst) throws IOException, JAXBException {
		JAXBContext context = JAXBContext.newInstance("com.actuate.aces.idapi.actions.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		VolumeUploadModel model = (VolumeUploadModel) unmarshaller.unmarshal(modelFile);
		return upload(sourcePath, targetPath, model, deleteFirst);
	}

	public boolean upload(String sourcePath, String targetPath, VolumeUploadModel model) throws IOException {
		return upload(sourcePath, targetPath, model, false);
	}

	public boolean upload(String sourcePath, String targetPath, VolumeUploadModel model, boolean deleteFirst) throws IOException {
        VolumeUploadModel model1 = model;
		this.listFilesFilter = new ListFilesFilter(model.getFilters());

		boolean retVal = true;
		if (deleteFirst)
			retVal = new FileRemover(this).delete(targetPath);

		return uploadFile(new File(sourcePath), targetPath) && retVal;
	}

	private boolean uploadFile(File sourceFile, String targetPath) throws IOException {

		if (sourceFile.isDirectory()) {
			boolean retVal = true;
			File[] files = sourceFile.listFiles(listFilesFilter);

			for (File file : files) {

				String sourceFileName = sourceFile.getAbsolutePath() + File.separatorChar + file.getName();
				if (targetPath.equals("/"))
					targetPath = "";
				String targetFileName = targetPath + "/" + file.getName();

				if (file.isDirectory()) {
					//TODO: if creating folder build and apply rules for targetFileName
					//TODO: if (folderRule.isIgnore()) continue;
					//TODO: createFolder
				}

				retVal = uploadFile(new File(sourceFileName), targetFileName) && retVal;
			}
			return retVal;
		} else {
			//TODO: build and apply rules for targetPath
			//TODO: if (fileRule.isIgnore()) return true;
			Uploader uploader = new Uploader(this);
			String objId = uploader.uploadFileAsStream(sourceFile, targetPath);
			return !(objId == null || objId.equals(""));
		}
	}

	private class ListFilesFilter implements FileFilter {

		private boolean includeAllFolders = false;
		private String includeFilter = null;
		private String excludeFilter = null;

		private ListFilesFilter(Filters filters) {
			if (filters == null)
				return;

			if (filters.getIncludeFilters() != null) {
				includeFilter = "";
				List<String> includeFilters = filters.getIncludeFilters().getIncludeFilter();
				for (int i = 0; i < includeFilters.size(); i++) {
					includeFilter += prepFilter(includeFilters.get(i));
					if (i < includeFilters.size() - 1)
						includeFilter += "|";

				}
			}

			if (filters.getExcludeFilters() != null) {
				excludeFilter = "";
				List<String> excludeFilters = filters.getExcludeFilters().getExcludeFilter();
				for (int i = 0; i < excludeFilters.size(); i++) {
					excludeFilter += prepFilter(excludeFilters.get(i));
					if (i < excludeFilters.size() - 1)
						excludeFilter += "|";

				}
			}
		}

		private String prepFilter(String filter) {
			filter = filter.replaceAll("\\[", "\\[");
			filter = filter.replaceAll("\\]", "\\]");
			filter = filter.replaceAll("\\{", "\\{");
			filter = filter.replaceAll("\\}", "\\}");
			filter = filter.replaceAll("\\.", "\\.");
			filter = filter.replaceAll("\\?", "\\?");
			filter = filter.replaceAll("\\+", "\\+");
			filter = filter.replaceAll("\\^", "\\^");
			filter = filter.replaceAll("\\$", "\\$");
			filter = filter.replaceAll("\\*", ".*");
			return filter;
		}

		public boolean accept(File pathname) {

			if (pathname.isDirectory() && includeAllFolders)
				return true;

			String name = pathname.getName();
			boolean retVal = true;

			if (includeFilter != null)
				retVal = name.matches(includeFilter);

			if (!retVal)
				return false;

			if (excludeFilter != null)
				retVal = !name.matches(excludeFilter);

			return retVal;

		}

	}
}
