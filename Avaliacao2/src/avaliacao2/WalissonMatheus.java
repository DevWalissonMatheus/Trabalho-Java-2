package avaliacao2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/* Fiz o trabalho usando o VS Code com o JDK na versão 19.0.2 */

class Pessoa implements Serializable {
	private String nome;
	private Long cpf;

	public Pessoa(String nome, Long cpf) {
		this.nome = nome;
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}
}

class Aluno extends Pessoa implements Serializable {
	private String matricula;
	private ArrayList<String> disciplinas;
	private ArrayList<Double> notas;

	public Aluno(String nome, Long cpf, String matricula,
			ArrayList<String> disciplinas, ArrayList<Double> notas) {
		super(nome, cpf);
		this.matricula = matricula;
		this.disciplinas = new ArrayList<>(disciplinas);
		this.notas = new ArrayList<>(notas);
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public ArrayList<String> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(ArrayList<String> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public ArrayList<Double> getNotas() {
		return notas;
	}

	public void setNotas(ArrayList<Double> notas) {
		this.notas = notas;
	}
}

public class WalissonMatheus {
	Scanner sc = null;
	ArrayList<Aluno> alunos;

	public WalissonMatheus() {
		sc = new Scanner(System.in);
		alunos = new ArrayList<Aluno>();
	}

	public static void main(String[] args) {
		WalissonMatheus walisson = new WalissonMatheus();
		walisson.metodo01();
		walisson.metodo02();
		walisson.metodo03();
	}

	public void metodo01() {
		boolean adAlunos = true;
		/* Coloquei esse while para que ficasse em loop para adicionar novos alunos */
		while (adAlunos) {
			System.out.println("Insira os dados do(a) aluno(a):");
			System.out.print("CPF: ");
			/*
			 * Coloquei esse try catch para tratar uma exceção se fosse inserido letras
			 * ao invés de números, fiquei bastante tempo tentando fazer funcionar
			 * mas no final deu certo :)
			 */
			Long cpf = null;
			try {
				cpf = sc.nextLong();
			} catch (InputMismatchException e) {
				System.out.println("CPF inválido, insira os números do CPF.");
				sc.next();
				continue;
			}
			sc.nextLine();
			/*
			 * Sem esse NextLine apresentava um erro que a matricula aparecia na
			 * frente do nome, então consegui arrumar fazendo isso.
			 */
			System.out.print("Nome: ");
			String nome = sc.nextLine();

			System.out.print("Matrícula: ");
			String matricula = sc.nextLine();

			System.out.print("Disciplinas (separe com vírgula): ");
			String[] dcp = sc.nextLine().split(",");
			ArrayList<String> disciplinas = new ArrayList<>(Arrays.asList(dcp));

			System.out.print("Notas (separe com vírgula): ");
			String[] nt2 = sc.nextLine().split(",");
			ArrayList<Double> notas = new ArrayList<>();
			for (String nt1 : nt2) {
				notas.add(Double.parseDouble(nt1));
			}
			/*
			 * Tentei colocar um try catch aqui
			 * mas não consegui sempre dada erro :(
			 */
			Aluno aluno = new Aluno(nome, cpf, matricula, disciplinas, notas);
			alunos.add(aluno);

			try {
				save("dados.txt");
				System.out.println("Aluno(a) salvo(a).");
			} catch (FileNotFoundException e) {
				System.out.println("Erro ao salvar: " + e.getMessage());
			}

			System.out.println("Adicionar outro(a) aluno(a)?");
			System.out.println("S/N: ");
			String cont = sc.nextLine();
			adAlunos = cont.equalsIgnoreCase("s");
		}
	}

	public void metodo02() {
		double mdNts = 0.0;
		System.out.println("Alunos aprovados:");
		try {
			load("dados.txt");
			for (Aluno aluno : alunos) {
				double smNts = 0.0;
				for (double nota : aluno.getNotas()) {
					smNts += nota;
				}
				double media = smNts / aluno.getNotas().size();
				if (media >= 7) {
					System.out.println("Nome: " + aluno.getNome());
					System.out.println("CPF: " + aluno.getCpf());
					System.out.println("Matrícula: " + aluno.getMatricula());
					System.out.println("Disciplinas: " + aluno.getDisciplinas());
					System.out.println("Notas: " + aluno.getNotas());
					System.out.println("Média: " + media);
					System.out.println();
					mdNts += smNts;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Erro no carregamento dos dados: " + e.getMessage());
		}

	}

	public void metodo03() {
		System.out.println("Alunos reprovados:");
		for (Aluno aluno : alunos) {
			double soma = 0;
			for (Double nota : aluno.getNotas()) {
				soma += nota;
			}
			double media = soma / aluno.getNotas().size();
			if (media < 7.0) {
				System.out.println("Nome: " + aluno.getNome());
				System.out.println("CPF: " + aluno.getCpf());
				System.out.println("Matrícula: " + aluno.getMatricula());
				System.out.println("Disciplinas: " + aluno.getDisciplinas());
				System.out.println("Notas: " + aluno.getNotas());
				System.out.println("Média: " + media);
				System.out.println();
			}
		}

	}

	public void save(String fileName) throws FileNotFoundException {
		FileOutputStream fout = new FileOutputStream(fileName);
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(fout);
			oos.writeObject(alunos);
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void load(String fileName) throws FileNotFoundException {
		FileInputStream fin = new FileInputStream(fileName);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(fin);
			alunos = (ArrayList<Aluno>) ois.readObject();
			fin.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
