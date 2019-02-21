package class12;
// ���e�X�g03

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

	protected int SIZE; // �}�X��ӂ�����̉�f��
	int topMargin; // ��̗]���T�C�Y
	int leftMargin; // ���̗]���T�C�Y
	int rightMargin; // �E�̗]���T�C�Y
	int bottomMargin; // ���̗]���T�C�Y
	int WIDTH;
	int HEIGHT;
	int nDeer = 0; // �g�i�J�C�̐�
	int score = 0; // �g�i�J�C�|�C���g
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
		// ��Q���摜�̓ǂݍ���
		String dirName = (getClass().getResource("./")).toString();
		System.out.println(dirName + obsImgFileName);
		obsImage = new Image(dirName + obsImgFileName);
		// �ړ��̉摜�̓ǂݍ���
		deerImage = new Image(dirName + deerImgFileName);
		batImage = new Image(dirName + batImgFileName);
		playerImage = new Image(dirName + playerImgFileName);
		// �A�C�e���摜�̓ǂݍ���
		itemImage = new Image(dirName + itemImgFileName);
	}
	
	// �I������
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
	
	// �T�C�Y��Ԃ�
	public int[] calcMapSize() {
		int[] size = new int[2];
		size[0] = width * SIZE + leftMargin + rightMargin;
		size[1] = height * SIZE + topMargin + bottomMargin;
		WIDTH = size[0];
		HEIGHT = size[1];
		return size;
	}
	
	// ���݂̃}�b�v��`��
	public void paintMap(GraphicsContext gc){
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				int fd = field[y][x];
				if (fd==0) { // �����Ȃ��ꏊ�͔��h��
					gc.setFill(Color.WHITE);
					gc.fillRect(x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				} else if (fd==-9) { // ��Q��
					gc.drawImage(obsImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				}else if (fd==-1) { // �A�C�e��
					gc.drawImage(itemImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				} else { // �ړ���
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

	// ���݂̃}�b�v��`�� (HP�t��)
	public void paintMap(GraphicsContext gc, Mover mv[]) {
		gc.clearRect(0, 0, WIDTH, HEIGHT);
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				int fd = field[y][x];
				if (fd==0) { // �����Ȃ��ꏊ�͔��h��
					gc.setFill(Color.WHITE);
					gc.fillRect(x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				} else if (fd==-9) { // ��Q��
					gc.drawImage(obsImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				}else if (fd==-1) { // �A�C�e��
					gc.drawImage(itemImage, x*SIZE+leftMargin, y*SIZE+topMargin, SIZE, SIZE);
				} else{ // �ړ���
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

	// �t���[���㕔�ɕ\�����镶����i�}�b�v�T�C�Y�ƃN�b�L�[�̐���HP�j���쐬
	String createLabelString(double time, int hp) {
		String str = " �k" + time + "�b�l"
				+ "�@HP:" + hp
				+ "�@�g�i�J�C�~�o:" + score
				+ "�@";
		return str;
	}


	// �}�b�v��̃A�C�e�������X�V
	void updateItems(double x, double y, MouseButton b){
		int i = ((int)y - topMargin) / SIZE;
		int j = ((int)x - leftMargin) / SIZE;
		if (0<=i && i<height && 0<=j && j<width) {
			if (b==MouseButton.PRIMARY) { // ���N���b�N�̂Ƃ�
				if (field[i][j] == 0) {
					field[i][j] = -1;
					nItem++;
				}
			} else if (b==MouseButton.SECONDARY) { // �E�N���b�N�̂Ƃ�
				if (field[i][j] == -1) {
					field[i][j]=0;
					nItem--;
				}
			}
		}
	}
}


class Map{
	protected int width;     // �}�b�v�̕�
	protected int height;	  // �}�b�v�̍���
	protected int field[][]; // �}�b�v�t�B�[���h
	protected int nObs; // ��Q���̌�
	protected int nItem = 0; // �N�b�L�[�̐�
	protected int mode;      // ���샂�[�h�i0:��~�C1:�ړ��j

	Map(){}

	Map(int w, int h, int p){
		mode = 0;
		width  = w;
		height = h;
		field = new int[height][width];
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) field[i][j] = 0;
		}
		// ��Q���ʒu�̌���
		nObs = p * height * width / 100;
		int k = 0; // ��Q���̌��J�E���g�p
		while(true) {
			int x = (int)(Math.random()*width);
			int y = (int)(Math.random()*height);
			if (field[y][x] != 0) continue;
			field[y][x] = -9; // ��Q���Ƃ��ċL�^
			k++;
			if(k==nObs) break;
		}
	}

	// �e�L�X�g�t�@�C������}�b�v����ǂݎ��D���������ꍇ��true�C���s�����ꍇ��false��Ԃ��D
	boolean loadMap(File fl, Mover[] mv){		
		String[] rows = new String[height]; // �}�b�v�������̍s���̃e�L�X�g�f�[�^�p
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
			if(i != height) return false; // �G���[�i�}�b�v����������Ȃ��j
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
		// �e�L�X�g�f�[�^����
		int new_field[][] = new int[height][width];
		for(int i=0; i<height; i++) {
			String[] cols = rows[i].split(" ", 0);
			if(cols.length != width) return false; //�@�G���[�i�}�b�v��������Ȃ��j
			for(int j=0; j<width; j++) {
				try {
					new_field[i][j] = Integer.parseInt(cols[j]);
				}catch(Exception ex) {
					return false; // �G���[�i�����ȊO�̃f�[�^�j
				}
			}
		}
		// �A�C�e�����X�V�{�}�b�v�X�V�{�ړ��̏��X�V
		nItem = 0;
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				int fd = new_field[i][j];
				field[i][j] = fd;
				if(fd > 0) { // �ړ��̍X�V
					mv[fd-1].IDX = fd;
					mv[fd-1].HP = 20;
					mv[fd-1].X = j;
					mv[fd-1].Y = i;
				}else if(fd==-1){
					nItem++;
				}
			}
		}
		return true; // ����Ƀ��[�h�ł���
	}

	// �}�b�v�����t�@�C���Ƀe�L�X�g�o�́D���������ꍇ��true�C���s�����ꍇ��false��Ԃ��D
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
	
	// �}�b�v�����e�L�X�g�\��
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
