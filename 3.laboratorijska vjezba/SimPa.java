import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class SimPa {
	
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		
		ArrayList<String> ulazi = new ArrayList<>();  //1.redak
		for(String s: sc.nextLine().split("\\|")) {
			ulazi.add(s);				
		}
		
		TreeSet<String> stanja = new TreeSet<>();		//2. redak	
		for(String s : sc.nextLine().trim().split(",")) {				
			stanja.add(s);
		}
		
		TreeSet<String> ulazniZnakovi = new TreeSet<>();  //3. redak	
		for(String s : sc.nextLine().trim().split(",")) {				
			ulazniZnakovi.add(s);
		}
		
		TreeSet<String> znakoviStoga = new TreeSet<>();  //4. redak	
		for(String s : sc.nextLine().trim().split(",")) {				
			znakoviStoga.add(s);
		}
		
		TreeSet<String> prihvatljivaStanja = new TreeSet<>();  //5. redak	
		for(String s : sc.nextLine().trim().split(",")) {				
			prihvatljivaStanja.add(s);
		}
		
		String pocetnoStanje = sc.nextLine().trim();		//6. redak
		String pocetniZnakStoga = sc.nextLine().trim();		//7. redak

		ArrayList<Fja> fjePrijelaza = new ArrayList<>();		//8. i ostali retci
		while(sc.hasNextLine()) {
			fjePrijelaza.add(stvoriFjuPrijelaza(sc.nextLine()));
		}
		
		sc.close();	
				
		for(String ul: ulazi) {
			Omotac stog = new Omotac(pocetniZnakStoga);
			Omotac stanje = new Omotac(pocetnoStanje);
		
			StringBuilder sb = new StringBuilder();
			
			sb.append(pocetnoStanje + "#" + pocetniZnakStoga + "|");
			
			for(String ulazniZnak: ul.split(",")) {  //more li bit vise epsilon prijelaza!?!?!?!?!?!?
//				System.out.println("Znak: " + ulazniZnak);
				sb.append(provozajSeEpsilonima(fjePrijelaza, stanje, stog));				
				boolean prijelazNePostoji = true;

				for(Fja f: fjePrijelaza) {
					if(stog.getData().length() > 1) {
						if(f.getTrenutnoStanje().equals(stanje.getData()) && stog.getData() .substring(0, 1).equals(f.getZnakStoga()) && f.getUlazniZnak().equals(ulazniZnak)) {
							
							prijelazNePostoji = false;
							stanje.setData(f.getNovoStanje()); 
							if(!f.getNizZnakovaStoga().equals("$")) {
								if(stog.getData() .length() > 1) {
									stog.setData(f.getNizZnakovaStoga() + stog.getData() .substring(1));
								} else {
									stog.setData(f.getNizZnakovaStoga());
								}
							} else {
								if(stog.getData() .length() > 1) {
									stog.setData(stog.getData() .substring(1));
								} else {
									stog.setData("");
								}
							}
							if(stog.getData() .length() > 0)
								sb.append(stanje.getData() + "#" + stog.getData() + "|");
							else
								sb.append(stanje.getData() + "#$|");
							break;
						}
					} else {
						if(f.getTrenutnoStanje().equals(stanje.getData()) && stog.getData().equals(f.getZnakStoga()) && f.getUlazniZnak().equals(ulazniZnak)) {
							
							prijelazNePostoji = false;
							stanje.setData(f.getNovoStanje()); 
							if(!f.getNizZnakovaStoga().equals("$")) {
								if(stog.getData() .length() > 1) {
									stog.setData(f.getNizZnakovaStoga() + stog.getData() .substring(1));
								} else {
									stog.setData(f.getNizZnakovaStoga());
								}
							} else {
								if(stog.getData() .length() > 1) {
									stog.setData(stog.getData() .substring(1));
								} else {
									stog.setData("");
								}
							}
							if(stog.getData() .length() > 0)
								sb.append(stanje.getData() + "#" + stog.getData() + "|");
							else
								sb.append(stanje.getData() + "#$|");
							break;
						}					
					}
				}
				
				if(prijelazNePostoji) {
					sb.append("fail|");
					break;
				}
			}

			if(!prihvatljivaStanja.contains(stanje.getData()))
				sb.append(provozajSeEpsilonimaNaKraju(fjePrijelaza, stanje, stog, prihvatljivaStanja));
			
			if(sb.toString().endsWith("fail|")) {
				sb.append("0");
			} else {
				if(prihvatljivaStanja.contains(stanje.getData())) {
					sb.append("1");
				} else {
					sb.append("0");
				}
			}
		
			System.out.println(sb.toString()); //OVAJ RED TRIBA OSTAVIT		
		}
	
	}
	
	private static String provozajSeEpsilonima(ArrayList<Fja> fjePrijelaza, Omotac stanje, Omotac stog) {
		String izlaz = "";
		for(Fja f: fjePrijelaza) {
			if(stog.getData().length() > 1) {
				if(f.getTrenutnoStanje().equals(stanje.getData()) && stog.getData().substring(0, 1).equals(f.getZnakStoga()) 
						&& f.getUlazniZnak().equals("$")) {
					
					stanje.setData(f.getNovoStanje()); 
					if(!f.getNizZnakovaStoga().equals("$")) {
						if(stog.getData().length() > 1)
							stog.setData(f.getNizZnakovaStoga() + stog.getData().substring(1)); 
						else
							stog.setData(f.getNizZnakovaStoga());
					} else {
						if(stog.getData().length() > 1) {
							stog.setData(stog.getData().substring(1));
						} else {
							stog.setData("");
						}
					}
					
					if(stog.getData().length() == 0)
						izlaz = stanje.getData() + "#$|";
					else
						izlaz = stanje.getData() + "#" + stog.getData() + "|";
					return izlaz + provozajSeEpsilonima(fjePrijelaza, stanje, stog);
				}
			} else {
				if(f.getTrenutnoStanje().equals(stanje.getData()) && stog.getData().equals(f.getZnakStoga()) 
						&& f.getUlazniZnak().equals("$")) {
					
					stanje.setData(f.getNovoStanje()); 
					if(!f.getNizZnakovaStoga().equals("$")) {
						if(stog.getData().length() > 1)
							stog.setData(f.getNizZnakovaStoga() + stog.getData().substring(1)); 
						else
							stog.setData(f.getNizZnakovaStoga());
					} else {
						if(stog.getData().length() > 1) {
							stog.setData(stog.getData().substring(1));
						} else {
							stog.setData("");
						}
					}
					
					if(stog.getData().length() == 0)
						izlaz = stanje.getData() + "#$|";
					else
						izlaz = stanje.getData() + "#" + stog.getData() + "|";
					return izlaz + provozajSeEpsilonima(fjePrijelaza, stanje, stog);
				}
				
			}
		}
		return "";
	}
	
	private static String provozajSeEpsilonimaNaKraju(ArrayList<Fja> fjePrijelaza, Omotac stanje, Omotac stog, TreeSet<String> prihvatljivaStanja) {
		String izlaz = "";
		for(Fja f: fjePrijelaza) {
			if(stog.getData().length() > 1) {
				if(f.getTrenutnoStanje().equals(stanje.getData()) && stog.getData().substring(0, 1).equals(f.getZnakStoga()) 
						&& f.getUlazniZnak().equals("$")) {

					stanje.setData(f.getNovoStanje()); 
					if(!f.getNizZnakovaStoga().equals("$")) {
						if(stog.getData().length() > 1)
							stog.setData(f.getNizZnakovaStoga() + stog.getData().substring(1)); 
						else
							stog.setData(f.getNizZnakovaStoga());
					} else {
						if(stog.getData().length() > 1) {
							stog.setData(stog.getData().substring(1));
						} else {
							stog.setData("");
						}
					}

					if(stog.getData().length() == 0)
						izlaz = stanje.getData() + "#$|";
					else
						izlaz = stanje.getData() + "#" + stog.getData() + "|";

					if(prihvatljivaStanja.contains(stanje.getData()))
						return izlaz;
					else
						return izlaz + provozajSeEpsilonimaNaKraju(fjePrijelaza, stanje, stog, prihvatljivaStanja);
				}
			} else {
				if(f.getTrenutnoStanje().equals(stanje.getData()) && stog.getData().equals(f.getZnakStoga()) 
						&& f.getUlazniZnak().equals("$")) {
					
					stanje.setData(f.getNovoStanje()); 
					if(!f.getNizZnakovaStoga().equals("$")) {
						if(stog.getData().length() > 1)
							stog.setData(f.getNizZnakovaStoga() + stog.getData().substring(1)); 
						else
							stog.setData(f.getNizZnakovaStoga());
					} else {
						if(stog.getData().length() > 1) {
							stog.setData(stog.getData().substring(1));
						} else {
							stog.setData("");
						}
					}
					
					if(stog.getData().length() == 0)
						izlaz = stanje.getData() + "#$|";
					else
						izlaz = stanje.getData() + "#" + stog.getData() + "|";
					
					if(prihvatljivaStanja.contains(stanje.getData()))
						return izlaz;
					else
						return izlaz + provozajSeEpsilonimaNaKraju(fjePrijelaza, stanje, stog, prihvatljivaStanja);
				}
				
			}
		}
		return "";
	}

	private static Fja stvoriFjuPrijelaza(String nextLine) {
		String[] dijelovi = nextLine.trim().split("->");
		String[] prvaTri = dijelovi[0].split(",");
		String[] ostatak = dijelovi[1].split(",");

		return new Fja(prvaTri[0], prvaTri[1], prvaTri[2], ostatak[0], ostatak[1]);
	}

}

class Fja {
	private String trenutnoStanje;
	private String ulazniZnak;
	private String znakStoga;
	private String novoStanje;
	private String nizZnakovaStoga;
	
	public Fja(String trenutnoStanje, String ulazniZnak, String znakStoga, String novoStanje,
			String nizZnakovaStoga) {
		this.trenutnoStanje = trenutnoStanje;
		this.ulazniZnak = ulazniZnak;
		this.znakStoga = znakStoga;
		this.novoStanje = novoStanje;
		this.nizZnakovaStoga = nizZnakovaStoga;
	}

	public String getTrenutnoStanje() {
		return trenutnoStanje;
	}

	public String getUlazniZnak() {
		return ulazniZnak;
	}

	public String getZnakStoga() {
		return znakStoga;
	}

	public String getNovoStanje() {
		return novoStanje;
	}

	public String getNizZnakovaStoga() {
		return nizZnakovaStoga;
	}

}

class Omotac {
	private String data;
	
	public Omotac(String data) {
		this.data = data;
	}
	
	public String getData() {
		return this.data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
}
