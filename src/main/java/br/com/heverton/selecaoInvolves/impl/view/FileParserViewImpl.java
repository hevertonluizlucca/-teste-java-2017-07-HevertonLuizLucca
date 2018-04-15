package br.com.heverton.selecaoInvolves.impl.view;

import br.com.heverton.selecaoInvolves.api.FileParserView;

public class FileParserViewImpl implements FileParserView{
	@Override
	public void show(String text) {
		System.out.println(text);
	}

}
