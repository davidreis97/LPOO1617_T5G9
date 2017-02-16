import java.util.Random;

public class Ogre {
	public int x;
	public int y;
	public int ogreClubX, ogreClubY;
	
	public Ogre(int startx, int starty, int clubx, int cluby){
		x = startx; y = starty; ogreClubX = clubx; ogreClubY = cluby;
	}
	
	public String generateNewDirection(){
		Random rand = new Random();

		int  n = rand.nextInt(4);
		
		if (n == 0){
			return "w";
		}else if (n == 1){
			return "a";
		}else if (n == 2){
			return "s";
		}else if (n == 3){
			return "d";
		}
		System.out.println("Error!");
		return "";
	}
}
