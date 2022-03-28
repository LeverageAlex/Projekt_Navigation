import java.util.Comparator;

class Sortierungskriterium implements Comparator<Knoten>
{ 
    // Used for sorting in ascending order of 
    // roll number 
    public int compare(Knoten a, Knoten b) 
    { 
  
        return a.getDistanz() - b.getDistanz(); 
    } 
} 