package class12;

public class Mover{
	protected GuiMap M;       // マップ
	protected int X, Y;    // 現在位置
	protected int IDX;       // 移動体番号
	protected int nStep = 0; // 移動歩数
	protected int keyInput = -1; // 押されたキー
	protected int HP = 20;  // 体力は20で固定
	protected int MAX_HP = 100; // 体力の最大値
	Mover(){}

	// 移動体の位置はランダムに決定
	Mover(int i, GuiMap map){
		IDX = i;
		M = map;

		// 初期位置決定
		while(true) {
			X = (int)(Math.random() * M.width);
			Y = (int)(Math.random() * M.height);
			if (M.field[Y][X]==0) break;
		}
		M.field[Y][X] = IDX; // 自分の移動体番号を用いてマップ上に位置を記録
	}
	
	// 移動体の位置を指定．すでにその場に他の物体があるならばランダムに決定
	Mover(int i, GuiMap map, int x, int y){
		IDX = i;
		M = map;
		if(M.field[y][x] == 0) {
			X = x;
			Y = y;
		}else {
			while(true) {
				X = (int)(Math.random() * M.width);
				Y = (int)(Math.random() * M.height);
				if (M.field[Y][X]==0) break;
			}			
		}
		M.field[Y][X] = IDX; // 自分の移動体番号を用いてマップ上に位置を記録
	}	
	
	boolean canMove(){
		if(HP==0 || IDX==0) return false;
		else return true;
	}
	
	public void receiveDamage(int damage) {
		HP -= damage;
		if(HP < 0) HP = 0;
		if(HP==0 && IDX%2==0) { // トナカイの体力が尽きた場合
			System.out.println(IDX+"obake dying");
			IDX = 0; // マップから消す
			if(M.field[Y][X]%2==0) {
				M.field[Y][X] = IDX; // マップから消す
			}
		}
	}

	public int[] moveAndAttack(){
		System.out.print("Mover" + IDX +" is moving...");
		int key;
		int[] hitInfo = new int[2]; // 移動によってダメージを受ける移動体の情報
		hitInfo[0] = 1; // ダメージを受ける移動体の番号
		hitInfo[1] = 0; // ダメージ量
		int prevNStep = nStep; // 現時点の歩数を記録
		int prevX = X; // 現在のx座標を一時記録
		int prevY = Y; // 現在のy座標を一時記録
	
		if(IDX==1) // プレイヤーの場合
			key = keyInput; // ユーザから押されたキーを利用
		else // プレイヤー以外の移動体
			key = (int)(Math.random()*4 + 1)*2; // 2, 4, 6, 8 いずれかの値をランダムで生成
		System.out.println(" key=" + key);

		nStep++;
		
		if      (key==6 && X<M.width-1 ) X++; // 右へ移動
		else if (key==4 && 0<X         ) X--; // 左へ移動
		else if (key==8 && 0<Y         ) Y--; // 上へ移動
		else if (key==2 && Y<M.height-1) Y++; // 下へ移動
		else nStep--; // 領域外のため移動失敗

		if(IDX==1 && M.field[Y][X] > 0 && M.field[Y][X]%2 == 0) { // プレイヤーがトナカイのいるマスに進んだとき
			hitInfo[0] = M.field[Y][X]; // 帰ってもらったトナカイの移動体番号を記録
			hitInfo[1] = MAX_HP; // トナカイの体力が尽きる
			M.score++;
		}else if(M.field[Y][X]==-1){ // 移動先がクッキー
			HP += 20; // 現在注目している移動体のHPが回復
			if(HP > MAX_HP) HP = MAX_HP;
			M.nItem--; // マップ上のクッキーカウントを減らす
		}else if(M.field[Y][X]!=0){ // その他，移動先に障害物または他の移動体が存在する場合
			// コウモリ→プレイヤー　または　プレイヤー→コウモリ　のときはプレイヤーにダメージ
			if( (IDX==1 && M.field[Y][X] > 1 && M.field[Y][X]%2==1) ||
			    (IDX%2==1 && IDX>1 && M.field[Y][X]==1)){
				hitInfo[0] = 1;
				hitInfo[1] = 2;
			}
			X = prevX;  //元いた場所に戻す
			Y = prevY; 
			nStep--;
		}
		if(nStep > 0 && nStep%10 == 0 && nStep>prevNStep) {
			this.receiveDamage(1);
		}
		M.field[prevY][prevX] = 0;
		M.field[Y][X] = IDX; // 移動
		return hitInfo;
	}
	
	// 自分を中心とした3x3ブロック内にコウモリがいるときtrue，それ以外でfalseを返す
	public boolean checkDanger() {
		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {
				int x = X + j;
				int y = Y + i;
				try {
					int fd = M.field[y][x];
					if(fd % 2 == 1 && fd > 1) {
						System.out.println("koumori");
						return true;
					}
				}catch(ArrayIndexOutOfBoundsException e) { // 添字が配列範囲外
					continue;
				}
			}
		}
		return false;
	}
	
	public String createLabelString() {
		if      (keyInput==8) return "＜上＞";
		else if (keyInput==2) return "＜下＞";
		else if (keyInput==4) return "＜左＞";
		else if (keyInput==6) return "＜右＞";
		else if (keyInput==0) return "＜止＞";
		else return "移動方向";
	}
}