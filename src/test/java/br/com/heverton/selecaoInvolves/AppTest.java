package br.com.heverton.selecaoInvolves;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import br.com.heverton.selecaoInvolves.api.FileParserPresenter;
import br.com.heverton.selecaoInvolves.impl.presenter.CSVFileParserPresenterImpl;
import br.com.heverton.selecaoInvolves.shared.FileBean;
import br.com.heverton.selecaoInvolves.shared.FileType;

/**
 * Unit test for simple App.
 */
public class AppTest {
	private FileParserPresenter parserTest;

	/**
	 * Rigorous Test :-)
	 * 
	 * @throws IOException
	 */

	@Test
	public void count() throws IOException {
		assertEquals(2L, this.parserTest.countLines(), 0L);
	}

	@Test
	public void CountDistinct() throws IOException {
		assertEquals(2, this.parserTest.countDistinctProperty("1"), 0L);
	}

	@Test
	public void filter() throws IOException {
		assertEquals(1, this.parserTest.getResultsFilter("1", "SC").size(), 0L);
	}

	@Before
	public void initFile() throws IOException {
		File file = new File(System.getProperty("user.dir"),
				"src/test/java/br/com/heverton/selecaoInvolves/cidadesTest.csv");
		FileBean fileBean = new FileBean();
		fileBean.setFile(file);
		fileBean.setPath(file.getPath());
		fileBean.setFileName(file.getName());
		fileBean.setType(FileType.CSV);
		this.parserTest = new CSVFileParserPresenterImpl();
		parserTest.loadFile(fileBean);
	}

}
