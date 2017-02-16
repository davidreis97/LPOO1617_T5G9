import java.util.Random;

public class Ogre {
	public int x;
	public int y;
	
	public Ogre(int startx, int starty){
		x = startx; y = starty;
	}
	
	public String generateNextStep(){
		Random rand = new Random();

		int  n = rand.nextInt(4);
		
		if (n == 0){
			return "a";
		}else if (n == 1){
			return "s";
		}else if (n == 2){
			return "d";
		}else if (n == 3){
			return "w";
		}
		System.out.println("Error!");
		return "";
	}
}
