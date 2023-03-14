package grecomg;
import java.time.LocalDate;
import java.util.*;


public class CallCenter
{   
    private HashMap<String, Operator> ops;
    private HashMap<String, LinkedList<Feedback>> feeds;
    
    public CallCenter() {
        ops = new HashMap<>();
        feeds = new HashMap<>();
    }
    
    public String registraOperatore(String n, String c, LocalDate b) throws NotUniqueOperatorException {
        Operator o = new Operator(n, c, b);
        String matricola = getMatricola(o);
        o.setMatricola(matricola);
        if (ops.containsKey(matricola))
            throw new NotUniqueOperatorException();
        o.setMatricola(matricola);
        ops.put(matricola, o);
        feeds.put(matricola, new LinkedList<>());
        return matricola;
    }
    
    public int registraValutazione(String m, int s, LocalDate d) throws InvalidOperatorException, InvalidScoreException {
        if (!ops.containsKey(m))
            throw new InvalidOperatorException();
        if (s > 3 || s < -3)
            throw new InvalidScoreException();
        Feedback f = new Feedback(m, s, d);
        feeds.get(m).add(f);
        return f.getId();
    }
    
    public String toString() {
        String s = "";
        
        for (Operator o : ops.values()) {
            s += o+"\n";
        }
        
        for (LinkedList<Feedback> l : feeds.values()) {
            for (Feedback f : l) {
                s += f+"\n";
            }
        }
        
        return s;
    }
    
    public Feedback[] restituisciValutazioni() {
        LinkedList<Feedback> fs = new LinkedList<Feedback>();
        for (LinkedList<Feedback> l : feeds.values()) {
            for (Feedback f : l) {
                fs.add(f);
            }
        }
        Feedback[] f = fs.toArray(new Feedback[0]);
        return f;
    }
    
    public int valutazioneComplessiva(String matricola) {
        if (!ops.containsKey(matricola))
            throw new InvalidOperatorException();
        int p = 0;
        for (LinkedList<Feedback> l : feeds.values()) {
            for (Feedback f : l) {
                if (f.getMatricola().equals(matricola)) {
                    p += f.getPunteggio();
                }
            }
        }
        return p;
    }
    
    public int valutazioneMese(String matricola, int m, int a) {
        if (!ops.containsKey(matricola))
            throw new InvalidOperatorException();
        int p = 0;
        for (LinkedList<Feedback> l : feeds.values()) {
            for (Feedback f : l) {
                LocalDate d = f.getLocalDate();
                if (f.getMatricola().equals(matricola) && d.getMonthValue() == m && d.getYear() == a) {
                    p += f.getPunteggio();
                }
            }
        }
        return p;
    }
    
    public Operator[] valutazioniNegative() {
        LinkedList<Operator> negOps = new LinkedList<Operator>();
        for (Operator o : ops.values()) {
            if (valutazioneComplessiva(o.getMatricola()) < 0)
                negOps.add(o);
        }
        return negOps.toArray(new Operator[0]);
    }
    
    public Operator[] best() {
        LinkedList<Operator> bestOps = new LinkedList<Operator>();
        int max = 0;
        for (Operator o : ops.values()) {
            int v = valutazioneComplessiva(o.getMatricola());
            if (v > max) {
                bestOps = new LinkedList<Operator>();
                bestOps.add(o);
                max = v;
            } else if (v == max) {
                bestOps.add(o);
            }
        }
        return bestOps.toArray(new Operator[0]);
    }
    
    private String getMatricola(Operator o)
    {
        return Integer.toString(o.getName().hashCode()) + 
        Integer.toString(o.getSurname().hashCode()) + 
        Integer.toString(o.getBirthDate().hashCode());
    }
}

class InvalidScoreException extends RuntimeException {}