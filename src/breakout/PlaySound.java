/*package breakout;

public class PlaySound implements LineListener {
	
	private boolean done = false;
	
	public static void playClip (File clipFile) {
		
		@Override
		public synchronized void update (LineEvent le) {
			Type eventType = event.getType();
			
		    if (eventType == Type.STOP || eventType == Type.CLOSE) {
		        done = true;
		        notifyAll();
		      }
		}
		
		public synchronized void waitUntilDone() {
			while (!done) wait();
			
		}
		
	}

}*/
