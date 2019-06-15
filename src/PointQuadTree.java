/**
 * Asıl ağacımızın oluşturulduğu ve aramaların yapıldığı sınıf
 */
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class PointQuadTree 
{
	Node root = null;;
	private final int KARE_KENARI = 512;
	
	/**
	 * Point Quad Tree'ye nokta ekleme
	 * @param x eklenecek noktanin x koordinati
	 * @param y eklenecek noktanin y koordinati
	 */
	public void noktaEkle( double x, double y)
	{
		// ilk eklenen nokta root oluyor
		if( root == null)
		{
			root = new Node(x, y, 0, 0, KARE_KENARI, KARE_KENARI);
		}
		else
                {
		Node eklenecekNode = root;
		Node parentNode = root;
		while( true)
		{
			// Kuzey bati durumu
			if( x <= eklenecekNode.getX() && y <= eklenecekNode.getY())
			{
				parentNode = eklenecekNode;
				eklenecekNode = eklenecekNode.KB;
				if( eklenecekNode == null)
				{
					// Parent node'un kuzey batisinda kalan dortgenin degerleri
					double dortgenX = parentNode.getDortgen().getX();
					double dortgenY = parentNode.getDortgen().getY();
					double dortgenW = parentNode.getX() - dortgenX;
					double dortgenH = parentNode.getY() - dortgenY;
					parentNode.KB = new Node(x, y, dortgenX, dortgenY, dortgenH, dortgenW);
					return;
				}
			}
			// Kuzey dogu durumu
			else if( x >= eklenecekNode.getX() && y <= eklenecekNode.getY())
			{
				parentNode = eklenecekNode;
				eklenecekNode = eklenecekNode.KD;
				if( eklenecekNode == null)
				{
					// Parent node'un kuzey dogusunda kalan dortgenin degerleri
					double dortgenX = parentNode.getX();
					double dortgenY = parentNode.getDortgen().getY();
					double dortgenW = parentNode.getDortgen().getWidth() - (parentNode.getX() - parentNode.getDortgen().getX());
					double dortgenH = parentNode.getY() - parentNode.getDortgen().getY();
					parentNode.KD = new Node(x, y, dortgenX, dortgenY, dortgenH, dortgenW);
					return;
				}
			}
			// Guney dogu durumu
			else if( x >= eklenecekNode.getX() && y >= eklenecekNode.getY())
			{
				parentNode = eklenecekNode;
				eklenecekNode = eklenecekNode.GD;
				if( eklenecekNode == null)
				{
					// Parent node'un guney dogusunda kalan dortgenin degerleri
					double dortgenX = parentNode.getX();
					double dortgenY = parentNode.getY();
					double dortgenW = parentNode.getDortgen().getWidth() - (parentNode.getX() - parentNode.getDortgen().getX());
					double dortgenH = parentNode.getDortgen().getHeight() - (parentNode.getY() - parentNode.getDortgen().getY());
					parentNode.GD = new Node(x, y, dortgenX, dortgenY, dortgenH, dortgenW);
					return;
				}
					
			}
			// Guney bati durumu
			else if( x <= eklenecekNode.getX() && y >= eklenecekNode.getY())
			{
				parentNode = eklenecekNode;
				eklenecekNode = eklenecekNode.GB;
				if( eklenecekNode == null)
				{
					// Parent node'un guney batisinda kalan dortgenin degerleri
					double dortgenX = parentNode.getDortgen().getX();
					double dortgenY = parentNode.getY();
					double dortgenW = parentNode.getX() - parentNode.getDortgen().getX();
					double dortgenH = parentNode.getDortgen().getHeight() - (parentNode.getY() - parentNode.getDortgen().getY());
					parentNode.GB = new Node(x, y, dortgenX, dortgenY, dortgenH, dortgenW);
					return;
				}
			}
                }
			
			
                }
		
	}
	
	/**
	 * Point Quad Tree'nin elemanlarini gezme
	 * Agacin doğru çalışıp çalışmadığını kontrol için
	 * @param n baslangic node
	 */
	public void traverse(Node n)
	{
		if(n != null)
		{
			System.out.println( n);
			traverse(n.KB);
			traverse(n.KD);
			traverse(n.GD);
			traverse(n.GB);
		}
	}
	
	/**
	 * Point Quad Tree de verilen dairenin (elips) icinde bulunan noktalari bulma
	 * @param startX dairein dis teget dortgeninin sol ust kosesinin x koordinati
	 * @param startY dairein dis teget dortgeninin sol ust kosesinin y koordinati
	 * @param endX dairein dis teget dortgeninin sag alt kosesinin x koordinati
	 * @param endY dairein dis teget dortgeninin sag alt kosesinin y koordinati
	 * @return Bulunan nodelar ArrayList icinde gonderiliyor
	 */
	public ArrayList<Node> search(int startX, int startY, int endX, int endY)
	{
		Ellipse2D.Double elips = new Ellipse2D.Double( startX, startY, endX - startX, endY - startY);
		return search(elips, root);
	}
	
	/**
	 * Point Quad Tree de verilen dairenin (elips) icinde bulunan noktalari bulma
	 * @param elips arastirilacak daire (elips)
	 * @param n aranan node
	 * @return Bulunan nodelar ArrayList icinde gonderiliyor
	 */
	private ArrayList<Node> search( Ellipse2D.Double elips, Node n)
	{
		ArrayList<Node> result = new ArrayList<>();
		//Eğer kontrol edilecek node null ise aramayı bitir
		if( n == null)
			return result;
		else
		{
			// Eğer daire noktanın içinde bulunduğu dikdörtgenle kesişiyorsa noktanın 4 çocuğuyla aramaya devam et
			if( elips.intersects( n.getDortgen()))
			{
				// eğer nokta dairenin içindeyse noktayı sonuç listesine ekle
				if( elips.contains( n.getX(), n.getY()))
				{
					result.add( n);
				}
				result.addAll( search( elips, n.KB));
				result.addAll( search( elips, n.KD));
				result.addAll( search( elips, n.GB));
				result.addAll( search( elips, n.GD));
			}
			return result;
		}
		
	}
}
