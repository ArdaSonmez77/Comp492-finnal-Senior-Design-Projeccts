import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.concurrent.TimeUnit;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class Game {
	int tur=0;
	double recx, recy;
	double Weight,Heigth;
	ArrayList<Stone> Stones = new ArrayList<Stone>();
	ArrayList<Stone> dropped=new ArrayList<Stone>();
	ArrayList<Wood> Wooders = new ArrayList<Wood>();
        
        String soundFile_1 = "click_2.mp3";
	String soundFile_2 = "click_3.mp3";
	String soundFile_3 = "turn_1.mp3";
	String soundFile_4 = "start.mp3";
	String soundFile_5 = "winner.wav";
	Media sound_1 = new Media(new File(soundFile_1).toURI().toString());
	Media sound_2 = new Media(new File(soundFile_2).toURI().toString());
	Media sound_3 = new Media(new File(soundFile_3).toURI().toString());
	Media sound_4 = new Media(new File(soundFile_4).toURI().toString());
	Media sound_5 = new Media(new File(soundFile_5).toURI().toString());	
	
	MediaPlayer mediaPlayer_1 = new MediaPlayer(sound_1);
	MediaPlayer mediaPlayer_2 = new MediaPlayer(sound_2);
	MediaPlayer mediaPlayer_3 = new MediaPlayer(sound_3);
	MediaPlayer mediaPlayer_4 = new MediaPlayer(sound_4);
	MediaPlayer mediaPlayer_5 = new MediaPlayer(sound_5);

        
	Wood wood;

	Group root;
	Button btn;
	Button nextPlayer;
	Button getNew;
	Button getSide;
	Button finish;
	Random rnd= new Random();
	int WinnerPlayer=0;
	public void Initialize(Stage primaryStage)
	{

		double xcor=30.0f;
		double ycor=10.0f;
		double slide=71.0f;
		double Width= 40.0f;
		double Height= 50.0f;
		root = new Group();
		//wood.DrawDropPlaces(root);
		Color Brown=Color.BURLYWOOD;
		Color Black= Color.BLACK;
		double recx=230;
		double recy=700;
		double Weight=1200;
		double Heigth=200;


		Line MidLine=new Line(recx,(recy+Heigth/2)+50,recx+Weight,(recy+Heigth/2)+50);
		//MidLine.setStrokeWidth(3.0f);
		//Woods Of the game;

		MixStones();

		wood = new Wood(1,recx,recy+50,Weight,Heigth,Brown,root,Stones);
		Wooders.add(wood);
		wood = new Wood(2,recx+1250,recy-610,Heigth-50, Weight-500,Brown,root,Stones);
		Wooders.add(wood);
		wood = new Wood(3,recx,recy-650,Weight,Heigth,Brown,root,Stones);
		Wooders.add(wood);
		wood = new Wood(4,recx-200,recy-610,Heigth-50, Weight-500,Brown,root,Stones);
		Wooders.add(wood);
		//Limits of the lines
		WinnerPlayer=rnd.nextInt(3)+1;
		Rectangle Limited3= new Rectangle(recx+160, 540, 300, 140);

		root.getChildren().add(MidLine);

		//System.out.println(yeni.Stones.toString());
		//taslari meydana cikartma
		/*for(int y=0; y<Stones.size(); y++) {
			root.getChildren().add(Stones.get(y).getTxt());
			}*/
		//linner= new Linedary(recx,(recy+Heigth/2)+30,recx+Weight,(recy+Heigth/2)+30,root);

		//BUTTON EKLEME
		btn = new Button("Let's Start");
		btn.setOnAction(event);
		btn.setLayoutX(750);
		btn.setLayoutY(350);
		btn.setPrefWidth(100);
		btn.setPrefHeight(100);
		root.getChildren().add(btn);

		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root, 1700,1000, Color.CHARTREUSE));

		primaryStage.setTitle("This Game real Name is= 183 game");
		primaryStage.show();
	}

	private void MixStones()
	{
		Color green=Color.GREEN;
		Color blue=Color.BLUE;
		Color yellow=Color.GOLD;
		Color red=Color.RED;
		Color black=Color.BLACK;
		Paint gray=Color.DARKGRAY;
		Paint white=Color.WHITE;
		ArrayList<Stone> CreatedStones = new ArrayList<Stone>();
		for(int a=1; a<107; a++) {
			Stone yeni= new Stone();
			if(a%13==0) {
				yeni.setValue(13);
			}else if(a/13==8 && a%13!=0) {
				yeni.setValue(0);
				yeni.setColor(black);
			}else {
				yeni.setValue(a%13);
			}

			if(a>=1 && a<=26){
				yeni.setColor(red);
			}else if(a>=27 && a<=52){
				yeni.setColor(yellow);
			}else if(a>=53 && a<=79){
				yeni.setColor(green);
			}else if(a>=80 && a<=104){
				yeni.setColor(blue); 
			}
			yeni.setTxt(yeni.getTxt());
			yeni.setId(a);

			yeni.getTxt().setOnMousePressed(labelpress);
			yeni.getTxt().setOnMouseDragged(labeldragg);
			yeni.getTxt().setOnMouseReleased(labelout);
			CreatedStones.add(yeni);
		}

		int iStoneIndex=0;
		//Taslari karýstýrýyorum burada
		for(int a=1; a<107; a++) {
			iStoneIndex= rnd.nextInt(CreatedStones.size());
			this.Stones.add(CreatedStones.get(iStoneIndex));
			CreatedStones.remove(iStoneIndex);
		}
	}


	double offsetX, offsetY, orgSceneX, orgSceneY, prspointX, prspointY,wayX,wayY,shortest;
	double newTranslateX, newTranslateY, orgTranslateX, orgTranslateY,msf,pstX,pstY,FirstX,FirstY;
	double prvX, prvY;
	Stone Closest;
	Line prevLine;
	//	int bfr=0;
	int placePress=0;
	int placeOut=0;
	int counting=0;
	Stone DrClosest;
	EventHandler<MouseEvent> labelpress =
			new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			
                        
			MouseButton button = t.getButton();
			if(button!=MouseButton.PRIMARY)
			{
				t.consume();
				return;
			}
			            
			mediaPlayer_1.stop();
			mediaPlayer_1.play();
			//tahtadan alýnan sadece Sað veya ortadan baþka bir yere koyulamaz TAMAMLANDI
			//Ortaya atýlan taþlarda hareket ettirilemez TAMAMLANDI

			//sað tarafa atýlan  taþ sabit kalýr TAMAMLANDI
			//Soldan alýnan taþ sadece tahtaya konulabilir
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			//Aldýgým tasýn konumu
			placePress= Wooders.get(0).WhereIsTaken(orgSceneX, orgSceneY);



			System.out.println("position= "+placePress);
			//Atýlan tas geri alýnamaz kuralý
			/*	if(placePress==2 || placePress==3) {
				if(button==MouseButton.PRIMARY)
				{
					t.consume();
					return;
				}
			}*/
			FirstX=orgSceneX;
			FirstY=orgSceneY;
			orgTranslateX = ((Text)(t.getSource())).getTranslateX();
			orgTranslateY = ((Text)(t.getSource())).getTranslateY();
			//System.out.println(orgSceneX+" "+orgSceneY);

			Closest = Wooders.get(0).FindClosest(orgSceneX, orgSceneY, root);
			DrClosest=Closest;
			Wooders.get(0).prevDropped.add(DrClosest);
			//System.out.println(Closest.getTxt());
			//prevLine=Wooders.get(0).GetInitialLine(Closest);
			//System.out.println("Closest Line is="+ prevLine.getId());
			//System.out.println(Closest.getXsutun() + ":" + Closest.getYsatir());
		}
	};
	EventHandler<MouseEvent> labeldragg =
			new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			MouseButton button = t.getButton();
			/*	if(button!=MouseButton.PRIMARY)
			{
				t.consume();
				return;
			}
			if(placePress==2 || placePress==3) {
				if(button==MouseButton.PRIMARY)
				{
					t.consume();
					return;
				}
			}*/
			offsetX = t.getSceneX() - orgSceneX;
			offsetY = t.getSceneY() - orgSceneY;
			newTranslateX = orgTranslateX + offsetX;
			newTranslateY = orgTranslateY + offsetY;

			((Text)(t.getSource())).setTranslateX(newTranslateX);
			((Text)(t.getSource())).setTranslateY(newTranslateY);

		}
	};

	EventHandler<MouseEvent> labelout =
			new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
		 // mediaPlayer_2.stop();
           // mediaPlayer_2.play();
			prspointX=t.getSceneX();
			prspointY=t.getSceneY();
			double ne=5;



			int shrt = 0;

			double shortest=999999;
			msf=0;
			MouseButton button = t.getButton();
			if(button==MouseButton.PRIMARY){
				//place= Wooders.get(0).WhereIsTaken(prspointX, prspointY);



				//int bfr=0;
				System.out.println("You clicked: "+prspointX+" "+prspointY);

				//System.out.println(Closest.getXsutun() + ":" + Closest.getYsatir());
				System.out.println(Closest.getTxt());
				//Wooders.get(0).prevDropped.add(Closest);
				//Tas sürükleme
				int Result=Wooders.get(0).StoneDragg(prspointX, prspointY, Closest);
                              //  mediaPlayer_2.stop();
                                //mediaPlayer_2.play();
				if(Result==1) {
					System.out.println("Dropped stones are:");
					/*for(int a=0; a<Wooders.get(0).droppedStones.size(); a++) {
						System.out.println(Wooders.get(0).droppedStones.get(a).getTxt());
					}*/

					System.out.println("NextPlayer Time");
					nextPlayer=new Button("Next player");

					nextPlayer.setOnAction(destro);
					nextPlayer.setLayoutX(730);
					nextPlayer.setLayoutY(300);
					nextPlayer.setPrefWidth(200);
					nextPlayer.setPrefHeight(100);
					root.getChildren().add(nextPlayer);
				}else if(Result==2) {
					System.out.println("Controller time");
					finish=new Button("Check");
					finish.setOnAction(Combiner);
					finish.setLayoutX(750);
					finish.setLayoutY(350);
					finish.setPrefWidth(100);
					finish.setPrefHeight(100);
					root.getChildren().add(finish);
				}

			}
		}
	};
	//Starting game button
	EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e)
		{
                        mediaPlayer_4.stop();
                        mediaPlayer_4.play();
			root.getChildren().remove(btn);

			Wooders.get(0).StartWood(root);
		}
	};

	public boolean OyunBittiMi(int iPlayerIndex)
	{
		boolean Sonuc=false;


		return Sonuc;

	}
	EventHandler<ActionEvent> Combiner = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e)
		{
			root.getChildren().remove(finish);
			/*for(Stone a: Wooders.get(0).Stones) {
				System.out.println(a.getTxt());
			}*/
			try {
				Wooders.get(0).FinalControl(root);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};

	//Next Player Button
	//Yapay zekanýn yeri
	//Süreyi cotacterler arasýnada koyabilirsin eger koyamazsan animasyonu
	EventHandler<ActionEvent> destro = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e)
		{
                        mediaPlayer_3.stop();
                        mediaPlayer_3.play();
			root.getChildren().remove(nextPlayer);

			//if(!OyunBittiMi(0))

			cotacter(1);



			//if(!OyunBittiMi(1))


			cotacter(2);




			//if(!OyunBittiMi(2))

			cotacter(3);



			//if(OyunBittiMi(3))
			//{
			//alert oyun bitti

			//}
			//wood.SagUsttekiTaslariGoster(root);
			//fill_Box=0;
			tur++;
			//SIRA TUR DONGUSUNDE
			//Sira bendeyken yerden mi yoksa atilan mi?
			if(tur>0) {
				getSide= new Button("get Dropped one");
				getSide.setOnAction(getter);
				getSide.setLayoutX(600);
				getSide.setLayoutY(300);
				getSide.setPrefWidth(200);
				getSide.setPrefHeight(100);
				root.getChildren().add(getSide);
				getNew= new Button("get New ones");
				getNew.setOnAction(newer);
				getNew.setLayoutX(830);
				getNew.setLayoutY(300);
				getNew.setPrefWidth(200);
				getNew.setPrefHeight(100);
				root.getChildren().add(getNew);
				//mediaPlayer_6.stop();
				//mediaPlayer_6.play();
			}



		}


	};
	//Taslarý alýp atma yeri
	private void cotacter(int iPlayerIndex){
		int sira=iPlayerIndex+1;
		System.out.println("P"+sira+" turn");
		int choice2= rnd.nextInt(2)+1;
		int iStoneIndex=0,iPrevPlayerIndex;

		iPrevPlayerIndex = iPlayerIndex==0 ? 3 : iPlayerIndex-1;
		System.out.println(iPrevPlayerIndex);
		ArrayList<Stone> PrevPlayer_DropStones = Wooders.get(iPrevPlayerIndex).droppedStones;
		ArrayList<Stone> Player_DropStones = Wooders.get(iPlayerIndex).droppedStones;
		ArrayList<Stone> PlayerStones = Wooders.get(iPlayerIndex).Stones; 
		if(choice2==1) {

			for(iStoneIndex = PrevPlayer_DropStones.size()-1;iStoneIndex>=0;iStoneIndex--) {
				PlayerStones.add(PrevPlayer_DropStones.get(iStoneIndex));
				root.getChildren().remove(PrevPlayer_DropStones.get(iStoneIndex).getTxt());

			}

		}else {
			//ortadan tas cektim
			for(int a=0; a<3; a++) {
				//System.out.println(a);
				PlayerStones.add(Stones.get(0));
				Stones.remove(0);
				//GetDroppedThreeStones(PlayerStones,Player_DropStones);
			}
		}	

		System.out.println("Player do= "+choice2);
		//Button [pratik] [oyun] 
		if(iPlayerIndex==WinnerPlayer) {
			Wooders.get(iPlayerIndex).PlayRules(tur, PlayerStones,Player_DropStones);
		}else {
			GetDroppedThreeStones(PlayerStones,Player_DropStones);
		}	
		System.out.println("Atilacaklar");
		for(int a=0; a<Player_DropStones.size(); a++) {
			System.out.println(Player_DropStones.get(a).getTxt());
		}



		//Atilacak 3 tane tasi belirledim
		//for(iStoneIndex = PrevPlayer_DropStones.size()-1;iStoneIndex>=0;iStoneIndex--) {

		for(int a=0; a<3; a++) {	
			Wooders.get(iPlayerIndex).DroppedStone_Place(tur, a, iPlayerIndex,  root);
		}
	}

	//O attýgým main kodunu burada yazacagýz yukarýda rastgele yerden veya ortadan almayý yaptýk burada galiba direkt atýlacak 3 tasý son 3 yere koyarýz arraylistte tersten
	//3 defa döndürüz for da 
	private void GetDroppedThreeStones(ArrayList<Stone> PlayerStones, ArrayList<Stone> Player_DropStones) {

		for(int a=0; a<3; a++) {
			int num=rnd.nextInt(PlayerStones.size()-3);	
			Player_DropStones.add(PlayerStones.get(num));
			PlayerStones.remove(num);
		}

	}
	//yerden tas alma
	EventHandler<ActionEvent> getter = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e)
		{
			root.getChildren().removeAll(getSide, getNew);
			for(int a=0; a<3; a++) {
				Wooders.get(0).Stones.add(Wooders.get(3).droppedStones.get(a));
				Wooders.get(0).PutIt(1, a, Wooders.get(3).droppedStones.get(a), root);
			}


		}
	};
	//ortadan tas alma
	EventHandler<ActionEvent> newer = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e)
		{
			for(int a=0; a<3; a++) {
				Wooders.get(0).Stones.add(Stones.get(0));
				Wooders.get(0).PutIt(0, a, Stones.get(0), root);
				Stones.remove(0);
			}
			root.getChildren().removeAll(getSide, getNew);
		}
	};
}