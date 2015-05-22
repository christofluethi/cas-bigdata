package ch.shaped.bfh.cas.bgd.textanalysis.statistics;

import java.util.*;

/* 
 * code from bigdata lecture
 */
public class TermDistribution {

	private class Term {
		public String term = "";
		public int frequency = 0;
		
		public Term(String term, int frequency) {
			super();
			this.term = term;
			this.frequency = frequency;
		}

		@Override
		public boolean equals(Object term) {
			if (!(term instanceof Term)) return false;
			return (this.term.equals(term));
		}
		
		@Override
		public int hashCode() {
			return this.term.hashCode();
		}
	}
	
	List<Term> terms = new ArrayList<Term>();
	
	public TermDistribution() {	
	}
	
	private Term get(String termToFind) {
		Iterator<Term> i = terms.iterator();
		while (i.hasNext()) {
			Term term = i.next();
			if (term.term.equals(termToFind)) {
				return term;
			}
		}
		return null;
	}
	
	public void addTerm(String newTerm) {
		Term term = get(newTerm);
		if (term == null) { // not found
			term = new Term(newTerm, 1);
			terms.add(term);
		} else {
			term.frequency++;
		}
	}
	
	private int getTermNbr() {
		int termNbr = 0;
		Iterator<Term> i = terms.iterator();
		while (i.hasNext()) {
			Term term = i.next();
			termNbr += term.frequency;
		}
		
		return termNbr;
	}
	
	
	public void printFrequencyDistribution() {
		List<Term> termsSorted = new ArrayList<Term>();
		termsSorted.addAll(terms);
		Collections.sort(termsSorted, new Comparator<Term>() {
			@Override
			public int compare(Term o1, Term o2) {
				if (o1.frequency < o2.frequency) return -1;
				if (o1.frequency > o2.frequency) return 1;
				return 0;
			}
		});
		
		Iterator<Term> i = termsSorted.iterator();
		while (i.hasNext()) {
			Term term = i.next();
			// FIXME
			System.out.println((term.frequency / (double)getTermNbr()) + " - " + term.term);
		}
	}		
}	
