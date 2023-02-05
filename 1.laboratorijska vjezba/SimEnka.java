import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

	public class SimEnka {
		public static void main(String[] args) throws Exception {
			Scanner sc = new Scanner(System.in);
			
			ArrayList<String> ulazi = new ArrayList<String>();

			for(String s: sc.nextLine().split("\\|")) {
				ulazi.add(s);				
			}
			
			TreeSet<String> stanja = new TreeSet<>();
			
			for(String s : sc.nextLine().trim().split(",")) {				
				stanja.add(s);
			}
			
			TreeSet<String> abeceda = new TreeSet<>();
			for(String s: sc.nextLine().trim().split(",")) {
				abeceda.add(s);
			}
						
			TreeSet<String> prihvatljivaStanja = new TreeSet<>();
			for(String s: sc.nextLine().trim().split(",")) {
				prihvatljivaStanja.add(s);
			}
						
			String pocetnoStanje = new String(sc.nextLine().trim());
			
			ArrayList<Prijelascic> prijelazi = new ArrayList<>();		//lista svih prijelaza
					
			while(sc.hasNextLine()) {
				Prijelascic[] p = stvoriPrijelaz(sc.nextLine());
				for(Prijelascic prijelascicici: p)
					prijelazi.add(prijelascicici);
			}
			
			sc.close();
	//prvo moram rijesit epsilon prijelaze pa onda vidit ima li normalnih prijelaza	
			//tribat ce iz seta uklanjat mrtva stanja
			
			
			for(String ulaznaLinija: ulazi) {
				String[] inputi = ulaznaLinija.split(",");
				
				TreeSet<String> trenutnaStanja = new TreeSet<>();
				trenutnaStanja.add(pocetnoStanje);
				epsilonPrijelaz(pocetnoStanje, prijelazi, trenutnaStanja);
				StringBuilder sb = new StringBuilder();
				for(String s: trenutnaStanja)
					sb.append(s + ",");
				
				sb.deleteCharAt(sb.length()-1);
				
				for(String input: inputi) {
					TreeSet<String> stanjaZaDaniInput = new TreeSet<>();

					for(String trenutno: trenutnaStanja) {
						for(Prijelascic prijelaz: prijelazi) {
							if(prijelaz.getTrenutnoStanje().compareTo(trenutno) > 0)      
								break;														
							if((prijelaz.getTrenutnoStanje().equals(trenutno) && prijelaz.getUlaz().equals(input)) && !(prijelaz.getIzlaz().equals("#"))) {
									stanjaZaDaniInput.add(prijelaz.getIzlaz());
									epsilonPrijelaz(prijelaz.getIzlaz(), prijelazi, stanjaZaDaniInput);
								}
						}
					}
									
					if(stanjaZaDaniInput.isEmpty()) {
						sb.append("|#");		
					} else {
						sb.append("|");
						for(String s: stanjaZaDaniInput)
							sb.append(s + ",");
						
						sb.deleteCharAt(sb.length()-1);
					}
					
					trenutnaStanja.removeIf(s -> s.length() > 0);
					for(String s: stanjaZaDaniInput)
						trenutnaStanja.add(s);
					
					stanjaZaDaniInput.removeIf(s -> s.length() > 0);
				}			
				System.out.println(sb.toString());		
			}	
			
		}

		private static void epsilonPrijelaz(String s, ArrayList<Prijelascic> prijelazi, TreeSet<String> stanjaZaDaniInput) {
			for(Prijelascic prijelaz: prijelazi) {
				if(prijelaz.getTrenutnoStanje().compareTo(s) > 0)      
					return;												
				if(prijelaz.getTrenutnoStanje().equals(s) && prijelaz.getUlaz().equals("$") && !(prijelaz.getIzlaz().equals("#"))) {
					if(stanjaZaDaniInput.contains(prijelaz.getIzlaz()))
						continue;
					stanjaZaDaniInput.add(prijelaz.getIzlaz());					
					epsilonPrijelaz(prijelaz.getIzlaz(), prijelazi, stanjaZaDaniInput);
				}
			}		
			return;
		}
		
		public static Prijelascic[] stvoriPrijelaz(String linija) {
			linija = linija.trim().replace("->",";");
			String[] dijelovi = linija.split(";");
			String[] ulazi = dijelovi[0].split(",");
			String[] izlaznaStanja = dijelovi[1].split(",");
			Prijelascic[] polje = new Prijelascic[izlaznaStanja.length];
			for(int i = 0; i < izlaznaStanja.length; i++)
				polje[i] = new Prijelascic(ulazi[0], ulazi[1], izlaznaStanja[i]);
			
			return polje;
		}
	}

	class Prijelascic {
		private String trenutnoStanje;
		private String ulaz;
		private String izlaz;
		
		public Prijelascic(String trenutnoStanje, String ulaz, String izlaz) {
			this.trenutnoStanje = trenutnoStanje;
			this.ulaz = ulaz;
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
	}
