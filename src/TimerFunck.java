import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerFunck {
	
	public static Timer timer;
	public static boolean timerwork = false;
	public TimerFunck()
	{
	 //Запускаем таймер
	 timer = new Timer(true);
	 timer.schedule(new MyTimerTask(), 0, 100);
	 timerwork = true;
	}
	 public class MyTimerTask extends TimerTask {
			public void run() {
				timerwork = true;
				//SR.Print(SR.format.format(new Date())+"  "+SR.LastTimeAPI+"  "+SR.StartBuy+" "+SR.StartSell+"  "+timer+"  "+timerwork,1);
		        if(SR.LastTimeAPI == 0)return;
		        double x = (double)(new Date().getTime() - SR.LastTimeAPI)/1000.0;
		        Gui.timer.setText(String.format("%.3f", x)+"s");
		    }
	    }
}
