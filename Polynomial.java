import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Polynomial{
	double[][] polynomial;
	
	public Polynomial() {
		polynomial = new double[2][1];
		polynomial[0][0] = 0;
		polynomial[1][0] = 0;
	}
	
	public Polynomial(double[] coef, double[] exp) {
		polynomial = new double[2][coef.length];
		for(int i = 0; i < exp.length; i++) {
			polynomial[0][i] = coef[i];
			polynomial[1][i] = exp[i];
		}
	}
	
	public Polynomial(File f) throws IOException {
		BufferedReader read = new BufferedReader(new FileReader(f));
		String line = read.readLine();
		String word[] = line.split("(?=[+-])");
		double[] coef = new double[word.length * 2];
		double[] exp = new double[word.length * 2];
		for(int i = 0; i < word.length; i++) {
			String[] word2 = word[i].split("x");
			if(word2.length > 1) {
				coef[i * 2] = Double.parseDouble(word2[0]);
				exp[i * 2] = Double.parseDouble(word2[1]);
			}
			else {
				coef[i * 2] = Double.parseDouble(word2[i]);
				exp[i * 2] = 0;
			}
		}
		
		polynomial = new double[2][coef.length];
		for(int i = 0; i < exp.length; i++) {
			polynomial[0][i] = coef[i];
			polynomial[1][i] = exp[i];
		}
		
		read.close();
		}
	
	public Polynomial add(Polynomial input) {
		double[] coef = new double[polynomial[0].length + input.polynomial[0].length];
		double[] exp = new double[polynomial[0].length + input.polynomial[0].length];
		
		int i = 0; int j = 0; int k = 0;
		while(i < polynomial[0].length && j < input.polynomial[0].length) {
			if(polynomial[1][i] > input.polynomial[1][j]) {
				coef[k] = polynomial[0][i];
				exp[k] = polynomial[1][i];
				i++;
				k++;
			}
			else if (polynomial[1][i] < input.polynomial[1][j]) {
				coef[k] = input.polynomial[0][j];
				exp[k] = input.polynomial[1][j];
				j++;
				k++;
			}
			else {
				coef[k] = polynomial[0][i] + input.polynomial[0][j];
				exp[k] = polynomial[1][i];
				i++;
				j++;
				k++;
			}
		}
			
		if(i == polynomial[0].length) {
			while(j < input.polynomial[0].length) {
				coef[k] = input.polynomial[0][j];
				exp[k] = input.polynomial[1][j];
				j++;
				k++;
			}
		}
		else {
			while(i < polynomial[0].length) {
				coef[k] = polynomial[0][i];
				exp[k] = polynomial[1][i];
				i++;
				k++;
			}
		}
		
		Polynomial output = new Polynomial(coef, exp);
		return output;
	}
			
	public double evaluate(double input) {
		double output = 0;
		for(int j = 0; j < polynomial[0].length; j++) {
			output += polynomial[0][j] * Math.pow(input, polynomial[1][j]);
		}
		return output;
	}
	
	public boolean hasRoot(double input) {
		if(evaluate(input) == 0) {
			return true;
		}
		return false;
	}
	

	
	public /*Polynomial*/void redundant() {
		int i = 0, j = 0;
		// counting amount of useful data outputting in j
		while(i < polynomial[0].length) {
			if(polynomial[0][i] != 0) {
				j++;
			}
			i++;
		}
		
		Polynomial output = new Polynomial(new double[j], new double[j]);
		j = 0;
		//only to get rid of the zeros
		for(i = 0; i < polynomial[0].length; i++) {
			if(polynomial[0][i] != 0) {
				output.polynomial[0][j] = polynomial[0][i];
				output.polynomial[1][j] = polynomial[1][i];
				j++;
			}
		}
		
		//to search for duplicated exponentials
		int k = 0; //to record how many is duplicated
		for(i = 0; i < output.polynomial[0].length; i++) {
			for(j = i + 1; j < output.polynomial[0].length; j++) {
				if(output.polynomial[1][i] == output.polynomial[1][j]) {
					output.polynomial[0][i] += output.polynomial[0][j];
					output.polynomial[0][j] = 0;
					k++;
				}
			}
		}
		
		
		polynomial = new double[2][i - k];
		j = 0;
		//get rid of the redundant exponentials
		for(i = 0; i < output.polynomial[0].length; i++) {
			if(output.polynomial[0][i] != 0) {
				polynomial[0][j] = output.polynomial[0][i];
				polynomial[1][j] = output.polynomial[1][i];
				j++;
			}
		}
		
//		return this;
	}
	
	public void print() {
		for (int i = 0; i < polynomial[0].length; i++) {
			System.out.println("coef: " + polynomial[0][i] + " exp: " + polynomial[1][i]);
		}
	}
	
	public Polynomial multiply(Polynomial input) {
		Polynomial output = new Polynomial(new double[input.polynomial[0].length * polynomial[0].length], 
				new double[input.polynomial[0].length * polynomial[0].length]);
		
		int k = 0;
		for(int i = 0; i < polynomial[0].length; i++) {
			for(int j = 0; j < input.polynomial[0].length; j++) {
				output.polynomial[0][k] = polynomial[0][i] * input.polynomial[0][j];
				output.polynomial[1][k] = polynomial[1][i] + input.polynomial[1][j];
				k++;
			}
		}
		
		output.redundant();
		return output;
	}
	
	public void saveToFile(String destination) throws FileNotFoundException {
		this.redundant();
		
		String output = String.valueOf(polynomial[0][0]);
		if(polynomial[1][0] != 0) {
			output = output + "x" + String.valueOf(polynomial[1][0]);
		}
		
		for(int i = 1; i < polynomial[0].length; i++) {
			if(polynomial[0][i] >= 0) {
				output = output + "+" + String.valueOf(polynomial[0][i]);
			}
			else {
				output = output + String.valueOf(polynomial[0][i]);
			}
			
			if(polynomial[1][i] != 0) {
				output = output + "x" + String.valueOf(polynomial[1][i]);
			}
		}
		
		PrintStream file = new PrintStream(destination);
		file.print(output);
		
		file.close();
	}
}
