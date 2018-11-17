
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.stream.JsonReader;

public class BinanceAPI
{
	public String userAgent = "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0";
    public String requestUrl = "";
    public static String lastResponse = "";
	public static SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss.SSS");
	public static long LastPost = 0;
    public String apiKey = "";
    public String secretKey = "";
    static TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
        }
    };
    public Map<String, String> headers = new HashMap<>();



/////////////////////////////////////////////////////////////////////////
//публичное API
	private static JsonReader getRequest(String url)	{
	    HttpsURLConnection con = null;
		try {
			IntervalFunck();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		SR.LastTimeAPI = new Date().getTime();
		try		{
			URL obj = new URL(url);
			if(SR.Debug)SR.Print(format1.format(new Date())+"  "+url,1);
			con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; JAVA AWT)");
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.connect();
			return new JsonReader(new InputStreamReader(con.getInputStream()));
		}
		catch (Exception e)	{}
		return null;
	}
/////////////////////////////////////////////////////////////////////////
//получаем котировки
public static void getTime() throws IOException{
    JsonReader reader = getRequest(SR.Exchange+"/api/v1/time");
    if(reader == null) {SR.Print("err1", 1);SR.LastTimeAPI = 0;return;}
    reader.setLenient(true);
    if(!reader.hasNext() || reader.toString().contains("Ddos")) {SR.Print("err2 : "+reader.toString(), 1);SR.LastTimeAPI = 0;reader.close(); return;}
    reader.beginObject();
    String name = reader.nextName();
    String time = reader.nextString();
	reader.endObject();
	reader.close();
	SR.Print(name+"  "+time+"  "+String.valueOf(new Date().getTime()),1);
	SR.LastTimeAPI = 0;
}
/////////////////////////////////////////////////////////////////////////
//получаем котировки для расчетов баланса
public static void getRate(String symbol) throws IOException{
	String str = SR.Exchange+"/api/v3/ticker/bookTicker";
	if(!symbol.equals(""))str += "?symbol="+symbol;
	JsonReader reader = getRequest(str);
	//for(int i=0;i<SR.Pairs.size();i++)SR.Print(SR.Pairs.get(i), 1);
	if(reader == null) {SR.LastTimeAPI = 0;return;}
	reader.setLenient(true);
	if(!reader.hasNext() || reader.toString().contains("Ddos")) {SR.Print(reader.toString(), 1);SR.LastTimeAPI = 0;reader.close(); return;}
	reader.beginArray();
	while (reader.hasNext()){
		reader.beginObject();
		String pair = "";
	    Quote quot = new Quote();
        int nom = -1;
		while (reader.hasNext()){
				String name = reader.nextName();
				if (name.equals("code")){
					SR.Print("err : "+reader.nextString(),1);
					while (reader.hasNext()){
					   reader.nextName();
					   reader.skipValue();
					  }
				}
				else if (name.contains("symbol")){
					pair = reader.nextString();
					nom = FindSymbol(pair);
					quot.symbol = pair;
					
				}
				else if (name.contains("bidPrice")){
					double r = reader.nextDouble();
					if(pair.equals("ETHBTC"))SR.BidETH = r;
					else if(pair.equals("BNBBTC"))SR.BidBNB = r;
					else if(pair.equals("BTCUSDT"))SR.BidUSDT = r;
					quot.bid = r;
				}
				else if (name.contains("askPrice")){
					double r = reader.nextDouble();
					quot.ask = r;
				}
				else reader.skipValue();
		}
		if(nom < 0) {
			quot.kol  = 1;
			if(quot.bid > 0)quot.spread = (quot.ask-quot.bid)/quot.bid*100;
			else quot.spread = 0;
			quot.aver = quot.spread;
			SR.Pairs.add(quot);
			
		}
		else {
			SR.Pairs.get(nom).ask = quot.ask;
			SR.Pairs.get(nom).bid = quot.bid;
			if(quot.bid > 0)SR.Pairs.get(nom).spread = (quot.ask-quot.bid)/quot.bid*100;
			else SR.Pairs.get(nom).spread = 0;
			SR.Pairs.get(nom).aver = (SR.Pairs.get(nom).aver*SR.Pairs.get(nom).kol + SR.Pairs.get(nom).spread)/(SR.Pairs.get(nom).kol+1); 
			SR.Pairs.get(nom).kol ++;
		}
		reader.endObject();
	}
	reader.endArray();
	reader.close();
	SR.LastTimeAPI = 0;
}

/////////////////////////////////////////////////////////////////////////
//получаем котировки для расчетов баланса
public static void getStat(String symbol) throws IOException{
	String str = SR.Exchange+"/api/v1/ticker/24hr";
	if(!symbol.equals(""))str += "?symbol="+symbol;
	JsonReader reader = getRequest(str);
	if(reader == null) {SR.LastTimeAPI = 0;return;}
	reader.setLenient(true);
	if(!reader.hasNext() || reader.toString().contains("Ddos")) {SR.Print(reader.toString(), 1);SR.LastTimeAPI = 0;reader.close(); return;}
	reader.beginArray();
	while (reader.hasNext()){
		reader.beginObject();
		String pair = "";
		int nom = -1;
		while (reader.hasNext()){
			String name = reader.nextName();
			if (name.equals("code")){
				SR.Print("err : "+reader.nextString(),1);
				while (reader.hasNext()){
					reader.nextName();
					reader.skipValue();
				}
			}
			else if (name.contains("symbol")){
				pair = reader.nextString();
				nom = FindSymbol(pair);
			}
			else if (name.contains("priceChangePercent")){
				double r = reader.nextDouble();
				if(nom >= 0)SR.Pairs.get(nom).change = r;
			}
			else if (name.contains("quoteVolume")){
				double r = reader.nextDouble();
				if(nom >= 0){
					if(pair.indexOf("USDT") > 1 && SR.BidUSDT > 0)SR.Pairs.get(nom).volume = r/SR.BidUSDT;
					else if(pair.indexOf("BNB") > 1)SR.Pairs.get(nom).volume = r*SR.BidBNB;
					else if(pair.indexOf("ETH") > 1)SR.Pairs.get(nom).volume = r*SR.BidETH;
					else SR.Pairs.get(nom).volume = r;
				}
			}
			else if (name.contains("count")){
				int r = reader.nextInt();
				if(nom >= 0)SR.Pairs.get(nom).orders = r;
			}
			else reader.skipValue();
		}
		reader.endObject();
	}
	reader.endArray();
	reader.close();
	SR.LastTimeAPI = 0;
}
/////////////////////////////////////////////////////////////////////////
//получаем инфо
public static void getInfo() throws IOException{
	JsonReader reader = getRequest(SR.Exchange+"/api/v1/exchangeInfo");
	if(reader == null) {SR.LastTimeAPI = 0;return;}
	reader.setLenient(true);
	if(!reader.hasNext() || reader.toString().contains("Ddos")) {SR.LastTimeAPI = 0;reader.close(); return;}
	int nom = -1;
	try {
		reader.beginObject();
		while (reader.hasNext()){
			String name = reader.nextName();
			if (name.equals("code")){
				SR.Print(reader.nextString(),1);
				while (reader.hasNext()){
					reader.nextName();
					reader.skipValue();
				}
			}
			else if (name.equals("symbols")){
				reader.beginArray();
				while (reader.hasNext()){
					reader.beginObject();
					String kotir = "";
					nom = -1;
					while (reader.hasNext()){
						String name1 = reader.nextName();
						if(name1.equals("symbol")) {
							kotir = reader.nextString();
							nom = FindSymbol(kotir);
						}
						else if (name1.equals("filters") && nom >= 0){
							reader.beginArray();
							while (reader.hasNext()) {
								reader.beginObject();
								while (reader.hasNext()) {
									//SR.Print(kotir+"  "+nom+"  "+reader.nextName(), 1);
									String nameset = reader.nextName();
									if(nameset.equals("tickSize")) {
										if(SR.Pairs.get(nom).bid > 0) {
											SR.Pairs.get(nom).tickproc = reader.nextDouble()*100/SR.Pairs.get(nom).bid;
											//SR.Print(kotir+"  "+String.valueOf(SR.Pairs.get(nom).tickproc), 1);
										}
										else {
											SR.Pairs.get(nom).tickproc = 0;
											reader.skipValue();
										}
									}
									else reader.skipValue();
								}							
								reader.endObject();
							}
							reader.endArray();
						}
						else reader.skipValue();
					}
					reader.endObject();
				}
				reader.endArray();
			}
			else reader.skipValue();
		}
		 reader.endObject();
		 reader.close();
		}catch ( IOException e) {SR.Print("error json.",1);	}
	 SR.LastTimeAPI = 0;
	}
	static boolean ChkPair(String pair) {
		for(int i = 0; i < 5; i++) {
			for(int i1 = 0; i1 < 5 ; i1++) {
				//if(i!=i1 && pair.equals(SR.Valuta[i]+SR.Valuta[i1]))return true;
			}
		}
		return false;
	}

static int FindSymbol(String symbol) {
	int size = SR.Pairs.size();
	if(size == 0)return -1;
	for(int f = 0;f < size;f++) {
		if(symbol.equals(SR.Pairs.get(f).symbol))return f;
	}
	return -1;
}

 static void IntervalFunck() throws InterruptedException {
	 //SR.Print(String.valueOf(new Date().getTime()) + "  " + String.valueOf(LastPost + SR.Interval * 1000), 1);
	 if(new Date().getTime() < LastPost + SR.Interval * 1000)Thread.sleep((LastPost + SR.Interval*1000) - new Date().getTime());
	 LastPost = new Date().getTime();
 }
}
