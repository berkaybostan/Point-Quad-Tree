/**
 * Kullanıcı ara yüzünün frame içine alındığı sınıf
 */
import java.awt.BasicStroke;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

public class PointQuadTreeFrame extends JFrame
{
	JButton randomNoktaButon, temizleButon, listeleButon;
	JLabel toplamIcerdekilerLabel;
	JPanel radioPanel, scrollPanel;
	JScrollPane scroll;
	PointQuadTreePanel pqtPanel;
	
	public PointQuadTreeFrame() 
	{
		super("Point Quad Tree");
		//Elemanlarin koordinatlari elle girebilmek icin layoutu null seciyoruz
		setLayout( null);
		
		// Cizimlerin olacagi ana panel
		pqtPanel = new PointQuadTreePanel();
		pqtPanel.setBounds(0, 0, 513, 513);
		add( pqtPanel);
		
		// Daire çizmek ve nokta eklemek arasında seçim yapmak için JRadioButton
		JRadioButton noktaEkleButon = new JRadioButton( "Nokta ekle");
		JRadioButton daireSecButon = new JRadioButton( "Daire sec");
		noktaEkleButon.setSelected( true);
		noktaEkleButon.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pqtPanel.setNoktaEklensin();
				pqtPanel.baslangic();
				repaint();
			}
		});
		daireSecButon.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pqtPanel.setDaireSec();
				pqtPanel.baslangic();
				repaint();
			}
		});
	
        // JRadioButton elemanlarından aynı anda sadece birini seçebilmek için ButtonGroup içine alıyoruz
	ButtonGroup bg = new ButtonGroup();
	bg.add( noktaEkleButon);
	bg.add( daireSecButon);
	
        //JRadioButtonlar için panel oluşturuyoruz
	radioPanel = new JPanel(new GridLayout(1, 0));
        radioPanel.add(daireSecButon);
        radioPanel.add(noktaEkleButon);
        
        //Bu panelin sınırlarını çiziyoruz
        radioPanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.5f)));
        radioPanel.setBounds(550, 20, 200, 50);
        add(radioPanel);
        
        // Random nokta eklemek için JButton
        randomNoktaButon = new JButton( "Random nokta ekle");
        randomNoktaButon.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				pqtPanel.baslangic();
				pqtPanel.randomNoktaEkle();
				toplamIcerdekilerLabel.setText("Daire icindeki node sayisi: 0");
				scrollPanel.removeAll();
				repaint();
			}
		});
        randomNoktaButon.setBounds( 550, 80, 200, 30);
        add(randomNoktaButon);
        
        //Ağaçtaki noktaları temizlemek için JButton
        temizleButon = new JButton( "Temizle");
        temizleButon.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				pqtPanel.temizle();
				toplamIcerdekilerLabel.setText( "Daire icindeki node sayisi: 0");
				scrollPanel.removeAll();
				repaint();
			}
		});
        temizleButon.setBounds( 550, 120, 200, 30);
        add( temizleButon);
        
        //Arama sonuçlarını bastırmak için JButton
        listeleButon = new JButton( "Listele");
                listeleButon.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
                            scrollPanel.removeAll();
                            toplamIcerdekilerLabel.setText( "Daire icindeki node sayisi: " + pqtPanel.icerdekilerSayisi());
                            ArrayList<Node> icerdekiler = pqtPanel.getIcerdekiNodelar();
                            // noktaları sırala önce x değerlerine göre sonra y değerlerine göre
                            Collections.sort( icerdekiler);
                            for( Node node : icerdekiler)
		        {
		        	scrollPanel.add( new JLabel( node.toString()));
		        }
				scrollPanel.revalidate();
				scrollPanel.repaint();
			}
		});
        listeleButon.setBounds( 550, 160, 200, 30);
        add( listeleButon);
        
        //Arama sonucundaki noktaların sayısını belirtmek için JLabel
        toplamIcerdekilerLabel = new JLabel( "Daire icindeki node sayisi: 0");
        toplamIcerdekilerLabel.setBounds( 550, 200, 200, 30);
        add( toplamIcerdekilerLabel);
        
        // Arama sonucundaki noktaları sıralamak için scrollPanel. Kaç tane nokta olacağını bilmediğimiz için JscrollPanel kullandım
        // Arama sonuçları şu şekildedir:
        // 1. root node
        // 2. Kuzey batı çocuğu (rekürsif olarak)
        // 3. Kuzey doğu çocuğu (rekürsif olarak)
        // 4. Güney batı çocuğu (rekürsif olarak)
        // 5. Güney doğu çocuğu (rekürsif olarak)
        scrollPanel = new JPanel();
        //scroll içine yukarıdan aşağıya eleman ekleme
        scrollPanel.setLayout( new BoxLayout( scrollPanel, BoxLayout.Y_AXIS));
        scroll = new JScrollPane( scrollPanel);
        scroll.setBounds( 550, 240, 200, 270);
        add(scroll);
        
	}
	
	public static void main(String[] args) 
	{
		PointQuadTreeFrame f = new PointQuadTreeFrame();
		f.setResizable( false);
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setVisible( true);
	}
}
