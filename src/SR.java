import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class SR {
	
	public static final String Exchange = "https://api.binance.com";
	public static int Interval = 10;
	public static ArrayList<Quote>  Pairs = new ArrayList<Quote>();
	public static ArrayList<Quote>  PairsTab = new ArrayList<Quote>();
	public static long LastTimeAPI,LastStat = 0;
	public static boolean Debug = true;
	public static double BidETH = 0,BidBNB = 0,BidUSDT = 0;
	public static double minPrice = 0.00000000,maxPrice = 10000;
	public static double minAver = 0.0,maxAver = 100;
	public static double minVolume = 0.0,maxVolume = 10000;
	public static double minCount = 0,maxCount = 10000;
	public static double minChange = -100,maxChange = 100;
	public static boolean chkSet = false,chkInfo = false;
	
    public static void main(String[] args) throws IOException, InterruptedException {
		
		//создаем интерфейс		
		new Gui();
		
		Work(1);

		//основной цикл
		while(true){
			Work(10);
		}
	}
	
    public static void Print(String txt,int mode){
    	if(mode > 0) {
    		if(mode == 1)System.out.println(txt);
    		if(mode == 2)System.out.println("[BUY] "+txt);
    		if(mode == 3)System.out.println("[SELL] "+txt);
    	}
    }
    
    public static void Work(int interval) throws IOException, InterruptedException {
    	Interval = interval;
		Date d = new Date();
		//получаем котировки
		BinanceAPI.getRate("");
		//получаем статистику
		if(d.getTime() - LastStat >= 10*60*1000) {
		  BinanceAPI.getStat("");
		  LastStat = d.getTime();
		}
		//получаем значения процента 1 пункта к цене
		if(!chkInfo) {
			BinanceAPI.getInfo();
			chkInfo = true;
		}
		//расчет поинтов
		GetPoint();
		//фильтр
		PairsTab.clear();
		int size = Pairs.size();
		Quote quot = new Quote();
		for(int i = 0; i < size; i++) {
			quot = Pairs.get(i);
			//SR.Print(i+"  "+quot.orders, 1);
			if(quot.ask < minPrice || quot.ask > maxPrice ||
			   quot.bid < minPrice || quot.bid > maxPrice ||
			   quot.aver < minAver || quot.aver > maxAver ||
			   quot.volume < minVolume || quot.volume > maxVolume ||
			   quot.orders/24 < minCount || quot.orders/24 > maxCount ||
			   quot.change < minChange || quot.change > maxChange) continue;
			PairsTab.add(quot);
		}
		Gui.label.setText("Всего : "+Pairs.size()+"     Показано : "+PairsTab.size());
		
		Gui.tModel.fireTableDataChanged();
		Thread.sleep(100);
    }
    
    public static void GetPoint() {
    	int size = Pairs.size();
    	for(int i=0;i<size;i++) {
    		Pairs.get(i).point = GetPlace(Pairs.get(i).aver,"aver",size)*2;
    		Pairs.get(i).point *= GetPlace(Pairs.get(i).volume,"volume",size);
    		Pairs.get(i).point *= GetPlace(Pairs.get(i).orders,"orders",size)*3;
    		Pairs.get(i).point *= GetPlace(Pairs.get(i).change,"change",size)/2;
    		Pairs.get(i).point *= GetPlace(Pairs.get(i).ask,"ask",size)/10;
    		Pairs.get(i).point *= GetPlace(Pairs.get(i).bid,"bid",size)/10;
    		Pairs.get(i).point *= GetPlace(Pairs.get(i).spread,"spread",size)/5;
    		Pairs.get(i).point *= GetPlace(Pairs.get(i).tickproc,"tickproc",size)*2;
    	}
    	for(int i=0;i<size;i++) {
    		Pairs.get(i).place = GetPlace(Pairs.get(i).point,"point",size);
    	}
    }
    public static int GetPlace(double x,String kom,int size) {
    	int summ = 0;
    	for(int i1=0;i1<size;i1++) {
    		if(kom.equals("aver") && Pairs.get(i1).aver < x)summ++;
    		if(kom.equals("ask") && Pairs.get(i1).ask < x)summ++;
    		if(kom.equals("bid") && Pairs.get(i1).bid < x)summ++;
    		if(kom.equals("spread") && Pairs.get(i1).spread < x)summ++;
    		if(kom.equals("volume") && Pairs.get(i1).volume < x)summ++;
    		if(kom.equals("orders") && Pairs.get(i1).orders < x)summ++;
    		if(kom.equals("change") && Math.abs(Pairs.get(i1).change) > x)summ++;
    		if(kom.equals("point") && Pairs.get(i1).point > x)summ++;
    		if(kom.equals("tickproc") && Pairs.get(i1).tickproc > x)summ++;
    	}
    	return summ;
    }
}
class Quote {
	public String symbol;
	public double ask;
	public double bid;
	public double spread;
	public double aver;
	public double volume;
	public int orders;
	public int kol;
	public double change;
	public long point;
	public int place;
	public double tickproc;
}
