package fa;

public enum Status {
	INTRO_STATE(0), PLAY_STATE(1), DONE_STATE(2);
	
	private int statusCode;
	private Status(int x){
		statusCode = x;
	}
	
	public int getStatusCode(){
		return statusCode;
	}
}
