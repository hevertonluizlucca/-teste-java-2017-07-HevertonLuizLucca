package br.com.heverton.selecaoInvolves.impl.presenter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.com.heverton.selecaoInvolves.api.FileParserPresenter;
import br.com.heverton.selecaoInvolves.api.FileParserView;
import br.com.heverton.selecaoInvolves.impl.view.FileParserViewImpl;
import br.com.heverton.selecaoInvolves.shared.FileBean;

public class CSVFileParserPresenterImpl implements FileParserPresenter {

	FileBean file;
	String delimitier = ",";
	private FileParserView view;
	private Scanner scanner;
	private HashMap<Integer, CommandsAvailable> mapCmd;

	public CSVFileParserPresenterImpl() {
		this.view = new FileParserViewImpl();
		this.scanner = new Scanner(System.in);
		this.mapCmd = new HashMap<>();
	}

	@Override
	public void loadFile(FileBean bean) throws IOException {
		this.file = bean;
		file.setHeader(this.getHeader());
	}

	private Map<String, String> getHeader() throws IOException {

		LinkedHashMap<String, String> header = new LinkedHashMap<>();
		List<String> keyWords;
		try {
			keyWords = Files.lines(Paths.get(file.getPath())).findFirst()
					.map(row -> Arrays.asList(row.split(delimitier))).orElse(new ArrayList<>());

			int index = 0;
			for (String value : keyWords) {
				header.put(String.valueOf(index), value);
				index++;
			}
		} catch (IOException e) {
			throw e;
		}

		return header;
	}

	private void count() throws IOException {

		Long count = countLines();
		String message = "O arquivo está vazio";
		if (count != null) {
			message = (count > 1 ? "Existem " + count + " registros " : "Existe " + count + " registro ")
					+ "no arquivo.\n\n";
		}
		System.out.println(message);

		this.view.show(getMessageContinue());
		String command = this.scanner.nextLine();
		if (command.equalsIgnoreCase("1")) {
			this.initCommand();
		} else {
			this.closeProgram();
		}
	}

	@Override
	public Long countLines() throws IOException {
		return Files.lines(Paths.get(file.getPath())).skip(1).count();
	}	

	private void closeProgram() {
		this.view.show("Programa finalizado, obrigado por utilizar!");
		this.scanner.close();
	}

	private void countDistinct() throws IOException {

		this.view.show("Digite o numero da propriedade abaixo");

		this.view.show(this.getHeaderNames());
		String command = this.scanner.nextLine();

		if (file.getHeader().containsKey(command)) {
			Integer count = countDistinctProperty(command);

			String message = "Não há registros na propriedade pesquisada";
			if (count != null) {
				message = (count > 1 ? "Existem " + count + " registros distintos "
						: "Existe " + count + " registro distinto ") + "na propriedade pesquisada.\n\n";
			}
			System.out.println(message);

			this.view.show(getMessageContinue());
			command = this.scanner.nextLine();
			if (command.equalsIgnoreCase("1")) {
				this.countDistinct();
			} else {
				this.closeProgram();
			}

		} else {
			if (command.equalsIgnoreCase("sair")) {
				this.closeProgram();
			} else if (command.equalsIgnoreCase("voltar")) {
				this.initCommand();
			} else {
				this.view.show("Opção inválida \n");
				this.countDistinct();

			}

		}

	}
	@Override
	public Integer countDistinctProperty(String property) throws IOException {
		return Files.lines(Paths.get(file.getPath())).skip(1).map(getColumn(property))
		.collect(Collectors.toSet()).size();
	}

	private String getMessageContinue() {
		return "Digite 1 para continuar ou qualquer outra tecla para sair.";
	}

	private Function<String, String> getColumn(final String index) {
		return line -> {
			String[] row = line.split(delimitier);
			return row[Integer.parseInt(index)];
		};
	}

	private String getColumn(String line, String index, String delimiter) {

		String[] row = line.split(delimiter);
		return row[Integer.parseInt(index)];
	}

	private void filter() throws IOException {

		this.view.show("Digite o numero da propriedade que deseja pesquisar");
		this.view.show(this.getHeaderNames());
		String command = this.scanner.nextLine();
		if (file.getHeader().containsKey(command)) {
			String index = command;
			String propriedade = file.getHeader().get(index);

			this.view.show("Digite o valor que deseja pesquisar na propriedade " + propriedade);
			String filter = this.scanner.nextLine();

			List<String> rows = new ArrayList<>();
			rows.add(this.file.getHeader().values().toString());

			List<String> results = getResultsFilter(index, filter);

			rows.addAll(results);
			rows.forEach(System.out::println);
			System.out.println("Foram encontrados " + results.size() + " resultados para o termo " + filter
					+ " pesquisado na propriedade " + propriedade);
			this.view.show(getMessageContinue());
			command = this.scanner.nextLine();
			if (command.equalsIgnoreCase("1")) {
				this.filter();
			} else {
				this.closeProgram();
			}

		} else {
			if (command.equalsIgnoreCase("sair")) {
				this.closeProgram();
			} else if (command.equalsIgnoreCase("voltar")) {
				this.initCommand();
			} else {
				this.view.show("Opção inválida \n");
				this.filter();

			}

		}

	}

	@Override
	public List<String> getResultsFilter(String index, String filter) throws IOException {
		List<String> results = Files.lines(Paths.get(file.getPath())).skip(1)
				.filter(string -> filter.equalsIgnoreCase(getColumn(string, index, delimitier)))
				.map(string -> string).collect(Collectors.toList());
		return results;
	}

	private String getHeaderNames() {
		StringBuilder stringBuilder = new StringBuilder();

		for (Entry<String, String> entry : this.file.getHeader().entrySet()) {
			stringBuilder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
		}
		return stringBuilder.toString();

	}

	private String getCommandsAvailable() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Digite o número do comando que deseja executar!").append("\n");
		for (CommandsAvailable entry : CommandsAvailable.values()) {
			stringBuilder.append(entry.id).append(" - ").append(entry.commandName).append(" - Sua função é: ")
					.append(entry.commandDescription).append("\n");

			this.mapCmd.put(entry.id, entry);

		}

		stringBuilder.append("\n Se desejar sair digite SAIR, caso queira voltar ao menu anterior digite VOLTAR");
		return stringBuilder.toString();
	}

	public enum CommandsAvailable {

		COUNT(1, "count *", "Contagem total de registros"), COUNT_DISTINCT_PROPERTY(2, "count distinct [propriedade]",
				"Contagem de registros distintos por propriedade"), FILTER_PROPERTY(3, "filter [propriedade] [valor]",
						"Exibe os valores da coluna escolhida de acordo com o parâmetro enviado");

		private Integer id;
		private String commandName;
		private String commandDescription;

		private CommandsAvailable(Integer id, String commandName, String commandDescription) {
			this.id = id;
			this.commandName = commandName;
			this.commandDescription = commandDescription;
		}
	}

	@Override
	public void initialize(FileBean bean) {

		try {
			this.loadFile(bean);
			this.initCommand();
		} catch (IOException e) {
			this.view.show("Ocorreu um erro não esperado ao ler o arquivo");
			this.closeProgram();
		}
		

	}

	private void initCommand() throws IOException {
		this.view.show(getCommandsAvailable());
		String command = this.scanner.nextLine();
		this.readCommand(command);
	}

	private void readCommand(String command) throws IOException {
		try {

			if (this.mapCmd.containsKey(Integer.valueOf(command))) {
				CommandsAvailable cmd = this.mapCmd.get(Integer.parseInt(command));
				switch (cmd) {
				case COUNT:
					this.count();
					break;
				case COUNT_DISTINCT_PROPERTY:
					this.countDistinct();
					break;
				case FILTER_PROPERTY:
					this.filter();
					break;
				default:
					this.view.show("Comando não encontrado!");
					break;
				}

			} else {
				verifyClose(command);
			}

		} catch (NumberFormatException e) {
			this.verifyClose(command);
		}

	}

	private void verifyClose(String command) throws IOException {
		if (command.equalsIgnoreCase("sair")) {
			this.closeProgram();
		} else if (command.equalsIgnoreCase("voltar")) {
			this.initCommand();
		} else {
			this.view.show("Opção inválida \n");
			this.initCommand();
		}
	}


}
