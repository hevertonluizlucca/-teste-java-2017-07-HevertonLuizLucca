package br.com.heverton.selecaoInvolves;

import br.com.heverton.selecaoInvolves.api.AppPresenter;
import br.com.heverton.selecaoInvolves.impl.presenter.AppPresenterImpl;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		AppPresenter presenter = new AppPresenterImpl();
		presenter.initialize();
	}
}
