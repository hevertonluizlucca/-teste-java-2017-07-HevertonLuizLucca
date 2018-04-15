package br.com.heverton.selecaoInvolves.shared;

import java.io.File;
import java.util.Map;

public class FileBean {
	
	private String fileName;
	private FileType type;
	private Map<String, String> header;
	private File file;
	private String path;
	
	/**
	 * @return the type
	 */
	public FileType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(FileType type) {
		this.type = type;
	}
	/**
	 * @return the header
	 */
	public Map<String, String> getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
		
}
