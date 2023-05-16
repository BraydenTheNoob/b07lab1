public class Polynomial{
	double[] polynomial;
	
	public Polynomial() {
		polynomial = new double[1];
		polynomial[0] = 0;
	}
	
	public Polynomial(double[] input) {
		polynomial = new double[input.length];
		for(int i = 0; i < input.length; i++) {
			polynomial[i] = input[i];
		}
	}
	
	public Polynomial add(Polynomial input) {
		if(input.polynomial.length < polynomial.length) {
			Polynomial output = new Polynomial(polynomial);
			for(int i = 0; i < input.polynomial.length; i++) {
				output.polynomial[i] = polynomial[i] + input.polynomial[i];	
			}
			return output;
		}
		else {
			Polynomial output = new Polynomial(input.polynomial);
			for(int i = 0; i < polynomial.length; i++) {
				output.polynomial[i] = polynomial[i] + input.polynomial[i];
			}
			return output;
		}
	}
	
	public double evaluate(double input) {
		double output = 0;
		for(int i = 0; i < polynomial.length; i++) {
			output += polynomial[i] * Math.pow(input, i);
		}
		return output;
	}
	
	public boolean hasRoot(double input) {
		if(evaluate(input) == 0) {
			return true;
		}
		return false;
	}
}