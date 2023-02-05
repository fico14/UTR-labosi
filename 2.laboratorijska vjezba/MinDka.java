import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class MinDka {
	//prvo ukloni nedohvatljiva pa onda istovjetna

	public static void main(String[] args) throws Exception{
		Scanner sc = new Scanner(System.in);
			
		TreeSet<String> pocStanja = new TreeSet<>();				//lista svih stanja
		for(String s : sc.nextLine().trim().split(",")) {				
			pocStanja.add(s);
		}
		
		TreeSet<String> abeceda = new TreeSet<>();				//lista svih ulaznih znakova
		for(String s: sc.nextLine().trim().split(",")) {
			abeceda.add(s);
		}
		
		TreeSet<String> prihvatljivaStanja = new TreeSet<>();	//lista prihvatljivih stanja
		for(String s: sc.nextLine().trim().split(",")) {
			prihvatljivaStanja.add(s);
		}
		
		String pocetnoStanje = new String(sc.nextLine().trim()); //pocetno stanje

		ArrayList<Prijelaz> prijelazi = new ArrayList<>();		//lista svih prijelaza
		while(sc.hasNextLine()) {
			Prijelaz[] p = stvoriPrijelaz(sc.nextLine());
			for(Prijelaz prijelascicici: p)
				prijelazi.add(prijelascicici);
		}
		
		sc.close();
		
		TreeSet<String> stanja = new TreeSet<>();	//lista svih dohvatljivih stanja
		stanja.add(pocetnoStanje);
		
		provozajSe(pocetnoStanje, stanja, prijelazi, abeceda);		//nadjemo sva dohvatljiva stanja
		
		TreeSet<String> novaPrihvatljiva = new TreeSet<String>();
		
		for(String prihvatljivo: prihvatljivaStanja) {
			if(stanja.contains(prihvatljivo)) {
				novaPrihvatljiva.add(prihvatljivo);
			}
		}
		
		Iterator<Prijelaz> itP = prijelazi.iterator();
		while(itP.hasNext()) {
			Prijelaz pr = itP.next();
			if(!stanja.contains(pr.getTrenutnoStanje()) || !stanja.contains(pr.getIzlaz()))
				itP.remove();
		}
		
		//napravit treeMap u kojoj ce bit parovi (stanje - prihvatljivost)!!!!!!!!!!!!!!!!!!!!!!!!!!
		TreeMap<String, Integer> prihvatljivosti = new TreeMap<>();
		for(String s: stanja) {
			boolean zastavica = false;
			for(String prih: prihvatljivaStanja) {
				if(s.equals(prih)) {
					zastavica = true;
					prihvatljivosti.put(s,1);
					break;
				}
			}
			if(zastavica == false)
				prihvatljivosti.put(s,0);
		}
		
		ArrayList<String> listaStanja = new ArrayList<>();
		listaStanja.addAll(stanja);
				
		int[][] matrica = new int [stanja.size()][stanja.size()];
		
		//punim matricu sa 1 na poljima gdje su stanja razlicite prihvatljivosti, a inace s 0
		
		for(int i = 1; i < listaStanja.size() ; i++) {							//kad iteriram sa i idem od 1 do n, a s j idem do 0 do n-1
			for(int j = 0; j < listaStanja.size() - 1 ; j++) {
				if(i <= j)
					break;
				if(prihvatljivosti.get(listaStanja.get(i)) != prihvatljivosti.get(listaStanja.get(j)))
					matrica[i][j] = 1;
				else
					matrica[i][j] = 0;
			}
		}
	
		while(true) {
			boolean ponovi = false;
			int i,j;
			int len = abeceda.size();
			for(i = 1; i < stanja.size() ; i++) {
				for(j = 0; j < stanja.size() - 1; j++) {
					if(i <= j)
						break;
					if(matrica[i][j] == 1)
						continue;
					String prvo = listaStanja.get(i);
					String drugo = listaStanja.get(j);
					int idx1 = 0, idx2 = 0;
					
					for(int k = 0; k < prijelazi.size(); k = k + len) {
						if(prijelazi.get(k).getTrenutnoStanje().equals(prvo))
							idx1 = k;
						if(prijelazi.get(k).getTrenutnoStanje().equals(drugo)) 
							idx2 = k;
					}
					
					for(int r = 0; r < len; r++) {
						if((prihvatljivosti.get(prijelazi.get(idx1 + r).getIzlaz()) != prihvatljivosti.get(prijelazi.get(idx2 + r).getIzlaz())) ||
							matrica[listaStanja.indexOf(prijelazi.get(idx1 + r).getIzlaz())][listaStanja.indexOf(prijelazi.get(idx2 + r).getIzlaz())] == 1) {
							matrica[i][j] = 1;
							ponovi = true;
							break;
						}
					}
				}
			}
			if(!ponovi)
				break;
		}
		
		//uredit ispis automata (rijesit tranzitivnost istovjetnih stanja i ponavljanje istih stanja
		ArrayList<Pair> parovi = new ArrayList<Pair>();
		
		for(int i = 1; i < stanja.size() ; i++) {
			for(int j = 0; j < stanja.size() - 1; j++) {
				if(i <= j)
					break;
				if(matrica[i][j] == 0) {
					parovi.add(new Pair(listaStanja.get(i), listaStanja.get(j)));
				}
			}
		}
		
		//napravit listu sa setovima (da mi se ne ponavljaju) u koje cu ubacit tranzitivna stanja
		ArrayList<TreeSet<String>> listaIstovjetnihGrupa = new ArrayList<>();
		
		for(Pair pair: parovi) {
			boolean zastavica = false;
			if(listaIstovjetnihGrupa.isEmpty()) {
				TreeSet<String> s = new TreeSet<>();
				s.add(pair.getPrvi());
				s.add(pair.getDrugi());
				listaIstovjetnihGrupa.add(s);
			} else {
				for(TreeSet<String> set: listaIstovjetnihGrupa) {
					if(set.contains(pair.getPrvi()) || set.contains(pair.getDrugi())) {
						set.add(pair.getPrvi());
						set.add(pair.getPrvi());
						zastavica = true;
					}
				}
				if(zastavica == false) {
					TreeSet<String> w = new TreeSet<>();
					w.add(pair.getPrvi());
					w.add(pair.getDrugi());
					listaIstovjetnihGrupa.add(w);
				}
			}
		}
				
		//eksperimentalni dio s iteratorom: OVDE JE GARANT NEKA GRESKA!!!!!!!!!!!!
		while(true) {
			boolean promjena = false;
			Iterator<TreeSet<String>> it = listaIstovjetnihGrupa.iterator();
			while(it.hasNext()) {
				TreeSet<String> iteratorovSet = it.next();
				for(TreeSet<String> setina: listaIstovjetnihGrupa) {
					if(setina.equals(iteratorovSet))				
						continue;
					for(String string: setina) {
						if(iteratorovSet.contains(string)) {
							setina.addAll(iteratorovSet);
							promjena = true;
						}
					}
				}
			}
			if(promjena == false)
				break;
			else
				it.remove();
		}

		//sad u listiIstovjetnihGrupa imam setove istovjetnih stanja. Tribam u svim prijelazima zaminit ime stanja iz svakog seta sa leksikografski najmanjim (tj.prvim iz seta)
		
		for(Prijelaz pr: prijelazi) {
			for(TreeSet<String> setcic: listaIstovjetnihGrupa) {
				if(setcic.contains(pr.getTrenutnoStanje()))
					pr.setTrenutnoStanje(setcic.first());
				if(setcic.contains(pr.getIzlaz())) {
					pr.setIzlaz(setcic.first());					
				}
			}
		}
		
		TreeSet<String> ispisPrijelaza = new TreeSet<String>();		//set s prijelazima spremnim za ispis
		for(Prijelaz pr: prijelazi) 
			ispisPrijelaza.add(pr.toString());
		
		for(TreeSet<String> setcic: listaIstovjetnihGrupa) {
			if(setcic.contains(pocetnoStanje))
				pocetnoStanje = setcic.first();						//aazuriram pocetno stanje
		}
		
		TreeSet<String> konacnaUkupnaDohvatljivaStanja = new TreeSet<>();	//azuriram sva stanja
		for(String state: stanja) {
			for(TreeSet<String> setcic: listaIstovjetnihGrupa) {
				if(setcic.contains(state)) {
					state = setcic.first();
					break;
				}
			}
			konacnaUkupnaDohvatljivaStanja.add(state);
		}
		
		TreeSet<String> konacnaPrihvatljiva = new TreeSet<>();			//azuriram prihvatljiva stanja
		for(String state: novaPrihvatljiva) {
			for(TreeSet<String> setcic: listaIstovjetnihGrupa) {
				if(setcic.contains(state)) {
					state = setcic.first();
					break;
				}
			}
			konacnaPrihvatljiva.add(state);
		}
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		StringBuilder sb = new StringBuilder();
		for(String str: konacnaUkupnaDohvatljivaStanja) {
			sb.append(str + ",");
		}
		
		String svaStanja = sb.toString();
		svaStanja = svaStanja.substring(0, svaStanja.length() - 1);
		
		StringBuilder sb1 = new StringBuilder();
		for(String str: abeceda) {
			sb1.append(str + ",");
		}
		
		String svaAbeceda = sb1.toString();
		svaAbeceda = svaAbeceda.substring(0, svaAbeceda.length() - 1);
		
		StringBuilder sb2 = new StringBuilder();
	
		for(String str: konacnaPrihvatljiva) {
			sb2.append(str + ",");
		}
		
		String stringPrihvatljiva = sb2.toString();
		if(!stringPrihvatljiva.isEmpty())
			stringPrihvatljiva = stringPrihvatljiva.substring(0, stringPrihvatljiva.length() - 1);
				
		System.out.println(svaStanja); 
		System.out.println(svaAbeceda); 
		if(!stringPrihvatljiva.isEmpty()) {
			System.out.println(stringPrihvatljiva); 
		}
		else {
			System.out.println(); 
		}
		System.out.println(pocetnoStanje); 
		for(String pr: ispisPrijelaza) {
			System.out.println(pr); 
		}	
	}

	private static void provozajSe(String stanje, TreeSet<String> dohvatljivaStanja, ArrayList<Prijelaz> prijelazi, TreeSet<String> abeceda) {		
			
		for(String s: abeceda) {
			String next = new String();
			for(Prijelaz p: prijelazi) {
				if(p.getTrenutnoStanje().equals(stanje) && p.getUlaz().equals(s) && !(p.getIzlaz().equals("#"))) {
					next = p.getIzlaz();
					if(dohvatljivaStanja.contains(next))
						break;
					dohvatljivaStanja.add(next);
					provozajSe(next, dohvatljivaStanja, prijelazi, abeceda);
					break;
				}
			}	
		}
		return;
	}

	public static Prijelaz[] stvoriPrijelaz(String linija) {
		linija = linija.trim().replace("->",";");
		String[] dijelovi = linija.split(";");
		String[] ulazi = dijelovi[0].split(",");
		String[] izlaznaStanja = dijelovi[1].split(",");
		Prijelaz[] polje = new Prijelaz[izlaznaStanja.length];
		for(int i = 0; i < izlaznaStanja.length; i++)
			polje[i] = new Prijelaz(ulazi[0], ulazi[1], izlaznaStanja[i]);
		
		return polje;
	}
}

class Prijelaz {
	private String trenutnoStanje;
	private String ulaz;
	private String izlaz;
	
	public Prijelaz(String trenutnoStanje, String ulaz, String izlaz) {
		this.trenutnoStanje = trenutnoStanje;
		this.ulaz = ulaz;
		this.izlaz = izlaz;
	}

	public void setTrenutnoStanje(String trenutnoStanje) {
		this.trenutnoStanje = trenutnoStanje;
	}

	public void setUlaz(String ulaz) {
		this.ulaz = ulaz;
	}

	public void setIzlaz(String izlaz) {
		this.izlaz = izlaz;
	}

	public String getTrenutnoStanje() {
		return trenutnoStanje;
	}

	public String getUlaz() {
		return ulaz;
	}

	public String getIzlaz() {
		return izlaz;
	}

	@Override
	public String toString() {
		return trenutnoStanje + "," + ulaz + "->" + izlaz;
	}	
}

class Pair {
	private String prvi;
	private String drugi;
	
	public Pair(String prvi, String drugi) {
		this.prvi = prvi;
		this.drugi = drugi;
	}
	public String getPrvi() {
		return prvi;
	}
	public String getDrugi() {
		return drugi;
	}
	
	public boolean sadrzi(String s) {
		if (prvi.equals(s) || drugi.equals(s))
			return true;
		return false;
	}
}