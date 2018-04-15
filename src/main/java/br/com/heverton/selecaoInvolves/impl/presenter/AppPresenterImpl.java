package br.com.heverton.selecaoInvolves.impl.presenter;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import br.com.heverton.selecaoInvolves.api.AppPresenter;
import br.com.heverton.selecaoInvolves.api.AppView;
import br.com.heverton.selecaoInvolves.api.FileParserPresenter;
import br.com.heverton.selecaoInvolves.impl.view.AppViewImpl;
import br.com.heverton.selecaoInvolves.shared.FileBean;
import br.com.heverton.selecaoInvolves.shared.FileType;

public class AppPresenterImpl implements AppPresenter {

	AppView view;
	LinkedHashMap<Integer, FileBean> mapFileOptions;
	FileParserPresenter parser;
	private Scanner scanner;

	public AppPresenterImpl() {
		this.scanner = new Scanner(System.in);
		this.view = new AppViewImpl();
		this.mapFileOptions = new LinkedHashMap<>();
	}

	@Override
	public void initialize() {
		this.fillIn();
		this.view.show("Vamos iniciar o programa, para finalizar digite SAIR a qualquer momento.");
		this.reloadFirstCommand();

	}

	@Override
	public void reloadFirstCommand() {
		this.view.show(getOptions(this.mapFileOptions));
		String command = this.scanner.nextLine();
		this.readFile(command);
	}

	private void readFile(String command) {
		try {
			
			if(this.mapFileOptions.containsKey(Integer.valueOf(command))) {
				FileBean bean = this.mapFileOptions.get(Integer.parseInt(command));
				this.parser = null;
				switch (bean.getType()) {
				case CSV:
					this.parser = new CSVFileParserPresenterImpl();
					this.parser.initialize(bean);
					break;
				case XML:
					// NOOP
					this.view.show("O leitor de xml não foi implementado, escolha outro arquivo. \n");
					break;
				case JSON:
					// NOOP
					this.view.show("O leitor de json não foi implementado, escolha outro arquivo. \n");
					break;
				default:
					this.verifyClose(command);
					break;
				}
				
				if(parser == null) {
					this.reloadFirstCommand();
				}
			}else {
				verifyClose(command);
			}
				

		} catch (NumberFormatException e) {
			verifyClose(command);
		}
				
	}

	private void verifyClose(String command) {
		if (command.equalsIgnoreCase("sair")) {
			this.closeProgram();
		}else {
			this.view.show("Opção inválida \n");
			this.reloadFirstCommand();			
		}
	}

	@Override
	public void closeProgram() {
		this.view.show("Programa finalizado, obrigado por utilizar!");
		this.scanner.close();
	}

	private String getOptions(LinkedHashMap<Integer, FileBean> options) {
		
		StringBuilder stringBuilder = new StringBuilder();
		if(options != null) {
			stringBuilder.append("Digite o número do arquivo escolhido para iniciar a leitura!");
			for (Entry<Integer, FileBean> entry : options.entrySet()) {
				stringBuilder.append("\n").append(entry.getKey()).append(" - ").append(entry.getValue().getFileName())
				.append("\n");
				
			}
		}

		return stringBuilder.toString();
	}

	private void fillIn() {
		this.verifyArchives();
	}

	private void verifyArchives() {
		int contador = 0;
		File pasta = new File(System.getProperty("user.dir"), "src/archives");
		File[] lista = pasta.listFiles();
		for (File file : lista) {
			if (file.isFile()) {
				contador++;
				FileBean fileBean = new FileBean();
				fileBean.setFile(file);
				fileBean.setPath(file.getPath());
				fileBean.setFileName(file.getName());
				fileBean.setType(this.getFileType(fileBean.getFileName()));
				this.mapFileOptions.put(contador, fileBean);
			}
		}

	}

	private FileType getFileType(String fileName) {
		String[] names = fileName.split("[.]");
		if (names != null && names[1] != null) {
			for (FileType type : FileType.values()) {
				if (type.name().equalsIgnoreCase(names[1])) {
					return type;
				}

			}
		}
		return null;
	}

}
