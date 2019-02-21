package class12;
// 小テスト03

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class GuiMap extends Map{

	protected int SIZE; // マス一辺あたりの画素数
	int topMargin; // 上の余白サイズ
	int leftMargin; // 左の余白サイズ
	int rightMargin; // 右の余白サイズ
	int bottomMargin; // 下の余白サイズ
	int WIDTH;
	int HEIGHT;
	int nDeer = 0; // トナカイの数
	int score = 0; // トナカイポイント
	final String obsImgFileName = "tree.jpg";
	final String deerImgFileName = "reindeer.jpg";
	final String batImgFileName = "bat.jpg";
	final String itemImgFileName = "cookie.jpg";
	final String playerImgFileName = "santa.jpg";
	Image obsImage;
	Image deerImage;
	Image batImage;
	Image itemImage;
	Image playerImage;
	
	public GuiMap(){}

	public GuiMap(int w, int h, int p, int s){
		super(w, h, p);
		SIZE = s;
		topMargin = s / 2;
		bottomMargin = 0;
		leftMargin = 0;
		rightMargin = 0;
		// 障害物画像の読み込み
		String dirName = (getClass().getResource("./")).toString();
		System.out.println(dirName + obsImgFileName);
		obsImage = new Image(dirName + obsImgFileName);
		// 移動体画像の読み込み
		deerImage = new Image(dirName + deerImgFileName);
		batImage = new Image(dirName + batImgFileName);
		playerImage = new Image(dirName + playerImgFileName);
		// アイテム画像の読み込み
		itemImage = new Image(dirName + itemImgFileName);
	}
	
	// 終了判定
	public boolean isFinished(Mover[] mv) {
		boolean flag = false;
		if(mv[0].HP==0) 
			flag = true;
		else {
			int cnt = 0;
			for(int i=0; i<mv.length; i++){
				if(mv[i].IDX>0 && mv[i].IDX%2==0)
					cnt++;
			}
			if(cnt==0)
				flag = true;
		}
		return flag;
	}
	
	// サイズを返す
	public int[] calcMapSize() {
		int[] size = new int[2];
		size[0] = width * SIZE + leftMargin + rightMargin;
		size[1] = height * SIZE + topMargin + bottomMargin;
		WIDTH = size[0];
		HEIGHT = size[1];
		return size;
	}
	
	// 現在のマップを描画
	public void paintMap(GraphicsContext gc){
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				int fd = field[y][x];
				if (fd==0) { // 何もない場所は白塗り
					gc.setFill(Color.WHITE);
					gc.fillRect(x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				} else if (fd==-9) { // 障害物
					gc.drawImage(obsImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				}else if (fd==-1) { // アイテム
					gc.drawImage(itemImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				} else { // 移動体
					if(fd==1)
						gc.drawImage(playerImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
					else if(fd % 2 == 0)
						gc.drawImage(deerImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
					else
						gc.drawImage(batImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				}
			}
		}
	}

	// 現在のマップを描画 (HP付き)
	public void paintMap(GraphicsContext gc, Mover mv[]) {
		gc.clearRect(0, 0, WIDTH, HEIGHT);
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				int fd = field[y][x];
				if (fd==0) { // 何もない場所は白塗り
					gc.setFill(Color.WHITE);
					gc.fillRect(x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				} else if (fd==-9) { // 障害物
					gc.drawImage(obsImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				}else if (fd==-1) { // アイテム
					gc.drawImage(itemImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				} else{ // 移動体
					if(fd==1)
						gc.drawImage(playerImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
					else if(fd % 2 == 0)
						gc.drawImage(deerImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
					else
						gc.drawImage(batImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
					int hp = mv[fd-1].HP;
					gc.setFill(Color.RED);
					gc.fillText(""+hp, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE);
				}
			}
		}	
		
	}

	// フレーム上部に表示する文字列（マップサイズとクッキーの数とHP）を作成
	String createLabelString(double time, int hp) {
		String str = " 〔" + time + "秒〕"
				+ "　HP:" + hp
				+ "　トナカイ救出:" + score
				+ "　";
		return str;
	}


	// マップ上のアイテム情報を更新
	void updateItems(double x, double y, MouseButton b){
		int i = ((int)y - topMargin) / SIZE;
		int j = ((int)x - leftMargin) / SIZE;
		if (0<=i && i<height && 0<=j && j<width) {
			if (b==MouseButton.PRIMARY) { // 左クリックのとき
				if (field[i][j] == 0) {
					field[i][j] = -1;
					nItem++;
				}
			} else if (b==MouseButton.SECONDARY) { // 右クリックのとき
				if (field[i][j] == -1) {
					field[i][j]=0;
					nItem--;
				}
			}
		}
	}
}


class Map{
	protected int width;     // マップの幅
	protected int height;	  // マップの高さ
	protected int field[][]; // マップフィールド
	protected int nObs; // 障害物の個数
	protected int nItem = 0; // クッキーの数
	protected int mode;      // 動作モード（0:停止，1:移動）

	Map(){}

	Map(int w, int h, int p){
		mode = 0;
		width  = w;
		height = h;
		field = new int[height][width];
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) field[i][j] = 0;
		}
		// 障害物位置の決定
		nObs = p * height * width / 100;
		int k = 0; // 障害物の個数カウント用
		while(true) {
			int x = (int)(Math.random()*width);
			int y = (int)(Math.random()*height);
			if (field[y][x] != 0) continue;
			field[y][x] = -9; // 障害物として記録
			k++;
			if(k==nObs) break;
		}
	}

	// テキストファイルからマップ情報を読み取る．成功した場合はtrue，失敗した場合はfalseを返す．
	boolean loadMap(File fl, Mover[] mv){		
		String[] rows = new String[height]; // マップ高さ分の行数のテキストデータ用
		try {
			BufferedReader br = new BufferedReader(new FileReader(fl));
			String str = null;
			int i = 0;
			while((str = br.readLine()) != null){
				if(i < height)
					rows[i] = str;
				i++;
			}
			br.close();
			if(i != height) return false; // エラー（マップ高さが合わない）
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		// テキストデータ処理
		int new_field[][] = new int[height][width];
		for(int i=0; i<height; i++) {
			String[] cols = rows[i].split(" ", 0);
			if(cols.length != width) return false; //　エラー（マップ幅が合わない）
			for(int j=0; j<width; j++) {
				try {
					new_field[i][j] = Integer.parseInt(cols[j]);
				}catch(Exception ex) {
					return false; // エラー（数字以外のデータ）
				}
			}
		}
		// アイテム数更新＋マップ更新＋移動体情報更新
		nItem = 0;
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				int fd = new_field[i][j];
				field[i][j] = fd;
				if(fd > 0) { // 移動体更新
					mv[fd-1].IDX = fd;
					mv[fd-1].HP = 20;
					mv[fd-1].X = j;
					mv[fd-1].Y = i;
				}else if(fd==-1){
					nItem++;
				}
			}
		}
		return true; // 正常にロードできた
	}

	// マップ情報をファイルにテキスト出力．成功した場合はtrue，失敗した場合はfalseを返す．
	boolean saveMap(File fl) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fl));
			PrintWriter pw = new PrintWriter(bw);
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					pw.printf("%02d", field[i][j]);
					if(j<(width-1)) pw.printf(" ");
				}
				pw.println("");
			}
			bw.close();
			return true;
		}catch(Exception ex) {
			return false;
		}
	}	
	
	// マップ情報をテキスト表示
	void printMap(){
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				int f = field[y][x];
				System.out.printf("%02d ", f);
			}
			System.out.println();
		}
	}
	
}
