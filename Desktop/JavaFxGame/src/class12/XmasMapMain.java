package class12;

import java.applet.Applet;
import java.io.File;

import com.sun.prism.Graphics;

//�w�Дԍ� 1116170055�@���� �����I��
//�U���̂��߂ɃR�[�h���c���Ă��邪�C�{�t�@�C���̒��g�͎��R�ɏ��������č\��Ȃ��D

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class XmasMapMain extends Application{
	int NUM = 20; // �ړ��̂̌�
	int cnt = 0;
	GuiMap guimap;
	Mover[] mv = new Mover[NUM];
	double time = 0.0; // �o�ߎ��ԁi�b�j
	BorderPane bp;

	Button bt; // �{�^��
	Label lbMsg; // �㕔�Ƀ}�b�v�T�C�Y�E�A�������e�L�X�g�\��
	Label lbKey; // ���͂��ꂽ�L�[���e�L�X�g�\��
	Label lbGame; // �Q�[���̏�Ԃ�\��
	Label lbState;
	Label lb;
	Canvas cv;
	Canvas mcv;
	Timeline timeline; // �A�j���[�V�����p�^�C�����C��
	GraphicsContext gc; // �L�����o�X�p�O���t�B�b�N�X�R���e�L�X�g
	GraphicsContext mgc;
	FlowPane[] fp = new FlowPane[2];
	int w = 24; // ���̃}�X��
	int h = 16; // �c�̃}�X��
	int p = 20; // ��Q���̊��� (%)
	int s = 30; // �}�X��ӂ�����̉�f��
	int dtime = 100; // �A�j���[�V�������s�~���b

	//���j���[�o�[�̕ϐ�
	MenuBar mb = new MenuBar();
	Menu[] mn = new Menu[2];
	MenuItem[] mi = new MenuItem[4];
	SeparatorMenuItem sm;

	public static void main(String args[]) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		// �}�b�v����
		guimap = new GuiMap(w, h, p, s);

		// �v���C���[(IDX=1)�Ƃ��̑��̈ړ���(IDX>1)����
		for (int i=0; i<NUM; i++)
			mv[i] = new Mover(i+1, guimap);
		guimap.printMap();

		// �K�v�ȃL�����o�X�T�C�Y
		int[] cvSize = guimap.calcMapSize();
		// �L�����o�X�쐬
		cv = new Canvas(cvSize[0], cvSize[1]);
		gc = cv.getGraphicsContext2D();
		int[] mcvSize= guimap.calcMapSize();
		mcv = new Canvas(cvSize[0]+10, cvSize[1]+10);

		mgc =mcv.getGraphicsContext2D();

		// �㕔���x���쐬
		lbMsg = new Label();
		lbMsg.setTextFill(Color.BLACK);
		lbMsg.setFont(Font.font("Serif",24));

		// �Q�[�����x���쐬
		lbGame = new Label("�Q�[�����J�n���Ă��������I");
		lbGame.setTextFill(Color.BLACK);
		lbGame.setFont(Font.font("Serif", 24));

		// ���x��(�L�[�ړ�����)
		lbKey = new Label(mv[0].createLabelString());
		lbKey.setTextFill(Color.BLACK);
		lbKey.setFont(Font.font("Serif",24));


		//menu bar
		mn[0] = new Menu("�t�@�C��");
		mn[1] = new Menu("さ");

		mi[0] = new MenuItem("�J��");
		mi[1] = new MenuItem("�ۑ�");
		mi[2] = new MenuItem("����");
		mi[3] = new MenuItem("�Ȃ�");

		mn[0].getItems().addAll(mi[0],mi[1]);
		mn[1].getItems().addAll(mi[2],mi[3]);

		mb.getMenus().addAll(mn[0],mn[1]);

		//���T�C�h�̐F
		//mcv = new Canvas();

		//Mover��checkDanger()���g��

		// �{�^��
		bt = new Button("�J�n");

		// �y�C���쐬
		bp = new BorderPane();
		fp[0] = new FlowPane();
		fp[1]= new FlowPane();

		// �R���g���[�����y�C���ɒǉ�
		bp.setTop(fp[0]);
		//bp.setCenter(mcv);
		bp.setCenter(cv);
		bp.setBottom(fp[1]);
		//bp.setLeft(lb);
		//bp.setRight(lb);

		fp[0].getChildren().add(mb);

		fp[0].getChildren().add(lbMsg);
		fp[0].setHgap(10);

		fp[1].getChildren().add(bt);
		fp[1].getChildren().add(lbKey);
		fp[1].getChildren().add(lbGame);
		fp[1].setAlignment(Pos.CENTER);
		fp[1].setHgap(10);


		// �V�[���̍쐬
		System.out.println(cvSize[0]+" "+cvSize[1]);
		Scene sc = new Scene(bp, cvSize[0]+s*2, cvSize[1]+s*2);

		// �V�[�����X�e�[�W�ɒǉ�
		stage.setScene(sc);

		// �X�e�[�W�̕\��
		stage.setTitle("���e�X�g03�@1116170055�@�����I��");
		stage.show();

		// �C�x���g�n���h���̓o�^
		cv.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
		bt.setOnAction(new MyEventHandler1());
		sc.setOnKeyPressed(new MyKeyEventHandler());
		mi[0].setOnAction(new MyMenuEventHandler1());
		mi[1].setOnAction(new MyMenuEventHandler1());
		mi[2].setOnAction(new MyMenuEventHandler2());
		mi[3].setOnAction(new MyMenuEventHandler2());

		// �A�j���[�V����(�^�C�����C����)
     startAnimation();
	}
	public class MyFirstApplet extends Applet {
		  public void init(){
		    setBackground(Color.GREEN);
		  }
		  private void setBackground(Color green) {
			// TODO 自動生成されたメソッド・スタブ

		}
		public void paint(Graphics g){
		    mgc.setColor(Color.RED);
		    g.drawString("This is My First Applet",5,20);
		  }
		}
	private void startAnimation() {
		myRun();
		timeline = new Timeline(new KeyFrame(
				new Duration(dtime),
				new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						if (guimap.mode==1) {
							cnt++;
							myRun();
						}
					}
				}
				)
		);
		timeline.setCycleCount(Timeline.INDEFINITE);
	}

	public void myRun(){
		cv.requestFocus();
		System.out.println("(myRun) mode=" + guimap.mode);
		if (guimap.mode==1) {
			lbGame.setText("�Q�[�����c");
			System.out.println("�ړ���" + cnt);
			for (int j=0; j<NUM; j++) {
				if(mv[j].canMove()) {
					int[] hitInfo = mv[j].moveAndAttack(); // �ړ��D�_���[�W���󂯂��ړ��̂̏���Ԃ�
					int hitIdx = hitInfo[0]; // �_���[�W���󂯂��ړ��̔ԍ�
					int damage = hitInfo[1]; // �_���[�W�̗�
					mv[hitIdx-1].receiveDamage(damage);
				}
			}
			// �Q�[���I������
			if(guimap.isFinished(mv)) {
				lbGame.setText("�Q�[���I���I");
				guimap.mode = 0;
				timeline.pause();
				// �{�^���̃C�x���g�n���h������
				bt.setOnAction(null);
			}
			time += dtime / 1000.0;
			time = (int)((1000*time)+0.01) / 1000.0;
		}
		updateMap(gc);
	}

	// �y�C���g
	public void updateMap(GraphicsContext gc){
		String str = guimap.createLabelString(time, mv[0].HP);
		lbMsg.setText(str);
		guimap.paintMap(gc, mv);
	}


	// �C�x���g�n���h���N���X for �}�E�X�i�}�b�v���N���b�N�j
	class MyEventHandler implements EventHandler<MouseEvent> {
		public void handle(MouseEvent e) {
			double x = e.getX();
			double y = e.getY();
			MouseButton b = e.getButton();
			System.out.println("\nMouseEvent.MOUSE_CLICKED "
					+ x + "," + y + " b=" + b );
			guimap.updateItems(x, y, b);
			updateMap(gc);
		}
	}




	// �C�x���g�n���h���N���X for �{�^��0�i�J�n/��~�j
	class MyEventHandler1 implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if (guimap.mode==0) {
				bt.setText(" ��~ ");
				guimap.mode = 1;
				timeline.play();
			} else {
				bt.setText(" �J�n ");
				guimap.mode = 0;
				timeline.pause();
			}
		}
	}

	// �C�x���g�n���h���N���X for �L�[
	class MyKeyEventHandler implements EventHandler<KeyEvent> {
		public void handle(KeyEvent e) {
			KeyCode k = e.getCode();
			switch (k) {
				case UP:    mv[0].keyInput=8; break;
				case DOWN:  mv[0].keyInput=2; break;
				case LEFT:  mv[0].keyInput=4; break;
				case RIGHT: mv[0].keyInput=6; break;
				case S:     mv[0].keyInput=0; break;
				default:    ;
			}
				lbKey.setText(mv[0].createLabelString());
		}
	}

//�C�x���g�n���h���N���X file menu
	class MyMenuEventHandler1 implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			FileChooser fc = new FileChooser();
			if(e.getSource() == mi[0]) {
				try {
					File fl = fc.showOpenDialog(new Stage());
					if(fl != null) {
						if(guimap.loadMap(fl, mv)) {
							lbGame.setText(fl.getName()+"���J���܂����D");
							updateMap(gc);
						}else {
							lbGame.setText(fl.getName()+"�F���[�h���s�I");
						}
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}else if(e.getSource() == mi[1]) {
				try {
					File fl = fc.showSaveDialog(new Stage());
					if(fl != null) {
						if(guimap.saveMap(fl))
							lbGame.setText(fl.getName()+"�ɕۑ����܂����D");
						else
							lbGame.setText(fl.getName()+"�F�ۑ����s�I");
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

////�C�x���g�n���h���N���X file state
	class MyMenuEventHandler2 implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			if (e.getSource() == mi[2]) {
				lbKey.setVisible(true);
				lbGame.setVisible(true);

			} else if(e.getSource() == mi[3])  {
				lbKey.setVisible(false);
				lbGame.setVisible(false);

			}
		}
	}
}

