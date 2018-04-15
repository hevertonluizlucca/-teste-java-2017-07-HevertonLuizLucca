package br.com.heverton.selecaoInvolves.impl.view;

import br.com.heverton.selecaoInvolves.api.AppView;

public class AppViewImpl implements AppView{

	@Override
	public void show(String text) {
		System.out.println(text);
	}

}
