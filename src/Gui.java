import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import org.jfree.ui.tabbedui.VerticalLayout;


public class Gui extends JFrame {
	private static final long serialVersionUID = 1L;
	public  static JFrame jFrame;
	public  static JButton  filtrMenu, journalMenu;
	public  static JDialog  setDialog, journalDialog;
    public  static JTextArea textArea_journal;
    public  static JScrollPane  scrollPane_journal;
    public  static JCheckBox chkBox_journal;
    public  static JLabel Val1,Val2;
    public  static JTextField bal11,bal12,bal13;
    public  static JTextField bal21,bal22,bal23;
    public  static JTextField bal1,bal2,bal3;
    public  static JLabel timer,proc0,proc1;
	public static JTable Table;
	public static JScrollPane scrollPane;
	public static TableModel tModel;
	public static int lastWidth = 600, lastHeight = 600;
	public static JLabel label;
    static Font font2 = new Font("Arial", Font.PLAIN, 10);
    static Color textcolor = Color.BLACK;
    
    
    public Gui() {
		jFrame = new JFrame();
		jFrame.setTitle("Aver spread on Binance.com");
		jFrame.setLayout(new VerticalLayout());
		jFrame.setMinimumSize(new Dimension(600, 600));
		//jFrame.setResizable(false);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//создаем окно журнала
		journalDialog = new JDialog(jFrame,"Журнал");
		journalDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		journalDialog.setSize(510, 350);
		journalDialog.setLocation(100, 100);
		journalDialog.setVisible(false);
		JPanel journal = new JPanel();
        journal.setLayout(null);
        textArea_journal = new JTextArea();
        textArea_journal.setCaretPosition(0);
	    //textArea.setFont(font_2);
        textArea_journal.setForeground(Color.BLACK);
	    scrollPane_journal = new JScrollPane(textArea_journal);
	    scrollPane_journal.setSize(485, 280);
	    scrollPane_journal.setLocation(0,0);
	    scrollPane_journal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane_journal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    chkBox_journal = new JCheckBox("Auto scroll",false);
	    chkBox_journal.setSize(100,20);
	    chkBox_journal.setLocation(0,285);
	    chkBox_journal.setSelected(true);
	    PrintStream printStream = null;
		try {
			printStream = new PrintStream(new CustomOutputStream(textArea_journal),true,"cp1251");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    System.setOut(printStream);
	    System.setErr(printStream);
	    journal.add(scrollPane_journal);
	    journal.add(chkBox_journal);
	    journalDialog.add(journal);
		
	    //меню
	    JMenuBar menuBar = new JMenuBar();
		jFrame.setJMenuBar(menuBar);
		journalMenu = new JButton("Журнал");
		journalMenu.setBorderPainted(false);
		journalMenu.setFocusPainted(false);
		filtrMenu = new JButton("Фильтр");
		filtrMenu.setBorderPainted(false);
		filtrMenu.setFocusPainted(false);
		menuBar.add(journalMenu);menuBar.add(filtrMenu);
		
		//фильтр
		GridLayout layout = new GridLayout(3,5);
		JPanel filtr = new JPanel(layout);
		SetBorder(filtr,"фильтр");
		filtr.setPreferredSize(new Dimension(450, 80));
		JLabel filtrLabel2 = CreateLabel("max", font2, 10,10, 100, 20, textcolor);
		JLabel filtrLabel3 = CreateLabel("min", font2, 10,10, 100, 20, textcolor);
		JLabel filtrHead0 = CreateLabel("Price", font2, 10,10, 100, 20, textcolor);
		JTextField filtrMax0 = CreateTextField(String.format("%.8f",SR.maxPrice),10, 10,10, 100, 20, textcolor, font2);
		JTextField filtrMin0 = CreateTextField(String.format("%.8f",SR.minPrice),10, 10,10, 100, 20, textcolor, font2);
		JLabel filtrHead1 = CreateLabel("Aver", font2, 10,10, 100, 20, textcolor);
		JTextField filtrMax1 = CreateTextField(String.valueOf(SR.maxAver),10, 10,10, 100, 20, textcolor, font2);
		JTextField filtrMin1 = CreateTextField(String.valueOf(SR.minAver),10, 10,10, 100, 20, textcolor, font2);
		JLabel filtrHead2 = CreateLabel("Volume", font2, 10,10, 100, 20, textcolor);
		JTextField filtrMax2 = CreateTextField(String.valueOf(SR.maxVolume),10, 10,10, 100, 20, textcolor, font2);
		JTextField filtrMin2 = CreateTextField(String.valueOf(SR.minVolume),10, 10,10, 100, 20, textcolor, font2);
		JLabel filtrHead3 = CreateLabel("Orders", font2, 10,10, 100, 20, textcolor);
		JTextField filtrMax3 = CreateTextField(String.valueOf(SR.maxCount),10, 10,10, 100, 20, textcolor, font2);
		JTextField filtrMin3 = CreateTextField(String.valueOf(SR.minCount),10, 10,10, 100, 20, textcolor, font2);
		JLabel filtrHead4 = CreateLabel("Change", font2, 10,10, 100, 20, textcolor);
		JTextField filtrMax4 = CreateTextField(String.valueOf(SR.maxChange),10, 10,10, 100, 20, textcolor, font2);
		JTextField filtrMin4 = CreateTextField(String.valueOf(SR.minChange),10, 10,10, 100, 20, textcolor, font2);
		JButton but = CreateButton("SET", textcolor, Color.yellow);
		
		
		filtr.add(but);filtr.add(filtrHead0);filtr.add(filtrHead1);filtr.add(filtrHead2);filtr.add(filtrHead3);filtr.add(filtrHead4);
		filtr.add(filtrLabel2);filtr.add(filtrMax0);filtr.add(filtrMax1);filtr.add(filtrMax2);filtr.add(filtrMax3);filtr.add(filtrMax4);
		filtr.add(filtrLabel3);filtr.add(filtrMin0);filtr.add(filtrMin1);filtr.add(filtrMin2);filtr.add(filtrMin3);filtr.add(filtrMin4);
		
		filtr.setVisible(false);
		jFrame.add(filtr,BorderLayout.CENTER);
        
		label = CreateLabel("Всего : 0     Показано : 0", font2, 10,10, 100, 20, textcolor);
		jFrame.add(label);
		
		//таблица
		//JPanel table = new JPanel(new VerticalLayout());
		//table.setPreferredSize(new Dimension(600, 600));
		tModel = new TableModel();
        Table = new JTable(tModel);
        Table.setAutoCreateRowSorter( true );
        scrollPane = new JScrollPane(Table);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        //scrollPane.setMinimumSize(new Dimension(600, 600));
        //scrollPane.setLocation(5,0);
        //panel.add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //table.add(scrollPane);
        jFrame.add(scrollPane);

		jFrame.pack();
		jFrame.setVisible(true);
		
		
		journalMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				journalDialog.setVisible(true);
			    journalDialog.addComponentListener(new java.awt.event.ComponentAdapter() {
			        public void componentResized(java.awt.event.ComponentEvent evt) {
			        	int xH = journalDialog.getWidth();
			        	int yH = journalDialog.getHeight();
			        	scrollPane_journal.setSize(485+xH-500,280+yH-350);
			        	chkBox_journal.setLocation(0,285+yH-350);
			         }
			    });
				}
			});
		filtrMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filtr.isVisible())filtr.setVisible(false);
				else filtr.setVisible(true);

				}
			});
		but.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SR.minPrice = ND(filtrMin0.getText(),8);
				SR.maxPrice = ND(filtrMax0.getText(),8);
				SR.minAver = ND(filtrMin1.getText(),2);
				SR.maxAver = ND(filtrMax1.getText(),2);
				SR.minVolume = ND(filtrMin2.getText(),2);
				SR.maxVolume = ND(filtrMax2.getText(),2);
				SR.minCount = ND(filtrMin3.getText(),0);
				SR.maxCount = ND(filtrMax3.getText(),0);
				SR.minChange = ND(filtrMin4.getText(),2);
				SR.maxChange = ND(filtrMax4.getText(),2);
				//фильтр
				SR.PairsTab.clear();
				int size = SR.Pairs.size();
				Quote quot = new Quote();
				for(int i = 0; i < size; i++) {
					quot = SR.Pairs.get(i);
					//SR.Print(i+"  "+quot.orders, 1);
					if(quot.ask < SR.minPrice || quot.ask > SR.maxPrice ||
					   quot.bid < SR.minPrice || quot.bid > SR.maxPrice ||
					   quot.aver < SR.minAver || quot.aver > SR.maxAver ||
					   quot.volume < SR.minVolume || quot.volume > SR.maxVolume ||
					   quot.orders/24 < SR.minCount || quot.orders/24 > SR.maxCount ||
					   quot.change < SR.minChange || quot.change > SR.maxChange) continue;
					SR.PairsTab.add(quot);
				}
				Gui.label.setText("Всего : "+SR.Pairs.size()+"     Показано : "+SR.PairsTab.size());
				
				Gui.tModel.fireTableDataChanged();
			}
			});
		
	}
  
    public static JLabel CreateLabel(String txt, Font fnt, int i, int j, int k, int l, Color col)
    {
    	JLabel lab;
    	lab = new JLabel(txt);
        lab.setFont(fnt);
        lab.setSize(k, l);
        lab.setLocation(i,j);
        lab.setForeground(col);
        lab.setHorizontalAlignment(JLabel.CENTER);
    	return lab ;
    }
    public JTextField CreateTextField(String txt, int kol, int i, int j, int k, int l, Color col,Font font)
    {
    	JTextField lab;
    	lab = new JTextField(txt,kol);
        lab.setSize(k, l);
        lab.setLocation(i,j);
        lab.setForeground(col);
        lab.setFont(font);
        lab.setHorizontalAlignment(JTextField.CENTER);
        //lab.setEditable(false);
    	return lab ;
    }
    public static JButton CreateButton(String txt, Color col,Color bg)
	  {
	  	JButton lab;
	  	lab = new JButton(txt);
	    lab.setForeground(col);
	    lab.setBackground(bg);
	    return lab ;
	  }
    public static double ND(double x, int dig) {
		  String str = String.format("%."+dig+"f",x);
		  return Double.valueOf(str.replace(" ", "").replace(",", "."));
	  }
	 public static double ND(String x, int dig) {
		  Double x1 = Double.valueOf(x.replace(" ", "").replace(",", "."));
		  return ND(x1,dig);
	  }
	  public static void SetBorder (JPanel pan,String name)
	    {
	    	TitledBorder border = new TitledBorder(name);
	        border.setTitleJustification(TitledBorder.CENTER);
	        border.setTitlePosition(TitledBorder.TOP);
	        pan.setBorder(border);
	    }


}
@SuppressWarnings("serial")
class TableModel extends AbstractTableModel
{
    // Количество строк
    @Override
    public int getRowCount() {
        return SR.PairsTab.size();
    }
    // Количество столбцов
    @Override
    public int getColumnCount() {
        return 10;
    }
    // Тип хранимых в столбцах данных
    @Override
    public Class<?> getColumnClass(int column) {
    	return getValueAt(0, column).getClass();
    }
    // Функция определения данных ячейки
    @Override
    public Object getValueAt(int row, int column)
    {
    	if(SR.PairsTab.size() == 0)return"";
    	// Данные для стобцов
        switch (column) {
        case 0:
            return SR.PairsTab.get(row).symbol;
        case 1:
            return String.format("%4.8f",SR.PairsTab.get(row).ask);
        case 2:
            return String.format("%4.8f",SR.PairsTab.get(row).bid);
        case 3:
            return SR.PairsTab.get(row).spread;
        case 4:
            return SR.PairsTab.get(row).aver;
        case 5:
            return SR.PairsTab.get(row).volume;
        case 6:
            return SR.PairsTab.get(row).orders/24;
        case 7:
            return SR.PairsTab.get(row).change;
        case 8:
            return SR.PairsTab.get(row).place;
        case 9:
            return SR.PairsTab.get(row).kol;
        default:
            return "";
        }
    }
    
    @Override
    public String getColumnName(int c) {
        String result = "";
        switch (c) {
            case 0:
            	result = "Symbol";
                break;
            case 1:
                result = "Ask";
                break;
            case 2:
                result = "Bid";
                break;
            case 3:
                result = "Spread,%";
                break;
            case 4:
                result = "Aver,%";
                break;
            case 5:
                result = "Volume,BTC";
                break;
            case 6:
                result = "Orders/h";
                break;
            case 7:
                result = "Change,%";
                break;
            case 8:
                result = "Place";
                break;
            case 9:
                result = "Count";
                break;
            }
        return result;
    }
}
