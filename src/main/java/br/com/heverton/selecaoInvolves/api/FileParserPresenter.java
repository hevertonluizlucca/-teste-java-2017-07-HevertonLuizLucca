package br.com.heverton.selecaoInvolves.api;

import java.io.IOException;
import java.util.List;

import br.com.heverton.selecaoInvolves.shared.FileBean;

public interface FileParserPresenter {
	void initialize(FileBean bean);
	Long countLines() throws IOException;
	void loadFile(FileBean bean) throws IOException;
	Integer countDistinctProperty(String property)throws IOException;
	List<String> getResultsFilter(String index, String filter) throws IOException;
}
