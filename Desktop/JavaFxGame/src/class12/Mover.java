package class12;

public class Mover{
	protected GuiMap M;       // �}�b�v
	protected int X, Y;    // ���݈ʒu
	protected int IDX;       // �ړ��̔ԍ�
	protected int nStep = 0; // �ړ�����
	protected int keyInput = -1; // �����ꂽ�L�[
	protected int HP = 20;  // �̗͂�20�ŌŒ�
	protected int MAX_HP = 100; // �̗͂̍ő�l
	Mover(){}

	// �ړ��̂̈ʒu�̓����_���Ɍ���
	Mover(int i, GuiMap map){
		IDX = i;
		M = map;

		// �����ʒu����
		while(true) {
			X = (int)(Math.random() * M.width);
			Y = (int)(Math.random() * M.height);
			if (M.field[Y][X]==0) break;
		}
		M.field[Y][X] = IDX; // �����̈ړ��̔ԍ���p���ă}�b�v��Ɉʒu���L�^
	}
	
	// �ړ��̂̈ʒu���w��D���łɂ��̏�ɑ��̕��̂�����Ȃ�΃����_���Ɍ���
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
		M.field[Y][X] = IDX; // �����̈ړ��̔ԍ���p���ă}�b�v��Ɉʒu���L�^
	}	
	
	boolean canMove(){
		if(HP==0 || IDX==0) return false;
		else return true;
	}
	
	public void receiveDamage(int damage) {
		HP -= damage;
		if(HP < 0) HP = 0;
		if(HP==0 && IDX%2==0) { // �g�i�J�C�̗̑͂��s�����ꍇ
			System.out.println(IDX+"obake dying");
			IDX = 0; // �}�b�v�������
			if(M.field[Y][X]%2==0) {
				M.field[Y][X] = IDX; // �}�b�v�������
			}
		}
	}

	public int[] moveAndAttack(){
		System.out.print("Mover" + IDX +" is moving...");
		int key;
		int[] hitInfo = new int[2]; // �ړ��ɂ���ă_���[�W���󂯂�ړ��̂̏��
		hitInfo[0] = 1; // �_���[�W���󂯂�ړ��̂̔ԍ�
		hitInfo[1] = 0; // �_���[�W��
		int prevNStep = nStep; // �����_�̕������L�^
		int prevX = X; // ���݂�x���W���ꎞ�L�^
		int prevY = Y; // ���݂�y���W���ꎞ�L�^
	
		if(IDX==1) // �v���C���[�̏ꍇ
			key = keyInput; // ���[�U���牟���ꂽ�L�[�𗘗p
		else // �v���C���[�ȊO�̈ړ���
			key = (int)(Math.random()*4 + 1)*2; // 2, 4, 6, 8 �����ꂩ�̒l�������_���Ő���
		System.out.println(" key=" + key);

		nStep++;
		
		if      (key==6 && X<M.width-1 ) X++; // �E�ֈړ�
		else if (key==4 && 0<X         ) X--; // ���ֈړ�
		else if (key==8 && 0<Y         ) Y--; // ��ֈړ�
		else if (key==2 && Y<M.height-1) Y++; // ���ֈړ�
		else nStep--; // �̈�O�̂��߈ړ����s

		if(IDX==1 && M.field[Y][X] > 0 && M.field[Y][X]%2 == 0) { // �v���C���[���g�i�J�C�̂���}�X�ɐi�񂾂Ƃ�
			hitInfo[0] = M.field[Y][X]; // �A���Ă�������g�i�J�C�̈ړ��̔ԍ����L�^
			hitInfo[1] = MAX_HP; // �g�i�J�C�̗̑͂��s����
			M.score++;
		}else if(M.field[Y][X]==-1){ // �ړ��悪�N�b�L�[
			HP += 20; // ���ݒ��ڂ��Ă���ړ��̂�HP����
			if(HP > MAX_HP) HP = MAX_HP;
			M.nItem--; // �}�b�v��̃N�b�L�[�J�E���g�����炷
		}else if(M.field[Y][X]!=0){ // ���̑��C�ړ���ɏ�Q���܂��͑��̈ړ��̂����݂���ꍇ
			// �R�E�������v���C���[�@�܂��́@�v���C���[���R�E�����@�̂Ƃ��̓v���C���[�Ƀ_���[�W
			if( (IDX==1 && M.field[Y][X] > 1 && M.field[Y][X]%2==1) ||
			    (IDX%2==1 && IDX>1 && M.field[Y][X]==1)){
				hitInfo[0] = 1;
				hitInfo[1] = 2;
			}
			X = prevX;  //�������ꏊ�ɖ߂�
			Y = prevY; 
			nStep--;
		}
		if(nStep > 0 && nStep%10 == 0 && nStep>prevNStep) {
			this.receiveDamage(1);
		}
		M.field[prevY][prevX] = 0;
		M.field[Y][X] = IDX; // �ړ�
		return hitInfo;
	}
	
	// �����𒆐S�Ƃ���3x3�u���b�N���ɃR�E����������Ƃ�true�C����ȊO��false��Ԃ�
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
				}catch(ArrayIndexOutOfBoundsException e) { // �Y�����z��͈͊O
					continue;
				}
			}
		}
		return false;
	}
	
	public String createLabelString() {
		if      (keyInput==8) return "���い";
		else if (keyInput==2) return "������";
		else if (keyInput==4) return "������";
		else if (keyInput==6) return "���E��";
		else if (keyInput==0) return "���~��";
		else return "�ړ�����";
	}
}