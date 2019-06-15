/**
 * Ağacımızın çizildiği panel
 * Daha sonra PointQuadTreeFrame içinde kullanılacak
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PointQuadTreePanel extends JPanel
{
	PointQuadTree pqt = new PointQuadTree();
	Graphics2D canvas;
	int startX, startY, endX, endY;
	boolean noktaEklensin = true;
	boolean daireSec = false;
	ArrayList<Node> icerdekiNodelar = new ArrayList<>();
	
	public PointQuadTreePanel() 
	{
		baslangic();
		//randomNoktaEkle();
		addMouseListener( new MouseDinleyici());
		addMouseMotionListener( new MouseHareketDinleyici());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		canvas = (Graphics2D)g;
		// (0,0)-(512,512) arasi kare cizme
		canvas.drawRect(0, 0, 512, 512);
		// bu kare icine nodelari cizme
		paint(pqt.root, canvas);
		// mouse ile belirlenen daireyi cizme
		canvas.drawOval(Math.min( startX, endX), Math.min( startY, endY), Math.abs(endX-startX), Math.abs(endY-startY));
		// daire icindeki noktalari cizme
		for( Node n : icerdekiNodelar)
		{
			canvas.setColor( new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256)));
			canvas.fill( new Ellipse2D.Double(n.getX()-4, n.getY()-4, 8, 8));
		}
	}
	
	/**
	 * Rekürsif olarak nodelari cizme
	 * @param n baslangic node
	 * @param g cizim icin gerekli olan Graphics2D objesi
	 */
	public void paint(Node n, Graphics2D g)
	{
		if(n != null)
		{
			// random renk seç
			//g.setColor( new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256)));
                        g.setColor( Color.BLUE);			

                        // noktadan geçen y eksenine paralel doğruyu çizme
			g.draw( new Line2D.Double(n.getX(), n.getDortgen().getY(), 
					n.getX(), n.getDortgen().getY() + n.getDortgen().getHeight()));
			// noktadan geçen x eksenine paralel doğruyu çizme
			g.draw( new Line2D.Double( n.getDortgen().getX(), n.getY(), 
					n.getDortgen().getX() + n.getDortgen().getWidth(), n.getY()));
			// noktayı çizmek için siyah kullan
			g.setColor( Color.BLACK);
			//noktayı çiz
			g.fill( new Ellipse2D.Double(n.getX()-2.5, n.getY()-2.5, 5, 5));
			//4 çocukla devam et
			paint(n.KB, g);
			paint(n.KD, g);
			paint(n.GD, g);
			paint(n.GB, g);
		}
	}
	
	/**
	 * Point Quad Tree'ye random olarak 50 nokta ekleme
	 */
	public void randomNoktaEkle()
	{
		for( int i = 0; i < 50; i++)
		{
			double x = Math.random()*512;
			double y = Math.random()*512;
			pqt.noktaEkle( x, y);
		}
	}
	
	/**
	 * mouse hareketlerini ve bulunan nodelari silme
	 */
	public void baslangic()
	{
		startX = 0;
		startY = 0;
		endX = 0;
		endY = 0;
		icerdekiNodelar.clear();
	}
	
	/**
	 * butun sistemi ve agaci silme
	 */
	public void temizle()
	{
		pqt = new PointQuadTree();
		baslangic();
	}
	
	/**
	 * Point Quad Tree de verilen dairenin (elips) icinde bulunan noktalari bulma
	 * @param startX dairein dis teget dortgeninin sol ust kosesinin x koordinati
	 * @param startY dairein dis teget dortgeninin sol ust kosesinin y koordinati
	 * @param endX dairein dis teget dortgeninin sag alt kosesinin x koordinati
	 * @param endY dairein dis teget dortgeninin sag alt kosesinin y koordinati
	 * @return Bulunan nodelar ArrayList icinde gonderiliyor
	 */
	public void search(int startX, int startY, int endX, int endY)
	{
		icerdekiNodelar = pqt.search(startX, startY, endX, endY);
	}
	
	
	public int icerdekilerSayisi()
	{
		return icerdekiNodelar.size();
	}

	public boolean noktaEklensinmi() {
		return noktaEklensin;
	}

	public void setNoktaEklensin() {
		this.noktaEklensin = true;
		this.daireSec = false;
	}

	public boolean daireSecilsinmi() {
		return daireSec;
	}

	public void setDaireSec() {
		this.daireSec = true;
		this.noktaEklensin = false;
	}
	
	public ArrayList<Node> getIcerdekiNodelar() {
		return icerdekiNodelar;
	}
	
	private class MouseDinleyici extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if( noktaEklensin)
			{
				int x = e.getX();
				int y = e.getY();
				// eğer seçilen nokta belirlenen sınırlar içindeyse ağaca ekle
				if( x >= 0 && x <= 512 && y >= 0 && y <= 512)
				{
					pqt.noktaEkle( x, y);
					repaint();
				}
				
			}
		}
		
		public void mousePressed(MouseEvent e) 
		{
			// dairenin başlangıç noktasını belirle
			if ( daireSec) 
			{
				startX = e.getX();
				startY = e.getY();
				endX = e.getX();
				endY = e.getY();
				repaint();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			// mouse bırakıldığında dairenin çizimini tamamla ve daire içindeki noktaları bul
			if( daireSec)
			{
				endX = e.getX();
				endY = e.getY();
				icerdekiNodelar = pqt.search( Math.min( startX, endX), Math.min( startY, endY), 
						Math.max( startX, endX), Math.max( startY, endY));
				repaint();
			}
		}
	}
	
	private class MouseHareketDinleyici extends MouseMotionAdapter
	{
		public void mouseDragged(MouseEvent e) 
		{
			// mouse hareket ederken dairenin boyutlarını değiştir ve yeni daireye göre arama yap
			if( daireSec)
			{
				endX = e.getX();
				endY = e.getY();
				icerdekiNodelar = pqt.search( Math.min( startX, endX), Math.min( startY, endY), 
						Math.max( startX, endX), Math.max( startY, endY));
				repaint();
			}
        	
                }
	}
}
