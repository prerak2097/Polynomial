package poly;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 *
 * @author runb-cs112
 *
 */
public class Polynomial {

	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3
	 * </pre>
	 *
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc)
			throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 *
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		Node p1 = poly1;
		Node p2 = poly2;
		Node polyResult= new Node(0, 0, null);
		Node ptr = polyResult;

		if (poly1 == null && poly2 != null) {
			return poly2;
		}
		else if(poly1 != null && poly2==null) {
			return poly1;
		}

		while (p1 != null || p2 != null) {

			if (p1 == null) {
				ptr.term.coeff = p2.term.coeff;
				ptr.term.degree = p2.term.degree;
				p2 = p2.next;
			} else if (p2 == null) {
				ptr.term.coeff = p1.term.coeff;
				ptr.term.degree = p1.term.degree;
				p1 = p1.next;
			} else if (p1.term.degree > p2.term.degree) {
				ptr.term.coeff = p1.term.coeff;
				ptr.term.degree = p1.term.degree;
				p1 = p1.next;
			} else if (p2.term.degree > p1.term.degree) {
				ptr.term.coeff = p2.term.coeff;
				ptr.term.degree = p2.term.degree;
				p2 = p2.next;
			} else {
				ptr.term.degree = p1.term.degree;
				ptr.term.coeff = p1.term.coeff + p2.term.coeff;
				p1 = p1.next;
				p2 = p2.next;
			}

			if(p1 != null || p2 != null) {
				System.out.println("Adding new");
				ptr.next = new Node(0, 0, null);
				ptr = ptr.next;
			}

		}


		return degSort(deleteOccurrences(polyResult));
	}




	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 *
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (poly1 == null || poly2 == null) {
			return null;
		}
		Node pone = poly1;
		Node ptwo = poly2;
		Node terms = null;
		int degreeSum;
		float proOfCo;
		int maxExpo = 0;
		while (pone != null) {
			while (ptwo != null) {
				proOfCo = pone.term.coeff * ptwo.term.coeff;
				degreeSum = pone.term.degree + ptwo.term.degree;
				terms = new Node(proOfCo, degreeSum, terms);
				if (degreeSum > maxExpo)
					maxExpo = degreeSum;
				ptwo = ptwo.next;
			}
			pone = pone.next;
			ptwo = poly2;
		}
		Node simplifiedNode = null;
		for (int i = 0; i<= maxExpo; i++)
		{
			float summation = 0;

			Node temporary = terms;
			while (temporary != null) {
				if (temporary.term.degree == i)
					summation+=temporary.term.coeff;
				temporary = temporary.next;
			}
			if (summation != 0)
				simplifiedNode = new Node(summation, i, simplifiedNode);
		}
		return flip(simplifiedNode);
	}



	/**
	 * Evaluates a polynomial at a given value.
	 *
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		float returnVal =0;
		Node ptr = poly;

		while (ptr != null) {
			returnVal += ptr.term.coeff * (Math.pow(x, ptr.term.degree));
			ptr = ptr.next;
		}


		return returnVal;
	}

	/**
	 * Returns string representation of a polynomial
	 *
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		}

		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
			 current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}

	// Helper function to reverse polynomial.
	private static Node flip(Node reversingNode)
	{
		Node previous = null;
		Node now = reversingNode;
		Node next = null ;
		while (now != null) {
			next = now.next;
			now.next = previous;
			previous = now;
			now = next;
		}
		reversingNode = previous;
		return reversingNode;
	}

	// helper function to delete any odd occurrences of coefficient of 0 to the power of
	// a viable degree.

	private static Node deleteOccurrences(Node deletedNode) {
		if (deletedNode ==null) {
			return null;
		}
		Node curr = deletedNode;
		Node prev = null;

		while(curr != null) {
			if(curr.term.coeff == 0) {
				if(prev ==null) {
					deletedNode = curr.next;
				} else {
					prev.next = curr.next;
				}
			} else {
				prev=curr;
			}
			curr = curr.next;
		}
		return deletedNode;
	}

	// helper function to sort output by degrees if not already properly sorted.
	private static Node degSort(Node degSorted)
	{

		Node current = degSorted.next;

		Node prev = degSorted;

		while (current != null)
		{
			if (current.term.degree < prev.term.degree)
			{
				int temp = current.term.degree;
				current.term.degree = prev.term.degree;
				prev.term.degree = temp;

				float temp2 = current.term.coeff;
				current.term.coeff = prev.term.coeff;
				prev.term.coeff = temp2;

				prev = degSorted;
				current = degSorted.next;
			}

			prev = prev.next;
			current = current.next;
		}

		return degSorted;
	}
}
