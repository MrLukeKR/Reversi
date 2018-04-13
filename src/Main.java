
public class Main {

	public static void main(String[] args) {
		Controller c = new Controller();
		Model m = new Model(c);
		View v = new View(c);
		
		c.setViewAndModel( v, m );
		m.create();
		v.create();
		c.startGame();
	}

}
