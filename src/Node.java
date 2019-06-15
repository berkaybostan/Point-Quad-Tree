/**
 * Node sınıfı agactaki noktaları ifade etmek için kullanılacak
 */
import java.awt.geom.Rectangle2D;

public class Node implements Comparable<Node>
{
	
	private double x, y;
	private Rectangle2D.Double dortgen;
	Node KD, KB, GD, GB;
	
	
	/**
	 * Node sinifi icin constructor
	 * @param x x koordinati
	 * @param y y koordinati
	 * @param dortgenX noktanin icinde bulundugu dortgenin sol ust noktasinin x koordinati
	 * @param dortgenY noktanin icinde bulundugu dortgenin sol ust noktasinin y koordinati 
	 * @param dortgenH noktanin icinde bulundugu dortgenin yuksekligi
	 * @param dortgenW noktanin icinde bulundugu dortgenin genisligi
	 */
	public Node( double x, double y, double dortgenX, double dortgenY, double dortgenH, double dortgenW)
	{
		this.x = x;
		this.y = y;
		this.dortgen = new Rectangle2D.Double(dortgenX, dortgenY, dortgenW, dortgenH);
	}

	@Override
	public String toString() {
		return String.format( "x = %.2f  y = %.2f", x, y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Rectangle2D.Double getDortgen() {
		return dortgen;
	}

    @Override
        public int compareTo(Node n) 
        {
            if( x < n.getX())
            return -1;
            
            else if( x > n.getX())
            return 1;
            
            else
            {   
                if( y < n.getY())
                return -1;
            
                else if( y > n.getY())
                return 1;
            
                else
                return 0;
            }
        }
		
}
