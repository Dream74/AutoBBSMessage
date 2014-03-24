package telnet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoPOBBSMessage {

	public static final  String CYCU_ICE_BBS_HOST  = "140.135.11.30" ;
	public static final  int      CYCU_ICE_BBS_PORT  = 23 ;
     public static final  String BBS_ROBOT_BBS_USR  = "CYCU_BBS_USER_NAME" ;
     public static final  String BBS_ROBOT_BBS_PWD = "CYCU_BBS_USER_PASSWORD" ;

		
	public static final String BBS_PL_CLASS_NAME   = "CS.Language";
     public static final String PL_PAGE_TITLE            = "[公告]PAL目前進度";

	
	private String mPageTitle   ;
	private String mClassName ;
	private String mHost ;
	private int      mPort  ;
	private Socket sock;

	public AutoPOBBSMessage(  String host, int port, String className, String pageTitle  ) { 
		this.mClassName = className ;
		this.mPageTitle    = pageTitle   ;
	    this.mHost 		  = host ;	
		this.mPort           = port ;
	}

	public void setPort( int port ) {
		this.mPort = port ;
	}

	public int getPort( ) {
		return this.mPort ;
	}

	public void setHost(String host){
		this.mHost = host ;
	}

	public String getHost() {
		return this.mHost ;
	}
	

	public void setClassName(String className ) {
		this.mClassName = className ;
	}

	
	public String getClassName(){
		return this.mClassName ;
	}

	public void setPageTitle( String pageTitle) {
		this.mPageTitle = pageTitle ;
	}

	public String getPageTitle( ) {
		return this.mPageTitle ;
	}
	
	public void startSend(final String content) {
		try {
			sock = new Socket(mHost, mPort);

			new Pipe(sock.getInputStream(), System.out).start();

			final PrintStream serverWriter = new PrintStream(sock.getOutputStream());

			new Thread() {
				public void run() {
					try {
						Thread.sleep(10000);
						serverWriter.println(BBS_ROBOT_BBS_USR);
						serverWriter.println(BBS_ROBOT_BBS_PWD);
						// 不要踢掉登入者！？！？
						// TODO 尚未加入
						

						// 跳過 使用者認證
						//	serverWriter.println();
						Thread.sleep(1000);
						// 跳過 進入BBS廣告
						serverWriter.println();
						Thread.sleep(1000);
						// 進入 所有討論區
						serverWriter.println();
						Thread.sleep(1000);
						// 收尋 課程名稱
						serverWriter.println("/" + mClassName);
						Thread.sleep(1000);
						// 進入 搜尋的課程
						serverWriter.println();
						Thread.sleep(1000);
						// PO 文
						serverWriter.println((char) 0x10);
						Thread.sleep(1000);

						{
							byte x[] = mPageTitle.getBytes("big5");
							serverWriter.write(x, 0, x.length);
						}
						serverWriter.println();
						Thread.sleep(1000);

						{
							byte x[] = content.getBytes("big5");
							serverWriter.write(x, 0, x.length);
						}
						Thread.sleep(1000);

						serverWriter.println(String.valueOf((char) 0x18));
						Thread.sleep(1000);

					} catch (	InterruptedException | UnsupportedEncodingException ex) {
						Logger.getLogger(AutoPOBBSMessage.class.getName()).log(Level.SEVERE, null, ex);
					} finally{
                        	     System.out.println("System Exit");
                       	     System.exit(0);
                    	}
				}
			}.start();
		} catch (IOException ex) {
		} 
	}

	
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
		String content  = "BBS_AUTO_MESSAGE" ;
		try {
				 
			
			AutoPOBBSMessage robot = new AutoPOBBSMessage( CYCU_ICE_BBS_HOST,  // 中原的BBS IP: 140.135.11.30
																						   CYCU_ICE_BBS_PORT,  // 中原的BBS Port: 23 
																						   BBS_PL_CLASS_NAME, //  PO 在哪個版
																						   PL_PAGE_TITLE ) ;       // 文章標頭
			
			robot.startSend(content);
		} catch (Throwable ex) {
			Logger.getLogger(AutoPOBBSMessage.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
